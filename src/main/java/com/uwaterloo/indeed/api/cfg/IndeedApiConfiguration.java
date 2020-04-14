package com.uwaterloo.indeed.api.cfg;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "indeed.api")
public class IndeedApiConfiguration {

	@Getter
	@Setter
	private String publisherId;

	@Getter
	@Setter
	private String streamName;
	
}
