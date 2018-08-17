package com.wuyson.common.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;

public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    /**
     * 首选这个
     *
     * @param context   上下文
     * @param resId     Drawable图片资源
     * @param reqWidth  图片显示的宽度
     * @param reqHeight 图片显示的高度
     * @return
     */
    public static Bitmap decodeBitmapFromResource(Context context, @IdRes int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        return bitmap;
    }

    /**
     * 获取当前View的Bitmap
     */
    public static Bitmap getBitmap(View view) {
        Bitmap bmp = view.getDrawingCache();
        view.destroyDrawingCache();
        return bmp;
    }

    /**
     * 获取当前View截图
     */
    public static Bitmap snapView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Log.e(TAG, "snapView: " + bmp.getWidth());
        int width = DisplayUtils.getViewWidth(view);
        int height = DisplayUtils.getViewHeight(view);
        Log.e(TAG, "snapView: width = " + width + "height=" + height);
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width,
                height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 直接传入inSampleASize
     */
    public static Bitmap decodeBitmapBySample(Context context, int resId, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //原始图片信息
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        int width = options.outWidth;
        int height = options.outHeight;
        String mimeType = options.outMimeType;
        Log.d(TAG, "BitmapInfo: width = " + width + "  height = " + height + "  mimeType = " + mimeType);

        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        return bitmap;
    }


    /**
     * 计算SampleSize的值
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            while ((halfWidth / inSampleSize) > reqWidth
                    && (halfHeight / inSampleSize) > reqHeight) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
