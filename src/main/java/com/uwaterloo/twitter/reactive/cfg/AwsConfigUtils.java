package com.uwaterloo.twitter.reactive.cfg;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

public class AwsConfigUtils {
    private static final String APPLICATION_NAME = "stream-app";
    private static final String VERSION = "1.0.0";

    public static ClientConfiguration getClientConfigWithUserAgent() {

        final ClientConfiguration config = new ClientConfiguration();
        final StringBuilder userAgent = new StringBuilder(ClientConfiguration.DEFAULT_USER_AGENT);

        userAgent.append(" ");
        userAgent.append(APPLICATION_NAME);
        userAgent.append("/");
        userAgent.append(VERSION);

        config.setUserAgent(userAgent.toString());

        return config;
    }

    public static AWSCredentialsProvider getCredentialsProvider() throws Exception {
        /*
         * reading from the credentials file located at (~/.aws/credentials).
         */
        AWSCredentialsProvider credentialsProvider = null;
        try {
            credentialsProvider = new ProfileCredentialsProvider("default");
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        return credentialsProvider;
    }
}
