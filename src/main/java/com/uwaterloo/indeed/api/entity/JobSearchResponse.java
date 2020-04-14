package com.uwaterloo.indeed.api.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobSearchResponse {
	@Getter@Setter private String query;
	@Getter@Setter private String location;
	@Getter@Setter private int totalResults;
	
	@Getter@Setter private List<JobSearchResult> results;
}
