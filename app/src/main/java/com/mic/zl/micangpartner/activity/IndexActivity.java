package com.mic.zl.micangpartner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mic.zl.micangpartner.R;

public class IndexActivity extends AppCompatActivity implements View.OnClickListener {

    private Button forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        forget=findViewById(R.id.forgot);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.forgot:
                 intent=new Intent(IndexActivity.this,ForgotActivity.class);
                 startActivity(intent);

        }
    }
}
