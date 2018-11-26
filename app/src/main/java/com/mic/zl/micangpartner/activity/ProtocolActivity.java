package com.mic.zl.micangpartner.activity;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mic.zl.micangpartner.R;

public class ProtocolActivity extends AppCompatActivity {
    private TextView title_tv ;
    private ImageView back;
    private WebView protocol_wv;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        title_tv=findViewById(R.id.title_tv);
        protocol_wv=findViewById(R.id.protocol_wv);
        progressBar=findViewById(R.id.progress);
        back=findViewById(R.id.back_iv);
        title_tv.setText("米仓伙伴用户协议");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

         protocol_wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view,url);
                progressBar.setVisibility(View.GONE);//当页面加载完成时，不在显示进度控件
            }
        });
        /*声明WebSetting子类,getSettings()方法可以设置某些浏览器属性*/
        WebSettings webSettings=protocol_wv.getSettings();

        /*允许访问页面时与JavaScript交互*/
        webSettings.setJavaScriptEnabled(true);

        /*当网页需要跳转到另一个网页时,任然在WebView中显示*/

        /*loadUrl()加载网址*/
        protocol_wv.loadUrl("http://hb.micangpay.com/McangPartner/page/PrivacyAgreement.html");

        /*允许图片调整到适合*/
        webSettings.setUseWideViewPort(true);

        /*设置缩放屏幕*/
        webSettings.setLoadWithOverviewMode(true);

        /*支持缩放功能*/
        webSettings.setSupportZoom(true);
        /*设置内置内置控件的缩放*/
        webSettings.setBuiltInZoomControls(true);
        /*隐藏原生的缩放控件*/
        webSettings.setDisplayZoomControls(true);

        /*设置显示字体格式*/
        webSettings.setDefaultTextEncodingName("utf-8");

        webSettings.setAppCacheEnabled(true);

    }
}
