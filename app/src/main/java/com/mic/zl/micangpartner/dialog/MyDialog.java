package com.mic.zl.micangpartner.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.mic.zl.micangpartner.R;

public class MyDialog extends AlertDialog implements View.OnClickListener {
    //控件
    private TextView titleTxt;//提示标题
    private TextView messageTxt;//消息内容
    private TextView submitBut;//确认按钮
    private TextView cancelBut;//取消按扭
    private View apartLine;

    //内容
    private Context context;//上下文环境
    private String title;//标题内容
    private String message;//消息内容
    private OnCloseListener listener;//点击外面可以取消Dialog显示
    private String positiveName;//确认按钮
    private String negativeName;//取消按钮

    public MyDialog(Context context) {
        super(context);
    }

    public MyDialog(@NonNull Context context, OnCloseListener listener) {
        super(context);
        this.listener = listener;
    }

    public MyDialog(@NonNull Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.listener = listener;
    }

    public MyDialog setTitle(String title) {
        this.title = title;
        return this;
    }


    public MyDialog setMessage(String message) {
        this.message = message;
        return this;
    }


    public MyDialog setPositiveName(String positiveName) {
        this.positiveName = positiveName;
        return this;
    }

    public MyDialog setNegativeName(String negativeName) {
        this.negativeName = negativeName;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        setCanceledOnTouchOutside(true);//是否允许点击空白处隐藏对话框
        initView();
    }

    private void initView() {
        titleTxt=findViewById(R.id.title_tv);//标题
        messageTxt=findViewById(R.id.message_tv);//消息
        submitBut=findViewById(R.id.positive_tv);//提交
        cancelBut=findViewById(R.id.negative_tv);//取消
        apartLine=findViewById(R.id.apart_line);//分隔线

        submitBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);

        titleTxt.setText(title);//设置标题
        messageTxt.setText(message);//设置提示内容

        //提交按钮，如果没有输入值侧将其隐藏
        if (!TextUtils.isEmpty(positiveName)){
            submitBut.setText(positiveName);
        }else {
            submitBut.setVisibility(View.GONE);
            apartLine.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(negativeName)){
            cancelBut.setText(negativeName);
        }else {
            cancelBut.setVisibility(View.GONE);
            apartLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.negative_tv:
                if (listener!=null){
                    listener.onClick(this,false);
                }
                this.dismiss();//销毁对话框
                break;
            case R.id.positive_tv:
                if (listener!=null){
                    listener.onClick(this,true);
                }
                break;
        }
    }


    public interface OnCloseListener{
        void onClick(Dialog dialog,boolean confirm);
    }

}
