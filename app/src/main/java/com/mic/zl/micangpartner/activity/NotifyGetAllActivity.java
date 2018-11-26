package com.mic.zl.micangpartner.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.task.GetAllNotifyAsyncTask;

public class NotifyGetAllActivity extends AppCompatActivity {
    private ListView listView;
    private TextView title;
    private ImageView back;
    private  GetAllNotifyAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_get_all);
        listView=findViewById(R.id.notify_lv);
        title=findViewById(R.id.title_tv);
        back=findViewById(R.id.back_iv);

        task=new GetAllNotifyAsyncTask(NotifyGetAllActivity.this,listView);
        task.execute();

        title.setText("通知");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task!=null && task.getStatus()==AsyncTask.Status.RUNNING){
            task.cancel(true);//当页面被销毁是结束任务执行
            //task.cancel(false);//当页面被销毁是任务会继续执行完
        }
    }
}
