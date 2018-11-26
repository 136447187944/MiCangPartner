package com.mic.zl.micangpartner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mic.zl.micangpartner.R;

public class IndexActivity extends AppCompatActivity implements View.OnClickListener {

    private Button title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        title=findViewById(R.id.title);
        title.setText("main pager");
        title.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.title:
                 intent=new Intent(IndexActivity.this,MainActivity.class);
                 startActivity(intent);

        }
    }
}
