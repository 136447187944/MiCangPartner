package com.mic.zl.micangpartner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.mic.zl.micangpartner.R;

public class GuidesActivity extends AppCompatActivity {
    private ImageSwitcher guideImage;
    private LinearLayout  guideDotLL;
    private int index=0;//记录当前的位置
    private   int previewIndex=0;//记录上一个位置
    private float touchDownX;//手指按下的X轴位置
    private float touchUpX;//手指拿开时的X轴位置
    private int[] pic;//图片
    private View dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guides);
        init();
    }

    private void init(){
        guideDotLL=findViewById(R.id.guide_dot_ll);
        guideImage=findViewById(R.id.guide_img_switcher);
        pic=new  int[]{R.mipmap.guide_first_1,R.mipmap.guide_first_2,R.mipmap.guide_first_3,R.mipmap.guide_first_4};
        for (int i=0;i<pic.length;i++){
            dot=new View(this);//创建小圆点
            dot.setBackgroundResource(R.drawable.guide_selector);//给小圆点添加背景样式
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(35,35);//设置圆点的宽高
            if (i!=0){
                params.leftMargin=20;//设置左边距
            }
            dot.setEnabled(false);//默认设置为未选中
            guideDotLL.addView(dot,params);//将创建的小圆点添加到布局中
        }
        guideDotLL.getChildAt(0).setEnabled(true);
        guideImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){ /*判断手指是否按下*/
                    touchDownX=motionEvent.getX();//当手指按下时记录下按下时的X轴坐标
                    return true;
                }else if (motionEvent.getAction()==MotionEvent.ACTION_UP){/*判断手指是否抬起*/
                    touchUpX=motionEvent.getX();//将手指抬起时的X轴坐标记录下来

                    if (touchUpX-touchDownX>0){//左到右
                        previewIndex=index;//记录下上个位置
                        index=index-1;//当前的位置
                        /*先赋值再判断,最后处理*/
                        if (index>=0 && index<pic.length){
                            guideImage.setImageResource(pic[index]);//给在ImageSwitcher创建的ImageView添加图片
                            guideDotLL.getChildAt(index).setEnabled(true);//将当前位置的小圆点设为选中状态
                            guideDotLL.getChildAt(previewIndex).setEnabled(false);//将上一个位置小圆点设为未选中状态
                            guideImage.setInAnimation(AnimationUtils.loadAnimation(GuidesActivity.this,android.R.anim.fade_in));//设置进入动画
                            guideImage.setOutAnimation(AnimationUtils.loadAnimation(GuidesActivity.this,android.R.anim.fade_out));//设置滑出动画
                        }else {
                            /*<0是将其定格在第一张图片位置*/
                            index=0;
                            guideImage.setImageResource(pic[index]);
                            guideDotLL.getChildAt(0).setEnabled(true);
                        }

                    }else if (touchDownX-touchUpX>0) { //右向左
                        previewIndex=index;
                        index=index+1;
                        if (index>=0 && index<pic.length){
                            guideImage.setImageResource(pic[index]);
                            guideDotLL.getChildAt(index).setEnabled(true);
                            guideDotLL.getChildAt(previewIndex).setEnabled(false);
                            guideImage.setInAnimation(AnimationUtils.loadAnimation(GuidesActivity.this,android.R.anim.fade_in));
                            guideImage.setOutAnimation(AnimationUtils.loadAnimation(GuidesActivity.this,android.R.anim.fade_out));
                        }else {
                            /*>4若在有滑动动作则进行跳转*/
                            Intent intent=new Intent(GuidesActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        /*使用ImageSwitcher时需要在工厂里面先创建承载图片的容器ImageView*/
        guideImage.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView=new ImageView(GuidesActivity.this);
                imageView.setImageResource(pic[index]);
                imageView.setAdjustViewBounds(true);
                return imageView;
            }
        });
    }
}
