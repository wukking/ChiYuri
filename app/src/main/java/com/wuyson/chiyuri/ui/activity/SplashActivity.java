package com.wuyson.chiyuri.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.wuyson.chiyuri.MainActivity;
import com.wuyson.chiyuri.R;

/**
 * 启动页
 *
 * @author : Wuyson
 * @date : 2018/8/15-15:15
 */
public class SplashActivity extends Activity {
    private ImageView imgBg;
    private ImageView imgIcon;

     ViewPropertyAnimation.Animator mAnimator = new ViewPropertyAnimation.Animator(){
        @Override
        public void animate(View view) {
            view.setAlpha(0f);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            alpha.setDuration(2000);
            alpha.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(null);
        initStatus();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgBg = findViewById(R.id.img_bg);
        imgIcon = findViewById(R.id.img_icon);
        imgIcon.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(SplashActivity.this).load(R.drawable.img_slogan)
                        .animate(mAnimator).into(imgBg);
                startAnimat();
            }
        });
    }

    private void initStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decoderView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decoderView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            //or ?
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void startAnimat() {

        int imgHeight = imgIcon.getHeight() / 5;
        Point outSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(outSize);
        int height = outSize.y;
        int curY = height / 2 + imgHeight / 2;
        int dy = (height - imgHeight) / 2;
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorTranslate = ObjectAnimator.ofFloat(imgIcon, "translationY", 0, dy - 150);
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(imgIcon, "ScaleX", 1f, 0.4f);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(imgIcon, "ScaleY", 1f, 0.4f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(imgIcon, "alpha", 1f, 0.5f, 1f);
        set.play(animatorTranslate)
                .with(animatorScaleX).with(animatorScaleY).with(animatorAlpha);
        set.setDuration(1200);
        set.setInterpolator(new AccelerateInterpolator());
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                imgIcon.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        SplashActivity.this.finish();
                    }
                }, 1500);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
