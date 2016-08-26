package com.example.helloworld.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class articleEditor extends EditText {

    private Context context;
    private List<String> stringList;
    private float oldY = 0;
    //用来标识存入图片
    public static final String mBitmapTag = "☆";
    private String mNewLineTag = "\n";

    public articleEditor(Context context) {
        super(context);
        init(context);
    }

    public articleEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public articleEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        stringList = getmContentList();
        insertData();
    }

    /**
     * 设置数据
     */
    public void insertData(){
        if (stringList.size()>0){
            for (String str : stringList){
                if (str.indexOf(mBitmapTag)!=-1){
                    //将设置的标识清除，准备替换图片
                    String path = str.replace(mBitmapTag,"");
                    //进行图片缩放
                    Bitmap bitmap = getSmallBitmap(path, 400, 400);
                    //插入图片
                    insertBitmap(path,bitmap);
                }else {
                    //插入文章
                    Spannable ss = new SpannableString(str);
                    append(ss);
                }
            }
        }
    }

    /**
     * 用集合的形式获取控件里的内容
     * @return
     */
    public List<String> getmContentList(){
        if(stringList == null){
            stringList = new ArrayList<String>();
        }
        String content = getText().toString().replaceAll(mNewLineTag,"");
        if (content.length()>0 && content.contains(mBitmapTag)){
            String[] split = content.split(mBitmapTag);
            stringList.clear();
            //将分割后的数据置换成图片后处理
            for (String str : split){
                stringList.add(str);
            }
        }else {
            stringList.add(content);
        }
        return stringList;
    }

    /**
     * 获取图片并进行压缩
     */
    public Bitmap getSmallBitmap(String filePath,int reqWidth,int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //计算压缩的比例
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        //进行压缩
        Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int w_width = w_screen;
        int b_width = bitmap.getWidth();
        int b_height = bitmap.getHeight();
        int w_height = w_width *b_height/b_width;
        bitmap = Bitmap.createScaledBitmap(bitmap,w_width,w_height,false);
        return bitmap;
    }

    /**
     * 压缩图片的比例
     */
    //计算图片的缩放值
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 插入图片
     *
     * @param bitmap
     * @param path
     * @return
     */
    private SpannableString insertBitmap(String path, Bitmap bitmap) {
        Editable edit_text = getEditableText();
        int index = getSelectionStart(); // 获取光标所在位置
        //插入换行符，使图片单独占一行
        SpannableString newLine = new SpannableString("\n");
        edit_text.insert(index,newLine);
        edit_text.insert(index,newLine);//插入图片前换行
        // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        path = mBitmapTag + path + mBitmapTag;
        SpannableString spannableString = new SpannableString(path);
        // 根据Bitmap对象创建ImageSpan对象
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        // 用ImageSpan对象替换你指定的字符串
        spannableString.setSpan(imageSpan, 0, path.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将选择的图片追加到EditText中光标所在位置
        if (index < 0 || index >= edit_text.length()) {
            edit_text.append(spannableString);
        } else {
            edit_text.insert(index, spannableString);
        }
        edit_text.insert(index, newLine);//插入图片后换行
        edit_text.insert(index,newLine);
        return spannableString;
    }

    /**
     * 插入图片
     *
     * @param path
     */
    public void insertBitmap(String path) {
        Log.d("test",path);
        Bitmap bitmap = getSmallBitmap(path, 400, 300);
        insertBitmap(path, bitmap);
    }
    /**
     * 设置显示的内容集合
     *
     * @param contentList
     */
    public void setmContentList(List<String> contentList) {
        if (stringList == null) {
            stringList = new ArrayList<>();
        }
        stringList.clear();
        stringList.addAll(contentList);
        insertData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldY = event.getY();
                requestFocus();
                break;
            case MotionEvent.ACTION_MOVE:
                float newY = event.getY();
                if (Math.abs(oldY - newY) > 20) {
                    clearFocus();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
