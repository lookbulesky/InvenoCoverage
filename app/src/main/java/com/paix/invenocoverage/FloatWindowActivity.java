package com.paix.invenocoverage;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.paix.invenocoverage.utils.ToastUtils;

public class FloatWindowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(FloatWindowActivity.this)) {
                Intent intent = new Intent(FloatWindowActivity.this, FloatWindowService.class);
                ToastUtils.showShort(FloatWindowActivity.this,"已开启Toucher");
                startService(intent);
                finish();
            } else {
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                ToastUtils.showShort(FloatWindowActivity.this,"需要取得权限以使用悬浮窗");
                startActivity(intent);
            }
        } else {
            //SDK在23以下，不用管.
            Intent intent = new Intent(FloatWindowActivity.this, FloatWindowService.class);
            startService(intent);
            finish();
        }
    }
}
