package com.example.helloworld.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.helloworld.R;
import com.example.helloworld.bean.FeedBack;
import com.example.helloworld.bean.FeedBackUrl;
import com.example.helloworld.bmob_bean.Article;
import com.example.helloworld.view.articleEditor;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/8/23.
 * 该页面实现写文章的功能
 */
public class WriteArticleActivity extends AppCompatActivity {

    private ImageView finishwrite, addpicture, more;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int RESIZE_REQUEST_CODE = 2;
    private static final int ERROR = 5;
    private static final int GETURL = 4;
    private articleEditor editor;
    private EditText title_et;

    private int count;
    private String [][] imageUrlReplace;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETURL:
                    String url = (String) msg.obj;
                    Log.d("test",url);
                    FeedBackUrl feedBack = new FeedBackUrl();
                    feedBack.setUrl(url);
                    feedBack.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            //将地址保存到网络上
//                            Toast.makeText(TestActivity.this,"已经将地址保存到网络上",0).show();
                        }
                    });
                    break;
                case ERROR:
                    Toast.makeText(WriteArticleActivity.this,"图片上传错误",0).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.write_article_main);
        Bmob.initialize(this, "028acd02f32e5fb5fd2af4c29557cdcf");
        initView();
        setOnclick();
    }

    private void setOnclick() {
        addpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加图片到文本框中，并传送到服务器中，保存URL
                //调用系统的选择图片的Intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");//图片
                startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
            }
        });

        finishwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交文章
                insertToArticle();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击后弹出菜单设置权限
                Toast.makeText(WriteArticleActivity.this,"设置文章权限",0).show();
            }
        });
    }

    /**
     * 提交文章数据到服务器
     */
    private void insertToArticle(){
        //替换url后提交数据
        String s = editor.getText().toString();
        StringBuffer stringBuffer = new StringBuffer(s);
        for (int i = 1;i<=count;i++){
            int index = stringBuffer.indexOf(imageUrlReplace[i][0]);
            stringBuffer.replace(index,index + imageUrlReplace[i][0].length(),imageUrlReplace[i][1]);
        }

        final Article article = new Article();
        article.setArticleID(1);
        article.setWriter("张三");
        article.setLevel(1);
        article.setClassify("故事");
        article.setTitle(title_et.getText().toString());
        article.setSummary(stringBuffer.toString());
        article.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e !=null){
                    Toast.makeText(WriteArticleActivity.this,"提交失败，请稍后再试",0).show();
                }else {
                    Toast.makeText(WriteArticleActivity.this,"发表成功",0).show();
                    finish();
                }
            }
        });
    }

    public void initView() {
        finishwrite = (ImageView) findViewById(R.id.finish);
        addpicture = (ImageView) findViewById(R.id.addpicture);
        more = (ImageView) findViewById(R.id.more);
        editor = (articleEditor) findViewById(R.id.content);
        title_et = (EditText) findViewById(R.id.titles);

        count = 0;
        imageUrlReplace = new String[10][2];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    //将图片进行压缩
                    if (count<10) {
                        count++;
                    }else {
                        Toast.makeText(WriteArticleActivity.this,"每篇文章最多插入9张图片",0).show();
                    }
                    Uri originalUri = data.getData();//获取图片uri
                    //下面方法将获取的uri转为String类型
                    String[] imgs1 = {MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
                    Cursor cursor = this.managedQuery(originalUri, imgs1, null, null, null);
                    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    final String img_url = cursor.getString(index);
                    editor.insertBitmap(img_url);

                    //存储插入的图片
                    imageUrlReplace[count][0]=img_url;

                    // 上传该文件并获取url
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            upload(img_url);
                        }
                    }).start();
                    break;
            }
        }
    }
    /**
     * 将图片上传
     * @param imgpath
     */
    private void upload(String imgpath){
        final BmobFile icon = new BmobFile(new File(imgpath));
        icon.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e != null){
                    Message message = new Message();
                    message.what = ERROR;
                    handler.sendMessage(message);
                    //上传图片失败
                }else {
                    FeedBack feedBack = new FeedBack();
                    feedBack.setFace(icon);
                    feedBack.save();
                    //发送地址
                    String url = icon.getFileUrl();
                    imageUrlReplace[count][1]=url;
                    Message message = new Message();
                    message.what = GETURL;
                    message.obj = url;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
