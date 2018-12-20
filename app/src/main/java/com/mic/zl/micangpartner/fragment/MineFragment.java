package com.mic.zl.micangpartner.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.activity.DetailActivity;
import com.mic.zl.micangpartner.activity.FindActivity;
import com.mic.zl.micangpartner.util.Constants;
import com.mic.zl.micangpartner.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MineFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView headPic;//头像
    private LinearLayout credit_layout;
    private Intent intent;//
    private PopupWindow popupWindow;//弹窗
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String imgUrl;//上传成功后的头像地址
    private List<AsyncTask> listTask;//存放AsyncTask

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view=inflater.inflate(R.layout.fragment_mine,null);
        initView();
        setListener();
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getActivity())
                .load(sp.getString("headPic",""))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(headPic);
    }

    /*初始化控件*/
    private void initView(){
        sp =getActivity().getSharedPreferences("mcPartner", Activity.MODE_PRIVATE);
        editor=sp.edit();
        headPic=view.findViewById(R.id.head_pic);
        credit_layout=view.findViewById(R.id.credit_layout);

    }

    /*初始化数据*/
    private void initData(){
        listTask=new ArrayList<>();
        Glide.with(getActivity())
                .load(sp.getString("headPic",""))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(headPic);
    }

    /*设置听见*/
    private void setListener(){
        headPic.setOnClickListener(this);
        credit_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_pic:
                UpLoadHeadPicTask  upLoadTask=new UpLoadHeadPicTask();
                upLoadTask.execute();
                listTask.add(upLoadTask);
                break;
            case R.id.credit_layout:
                EventBus.getDefault().post(new MessageEvent("DetailActivity"));//发送消息
                Intent intent=new Intent(getActivity(),DetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    /*上传头像*/
    class UpLoadHeadPicTask extends AsyncTask<File, Long, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(File... files) {
            File file =new File( Environment.getExternalStorageDirectory(),"kabaunion/small.jpg");//获取文件的存储路劲
            if (!file.exists()){
                Log.i("tag",file.getAbsolutePath()+" not exist");
                return null;
            }
            String fileType="image/png";//指定文件类型
            String url=Constants.urlHead+"upload.servlet";
            String result;
            OkHttpClient client=new OkHttpClient();
            RequestBody fileBody=RequestBody.create(MediaType.parse(fileType),file);//构造上传文件的参数
            RequestBody body=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)//设置提交方式为表单式提交
                    .addFormDataPart("type","uh")//请求数据
                    .addFormDataPart("img","head.jpg",fileBody)//参数：arg1:key,arg2:上传图片的名字,arg3:带有图片地址的请求body
                    .build();
            Request request=new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response=client.newCall(request).execute();
                if (!response.isSuccessful()){
                    result=Constants.CONNECT_FAIL;
                    return result;
                }
                result=response.body().string();
                JSONObject object= JSON.parseObject(result);
                JSONArray jsonArray=object.getJSONArray("data");
                if (object.getString( "result").equals("上传成功")){
                    imgUrl=jsonArray.getJSONObject(0).getString("imgSrc");
                    editor.putString("headPic",imgUrl);
                    Log.i("tag","imgUrl:"+imgUrl);
                    editor.commit();
                    Request requestResult=new Request.Builder()
                            .url(Constants.urlHead+"user.do?action=upUserHead&merId=" + sp.getString("merId","") + "&headImg=" + imgUrl)
                            .build();
                    Response responseResult=client.newCall(requestResult).execute();
                    if (!responseResult.isSuccessful()){
                        result=Constants.CONNECT_FAIL;
                    }else result=responseResult.body().string();Log.i("tag",result);
                    return  result;
                }
            } catch (IOException e) {
                result=Constants.CONNECT_FAIL;
                return result;
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals(Constants.CONNECT_FAIL)){
                Toast.makeText(getActivity(),Constants.CONNECT_FAIL,Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject object=JSON.parseObject(result);
            //File file =new File( Environment.getExternalStorageDirectory(),"kabaunion/small.jpg");
            if (object.getString("result").equals("头像修改成功")){
                Glide.with(getActivity())
                        .load(imgUrl)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(headPic);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (AsyncTask task:listTask){
            if (task!=null&&task.isCancelled()&&task.getStatus()==AsyncTask.Status.RUNNING){
                task.cancel(true);//true代表结束当前的线程，false继续执行
                task=null;//将当前位置的
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

