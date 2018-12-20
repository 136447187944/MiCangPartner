package com.mic.zl.micangpartner.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.util.Constants;
import com.mic.zl.micangpartner.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DetailActivity extends AppCompatActivity {
    private ImageView headPic;
    private SharedPreferences sp;
    private Button downLoad;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    private void init(){
        sp=getSharedPreferences("mcPartner", Activity.MODE_PRIVATE);
        headPic=findViewById(R.id.head_pic);
        downLoad=findViewById(R.id.downLoad);
        downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownLoadTask downLoadTask=new DownLoadTask();
                downLoadTask.execute();
            }
        });

        EventBus.getDefault().register(this);
        Glide.with(this)
                .load(sp.getString("headPic",""))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(headPic);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(MessageEvent msg){
        if(msg.getMsg().equals("DetailActivity")){
            Glide.with(this)
                    .load(sp.getString("headPic",""))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(headPic);
        }
    }

    private class  DownLoadTask extends AsyncTask<String,Long,File> {
        //http://121.201.66.138:8867/McangPartner/download.servlet?path=D:/McangPartnertest/APP/Android/201811282043301.apk"
        // http://121.201.66.138:8867/McangPartner/version.do?action=getVersion&appType=android
        String url=Constants.urlHead+"download.servlet?path=D:/McangPartnertest/APP/Android/201811282043301.apk";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(DetailActivity.this);
            progressDialog.setTitle("正在下载...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置为样式为水平
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected File doInBackground(String... strings) {
            File dir=new File(Environment.getExternalStorageDirectory(),"/micang");//获取文件夹micang
            if(!dir.exists()) dir.mkdir();//如果文件不存在则创建
            final File file=new File(dir,"micang.apk");//文件存在本地的名字
            if (file.exists()) file.delete();
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream inputStream=response.body().byteStream();//获取文件输出形式
                    long contentLengths=response.body().contentLength();//文件总总大小,单位M
                    Log.i("tag","contentLengths:"+contentLengths);
                    int len;
                    long countLengths=0;//记录已经下载是数
                    long beforeTime = System.currentTimeMillis();
                    byte[] bit=new byte[1024];
                    FileOutputStream fos=new FileOutputStream(file);//存放的位置
                    while((len=inputStream.read(bit))!=-1){
                        countLengths+=len;
                        //Log.i("tag","countLengths:"+countLengths);
                        fos.write(bit,0,len);//写一次的最小单位,起始点,每一次写的长度
                        if (System.currentTimeMillis() - beforeTime > 100) {
                            publishProgress(countLengths, contentLengths);//前一个单次下载量，第二个总下载量
                            beforeTime = System.currentTimeMillis();
                        }
                    }
                    fos.flush();
                    fos.close();
                    inputStream.close();
                }
            });
            return file;
        }

        @Override
        protected void onProgressUpdate(Long... values) {// publishProgress(countLengths, contentLongth);
            super.onProgressUpdate(values);
            DecimalFormat format=new DecimalFormat("#.##");//设置小数点位数
            double content=values[1]/1024/1024;
            double count=values[0]/1024/1024;
            String contentLengths=format.format(content)+"M";
            String countLengths=format.format(count)+"M";
            int value=(int)(count/content*100);
            progressDialog.setTitle("正在下载... ("+countLengths+"/"+contentLengths+")");
            progressDialog.setProgress(value);
            Log.i("tag",""+value);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            progressDialog.dismiss();
            if (file==null || !file.exists()){
                Toast.makeText(DetailActivity.this,"下载失败！请重新下载",Toast.LENGTH_SHORT).show();
            }else Toast.makeText(DetailActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
        }
    }

}
