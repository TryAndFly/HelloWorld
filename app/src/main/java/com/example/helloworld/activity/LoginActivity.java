package com.example.helloworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helloworld.HomeMainActivity;
import com.example.helloworld.R;
import com.example.helloworld.bmob_bean.User;
import com.example.helloworld.util.Md5;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/8/7.
 */
public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_main);
        initView();
    }

    private void initView() {
        //设置toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //设置点击事件,必须在setActionBar后设置才行，不然会被置换掉而无效
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, HomeMainActivity.class);
                startActivity(i);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //其余组件初始化
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
    }

    /**
     * 登录功能
     *
     * @param v
     */
    public void login(View v) {
        String name = username.getText().toString();
        String pword = password.getText().toString();
        if (!isLegal(name, pword)) return;
        queryFromUser(name, pword);
    }

    public boolean isLegal(String username, String password) {
        if (username.length() != 11) {
            Toast.makeText(this, "手机号码位数不正确", 0).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "密码长度应大于六位", 0).show();
            return false;
        }
        return true;
    }

    /**
     * 注册帐号
     */
    public void register(View v) {
        //跳转到注册页面
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    /**
     * 忘记密码
     */
    public void forget(View v) {
        //跳转到忘记密码页面

    }

    private void queryFromUser(String name, String pword) {
        //到服务器查询数据
        BmobQuery<User> bmobQuery = new BmobQuery();
        bmobQuery.addWhereEqualTo("username", name);
        bmobQuery.addWhereEqualTo("password", Md5.md5Password(pword));
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 1) {
                        //登录成功
                        Toast.makeText(LoginActivity.this, "欢迎你的到来", 0).show();
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "帐号或密码输入错误", 0).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "查询出错", 0).show();
                }
            }
        });
    }
}
