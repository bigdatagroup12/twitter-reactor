package com.uwaterloo.twitter.reactive.cfg;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix="twitter.api")
public class TwitterApiConfiguration {

	private String key;
	private String secret;
	private String accessToken;
	private String accessTokenSecret;
	private String streamName;
	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
}
