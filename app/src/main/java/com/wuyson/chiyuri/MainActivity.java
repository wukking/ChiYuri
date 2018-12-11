package com.wuyson.chiyuri;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.Window;

import com.wuyson.chiyuri.ui.activity.AnimationActivity;
import com.wuyson.chiyuri.ui.activity.CardFlipAnimationActivity;
import com.wuyson.chiyuri.ui.activity.CodeActivity;
import com.wuyson.chiyuri.ui.activity.ZoomAnimationActivity;
import com.wuyson.common.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    public void entrance(View view){
        Intent intent = new Intent(this,CardFlipAnimationActivity.class);
        startActivity(intent);
    }

    public void entranceZoom(View view){
        Intent intent = new Intent(this,ZoomAnimationActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptions options = ActivityOptions
//                    .makeSceneTransitionAnimation((Activity) mContext,view,"111");
//            mContext.startActivity(intent, options.toBundle());
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {

            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
        }
//        startActivity(intent);
    }
}
