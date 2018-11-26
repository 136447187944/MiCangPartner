package com.mic.zl.micangpartner.activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
import com.mic.zl.micangpartner.util.MD5Util;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 *On Create 2018.11.9
 * */
public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {
 private TextView forgot_title_tv,forgot_request_test_code,forgot_submit_tv;
 private EditText forgot_username_et,forgot_test_code_et,forgot_input_pwd_et,forgot_password_et;
 private ImageView back_imgView;
 private SharedPreferences sp;
 private OkHttpClient client;
 private RequestBody body;//post数据
 private String url;//请求路径
 private Request request;//请求
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        init();
    }

    private void init(){

        /*创建数据访问对象*/
        sp=getSharedPreferences("mcPartner",Context.MODE_PRIVATE);

        /*初始化控件*/
        forgot_title_tv=findViewById(R.id.title_tv);//标题
        forgot_username_et=findViewById(R.id.forgot_username_et);//账号
        forgot_request_test_code=findViewById(R.id.forgot_request_test_code);//请求获取验证码
        forgot_submit_tv=findViewById(R.id.forgot_submit_tv);//登录按钮
        forgot_test_code_et=findViewById(R.id.forgot_test_code_et);//验证码
        forgot_input_pwd_et=findViewById(R.id.forgot_input_pwd_et);//输入密码
        forgot_password_et=findViewById(R.id.forgot_password_et);//确认密码
        back_imgView=findViewById(R.id.back_iv);//返回键

        /*初始化数据*/
        forgot_title_tv.setText(R.string.pwd_find);//设置标题
        forgot_username_et.setText(sp.getString("username",""));//填写账号
        url=Constants.urlHead+"user.do";//连接地址

        /*创建监听*/
        forgot_request_test_code.setOnClickListener(this);//请求验证码
        forgot_submit_tv.setOnClickListener(this);//提交按钮
        back_imgView.setOnClickListener(this);//返回按钮
    }

    
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgot_request_test_code:getTestCode();
                break;
            case R.id.forgot_submit_tv:submit();
                break;
            case R.id.back_iv:finish();
                break;
        }
    }

    /*请求获取验证码*/
    private void getTestCode() {
        String uname=forgot_username_et.getText().toString();
        if("".equals(uname)){
            Toast.makeText(ForgotActivity.this,R.string.username_not_empty,Toast.LENGTH_SHORT).show();
            return;
        }
        if (uname.length()<11){
            Toast.makeText(ForgotActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
            return;
        }

        timer.start();//启动到计时器
        url=Constants.urlHead+"sms.do";
        client= new OkHttpClient();
        body=new FormBody.Builder()
                .add("action","forgetPwd")
                .add("phoneNum",uname.trim())
                .build();
        request=new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject object=JSON.parseObject(response.body().string());
                Message message=new Message();
                message.obj=object.getString("result");
                message.what=3;
                handler.sendMessage(message);
            }
        });
    }

    

    /*提交方法*/
    private void submit() {
        String uname=forgot_username_et.getText().toString();
        String smsCode=forgot_request_test_code.getText().toString();
        String newPwd=forgot_input_pwd_et.getText().toString();
        String confirm=forgot_password_et.getText().toString();
        if ("".equals(uname)){
            Toast.makeText(ForgotActivity.this,R.string.username_not_empty,Toast.LENGTH_SHORT).show();
            return;
        }
        /*验证码*/
        if (smsCode.equals("")){
            Toast.makeText(ForgotActivity.this,R.string.test_code_not_empty,Toast.LENGTH_SHORT).show();
            return;
        }
        /*新密码*/
        if (newPwd.equals("")){
            Toast.makeText(ForgotActivity.this,R.string.pwd_not_empty,Toast.LENGTH_SHORT).show();
            return;
        }
        if (!FormatTool.testPwd(newPwd)){
            Toast.makeText(ForgotActivity.this,R.string.pwd_register_input,Toast.LENGTH_SHORT).show();
            return;
        }
        /*确认密码*/
        if (confirm.equals("")){
            Toast.makeText(ForgotActivity.this,R.string.pwd_confirm,Toast.LENGTH_SHORT).show();
            return ;
        }
        /*比较两次输入的密码是否相等*/
        if (!confirm.equals(newPwd)){
            Toast.makeText(ForgotActivity.this,R.string.pwd_not_equal,Toast.LENGTH_SHORT).show();
            return ;
        }
        //http://121.201.66.138:8867/McangPartner/user.do?action=forgetPwd&uname=13647187944&newPwd=E10ADC3949BA59ABBE56E057F20F883E&smsCode=4592
        client=new OkHttpClient();
        body=new FormBody.Builder()
                .add("action","forgetPwd")
                .add("uname",uname)
                .add("newPwd",MD5Util.MD5Encode(newPwd,"utf-8"))
                .add("smsCode",smsCode)
                .build();
        request=new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject object=JSON.parseObject(response.body().string());
                if (object.getString("result").equals(getResources().getString(R.string.pwd_alter_success))){
                    finish();
                }else {
                    Message message=new Message();
                    message.obj=object.getString("result");
                    message.what=1;
                    handler.sendMessage(message);
                }
            }
        });
    }

    /*使用Handle更新UI*/
    private Handler handler=new Handler(new Handler.Callback() {//有返回值类型
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 0:
                    if (message.obj.toString().equals(getResources().getString(R.string.pwd_alter_success))) {
                        Toast.makeText(ForgotActivity.this,R.string.pwd_alter_success,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ForgotActivity.this,"密码修改失败，请重试",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    Toast.makeText(ForgotActivity.this,message.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    if ((int)message.obj==0){
                        forgot_request_test_code.setText(R.string.test_code_request_again);
                        forgot_request_test_code.setClickable(true);
                    }else {
                        forgot_request_test_code.setText((int)message.obj+"s");
                        forgot_request_test_code.setClickable(false);
                    }
                    break;
                case 3:
                    Toast.makeText(ForgotActivity.this,R.string.connect_fail,Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    /*倒计时方法1:倒计时器实现*/
    private CountDownTimer timer= new CountDownTimer(60000, 1000) {//arg1:倒计时总时间,arg2:倒计时时间间隔*/

           /*回调方法*/
            @Override
            public void onTick(long l) {//参数表示剩余时间
                forgot_request_test_code.setText(l/1000+"s");//显示时间
                forgot_request_test_code.setClickable(false);//倒计时期间不可点击
            }

            /*计时完成处理方法*/
            @Override
            public void onFinish() {
                forgot_request_test_code.setText(R.string.test_code_request_again);
                forgot_request_test_code.setClickable(true);//恢复点击功能
            }
        };

    /*倒计时方法1:倒计时器实现*/
    private void setTime(){
        new Thread(new Runnable() {
            Message message=Message.obtain();
            @Override
            public void run() {
                for (int i=59;i>=0;i--){
                    message.obj=i;
                    message.what=2;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
