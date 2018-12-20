package com.mic.zl.micangpartner.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mic.zl.micangpartner.activity.MachineDetailActivity;
import com.mic.zl.micangpartner.util.Constants;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MachineExchangeTask extends AsyncTask<String,Void,String> {
    private Context context;

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
        JSONArray data=object.getJSONArray("data");
    }

}
