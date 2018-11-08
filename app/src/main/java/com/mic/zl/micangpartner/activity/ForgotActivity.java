package com.mic.zl.micangpartner.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mic.zl.micangpartner.R;

import okhttp3.OkHttpClient;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {
 private TextView forgot_title_tv,forgot_request_test_code,forgot_submit_tv;
 private EditText forgot_username_et,forgot_test_code_et,forgot_input_pwd_et,forgot_password_et;
 private ImageView back_imgView;
 private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        init();
    }

    private void init(){
        sp=getSharedPreferences("mcPartner",Context.MODE_PRIVATE);

        forgot_title_tv=findViewById(R.id.title_tv);//标题
        forgot_title_tv.setText(R.string.find_password);//设置标题

        forgot_username_et=findViewById(R.id.forgot_username_et);//账号
        forgot_username_et.setText(sp.getString("username",""));//填写账号

        forgot_request_test_code=findViewById(R.id.forgot_request_test_code);//请求获取验证码
        forgot_submit_tv=findViewById(R.id.forgot_submit_tv);//登录按钮
        forgot_test_code_et=findViewById(R.id.forgot_test_code_et);//验证码
        forgot_input_pwd_et=findViewById(R.id.forgot_input_pwd_et);//输入密码
        forgot_password_et=findViewById(R.id.forgot_password_et);//确认密码
        back_imgView=findViewById(R.id.forgot_back);//返回键
        forgot_request_test_code.setOnClickListener(this);
        forgot_submit_tv.setOnClickListener(this);
        back_imgView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgot_request_test_code:getTestCode();
                break;
            case R.id.forgot_submit_tv:submit();
                break;
            case R.id.forgot_back:finish();
                break;
        }
    }

    private void submit() {
        check();
        OkHttpClient client=new OkHttpClient();
    }

    private void getTestCode() {

    }

    private void check(){
        if (forgot_username_et.getText().toString().equals("")){
            Toast.makeText(ForgotActivity.this,"手机号不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (forgot_test_code_et.getText().toString().equals("")){
            Toast.makeText(ForgotActivity.this,"验证码不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (forgot_input_pwd_et.getText().toString().equals("")){
            Toast.makeText(ForgotActivity.this,"密码不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (forgot_password_et.getText().toString().equals("")){
            Toast.makeText(ForgotActivity.this,"请确认密码!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!forgot_password_et.getText().toString().trim().equals(forgot_input_pwd_et.getText().toString().trim())){
            Toast.makeText(ForgotActivity.this,"两次密码不一致,请重新输入!",Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
