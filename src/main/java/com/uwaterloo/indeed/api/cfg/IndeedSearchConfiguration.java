package com.uwaterloo.indeed.api.cfg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.uwaterloo.indeed.api.IndeedToKinesisProducer;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix="indeed.api")
public class IndeedSearchConfiguration {

	@Getter
	@Setter
	private String streamName;
	
	@Autowired
	IndeedApiConfiguration apiConfig;
	
	@Bean
	public IndeedToKinesisProducer indeedToKinesisProducer() throws Exception {
		return new IndeedToKinesisProducer(apiConfig.getStreamName());
	}
	
}
