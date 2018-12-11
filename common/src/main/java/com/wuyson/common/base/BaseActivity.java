package com.wuyson.common.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.wuyson.common.R;
import com.wuyson.common.app.AppManager;
import com.wuyson.common.app.StatusBarCompat;
import com.wuyson.common.util.ToastUtils;
import com.wuyson.common.widget.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类
 *
 * @author : Wuyson
 * @date : 2018/5/18-15:12
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = this.getClass().getSimpleName();
    private Unbinder butterKnife;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetContentView();
        setContentView(setContentViewId());
        butterKnife = ButterKnife.bind(this);
        mContext = this;
        initView(savedInstanceState);
    }

    protected void doBeforeSetContentView() {
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
        // 无标题(1.在AppCompatActivity是无效的，继承Activity才有效，可以设置Application主题去掉Actionbar)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 默认着色状态栏
        SetStatusBarColor();
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color) {
        StatusBarCompat.setStatusBarColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar() {
        StatusBarCompat.translucentStatusBar(this);
    }

    @LayoutRes
    protected abstract int setContentViewId();

    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 短暂显示Toast提示(来自String)
     **/
    protected void showShortToast(String message) {
        ToastUtils.showShort(message);
    }

    /**
     * 短暂显示Toast提示(来自res)
     **/
    protected void showShortToast(int resId) {
        ToastUtils.showShort(resId);
    }

    /**
     * 长时间显示Toast提示(来自String)
     **/
    public void showLongToast(String message) {
        ToastUtils.showLong(message);
    }

    /**
     * 长时间显示Toast提示(来自res)
     **/
    public void showLongToast(int resId) {
        ToastUtils.showLong(resId);
    }

    /**
     * 带图片的toast
     *
     * @param message
     * @param resId
     */
    public void showToastWithImg(String message, int resId) {
        ToastUtils.showToastWithImg(message, resId);
    }

    /**
     * 开启浮动加载进度条
     *
     * @param message 文本信息
     */
    protected void showProgressDialog(String message) {
        LoadingDialog.showDialogForLoading(this, message, true);
    }

    /**
     * 停止浮动加载进度条
     */
    protected void closeProgressDialog() {
        LoadingDialog.cancelDialogForLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        butterKnife.unbind();
        AppManager.getAppManager().finishActivity(this);
    }
}
