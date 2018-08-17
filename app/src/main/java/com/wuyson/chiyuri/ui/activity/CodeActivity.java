package com.wuyson.chiyuri.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyson.chiyuri.R;
import com.wuyson.common.base.BaseActivity;
import com.wuyson.common.util.BitmapUtils;

public class CodeActivity extends BaseActivity {
    private TextView tvVertical;
    private TextView tvHorizontal;
    private ImageView imgShow;
    private AnimatedVectorDrawable ad;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_code;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        imgShow = findViewById(R.id.img_show);
        tvVertical = findViewById(R.id.tv_vertical);
        tvHorizontal = findViewById(R.id.tv_horizontal);

        tvVertical.setMovementMethod(new ScrollingMovementMethod());
        tvHorizontal.setMovementMethod(new ScrollingMovementMethod());

        imgShow.setBackgroundResource(R.drawable.animvectordrawable);
        ad = (AnimatedVectorDrawable) imgShow.getBackground();
        imgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.start();
            }
        });
    }

    public void snap(View view){
        Bitmap bitmap = BitmapUtils.snapView(tvVertical);
        imgShow.setImageBitmap(bitmap);
    }
}
