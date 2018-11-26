package com.mic.zl.micangpartner.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mic.zl.micangpartner.R;

public class NotifyActivity extends AppCompatActivity {
    private TextView title,content,time;
    private ImageView back;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        sp = getSharedPreferences("mcPartner", Activity.MODE_PRIVATE);
        title=findViewById(R.id.title_tv);
        back=findViewById(R.id.back_iv);
        content=findViewById(R.id.content_tv);
        time=findViewById(R.id.time_tv);

        title.setText("通知");
        content.setText(sp.getString("notify","米仓伙伴更新已经上线了！！！"));
        time.setText(sp.getString("createDate",""));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
