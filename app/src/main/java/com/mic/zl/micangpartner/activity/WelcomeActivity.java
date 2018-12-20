package com.mic.zl.micangpartner.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mic.zl.micangpartner.R;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView bg_imageView;//背景
    private TextView  version_tv;//版本号
    private SharedPreferences sp;
    private String[] permissions={
            Manifest.permission.CAMERA,//相机
            Manifest.permission.ACCESS_WIFI_STATE,//网络
            Manifest.permission.READ_EXTERNAL_STORAGE,//读取外部存储文件
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写入外部存储文件
            Manifest.permission.ACCESS_WIFI_STATE,//允许访问wifi
            Manifest.permission.READ_PHONE_STATE//允许读取手机状态
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置为全屏
          setContentView(R.layout.activity_welcome);
        init();
    }
    private void init(){
        sp=getSharedPreferences("mcPartner",Activity.MODE_PRIVATE);
        /**显示版本号*/
        version_tv=findViewById(R.id.version_tv);//版本号
        try {
            PackageInfo pkInfo=getPackageManager().getPackageInfo(getApplication().getPackageName(),0);
            String curVersion=pkInfo.versionName;
            version_tv.setText("版本号V"+curVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        /**进入时的动画
         * 第一步：创建淡出动画,消失时间为Activity的生命时间
         * 第二步：添加动画监听事件
         * 第三步：在动画完成方法onAnimationEnd()中编写相应的业务逻辑
         * */
        bg_imageView=findViewById(R.id.bg_iv);//最外层遮罩层
        Animation animation=AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.alpha);//加载淡出动画
        bg_imageView.startAnimation(animation);//启动动画
        /*添加动画监听器*/
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /*API23及以上就需要询问权限问题*/
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    getPermission();
                }else {
                    setGuides();//进去指南
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        bg_imageView.setAnimation(animation);//为遮罩层设置动画
    }

    /**逻辑：首先判断是不是第一次打开，若是进入GuidesActivity，
     * 否接着判断是否已经登陆，是进入MainActivity，
     * 否则进入LoginActivity。
     * isLogin记录登陆的状态：1表示未登陆
     * */
    private void setGuides() {
        String key="_first";
        SharedPreferences.Editor editor=sp.edit();
        String values=sp.getString(key,"");

        /*判断是不是首次打开，是进去指南界面，否则进入*/
        if (!"No".equals(values)){
            values="No";
            editor.putString(key,values);
            editor.commit();
            Intent intent=new Intent(WelcomeActivity.this,GuidesActivity.class);
            startActivity(intent);
            finish();
        }else{
            if (sp.getString("isLogin","").equals("1")){//判断是否已经登陆过了，是进入主界面
                Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(WelcomeActivity.this,permissions,1);//获取权限
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
