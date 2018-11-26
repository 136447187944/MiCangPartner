package com.mic.zl.micangpartner.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.util.Constants;
import com.mic.zl.micangpartner.util.FormatTool;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register1Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView title_tv,immediate_login,next_step_tv,test_code_tv;
    private EditText register_code_et,register_username_et,register_phone_et;
    private ImageView back_iv;
    private   String chineseName,phone,smsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        init();
    }

    private void init(){
        title_tv=findViewById(R.id.title_tv);//标题
        title_tv.setText(R.string.register);//显示标题
        back_iv=findViewById(R.id.back_iv);//返回按钮
        immediate_login=findViewById(R.id.immediate_login_tv);//立即登录按钮
        next_step_tv=findViewById(R.id.next_step_tv);//下一步按钮
        test_code_tv=findViewById(R.id.test_code_tv);//获取验证码
        register_code_et=findViewById(R.id.register_code_et);//验证码
        register_username_et=findViewById(R.id.register_username_et);//用户姓名
        register_phone_et=findViewById(R.id.register_phone_et);//手机号
        back_iv.setOnClickListener(this);
        next_step_tv.setOnClickListener(this);
        test_code_tv.setOnClickListener(this);
        immediate_login.setOnClickListener(this);
    }

    private void initData(){
        chineseName=register_username_et.getText().toString();
        phone=register_phone_et.getText().toString();
        smsCode=register_code_et.getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_iv:finish();break;
            case R.id.immediate_login_tv:finish();break;
            case R.id.test_code_tv:getSmsCode();break;
            case R.id.next_step_tv:nextStep();break;
        }
    }


    //   http://121.201.66.138:8867/McangPartner/sms.do?action=getSmsCode&phoneNum=13647187944
    /*获取验证码*/
    private void getSmsCode(){
        phone=register_phone_et.getText().toString();
        if ("".equals(phone)){
            Toast.makeText(Register1Activity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length()<11){
            Toast.makeText(Register1Activity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client=new OkHttpClient();
        String url=Constants.urlHead+"sms.do";
        RequestBody body=new FormBody.Builder()
                .add("action","getSmsCode")
                .add("phoneNum",phone)
                .build();
        Request request=new Request.Builder().url(url).post(body).build();
        setTime.start();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(Register1Activity.this,Constants.CONNECT_FAIL,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               final JSONObject jsonObject = JSON.parseObject(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register1Activity.this,jsonObject.getString("result"),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private CountDownTimer setTime=new CountDownTimer(60000,1000) {
        @Override
        public void onTick(long l) {
            test_code_tv.setClickable(false);
            test_code_tv.setText(l/1000+"s秒后重试");
        }

        @Override
        public void onFinish() {
            test_code_tv.setText(R.string.test_code_request_again);
            test_code_tv.setClickable(true);
        }
    };

    /*判断是否为空*/
    private boolean check(){
        if (chineseName.equals("")){
            Toast.makeText(Register1Activity.this,"名字不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }
        //判断是否为中文名字
        if (!FormatTool.isChinese(chineseName)){
            Toast.makeText(Register1Activity.this,"请输入中文名字",Toast.LENGTH_SHORT).show();
            return true;
        }
        if ("".equals(phone)){
            Toast.makeText(Register1Activity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!FormatTool.testPhone(phone)){
            Toast.makeText(Register1Activity.this,R.string.input_right_phone,Toast.LENGTH_SHORT).show();
            return true;
        }
        if ("".equals(smsCode)){
            Toast.makeText(Register1Activity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (smsCode.length()<4){
            Toast.makeText(Register1Activity.this,"请输入正确的验证码",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void nextStep(){
        initData();
        if (check()) return;
        Intent intent=new Intent(Register1Activity.this,Register2Activity.class);
        intent.putExtra("nickName",chineseName);
        intent.putExtra("username",phone);
        intent.putExtra("smsCode",smsCode);
        startActivity(intent);
    }
}
