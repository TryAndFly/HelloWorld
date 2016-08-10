package com.example.helloworld.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helloworld.R;
import com.example.helloworld.util.BaseActivity;
import com.example.helloworld.util.Md5;

/**
 * Created by Administrator on 2016/8/7.
 */
public class RegisterActivity extends BaseActivity{

    private EditText et_username,et_password,et_name;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_first);
        initView();
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_name = (EditText) findViewById(R.id.et_name);
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

    public boolean isLegal() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        String name = et_name.getText().toString();
        if (username.length() != 11){
            Toast.makeText(this,"手机号码位数不正确",0).show();
            return false;
        }
        if (password.length() < 6){
            Toast.makeText(this,"密码长度应大于六位",0).show();
            return false;
        }
        if (name.equals("")){
            Toast.makeText(RegisterActivity.this,"请输入昵称",0).show();
            return false;
        }
        return true;
    }
    /***
     * 下一步
     */
    public void next(View view){
        //判断数据是否合法
        if (!isLegal()){
            return;
        }
        //将数据存储入本地后，开启下一步
        SharedPreferences.Editor editor = getSharedPreferences("user_data",MODE_PRIVATE).edit();
        editor.putString("username",et_username.getText().toString());
        //MD5加密密码后存入数据
        String password= Md5.md5Password(et_password.getText().toString());
        editor.putString("password",password);
        editor.putString("name",et_name.getText().toString());
        editor.commit();
        //存储
        String phone = et_username.getText().toString();
        Intent intent = new Intent(RegisterActivity.this,RegisterNextActivity.class);
        intent.putExtra("phone",phone);
        startActivity(intent);
    }

    /**
     * 查看用户协议
     * @param view
     */
    public void protocol(View view){
        Toast.makeText(RegisterActivity.this,"用户协议还没想好",0).show();
    }
}
