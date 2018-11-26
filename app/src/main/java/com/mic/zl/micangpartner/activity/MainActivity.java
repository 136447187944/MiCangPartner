package com.mic.zl.micangpartner.activity;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.fragment.HomeFragment;
import com.mic.zl.micangpartner.fragment.MineFragment;
import com.mic.zl.micangpartner.fragment.ShareGainFragment;
import com.mic.zl.micangpartner.task.CheckUpdateAsyncTask;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView index_iv,shareGain_iv,mine_iv;
    private TextView  index_tv,shareGain_tv,mine_tv;
    private LinearLayout index_ll,shareGain_ll,mine_ll;
    private FrameLayout replace_fl;
    private Fragment fragment;
    private FragmentTransaction ft;
    private List<AsyncTask> listTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
        listTask=new ArrayList<>();
        //首页
        index_iv=findViewById(R.id.index_iv);
        index_tv=findViewById(R.id.index_tv);
        index_ll=findViewById(R.id.index_ll);
        replace_fl=findViewById(R.id.replace_fl);
        //分润
        shareGain_tv=findViewById(R.id.share_gain_tv);
        shareGain_iv=findViewById(R.id.share_gain_iv);
        shareGain_ll=findViewById(R.id.share_gain_ll);
        //我的
        mine_tv=findViewById(R.id.mine_tv);
        mine_iv=findViewById(R.id.mine_iv);
        mine_ll=findViewById(R.id.mine_ll);

        /*监听事件*/
        //首页
        index_ll.setOnClickListener(this);
        //分润
        shareGain_ll.setOnClickListener(this);
        //我的
        mine_ll.setOnClickListener(this);

    }

    private void initData(){
        /*默认显示页面的图标指示*/
        index_iv.setImageResource(R.mipmap.mall_img_yes);
        index_tv.setTextColor(getResources().getColor(R.color.colorAccent));

        //检查版本更新
        CheckUpdateAsyncTask updateTask=new CheckUpdateAsyncTask( MainActivity.this);
        updateTask.execute();
        listTask.add(updateTask);

        /*设置默认显示页面*/
        fragment=new HomeFragment();
        ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.replace_fl,fragment)
                .show(fragment)//显示当前页面
                .commit();//提交
    }

    @Override
    public void onClick(View view) {
        Animation animation;
        switch (view.getId()){
            case R.id.index_ll:
                animation=AnimationUtils.loadAnimation(MainActivity.this,R.anim.fragment_anim);
                replace_fl.setAnimation(animation);
                setSelect(0);
                setFragment(0);
                break;
            case R.id.share_gain_ll:
                animation=AnimationUtils.loadAnimation(MainActivity.this,R.anim.fragment_anim);
                replace_fl.setAnimation(animation);
                setSelect(1);
                setFragment(1);
                break;
            case R.id.mine_ll:
                animation=AnimationUtils.loadAnimation(MainActivity.this,R.anim.fragment_anim);
                replace_fl.setAnimation(animation);
                setSelect(2);
                setFragment(2);
                break;
            default:break;
        }
    }

    /*设置对应的图标*/
    private void setSelect(int i){
        index_iv.setImageResource(R.mipmap.mall_img_no);
        index_tv.setTextColor(getResources().getColor(R.color.gray));
        shareGain_iv.setImageResource(R.mipmap.find_img_no);
        shareGain_tv.setTextColor(getResources().getColor(R.color.gray));
        mine_iv.setImageResource(R.mipmap.own_img_no);
        mine_tv.setTextColor(getResources().getColor(R.color.gray));
        switch (i){
            case 0:
                index_iv.setImageResource(R.mipmap.mall_img_yes);
                index_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case 1:
                shareGain_iv.setImageResource(R.mipmap.find_img_yes);
                shareGain_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case 2:
                mine_iv.setImageResource(R.mipmap.own_img_yes);
                mine_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
        }
    }

    /*设置对应的Fragment*/
    private  void setFragment(int i){
        switch (i){
            case 0:
                fragment=new HomeFragment();
                break;
            case 1:
                fragment=new ShareGainFragment();
                break;
            case 2:
                fragment=new MineFragment();
                break;
        }
        ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.replace_fl,fragment)
                .show(fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (AsyncTask task:listTask){
            if (task!=null&&task.getStatus()==AsyncTask.Status.RUNNING){
                task.cancel(true);
            }
        }

    }
}
