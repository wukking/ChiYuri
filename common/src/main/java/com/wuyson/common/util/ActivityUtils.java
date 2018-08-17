package com.wuyson.common.util;

import android.content.Context;
import android.content.res.TypedArray;

public class ActivityUtils {
    /**
     * 获取ActionBar高度
     * @param context 上下文
     * @return 高度
     */
    public static int getActionBarSize(Context context){
        TypedArray typedArray = context.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int barHeight = (int) typedArray.getDimension(0, 0);
        typedArray.recycle();
        return barHeight;
    }
}
