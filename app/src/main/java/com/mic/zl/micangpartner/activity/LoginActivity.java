package com.mic.zl.micangpartner.activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.util.Constants;
import com.mic.zl.micangpartner.util.GetDeviceId;
import com.mic.zl.micangpartner.util.MD5Util;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout testLayout;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String userName, passWord;
    private EditText userName_et, passWord_et,testCode_et;
    private CheckBox rememberPwd_cb;
    private TextView forgotPwd_tv, register_tv, login_tv,requestTestCode_tv;
    private Intent intent;
    private String phoneId;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private LoginAsyncTask loginAsyncTask;
    private  RequestBody body;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //给状态栏设置透明，让这个布局填充整个界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        /*创建本地存储数据的文件mcPartner,永久保存*/
        sp = getSharedPreferences("mcPartner", Context.MODE_PRIVATE);
        editor = sp.edit();
        init();
    }

    /*初始化方法*/
    private void init() {
        /*初始化控件*/
        userName_et = findViewById(R.id.username_et);//账号
        passWord_et = findViewById(R.id.password_et);//密码
        rememberPwd_cb = findViewById(R.id.remember_pwd);//记住密码
        forgotPwd_tv = findViewById(R.id.forgot_pwd);//忘记密码
        register_tv = findViewById(R.id.register_tv);//注册
        login_tv = findViewById(R.id.login_tv);//登录按钮
        testLayout=findViewById(R.id.test_ll);//验证Layout布局
        testCode_et=findViewById(R.id.test_code);//获取输入的验证码
        requestTestCode_tv=findViewById(R.id.request_test_code);//请求验证码
        alertDialog = new AlertDialog.Builder(LoginActivity.this);//实例化提示对话框对象
        progressDialog=new ProgressDialog(LoginActivity.this);//实例化等待对话框对象

        /*使控件显示数据*/
        userName=(sp.getString("username",""));
        passWord=(sp.getString("password",""));
        userName_et.setText(userName);
        rememberPwd_cb.setChecked(sp.getBoolean("remember_checkbox",false));
        if (rememberPwd_cb.isChecked()){
            passWord_et.setText(passWord);
        }

        /*注册点击事件监听器*/
        forgotPwd_tv.setOnClickListener(this);//忘记密码
        register_tv.setOnClickListener(this);//注册
        login_tv.setOnClickListener(this);//登录
        requestTestCode_tv.setOnClickListener(this);//获取验证码

        /*注册文本改变监听器*/
        userName_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editor.putString("userName",editable.toString().trim());
                editor.commit();//提交过后才能执行下一个任务
            }
        });

        /*记录下多选按钮的状态*/
        rememberPwd_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                editor.putBoolean("remember_checkbox",isCheck);
                editor.commit();//提交过后才能执行下一个任务
            }
        });

    }

    /*点击事件方法*/
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_tv:
                 login();
                 break;
            case R.id.forgot_pwd:
                intent=new Intent(LoginActivity.this,ForgotActivity.class);
                startActivity(intent);
                break;
            case R.id.register_tv:
                intent=new Intent(LoginActivity.this,Register1Activity.class);
                startActivity(intent);
                break;
            case R.id.request_test_code:
                getTestCode();
                break;

        }
    }

    /*登录方法*/
    private void login() {
        /*调用GetDeviceId类的getDeviceId()方法
        * 需要自己去研究一下加密方式
        * */
        phoneId=GetDeviceId.getDeviceId(this);//获取phoneId，并加密
        userName=userName_et.getText().toString();
        passWord=passWord_et.getText().toString();

        //&&还具有短路的功能，即如果第一个表达式为false
        if (userName.equals("")){
            Toast.makeText(LoginActivity.this,"账号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (passWord.equals("")){
            Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneId.equals("")){
            Toast.makeText(LoginActivity.this," 设置ID不能为空(IMES)",Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        * 对密码进行MD5加密
        * 自己去研究加密方法
        * */
        //http://121.201.66.138:8867/McangPartner/user.do?

        loginAsyncTask=new LoginAsyncTask();
        if (testLayout.getVisibility()==View.GONE){
            // action=login
            // &username=13647187944
            // &password=5FF7F42695637EE98EE63114566D605B
            loginAsyncTask.execute(userName,MD5Util.MD5Encode(passWord,"utf-8"),phoneId);
        }else {
            //http://121.201.66.138:8867/McangPartner/user.do?
            // action=loginAndChange
            // &phone=13647187944
            // &phoneId=e7487acf56ea1e3392cf3f028638947f
            // &smsCode=1798
            loginAsyncTask.execute(userName,phoneId,testCode_et.getText().toString());
        }
    }

    private class LoginAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*待优化*/
            progressDialog.setMessage("登录中...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        //http://121.201.66.138:8867/McangPartner/user.do?
        // action=login
        // &username=13647187944
        // &password=5FF7F42695637EE98EE63114566D605B
        // &phoneId=e7487acf56ea1e3392cf3f028638946f

        //http://121.201.66.138:8867/McangPartner/user.do?
        // action=loginAndChange
        // &phone=13647187944
        // &phoneId=e7487acf56ea1e3392cf3f028638947f
        // &smsCode=1798
        @Override
        protected String doInBackground(String... params) {
            OkHttpClient okHttpClient=new OkHttpClient();
            String url=Constants.urlHead+"user.do";
            if (testLayout.getVisibility()==View.GONE){
                body=new FormBody.Builder()
                        .add("action","login")
                        .add("username",params[0])
                        .add("password",params[1])
                        .add("phoneId",params[2])
                        .build();
            }else {
                body=new FormBody.Builder()
                         .add("action","loginAndChange")
                        .add("phone",params[0])
                        .add("phoneId",params[1])
                        .add("smsCode",params[2])
                        .build();
            }
            request=new Request.Builder().url(url).post(body).build();
            String result;
            try {
                Response response=okHttpClient.newCall(request).execute();
                if (response.isSuccessful()){
                    result=response.body().string();
                }else{
                    result=Constants.CONNECT_FAIL;
                }
            } catch (IOException e) {
                result=Constants.CONNECT_FAIL;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (Constants.CONNECT_FAIL.equals(s)) {
                Toast.makeText(LoginActivity.this, Constants.CONNECT_FAIL, Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject object = JSON.parseObject(s);
            try {
                if (object.getString("result").equals("登录成功")) {
                    editor.putString("isLogin", "1");
                    editor.putString("username", userName);
                    editor.putString("password", passWord);
                    editor.putString("address", object.getString("address"));//收货地址
                    editor.putString("createDate", object.getString("createDate"));//注册日期
                    editor.putString("createTime", object.getString("createTime"));//注册时间
                    editor.putString("credit", object.getString("credit"));//积分
                    editor.putString("headImgSrc", object.getString("headImgSrc"));//头像图片
                    editor.putString("identity", object.getString("identity"));//身份：2表示代理
                    editor.putString("isRealName", object.getString("isRealName"));//是否实名认证
                    editor.putString("isSalesman", object.getString("isSalesman"));//1表示业务员
                    editor.putString("merId", object.getString("merId"));//用户id
                    editor.putString("nickname", object.getString("nickname"));
                    editor.putString("phoneNum", object.getString("phoneNum"));
                    editor.putString("token", object.getString("token"));
                    editor.putString("wallet", object.getString("wallet"));//钱包
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                } else {
                    alertDialog.setTitle("提示");
                    if (object.getString("result").equals("设备不匹配")) {
                        alertDialog.setMessage("检测到您在新设备上登录，是否重新登录？");
                        testLayout.setVisibility(View.VISIBLE);//设置显示控件
                    } else {
                        alertDialog.setMessage(object.getString("result"));
                    }
                    alertDialog.setPositiveButton("确定", null);
                    alertDialog.create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void  getTestCode(){
        //http://121.201.66.138:8867/McangPartner/sms.do?
        // &action=changeAndLogin
        // &phoneNum=13647187944
        String phone=userName_et.getText().toString().trim();
        if (phone.equals("")){
            Toast.makeText(LoginActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        setTime();
        String url=Constants.urlHead+"sms.do";
        OkHttpClient client=new OkHttpClient();
        body=new FormBody.Builder()
                .add("action","changeAndLogin")
                .add("phoneNum",phone)
                .build();
        request=new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /*显示倒计时时间*/
    private void setTime(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=59;i>=0;i--){
                    Message msg=new Message();
                    msg.obj=i;//存值
                    msg.what=1;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);//让线程沉睡1s，实现间隔1s
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what==1){
                int i= (int) message.obj;//保存返回的值
                if (i==0){
                    requestTestCode_tv.setText("重新获取");
                    requestTestCode_tv.setClickable(true);//让TextView可点击
                }else{
                    requestTestCode_tv.setText(i+"s");
                    requestTestCode_tv.setClickable(false);//计时没有结束前没不能点击
                }
            }
            return false;
        }
    });
}