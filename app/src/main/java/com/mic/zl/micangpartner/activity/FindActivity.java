package com.mic.zl.micangpartner.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FindActivity extends AppCompatActivity {
    private Context context=FindActivity.this;
    private SharedPreferences sp;
    private ImageView headPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        init();
    }

    private void init() {
        EventBus.getDefault().register(this);//注册EventBus
        sp = getSharedPreferences("mcPartner", Activity.MODE_PRIVATE);
        headPic=findViewById(R.id.head_pic);
    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent msg){
        if (msg.getMsg().equals("FindActivity")){
            Glide.with(this)
                    .load(sp.getString("headPic",""))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(headPic);
        }
    }*/


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
