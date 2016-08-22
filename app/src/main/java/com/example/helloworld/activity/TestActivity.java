package com.example.helloworld.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.helloworld.R;
import com.example.helloworld.bean.FeedBack;
import com.example.helloworld.bean.FeedBackUrl;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 测试上传图片和下载图片
 * Created by Administrator on 2016/8/10.
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESIZE_REQUEST_CODE = 2;

    private static final int GETURL = 4;

    private String IMAGE_FILE_NAME = "header.jpg";

    private ImageView mImageHeader,imageView2;

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
                            Toast.makeText(TestActivity.this,"已经将地址保存到网络上",0).show();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        Bmob.initialize(this,"028acd02f32e5fb5fd2af4c29557cdcf");
        setupViews();
    }

    private void setupViews() {
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        mImageHeader = (ImageView) findViewById(R.id.image_header);
        final Button selectBtn1 = (Button) findViewById(R.id.btn_selectimage);
        final Button selectBtn2 = (Button) findViewById(R.id.btn_takephoto);
        final Button selectBtn3 = (Button) findViewById(R.id.loads);
        selectBtn1.setOnClickListener(this);
        selectBtn2.setOnClickListener(this);
        selectBtn3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loads:
                Log.d("test","下载中");
                download();
                break;
            case R.id.btn_selectimage:
                //调用系统的选择图片的Intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");//图片
                startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
                break;
            case R.id.btn_takephoto:
                if (isSdcardExisting()) {
                    Intent cameraIntent = new Intent(
                            "android.media.action.IMAGE_CAPTURE");//拍照
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                    cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);

                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                } else {
                    Toast.makeText(v.getContext(), "请插入sd卡", Toast.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    Uri originalUri=data.getData();//获取图片uri
                    resizeImage(originalUri);
                    //下面方法将获取的uri转为String类型
                    String []imgs1={MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
                    Cursor cursor=this.managedQuery(originalUri, imgs1, null, null, null);
                    int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String img_url=cursor.getString(index);
                    upload(img_url);

                    break;
                case CAMERA_REQUEST_CODE:
                    if (isSdcardExisting()) {
                        resizeImage(getImageUri());
                        String []imgs={MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
                        Cursor cursor1=this.managedQuery(getImageUri(), imgs, null, null, null);
                        int index1=cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor1.moveToFirst();
                        String img_url1=cursor1.getString(index1);
                        upload(img_url1);
                    } else {
                        Toast.makeText(TestActivity.this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }
                    break;

                case RESIZE_REQUEST_CODE:
                    if (data != null) {
                        showResizeImage(data);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isSdcardExisting() {//判断SD卡是否存在
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public void resizeImage(Uri uri) {//重塑图片大小
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//可以裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    private void showResizeImage(Intent data) {//显示图片
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            mImageHeader.setImageDrawable(drawable);
        }
    }

    private Uri getImageUri() {//获取路径
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_NAME));
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
                FeedBack feedBack = new FeedBack();
                feedBack.setFace(icon);
                feedBack.save();
                //发送地址
                String url = icon.getFileUrl();
                Message message = new Message();
                message.what = GETURL;
                message.obj = url;
                handler.sendMessage(message);
            }
        });
    }

    private void download(){
        //获取网络上的图片地址
        String ImageUrl = "http://bmob-cdn-5506.b0.upaiyun.com/2016/08/10/553baf588b1d4f47b5dbc3aa99373822.jpg";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //创建一个图片缓存
        final LruCache<String,Bitmap> lruCache = new LruCache<String, Bitmap>(20);

        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return lruCache.get(s);
            }

            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key,value);
            }
        };

        //加载请求和缓存
        ImageLoader imageLoader = new ImageLoader(requestQueue,imageCache);


        ImageLoader.ImageListener listener = imageLoader.getImageListener(imageView2,R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        //加载图片
        imageLoader.get(ImageUrl,listener);
        Log.d("test","下载结束");
    }
}
