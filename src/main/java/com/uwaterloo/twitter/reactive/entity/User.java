package com.uwaterloo.twitter.reactive.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("screen_name")
    private String screenName;

    private String location;

    //private String description;
    
    @JsonProperty("followers_count")
    private Long followersCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", location='" + location + '\'' +
                //", description='" + description + '\'' +
                /*", profileImageUrl='" + profileImageUrl + '\'' +
                ", profileImageUrlHttps='" + profileImageUrlHttps + '\'' +
                ", defaultProfileImage=" + defaultProfileImage +*/
                '}';
    }
}
