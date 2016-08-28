package com.example.helloworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.R;
import com.example.helloworld.bmob_bean.Article;
import com.example.helloworld.view.articleEditor;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/8/28.
 */
public class ReadArticleActivity extends AppCompatActivity {

    private TextView tv_title,tv_writer;
    private articleEditor editor;
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    Article article = (Article) msg.obj;
                    //测试数据
                    tv_title.setText(article.getTitle());
                    tv_writer.setText("-"+article.getWriter()+"-");
                    //加载URL中的图片

                    editor.setText(article.getSummary());
                    break;
                case ERROR:
                    Toast.makeText(ReadArticleActivity.this,"刷新异常，请稍后再试",0).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.article_read_layout);
        initView();
        initData();
    }
    public void initView(){
        tv_title = (TextView) findViewById(R.id.titles);
        tv_writer = (TextView) findViewById(R.id.writer);
        editor = (articleEditor) findViewById(R.id.content);
    }

    public void initData(){
        String id = getIntent().getStringExtra("articleID");

        BmobQuery <Article> query = new BmobQuery();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.addWhereEqualTo("articleID",id);
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null){
                    if (list.size() == 1){
                        Article article = list.get(0);
                        Message message = new Message();
                        message.what = SUCCESS;
                        message.obj = article;
                        handler.sendMessage(message);
                    }else {
                        Log.d("test","数据不存在或存在多条");
                    }
                }else {
                    Message message = new Message();
                    message.what = ERROR;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
