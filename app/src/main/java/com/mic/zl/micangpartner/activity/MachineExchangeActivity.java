package com.mic.zl.micangpartner.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.util.Constants;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MachineExchangeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title,goodsName_left,goodsPrice_left,goodsInfo_left,
            goodsName_right,goodsPrice_right,goodsInfo_right;
    private ImageView back;
    private String goodsId,goodsImgSrc,
            goodsInfo,goodsName,goodsPrice;
    private RelativeLayout machineLayout_left,machineLayout_right;
    private Context context=MachineExchangeActivity.this;
    private JSONArray data;
    private   MachineExchangeTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_exchange);
        init();
        task=new MachineExchangeTask();
        task.execute();
    }

    private void init(){

        //标题栏
        title=findViewById(R.id.title_tv);
        title.setText(R.string.back);
        back=findViewById(R.id.back_iv);

        //支付通
        goodsName_left=findViewById(R.id.machine_name_left_tv);//商品名
        goodsInfo_left=findViewById(R.id.goods_info_left_tv);//商品描述信息
        goodsPrice_left=findViewById(R.id.machine_price_left_tv);//商品价格
        machineLayout_left=findViewById(R.id.machine_layout_left);//Layout点击事件

        //海科融通
        goodsName_right=findViewById(R.id.machine_name_right_tv);
        goodsInfo_right=findViewById(R.id.goods_info_right_tv);
        goodsPrice_right=findViewById(R.id.machine_price_right_tv);
        machineLayout_right=findViewById(R.id.machine_layout_right);

        //监听
        back.setOnClickListener(this);
        goodsName_left.setOnClickListener(this);
        machineLayout_left.setOnClickListener(this);
        goodsName_right.setOnClickListener(this);
        machineLayout_right.setOnClickListener(this);
    }

    class MachineExchangeTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client=new OkHttpClient();
            RequestBody body=new FormBody.Builder()
                    .add("action","getGoodsByStat")
                    .add("Stat","A")
                    .build();
            Request request=new Request.Builder()
                    .url(Constants.urlHead+"goods.do")
                    .post(body)
                    .build();
            String result;

            try {
                Response response=client.newCall(request).execute();
                if (!response.isSuccessful()){
                    result=Constants.CONNECT_FAIL;
                }else result=response.body().string();
            } catch (IOException e) {
                result=Constants.CONNECT_FAIL;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject object=JSON.parseObject(result);
            data=object.getJSONArray("data");
            //支付通
            goodsName_left.setText(data.getJSONObject(0).getString("goodsName"));
            goodsInfo_left.setText(data.getJSONObject(0).getString("goodsInfo"));
            goodsPrice_left.setText(data.getJSONObject(0).getString("price"));
            //海科融通
            goodsName_right.setText(data.getJSONObject(1).getString("goodsName"));
            goodsInfo_right.setText(data.getJSONObject(1).getString("goodsInfo"));
            goodsPrice_right.setText(data.getJSONObject(1).getString("price"));
        }
    }

    private void setActivity(String goodsName) {
        if (data.getJSONObject(0).getString("goodsName").equals(goodsName)){
            goodsImgSrc=data.getJSONObject(0).getString("goodsImgSrc");
            goodsInfo=data.getJSONObject(0).getString("goodsInfo");
            goodsPrice=data.getJSONObject(0).getString("price");
            goodsId=data.getJSONObject(0).getString("goodsId");
            goodsName=data.getJSONObject(0).getString("goodsName");
        }else if (data.getJSONObject(1).getString("goodsName").equals(goodsName)){
            goodsImgSrc=data.getJSONObject(1).getString("goodsImgSrc");
            goodsInfo=data.getJSONObject(1).getString("goodsInfo");
            goodsPrice=data.getJSONObject(1).getString("price");
            goodsId=data.getJSONObject(1).getString("goodsId");
            goodsName=data.getJSONObject(1).getString("goodsName");
        }
        Intent intent = new Intent(context, MachineDetailActivity.class);
        intent.putExtra("goodsImgSrc", goodsImgSrc);
        intent.putExtra("goodsInfo", goodsInfo);
        intent.putExtra("goodsName", goodsName);
        intent.putExtra("goodsId", goodsId);
        intent.putExtra("price", goodsPrice);
        Log.i("goodName",goodsName);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.machine_layout_left:
                goodsName=goodsName_left.getText().toString();
                setActivity(goodsName);
                finish();
                break;
            case R.id.machine_name_left_tv:
                goodsName=goodsName_left.getText().toString();
                setActivity(goodsName);
                finish();
                break;
            case R.id.machine_layout_right:
                goodsName=goodsName_right.getText().toString();
                setActivity(goodsName);
                finish();
                break;
            case R.id.machine_name_right_tv:
                goodsName=goodsName_right.getText().toString();
                setActivity(goodsName);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task!=null&&task.getStatus()==AsyncTask.Status.RUNNING){
            task.cancel(true);
        }
    }
}
