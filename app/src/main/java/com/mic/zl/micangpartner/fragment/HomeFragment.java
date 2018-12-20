package com.mic.zl.micangpartner.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.loopj.android.image.SmartImageView;
import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.activity.NotifyActivity;
import com.mic.zl.micangpartner.activity.NotifyGetAllActivity;
import com.mic.zl.micangpartner.adapter.BannerAdapter;
import com.mic.zl.micangpartner.task.AppMenuAsyncTask;
import com.mic.zl.micangpartner.task.BannerAsyncTask;
import com.mic.zl.micangpartner.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView notify_tv;//消息通知
    private LinearLayout notify_layout;
    private ImageView notify_img;
    private RecyclerView menu_rv;
    private RadioGroup banner_rg;
    private ViewPager viewPager;
    private SmartImageView banner_background;
    private SharedPreferences sp;
    private String notify;
    private Intent intent;
    private List<AsyncTask> listTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,null);
        initView();
        return view;
    }

    private void initView(){
        //初始化控件
        notify_tv=view.findViewById(R.id.notify_text_tv);
        notify_layout=view.findViewById(R.id.notify_layout);
        notify_img=view.findViewById(R.id.notify_img);//消息通知图片
        menu_rv=view.findViewById(R.id.recycler_img);
        banner_rg=view.findViewById(R.id.banner_dot_rg);
        banner_background=view.findViewById(R.id.banner_background);
        viewPager=view.findViewById(R.id.banner_img);
        viewPager.setCurrentItem(Integer.MAX_VALUE/2);

        //初始化数据
        listTask = new ArrayList<>();
        sp = getContext().getSharedPreferences("mcPartner", Activity.MODE_PRIVATE);
        notify = sp.getString("notify", "米仓伙伴已经上线！");
        if (notify.length()>16){
            notify_tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);//超出长度后不隐藏文字
            notify_tv.setFocusable(true);//获取焦点
            notify_tv.setFocusableInTouchMode(true);
            notify_tv.setText(notify);

        }else {
            notify_tv.setText(notify);
        }

        //获取主功能菜单
        AppMenuAsyncTask menuTask=new AppMenuAsyncTask(getContext(),menu_rv);
        menuTask.execute();
        listTask.add(menuTask);

        //轮播图
        BannerAsyncTask bannerTask=new BannerAsyncTask(banner_background,banner_rg,getContext(),viewPager,getActivity());
        bannerTask.execute();
        listTask.add(bannerTask);

        notify_layout.setOnClickListener(this);
        notify_img.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notify_layout: //跳转到详细的通知消息界面
                intent = new Intent(getContext(), NotifyActivity.class);
                startActivity(intent);
                break;
            case R.id.notify_img: //跳转到详细的通知消息界面
                intent = new Intent(getContext(), NotifyGetAllActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (AsyncTask task:listTask){
            if (task!=null&&task.isCancelled()&&task.getStatus()==AsyncTask.Status.RUNNING){
                task.cancel(true);
                task=null;
            }
        }
    }
}


