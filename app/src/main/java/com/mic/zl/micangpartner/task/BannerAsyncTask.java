package com.mic.zl.micangpartner.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.image.SmartImageView;
import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.adapter.BannerAdapter;
import com.mic.zl.micangpartner.util.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BannerAsyncTask extends AsyncTask<String,Void,String> {
    private List<String> listUrl;
    private List<String>listPinfo;
    private SmartImageView banner_background;
    private RadioGroup banner_rg;
    private Context context;
    private ViewPager viewPager;
    private Activity activity;
    private  OkHttpClient client=new OkHttpClient();



    public BannerAsyncTask(SmartImageView banner_background,
                           RadioGroup banner_rg, Context context, ViewPager viewPager,Activity activity) {
        this.banner_background = banner_background;
        this.banner_rg = banner_rg;
        this.context = context;
        this.viewPager = viewPager;
        this.activity = activity;
        viewPager.setCurrentItem(Integer.MAX_VALUE/2);

    }

    @Override
    protected String doInBackground(String... strings) {
        //OkHttpClient client=new OkHttpClient();
        String url=Constants.urlHead+"picture.do";
        RequestBody body=new FormBody
                .Builder()
                .add("action","getAllPicture")
                .build();
        Request request=new Request
                .Builder()
                .url(url)
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
        JSONArray jsonArray=object.getJSONArray("data");
        listUrl=new ArrayList<>();
        listPinfo=new ArrayList<>();
        for (int i=0;i<jsonArray.size();i++){
            listUrl.add(jsonArray.getJSONObject(i).getString("pimages"));
            listPinfo.add(jsonArray.getJSONObject(i).getString("pinfo"));
            RadioButton dot=new RadioButton(context);
            dot.setBackgroundResource(R.drawable.banner_dot_selector);
            dot.setButtonDrawable(android.R.color.transparent);
            RadioGroup.LayoutParams params=new  RadioGroup.LayoutParams(20,20);
            if (i!=0){
                params.leftMargin=10;
            }
            banner_rg.addView(dot,params);
        }
        BannerAdapter adapter=new BannerAdapter(context,listUrl,listPinfo,banner_background,banner_rg,viewPager,activity);
        viewPager.setAdapter(adapter);
    }
}
