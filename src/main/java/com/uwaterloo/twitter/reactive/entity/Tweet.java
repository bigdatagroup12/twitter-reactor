package com.uwaterloo.twitter.reactive.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tweet {

	private Long id;

	@JsonProperty("created_at")
	private String createdAt;

	private String text;

	@JsonProperty("user")
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Tweet{" + "id=" + id + ", createdAt='" + createdAt + '\'' + ", text='" + text + '\'' +
		/*
		 * ", source='" + source + '\'' + ", truncated=" + truncated +
		 * ", inReplyToStatusId=" + inReplyToStatusId + ", inReplyToUserId=" +
		 * inReplyToUserId + ", inReplyToScreenName='" + inReplyToScreenName + '\'' +
		 */
				", user=" + user + '}';
	}
}
