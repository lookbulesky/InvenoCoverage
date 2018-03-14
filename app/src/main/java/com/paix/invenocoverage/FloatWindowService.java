package com.paix.invenocoverage;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.paix.invenocoverage.utils.DevicesInfo;
import com.paix.invenocoverage.utils.LogUtils;
import com.paix.invenocoverage.utils.ToastUtils;

public class FloatWindowService extends Service {

    //Log用的TAG
    private static final String TAG = "Paix_FloatWindowService";

    //要引用的布局文件.
    LinearLayout toucherLayout;
    //布局参数.
    WindowManager.LayoutParams params;
    //实例化的WindowManager.
    WindowManager windowManager;

    Button FloatWindowBT;

    //状态栏高度.（接下来会用到）
    int statusBarHeight = -1;

    public FloatWindowService() {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        LogUtils.i(TAG, "MainService Created");
        //OnCreate中来生成悬浮窗.
        createToucher();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return  null;
    }

    private void createToucher()
    {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
        //注意，这里的width和height均使用px而非dp.这里我偷了个懒
        //如果你想完全对应布局设置，需要先获取到机器的dpi
        //px与dp的换算为px = dp * (dpi / 160).
        params.width = 50 * (DevicesInfo.getDeviceDpi(this) / 160);
        params.height = 50 * (DevicesInfo.getDeviceDpi(this) / 160);
        LogUtils.e(params.width +" kuan gao " +params.height + " dpi "+ DevicesInfo.getDeviceDpi(this));

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (LinearLayout) inflater.inflate(R.layout.floatwindow,null);

        //添加toucherlayout
        windowManager.addView(toucherLayout,params);

        LogUtils.i(TAG, "toucherlayout-->left:" + toucherLayout.getLeft());
        LogUtils.i(TAG, "toucherlayout-->right:" + toucherLayout.getRight());
        LogUtils.i(TAG, "toucherlayout-->top:" + toucherLayout.getTop());
        LogUtils.i(TAG, "toucherlayout-->bottom:" + toucherLayout.getBottom());

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId > 0)
        {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        LogUtils.i(TAG, "状态栏高度为:" + statusBarHeight);

        //浮动窗口按钮.
        FloatWindowBT = (Button) toucherLayout.findViewById(R.id.stopBT);

        //其他代码...
        FloatWindowBT.setOnClickListener(new View.OnClickListener() {
            long[] hints = new long[2];
            @Override
            public void onClick(View v) {
                LogUtils.i(TAG, "点击了");
                System.arraycopy(hints,1,hints,0,hints.length -1);
                hints[hints.length -1] = SystemClock.uptimeMillis();
                if (SystemClock.uptimeMillis() - hints[0] >= 700)
                {
                    LogUtils.i(TAG, "要执行");
                    ToastUtils.showShort(FloatWindowService.this, "连续点击两次以退出");
                }else
                {
                    LogUtils.i(TAG, "即将关闭");
                    stopSelf();
                }
            }
        });

        FloatWindowBT.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //ImageButton我放在了布局中心，布局一共300dp
                params.x = (int) event.getRawX() - 75;
                //这就是状态栏偏移量用的地方
                params.y = (int) event.getRawY() - statusBarHeight- 75;
                windowManager.updateViewLayout(toucherLayout,params);
                return false;
            }
        });

    }

    @Override
    public void onDestroy()
    {
        //用imageButton检查悬浮窗还在不在，这里可以不要。优化悬浮窗时要用到。
        if (FloatWindowBT != null)
        {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }


}
