package com.charade.mathcharade;

/**
 * Created by Nat on 18/4/2016.
 */
public class Topic {
    private String topicName;
    private String topicDetails;

    public Topic(String topicName) {
        this.topicName = topicName;
    }
    public Topic(String topicName, String topicDetails) {
        this.topicName = topicName;
        this.topicDetails = topicDetails;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDetails() {
        return topicDetails;
    }

    public void setTopicDetails(String topicDetails) {
        this.topicDetails = topicDetails;
    }
}
