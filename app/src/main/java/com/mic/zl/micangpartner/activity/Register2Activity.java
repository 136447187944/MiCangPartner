package com.mic.zl.micangpartner.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.dialog.MyDialog;
import com.mic.zl.micangpartner.util.Constants;
import com.mic.zl.micangpartner.util.FormatTool;
import com.mic.zl.micangpartner.util.MD5Util;

import java.io.IOException;
import java.text.Normalizer;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register2Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView title_tv,immediate_login,agree_and_register_tv,protocol;
    private EditText register_pwd_et,register_pwd_again_et,register_invite_code_et;
    private CheckBox checkBox;
    private ImageView back_iv;
    private ProgressDialog myProgress;
    private RegisterAsyncTask registerAsyncTask;
    private String nickName,username,smsCode,password,refree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        init();
    }

    private void init(){
        title_tv=findViewById(R.id.title_tv);
        title_tv.setText(R.string.register);
        back_iv=findViewById(R.id.back_iv);
        immediate_login=findViewById(R.id.immediate_login_tv);//登录按钮
        agree_and_register_tv=findViewById(R.id.agree_and_register_tv);//注册按钮
        register_invite_code_et=findViewById(R.id.register_invite_code_et);//邀请码
        register_pwd_again_et=findViewById(R.id.register_pwd_again_et);//第二次输入密码
        register_pwd_et=findViewById(R.id.register_pwd_et);//第一次输入密码
        checkBox=findViewById(R.id.agree_cb);
        protocol=findViewById(R.id.protocol_tv);
        myProgress=new ProgressDialog(Register2Activity.this);

        //获取上一个页面的传递过来的值
        Intent intent=getIntent();
        nickName=intent.getStringExtra("nickName");
        username=intent.getStringExtra("username");
        smsCode=intent.getStringExtra("smsCode");

        back_iv.setOnClickListener(this);
        immediate_login.setOnClickListener(this);
        agree_and_register_tv.setOnClickListener(this);
        protocol.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_iv:finish();break;
            case R.id.immediate_login_tv:finish();break;
            case R.id.agree_and_register_tv:register();break;
            case R.id.protocol_tv: Intent intent=new Intent(Register2Activity.this,ProtocolActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void register() {
        password=register_pwd_et.getText().toString();
        String password_two=register_pwd_again_et.getText().toString();
        refree=register_invite_code_et.getText().toString();
        if (password.equals("")){
            Toast.makeText(Register2Activity.this,R.string.pwd_not_empty,Toast.LENGTH_SHORT).show();
            return;
        }
        if (password_two.equals("")){
            Toast.makeText(Register2Activity.this,R.string.pwd_confirm,Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length()<8){
            Toast.makeText(Register2Activity.this,"密码长度最少8位",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password_two.equals(password)){
            Toast.makeText(Register2Activity.this,R.string.pwd_not_equal,Toast.LENGTH_SHORT).show();
            return;
        }
        if (refree.equals("")){
            Toast.makeText(Register2Activity.this,"邀请码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkBox.isChecked()){
           Toast.makeText(Register2Activity.this,"请先同意米仓协议",Toast.LENGTH_SHORT).show();
            return;
        }

        registerAsyncTask=new RegisterAsyncTask();
        registerAsyncTask.execute(nickName,username,MD5Util.MD5Encode(password,"utf-8"),refree,smsCode);
    }

    //http://121.201.66.138:8867/McangPartner/user.do?action=register
    // &nickName=? &username=? &password=? &refree=? &smsCode=?
    class RegisterAsyncTask extends AsyncTask<String,Void,String>{
        String result;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgress.setMessage("注册中...");
            myProgress.setIndeterminate(true);
            myProgress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client=new OkHttpClient();
            RequestBody body=new FormBody.Builder()
                    .add("action","register")
                    .add("nickName",params[0])
                    .add("username",params[1])
                    .add("password",params[2])
                    .add("refree",params[3])
                    .add("smsCode",params[4])
                    .build();
            String url=Constants.urlHead+"user.do";
            Request request=new Request.Builder().url(url).post(body).build();
            try {
               Response response=client.newCall(request).execute();
                if(response.isSuccessful()){
                    myProgress.dismiss();
                    JSONObject object= JSON.parseObject(response.body().string());
                    result=object.getString("result");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("注册成功")){
                SharedPreferences sp=getSharedPreferences("mcPartner",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("username",username);
                editor.commit();
                finish();
            }else {
                Toast.makeText(Register2Activity.this,s,Toast.LENGTH_SHORT).show();
            }

        }
    }

}
