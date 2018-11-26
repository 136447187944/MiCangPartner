package com.mic.zl.micangpartner.task;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mic.zl.micangpartner.dialog.MyDialog;
import com.mic.zl.micangpartner.util.Constants;

import org.w3c.dom.Text;

import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//检测更新
public class CheckUpdateAsyncTask extends AsyncTask<String,Void,String> {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context context;
    private MyDialog myDialog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public CheckUpdateAsyncTask(Context context) {
        this.context = context;
        sp =context.getSharedPreferences("mcPartner", Activity.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client=new OkHttpClient();
        String url=Constants.urlHead+"version.do?";
        RequestBody requestBody=new FormBody.Builder()
                .add("action","getVersion")
                .add("appType","android")
                .build();
        Request request=new Request.Builder().url(url).post(requestBody).build();
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
        if (Constants.CONNECT_FAIL.equals(result)){
            Toast.makeText(context,Constants.CONNECT_FAIL,Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject object=JSON.parseObject(result);
        final String notify=object.getString("notify");;
        if (object.getString("result").equals("获取成功")){
            if (!"".equals(notify)&&!TextUtils.isEmpty(notify)){
                    if (!sp.getBoolean(notify,false)){
                        myDialog=new MyDialog(context);
                        myDialog.setTitle("通知")
                                .setMessage(notify)
                                .setNegativeName("知道了");
                        myDialog.setCanceledOnTouchOutside(false);//不允许点击空白处退出
                        myDialog.create();
                        myDialog.show();
                        editor.putBoolean(notify,true);
                        editor.apply();
                    }
            }
            editor.putString("createDate",object.getString("createDate"));//创建时间
            editor.putString("downSrc",object.getString("downSrc"));//下载路径
            editor.putString("isForce",object.getString("isForce"));//版本是否发布
            editor.putString("notify",notify);//消息
            editor.putString("registerSrc",object.getString("registerSrc"));//注册链接
            editor.putString("version",object.getString("version"));//SDK版本号
            editor.commit();
        }
    }
}
