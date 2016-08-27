package com.example.helloworld.bmob_bean;

import cn.bmob.v3.BmobObject;

/**
 * 用于存储用户的文章的表
 * Created by Administrator on 2016/8/23.
 */
public class Article extends BmobObject{
    private int articleID;
    private String writer;
    private String classify;

    private String title;
    private String summary;
    private int level;
    //评论，及喜欢暂略

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
