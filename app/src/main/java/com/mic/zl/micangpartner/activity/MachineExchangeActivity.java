package com.mic.zl.micangpartner.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mic.zl.micangpartner.R;
public class MachineExchangeActivity extends AppCompatActivity {
    private TextView title,back,goodsName_tv,goodsPrice,goodsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_exchange);
    }
}
