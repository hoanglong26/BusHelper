package com.example.hoanglong.bushelper;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    @BindView(R.id.ivLoading)
    ImageView ivLoading;

    Animation animation;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent();
            intent.setClass(LoadingActivity.this, LoginActivity.class);

            startActivity(intent);
            LoadingActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_grey_100));


        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/homestead.TTF");

        appName.setTypeface(custom_font);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_bottom);
        appName.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_top);
        ivLoading.startAnimation(animation);

        mHandler.sendEmptyMessageDelayed(1, 3500);
    }
}

