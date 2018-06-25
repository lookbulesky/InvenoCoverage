package com.paix.invenocoverage.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**
 * Created by paix on 2017/12/20.
 * ΢�� ColorOs
 */

public class DevicesInfo {
    private  static DisplayMetrics dm ;
    private static Resources resources;

    /**
     * ��������Ƿ�����
     * @param context ȫ��context
     * @return ������true  δ����false
     */
    public static boolean checkNetworkConnect(Context context){
        if(context != null){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getDeviceDpi(Context context){
        resources = context.getResources();
        dm = resources.getDisplayMetrics();
        return dm.densityDpi;
    }
}
