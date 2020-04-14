package com.uwaterloo.twitter.reactive;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.sagemaker.model.ResourceNotFoundException;
import com.uwaterloo.twitter.reactive.cfg.AwsConfigUtils;
import com.uwaterloo.twitter.reactive.entity.Tweet;

public class TwitterToKinesisProducer {

	private static final Log LOG = LogFactory.getLog(TwitterToKinesisProducer.class);

	private String streamName;
	private AmazonKinesis kinesisClient;

	public TwitterToKinesisProducer(String streamName) throws Exception {
		this.streamName = streamName;
		Region region = RegionUtils.getRegion(Regions.CA_CENTRAL_1.getName());
		if (region == null) {
			System.err.println("not a valid AWS region.");
			System.exit(1);
		}

		AWSCredentials awsCredentials = AwsConfigUtils.getCredentialsProvider().getCredentials();

		kinesisClient = AmazonKinesisClientBuilder.defaultClient();

		CheckForStreamAvailability(kinesisClient, streamName);

	}

	public byte[] tweetToCsvLine(Tweet tweet) {
		String[] fields = { Long.toString(tweet.getId())
				, tweet.getCreatedAt()
				, tweet.getText()
				, tweet.getUser().getLocation() };
		String line = "\"" + StringUtils.join(fields, "\",\"") + "\"\n";

		return line.getBytes();
	}

	public void sendTweetToKinesis(Tweet tweet) {
		byte[] bytes = tweetToCsvLine(tweet);
		if (bytes == null) {
			LOG.warn("Could not convert tweet to CSV");
			return;
		}

		PutRecordRequest putRecord = new PutRecordRequest();
		putRecord.setStreamName(streamName);
		putRecord.setPartitionKey("021");// Don't care about charding for the demo
		putRecord.setData(ByteBuffer.wrap(bytes));

		try {
			kinesisClient.putRecord(putRecord);
			Thread.sleep(100);
		} catch (Exception ex) {
			LOG.warn("Error sending record to Amazon Kinesis.", ex);
		}
	}

	/**
	 * Checking whether the stream is already exist in account
	 * 
	 * @param amazonKinesis
	 * @param streamName
	 */
	private static void CheckForStreamAvailability(AmazonKinesis amazonKinesis, String streamName) {
		try {
			DescribeStreamResult result = amazonKinesis.describeStream(streamName);
			if (!"ACTIVE".equals(result.getStreamDescription().getStreamStatus())) {
				System.err.println("Stream " + streamName + " is not active. Please wait a few moments and try again.");
				System.exit(1);
			}
		} catch (ResourceNotFoundException e) {
			System.err.println("Stream " + streamName + " does not exist. Please create it in the console.");
			System.err.println(e);
			System.exit(1);
		} catch (Exception e) {
			System.err.println("Error found while describing the stream " + streamName);
			System.err.println(e);
			System.exit(1);
		}
	}
}
