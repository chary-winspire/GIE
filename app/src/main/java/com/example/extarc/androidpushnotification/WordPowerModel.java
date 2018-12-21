package com.example.extarc.androidpushnotification;

public class WordPowerModel {

    private String WPTitle, WPType, WPMening, WPExample, Extra;
    private String TopicImage, TopicTitle, TopicDes, TopicURL;

    public WordPowerModel (String wpTitle, String wpType, String wpMeaning, String wpExample, String extra){
        this.WPTitle = wpTitle;
        this.WPType = wpType;
        this.WPMening = wpMeaning;
        this.WPExample = wpExample;
        this.Extra = extra;

    }

    public WordPowerModel(String topicImage, String topicTitle, String topicDes, String topicURL) {
        TopicImage = topicImage;
        TopicTitle = topicTitle;
        TopicDes = topicDes;
        TopicURL = topicURL;
    }

    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }

    public String getTopicURL() {
        return TopicURL;
    }

    public void setTopicURL(String topicURL) {
        TopicURL = topicURL;
    }

    public String getTopicImage() {
        return TopicImage;
    }

    public void setTopicImage(String topicImage) {
        TopicImage = topicImage;
    }

    public String getTopicTitle() {
        return TopicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        TopicTitle = topicTitle;
    }

    public String getTopicDes() {
        return TopicDes;
    }

    public void setTopicDes(String topicDes) {
        TopicDes = topicDes;
    }

    public String getWPTitle() {
        return WPTitle;
    }

    public void setWPTitle(String WPTitle) {
        this.WPTitle = WPTitle;
    }

    public String getWPType() {
        return WPType;
    }

    public void setWPType(String WPType) {
        this.WPType = WPType;
    }

    public String getWPMening() {
        return WPMening;
    }

    public void setWPMening(String WPMening) {
        this.WPMening = WPMening;
    }

    public String getWPExample() {
        return WPExample;
    }

    public void setWPExample(String WPExample) {
        this.WPExample = WPExample;
    }
}
