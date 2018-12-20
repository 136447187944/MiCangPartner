package com.mic.zl.micangpartner.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.loopj.android.image.SmartImageView;
import com.mic.zl.micangpartner.activity.CreditCardActivity;
import com.mic.zl.micangpartner.activity.InvitePartnerActivity;
import com.mic.zl.micangpartner.activity.LoanActivity;
import com.mic.zl.micangpartner.activity.MachineExchangeActivity;
import com.mic.zl.micangpartner.activity.MyCreditActivity;
import com.mic.zl.micangpartner.activity.MyMerchantActivity;
import com.mic.zl.micangpartner.activity.MyPartnerActivity;
import com.mic.zl.micangpartner.activity.PointsMallActivity;
import com.mic.zl.micangpartner.util.ScalePageTransformer;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private Context context;
    private List<String> listUrl;
    private List<String> listPinfo;
    private SmartImageView banner_background;
    private RadioGroup banner_rg;
    private int currentPosition;
    private ViewPager viewPager;
    private Activity activity;

    public BannerAdapter(Context context, List<String> listUrl,
                         List<String> listPinfo, SmartImageView banner_background,
                         RadioGroup banner_rg, ViewPager viewPager,Activity activity) {
        this.context = context;
        this.listUrl = listUrl;
        this.listPinfo = listPinfo;
        this.banner_background = banner_background;
        this.banner_rg = banner_rg;
        this.viewPager = viewPager;
        this.activity = activity;
        this.viewPager.addOnPageChangeListener(this);
        this.viewPager.setAdapter(this);
        this.viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        this.viewPager.setPageMargin(-(getBannerMargin() - dp2px(14)));//设置ViewPaper的边距
        this.viewPager.setPageTransformer(true, new ScalePageTransformer());
        ((RadioButton) this.banner_rg.getChildAt(0)).setChecked(true);
        Glide.with(context)
                .load(listUrl.get(0))
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(15,1)))
                .apply(RequestOptions.signatureOf(new CenterCrop()))
                .into(banner_background);
        autoPlay();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        CardView cardView=new CardView(context);
        currentPosition = position % listUrl.size();
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context)
                .load(listUrl.get(position % listUrl.size()))
                .apply(RequestOptions.signatureOf(new CenterCrop()))
                .into(imageView);
        cardView.setRadius(dp2px(10));
        cardView.setCardElevation(dp2px(10));
        cardView.addView(imageView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = listPinfo.get(currentPosition);
                setActivity(title);
            }
        });
        container.addView(cardView);
        return cardView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void setActivity(String title) {
        Intent intent;
        switch (title) {
            case "邀请伙伴":
                intent = new Intent(context, InvitePartnerActivity.class);
                context.startActivity(intent);
                break;
            case "机具兑换":
                intent = new Intent(context, MachineExchangeActivity.class);
                context.startActivity(intent);
                break;
            case "积分商城":
                intent = new Intent(context, PointsMallActivity.class);
                context.startActivity(intent);
                break;
            case "信用卡":
                intent = new Intent(context, CreditCardActivity.class);
                context.startActivity(intent);
                break;
            case "我的商户":
                intent = new Intent(context, MyMerchantActivity.class);
                context.startActivity(intent);
                break;
            case "我的积分":
                intent = new Intent(context, MyCreditActivity.class);
                context.startActivity(intent);
                break;
            case "我的伙伴":
                intent = new Intent(context, MyPartnerActivity.class);
                context.startActivity(intent);
                break;
            case "任我贷":
                intent = new Intent(context, LoanActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (activity!= null) {
            Glide.with(context)
                    .load(listUrl.get(position % listUrl.size()))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(15, 1)))
                    .into(banner_background);
            ((RadioButton) banner_rg.getChildAt(position % listUrl.size())).setChecked(true);
        }else Log.i("tag","Picture loading failed,activity is Destroyed");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //把dp值转化为px=dpValue*scale+0.5f
    //dp=pxValue/scale+0.5f
    private int dp2px(float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;//density密度，像素点
        return (int)(dpValue*scale+0.5f) ;
    }

    //设置pager页面边距
    private int getBannerMargin() {
        /*获取屏幕分辨率步骤*/
        WindowManager manager =activity.getWindowManager();//创建Activity管理者
        DisplayMetrics metrics = new DisplayMetrics();//获取屏幕的信息
        manager.getDefaultDisplay().getMetrics(metrics);
        //metrics.widthPixels是获取屏幕宽度
        int margin = (int) ((metrics.widthPixels - dp2px(39) * 2) * 0.15 / 2);//设置屏幕宽度的偏移量
        return margin;
    }

    private void autoPlay(){
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                handler.postDelayed(this,3000);
            }
        });
    }

}
