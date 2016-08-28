package com.example.helloworld.bean;

/**
 * Created by Administrator on 2016/8/22.
 */
public class HomeSummary {
    private String articleID;
    private String classify;
    private String title;
    private String summary;
    private String auth;
    private int imageId;

    public HomeSummary(String articleID,String classify, String title, String summary, String auth, int imageId) {
        this.articleID = articleID;
        this.classify = classify;
        this.title = title;
        this.summary = summary;
        this.auth = auth;
        this.imageId = imageId;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
