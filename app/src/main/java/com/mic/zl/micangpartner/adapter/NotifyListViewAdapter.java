package com.mic.zl.micangpartner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.mic.zl.micangpartner.R;


public class NotifyListViewAdapter extends BaseAdapter {
    private Context context;
    private JSONArray dataArray;//数据集
    private ViewHolder vh;

    public NotifyListViewAdapter(Context context, JSONArray dataArray) {
        this.context = context;
        this.dataArray = dataArray;
    }

    @Override
    public int getCount() {
        return dataArray.size();
    }

    @Override
    public Object getItem(int position) {
        return dataArray.getJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        vh=new ViewHolder();
        if (view==null){
            view=LayoutInflater.from(context).inflate(R.layout.notify_layout,parent,false);
            vh.notify_tv=view.findViewById(R.id.notify_content_tv);
            vh.notify_time_tv=view.findViewById(R.id.notify_time);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        vh.notify_time_tv.setText(dataArray.getJSONObject(position).getString("createDate"));//设置时间
        vh.notify_tv.setText(dataArray.getJSONObject(position).getString("message"));//设置通知内容
        return view;
    }

    class ViewHolder{
        TextView notify_tv,notify_time_tv;
    }
}
