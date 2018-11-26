package com.mic.zl.micangpartner.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mic.zl.micangpartner.adapter.MenuRecyclerViewAdapter;
import com.mic.zl.micangpartner.util.Constants;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppMenuAsyncTask extends AsyncTask<String,Void,String> {
    private List<String> listMenuIcon;//存放菜单图片链接
    private List<String> listMenuName;//存放菜单名称
    private Context context;
    private RecyclerView menuView;

    public AppMenuAsyncTask(Context context, RecyclerView menuView) {
        this.context = context;
        this.menuView = menuView;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url=Constants.urlHead+"version.do";
        OkHttpClient client=new OkHttpClient();
        //version.do?action=getAllAppMenu&brand=android&version=?
        RequestBody body=new FormBody.Builder()
                .add("action","getAllAppMenu")
                .add("brand","android")
                .build();
        Request request=new Request.Builder().url(url).post(body).build();
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
        JSONArray jsonArray=object.getJSONArray("data");
         listMenuIcon=new ArrayList<>();
         listMenuName=new ArrayList<>();
        for (int i=0;i<jsonArray.size();i++){
            listMenuName.add(jsonArray.getJSONObject(i).getString("menuName"));
            listMenuIcon.add(jsonArray.getJSONObject(i).getString("menuIcon"));
        }
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        menuView.setLayoutManager(manager);
        MenuRecyclerViewAdapter adapter=new MenuRecyclerViewAdapter(listMenuIcon,listMenuName,context);
        menuView.setAdapter(adapter);
    }
}
