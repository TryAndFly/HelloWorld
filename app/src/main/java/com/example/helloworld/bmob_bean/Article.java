package com.example.helloworld.bmob_bean;

import cn.bmob.v3.BmobObject;

/**
 * 用于存储用户的文章的表
 * Created by Administrator on 2016/8/23.
 */
public class Article extends BmobObject{
    private int articleID;
    private int writerID;
    private int classifyID;
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

    public int getWriterID() {
        return writerID;
    }

    public void setWriterID(int writerID) {
        this.writerID = writerID;
    }

    public int getClassifyID() {
        return classifyID;
    }

    public void setClassifyID(int classifyID) {
        this.classifyID = classifyID;
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
