package com.wuyson.common.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * @author: Wuyson
 * @date: 2017/12/5 - 11:42
 * @description: 状态栏工具类
 */

public class StatusBarUtils {

    private static final int COLOR_TRANSLUCENT = Color.parseColor("#00000000");
    public static final int DEFAULT_COLOR_ALPHA = 112;

    public static void translucentStatusBar(Activity activity) {
        translucentStatusBar(activity, false);
    }
    /**
     * change to full screen mode
     *
     * @param hideStatusBarBackground hide status bar alpha Background when SDK > 21, true if hide it
     */
    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        ViewGroup mContentView = activity.findViewById(Window.ID_ANDROID_CONTENT);

        //set child View not fill the system window
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight(activity);

            //First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //After LOLLIPOP just set LayoutParams.
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (hideStatusBarBackground) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(COLOR_TRANSLUCENT);
                } else {
                    window.setStatusBarColor(calculateStatusBarColor(COLOR_TRANSLUCENT, DEFAULT_COLOR_ALPHA));
                }
                //must call requestApplyInsets, otherwise it will have space in screen bottom
                if (mChildView != null) {
                    ViewCompat.requestApplyInsets(mChildView);
                }
            } else {
                ViewGroup mDecorView = (ViewGroup) window.getDecorView();
                if (mDecorView.getTag() != null && mDecorView.getTag() instanceof Boolean && (Boolean) mDecorView.getTag()) {
                    mChildView = mDecorView.getChildAt(0);
                    //remove fake status bar view.
                    mContentView.removeView(mChildView);
                    mChildView = mContentView.getChildAt(0);
                    if (mChildView != null) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                        //cancel the margin top
                        if (lp != null && lp.topMargin >= statusBarHeight) {
                            lp.topMargin -= statusBarHeight;
                            mChildView.setLayoutParams(lp);
                        }
                    }
                    mDecorView.setTag(false);
                }
            }
        }
    }
    /**
     * 设置状态栏透明+颜色
     * @param activity
     * @param color
     * @param alpha 0为不透明，255为全透明
     */
    public static void setStatusBarColor(Activity activity,int color, @IntRange(from = 0,to = 255)int alpha){
        setStatusBarColor(activity,calculateStatusBarColor(color,alpha));
    }

    /**
     * 给状态栏填色
     * 5.0 以上 ：通过flag
     * 4.4 ： 通过调整布局的topMargin,并给topMargin设置颜色
     * @param activity
     * @param statusBarColor  : 0xFFFF4081
     * Notice: statusbar设为透明，或者界面设为全屏显示：setFitsSystemWindows()才生效
     */
    public static void setStatusBarColor(Activity activity, int statusBarColor) {
        Window window = activity.getWindow();
        ViewGroup content = activity.findViewById(Window.ID_ANDROID_CONTENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor);
                View childViewL = content.getChildAt(0);
                if (childViewL != null) {
                    ViewCompat.setFitsSystemWindows(childViewL, true);
                }
            } else {
                ViewGroup decorView = (ViewGroup) window.getDecorView();
                if (decorView.getTag() != null && decorView.getTag() instanceof Boolean && ((Boolean) decorView.getTag())) {
                    View statusBarView = decorView.getChildAt(0);
                    if (statusBarView != null) {
                        statusBarView.setBackgroundColor(statusBarColor);
                    }
                } else {
                    int statusBarHeight = getStatusBarHeight(activity);
                    View childViewK = content.getChildAt(0);
                    if (childViewK != null) {
                        ViewCompat.setFitsSystemWindows(childViewK, false);
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) childViewK.getLayoutParams();
                        lp.topMargin += statusBarHeight;
                        childViewK.setLayoutParams(lp);
                    }

                    View statusBarView = new View(activity);
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
                    lp.gravity = Gravity.TOP;
                    statusBarView.setLayoutParams(lp);
                    statusBarView.setBackgroundColor(statusBarColor);
                    decorView.addView(statusBarView, 0);
                    decorView.setTag(true);
                }
            }
        }
    }




// --------------------------------calculate----------------------------------------------
    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    /**
     * 计算透明颜色
     * @param color
     * @param alpha 0为不透明，255为全透明
     * @return
     */
    private static int calculateStatusBarColor(int color,int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }
}
