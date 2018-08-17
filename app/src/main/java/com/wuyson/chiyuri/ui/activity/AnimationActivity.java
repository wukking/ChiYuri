package com.wuyson.chiyuri.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wuyson.chiyuri.R;
import com.wuyson.common.base.BaseActivity;

public class AnimationActivity extends BaseActivity {
    private ScrollView mContent;
    private ProgressBar mProgressBar;
    private int mShortAnimationDuration;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_animation;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContent = findViewById(R.id.content);
        mProgressBar = findViewById(R.id.loading_spinner);

        //1.隐藏
        mContent.setVisibility(View.GONE);
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        crossFade();
    }

    private void crossFade() {
        mContent.setAlpha(0f);
        mContent.setVisibility(View.VISIBLE);

        mContent.animate().alpha(1f).setDuration(mShortAnimationDuration).setListener(null);
        mProgressBar.animate().alpha(0f).setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void rotationByIt(View view){
        mContent.animate().xBy(18).setDuration(mShortAnimationDuration).setListener(null);
    }
}
