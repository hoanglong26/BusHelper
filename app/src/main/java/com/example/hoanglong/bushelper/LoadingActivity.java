package com.example.hoanglong.bushelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoanglong on 17-Nov-16.
 */

public class LoadingActivity extends AppCompatActivity {
    @BindView(R.id.appName)
    TextView appName;



    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent();
            intent.setClass(LoadingActivity.this, MainActivity.class);

            startActivity(intent);
            LoadingActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);


        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/homestead.TTF");

        appName.setTypeface(custom_font);


        mHandler.sendEmptyMessageDelayed(1, 2000);
    }
}

