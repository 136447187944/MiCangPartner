package com.mic.zl.micangpartner.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mic.zl.micangpartner.adapter.NotifyListViewAdapter;
import com.mic.zl.micangpartner.util.Constants;


import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetAllNotifyAsyncTask  extends AsyncTask<String,Void,String> {
    private Context context;
    private ProgressDialog dialog;
    private ListView notifyListView;

    public GetAllNotifyAsyncTask(Context context, ListView notifyListView) {
        this.context = context;
        this.notifyListView = notifyListView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog= new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setMessage("加载中...");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        dialog.dismiss();
        String result;
        String url=Constants.urlHead+"app.do";
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder().add("action","getAllNotify").build();
        Request request=new Request.Builder().url(url).post(body).build();
        try {
            Response response=client.newCall(request).execute();
            if (response.isSuccessful()){
                result=response.body().string();
            }else result=Constants.CONNECT_FAIL;
        } catch (IOException e) {
            result=Constants.CONNECT_FAIL;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        JSONObject object=JSON.parseObject(result);
        JSONArray jsonArray=object.getJSONArray("data");
        NotifyListViewAdapter adapter=new NotifyListViewAdapter(context,jsonArray);
        notifyListView.setAdapter(adapter);
    }
}
