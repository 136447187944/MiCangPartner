package com.mic.zl.micangpartner.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mic.zl.micangpartner.R;
import com.mic.zl.micangpartner.activity.CreditCardActivity;
import com.mic.zl.micangpartner.activity.InvitePartnerActivity;
import com.mic.zl.micangpartner.activity.LoanActivity;
import com.mic.zl.micangpartner.activity.MachineExchangeActivity;
import com.mic.zl.micangpartner.activity.MyCreditActivity;
import com.mic.zl.micangpartner.activity.MyMerchantActivity;
import com.mic.zl.micangpartner.activity.MyPartnerActivity;
import com.mic.zl.micangpartner.activity.PointsMallActivity;

import java.util.List;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {
    private List<String> listMenuIcon;//存放菜单图片链接
    private List<String> listMenuName;//存放菜单名称
    private Context context;
    private Intent intent;

    public MenuRecyclerViewAdapter(List<String> listMenuIcon, List<String> listMenuName, Context context) {
        this.listMenuIcon = listMenuIcon;
        this.listMenuName = listMenuName;
        this.context = context;
    }

    /**
     * ViewHolder类，继承自RecyclerView的ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        View menuLayout;
        ImageView menuIcon;
        TextView menuName;
        public ViewHolder(View itemView) {
            super(itemView);
            menuLayout=itemView;//itemView=R.layout.menu_layout
            menuIcon=itemView.findViewById(R.id.menu_img_iv);
            menuName=itemView.findViewById(R.id.menu_name_tv);
        }
    }

    @NonNull
    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_layout,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                String menuName=listMenuName.get(position);
                setActivity(menuName);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(listMenuIcon.get(position)).into(holder.menuIcon);
        holder.menuName.setText(listMenuName.get(position));
    }

    @Override
    public int getItemCount() {
        return listMenuIcon.size();
    }


    private void setActivity(String menuName) {
        switch (menuName){
            case "机具兑换":
                intent=new Intent(context,MachineExchangeActivity.class);
                context.startActivity(intent);
                break;
            case "信用卡":
                intent=new Intent(context,CreditCardActivity.class);
                context.startActivity(intent);
                break;
            case "我的商户":
                intent=new Intent(context,MyMerchantActivity.class);
                context.startActivity(intent);
                break;
            case "我的伙伴":
                intent=new Intent(context,MyPartnerActivity.class);
                context.startActivity(intent);
                break;
            case "邀请伙伴":
                intent=new Intent(context,InvitePartnerActivity.class);
                context.startActivity(intent);
                break;
            case "积分商城":
                intent=new Intent(context,PointsMallActivity.class);
                context.startActivity(intent);
                break;
            case "我的积分":
                intent=new Intent(context,MyCreditActivity.class);
                context.startActivity(intent);
                break;
            case "任你贷":
                intent=new Intent(context,LoanActivity.class);
                context.startActivity(intent);
                break;


        }

    }

}
