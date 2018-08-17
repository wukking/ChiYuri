package com.wuyson.chiyuri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wuyson.chiyuri.ui.activity.AnimationActivity;
import com.wuyson.chiyuri.ui.activity.CardFlipAnimationActivity;
import com.wuyson.chiyuri.ui.activity.CodeActivity;
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

}
