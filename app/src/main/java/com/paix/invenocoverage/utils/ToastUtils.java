package com.paix.invenocoverage.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by paix on 2017/12/20.
 * 微信 ColorOs
 */

public class ToastUtils {

    /**
     * 显示short message
     * @param context 全局context
     * @param resId String String 资源id
     */
    public static void showShort(Context context, int resId){
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示short message
     * @param context 全局context
     * @param message 显示message
     */
    public static void showShort(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示long message
     * @param context 全局context
     * @param resId String String 资源id
     */
    public static void showLong(Context context, int resId){
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示long message
     * @param context 全局context
     * @param message 显示message
     */
    public static void showLong(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
