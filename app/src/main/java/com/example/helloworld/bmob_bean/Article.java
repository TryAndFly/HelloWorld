package com.example.helloworld.bmob_bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 用于存储用户的文章的表
 * Created by Administrator on 2016/8/23.
 */
public class Article extends BmobObject implements Serializable {
    //由标题MD5产生
    private String articleID;
    private String writer;//作者
    private String classify;//分类
    private String title;//标题

    //评论，及喜欢暂略
    private String summary;//全文
    private String summary_part;//摘要
    private int level;//权限

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getSummary_part() {
        return summary_part;
    }

    public void setSummary_part(String summary_part) {
        this.summary_part = summary_part;
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
