package com.paix.invenocoverage.utils;

import android.util.Log;

/**
 * Created by paix on 2017/12/19.
 * 微信 ColorOs
 */

public class LogUtils {

    private static final String TAG = "PAIX_LOGUTILS";
    private static boolean LOG_ENABLE = true;
    private static boolean LOG_DETATL_ENABLE = true;

    private static String buildMsg(String msg){
        StringBuilder buffer = new StringBuilder();

        buffer.append(msg);
        return buffer.toString();
    }

    /**
     * 设置是否显示log
     * @param enable true-显示 fase-不显示
     */
    public static void setLogEnable(boolean enable){
        LOG_ENABLE=enable;
    }

    /**
     * 返回是否打印log
     * @return
     */
    public static boolean getLogEnable(){
        return LOG_ENABLE;
    }

    /**
     * 设置是否显示详细log
     * @param detatlEnable true-显示详细 false-不显示详细
     */
    public static void setLogDetatlEnable(boolean detatlEnable){
        LOG_DETATL_ENABLE=detatlEnable;
    }

    /**
     * verbose log
     * @param msg log msg
     */
    public static void v(String msg){
        if(LOG_ENABLE){
            Log.v(TAG, buildMsg(msg));
        }
    }

    /**
     * verbose log
     * @param tag tag
     * @param msg log msg
     */
    public static void v(String tag, String msg){
        if(LOG_ENABLE){
            Log.v(tag, buildMsg(msg));
        }
    }

    /**
     * debug log
     * @param msg log msg
     */
    public static void d(String msg){
        if(LOG_ENABLE){
            Log.d(TAG, buildMsg(msg));
        }
    }

    /**
     * debug log
     * @param tag tag
     * @param msg log msg
     */
    public static void d(String tag, String msg){
        if(LOG_ENABLE){
            Log.d(tag, buildMsg(msg));
        }
    }

    /**
     * info log
     * @param msg log msg
     */
    public static void i(String msg){
        if(LOG_ENABLE){
            Log.i(TAG, buildMsg(msg));
        }
    }

    /**
     * info log
     * @param tag tag
     * @param msg log msg
     */
    public static void i(String tag, String msg){
        if(LOG_ENABLE){
            Log.i(tag, buildMsg(msg));
        }
    }

    /**
     * warning log
     * @param msg log msg
     */
    public static void w(String msg){
        if(LOG_ENABLE){
            Log.w(TAG, buildMsg(msg));
        }
    }

    /**
     * warning log
     * @param tag tag
     * @param msg log msg
     */
    public static void w(String tag, String msg){
        if(LOG_ENABLE){
            Log.w(tag, buildMsg(msg));
        }
    }

    /**
     * error log
     * @param msg log msg
     */
    public static void e(String msg){
        if(LOG_ENABLE){
            Log.e(TAG, buildMsg(msg));
        }
    }

    /**
     * error log
     * @param tag tag
     * @param msg log msg
     */
    public static void e(String tag, String msg){
        if(LOG_ENABLE){
            Log.e(tag, buildMsg(msg));
        }
    }

    /**
     * error log
     * @param msg tag
     * @param e exception msg
     */
    public static void e(String msg, Exception e){
        if(LOG_ENABLE){
            Log.e(TAG, buildMsg(msg),e);
        }
    }

    /**
     * error log
     * @param tag tag
     * @param msg log msg
     * @param e   exceprtion msg
     */
    public static void e(String tag, String msg ,Exception e){
        if(LOG_ENABLE){
            Log.e(tag, buildMsg(msg),e);
        }
    }
}
