package com.mic.zl.micangpartner.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.activity.NotifyActivity;
import com.mic.zl.micangpartner.activity.NotifyGetAllActivity;
import com.mic.zl.micangpartner.task.AppMenuAsyncTask;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView notify_tv;//消息通知
    private LinearLayout notify_layout;
    private ImageView notify_img;
    private RecyclerView menu_rv;
    private SharedPreferences sp;
    private String notify;
    private Intent intent;
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

        //初始化数据
        sp=getContext().getSharedPreferences("mcPartner", Activity.MODE_PRIVATE);
        notify=sp.getString("notify","米仓伙伴已经上线！");
        notify_tv.setText(notify);

        //获取主功能菜单
        AppMenuAsyncTask menuTask=new AppMenuAsyncTask(getContext(),menu_rv);
        menuTask.execute();

        notify_layout.setOnClickListener(this);
        notify_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.notify_layout: //跳转到详细的通知消息界面
                intent=new Intent(getContext(),NotifyActivity.class);
                startActivity(intent);
                break;
            case R.id.notify_img: //跳转到详细的通知消息界面
                intent=new Intent(getContext(),NotifyGetAllActivity.class);
                startActivity(intent);
                break;
        }
    }
}
