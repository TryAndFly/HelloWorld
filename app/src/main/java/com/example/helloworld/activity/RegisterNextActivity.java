package com.example.helloworld.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.R;
import com.example.helloworld.bmob_bean.User;
import com.example.helloworld.util.ActivityCollector;
import com.example.helloworld.util.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static cn.smssdk.SMSSDK.submitVerificationCode;

/**
 * Created by Administrator on 2016/8/8.
 */
public class RegisterNextActivity extends BaseActivity {
    private TextView tv_username;
    private Button getcode;
    private EditText et_identify;
    private Toolbar toolbar;

    /**
     * 短信验证错误码
     */

    private static final int SENDOK = 1;
    private static final int SUBMITOK = 3;
    private static final int FALSE = 4;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SENDOK:
                    Toast.makeText(RegisterNextActivity.this, "获取验证码中", 0).show();
                    break;
                case SUBMITOK:
                    //注册到服务器
                    Toast.makeText(RegisterNextActivity.this, "验证成功", 0).show();
                    identifyPass();
                    break;
                case FALSE:
                    Object data = msg.obj;
                    Toast.makeText(RegisterNextActivity.this, data.toString(), 0).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_next);
        //验证码接口密钥初始化
        SMSSDK.initSDK(this, "15e0196a43d09", "6a4f2502c3a0361b87c98320aa4ed929");
        initView();
        initData();
        initSMS();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定
        SMSSDK.unregisterAllEventHandler();
    }

    private void initView() {
        tv_username = (TextView) findViewById(R.id.tv_username);
        getcode = (Button) findViewById(R.id.getcode);
        et_identify = (EditText) findViewById(R.id.et_identify);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        String phone = getIntent().getStringExtra("phone");
        tv_username.setText(phone);
    }

    private void initSMS() {
        //注册接口
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                        Log.d("test", "提交验证吗成功"+data.toString());
                        Message message = new Message();
                        message.what = SUBMITOK;
                        handler.sendMessage(message);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Message message = new Message();
                        message.what = SENDOK;
                        handler.sendMessage(message);
                        //接触注册
//                        Log.d("test", "获取验证吗成功" + data.toString());
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
//                    Log.d("test","验证码错误");
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    JSONObject object = null;
                    try {
                        object = new JSONObject(throwable.getMessage());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String des = object.optString("detail");//错误描述
//                    int status = object.optInt("status");//错误代码
                    Message message = new Message();
                    message.what = FALSE;
                    message.obj = des;
                    handler.sendMessage(message);
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    public void getCode(View v) {
        //获取验证码,请求获取短信验证码，在监听中返回
        SMSSDK.getVerificationCode("86", tv_username.getText().toString());
    }

    public void finished(View v) {
        //判断验证码正确则写入数据库完成注册
        //获取输入的验证码
        String number = et_identify.getText().toString();
        // 提交短信验证码，在监听中返回
        submitVerificationCode("86", "15700799398", number);
    }

    /**
     * 验证通过
     */
    public void identifyPass() {
        //从存储中取出数据
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        String name = sharedPreferences.getString("name", "");//还未使用
        insertToUser(username, password);
        //返回登录页面
        ActivityCollector.finishAll();
    }

    /**
     * 注册帐号到服务器
     *
     * @param username
     * @param password
     */
    private void insertToUser(final String username, final String password) {
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setLevel(1);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterNextActivity.this, "注册成功", 0).show();
                } else {
                    Toast.makeText(RegisterNextActivity.this, "该手机号已注册", 0).show();
                }
            }
        });
    }
}
