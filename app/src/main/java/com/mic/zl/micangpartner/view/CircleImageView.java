package com.mic.zl.micangpartner.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {
    private Paint paint;//创建画笔
    private int radius;//半径
    private float scale;//图片的缩放比例
    public CircleImageView(Context context) {
        this(context,null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();

    }
    // 测量方法，计算出半径
    protected void onMeasure(int width,int height){
        super.onMeasure(width,height);
        int size=Math.min(getMeasuredHeight(),getMeasuredWidth());
        radius=size/2;
        setMeasuredDimension(size,size);
    }

    protected void onDraw(Canvas canvas){
        paint=new Paint();
        Bitmap bitmap=drawableToBitmap(getDrawable());
        //初始化BitmapShader，传入bitmap对象
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        scale = (radius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());//计算缩放比例
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);

        //画圆形，指定好中心点坐标、半径、画笔
        canvas.drawCircle(radius, radius, radius, paint);

    }

    //写一个drawble转Bitmap的方法
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

}
