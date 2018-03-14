package com.paix.invenocoverage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paix.invenocoverage.utils.Jacoco;
import com.paix.invenocoverage.utils.PermisionUtils;
import com.paix.invenocoverage.utils.ToastUtils;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;



public class InvenoCoverageActivity extends AppCompatActivity {

    private Context context;
    private static String DEFAULT_COVERAGE_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/invenocoverage.ec";
    TextView notesTV;
    TextView timeinitTV;
    TextView timeupdaTV;
    Button initBT,updataBT,stopBT,helpBT,downloadBT,goonBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invenocoverage);
        PermisionUtils.verifyStoragePermissions(this);
        context = getApplicationContext();
        notesTV = (TextView)findViewById(R.id.notesTV);
        timeinitTV = (TextView)findViewById(R.id.timeinitTV);
        timeupdaTV = (TextView)findViewById(R.id.timeupdataTV);
        initBT =(Button)findViewById(R.id.initBT);
        updataBT =(Button)findViewById(R.id.updataBT);
        stopBT =(Button)findViewById(R.id.stopBT);
        helpBT =(Button)findViewById(R.id.helpBT);
        downloadBT =(Button)findViewById(R.id.downloadBT);
        goonBT =(Button)findViewById(R.id.goonBT);
        initBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInitBT();
            }
        });
        updataBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdataBT();
            }
        });
        stopBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStopBT();
            }
        });
        helpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHelpBT();
            }
        });
        downloadBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDownloadBT();
            }
        });
        goonBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGoonBT();
            }
        });
    }



    public void setInitBT(){
//        ToastUtils.showShort(context, "init");
//        ToastUtils.showShort(InvenoCoverageActivity.this, "init2");
//        PermisionUtils.verifyStoragePermissions(this);
        //创建文件
            File file = new File("/sdcard/001ping.li.txt");//文件位置
        try {
            FileOutputStream outputStream = new FileOutputStream(file);//打开文件输出流
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));//写入到缓存流
            writer.write("这里是要写入到文件的数据");//从从缓存流写入
            writer.close();//关闭流
            ToastUtils.showShort(context,"输出成功");
        }
        catch(Exception exception) {
            ToastUtils.showShort(context,"输出失败"+exception.toString());
        }
    }

    public void setUpdataBT(){
//        ToastUtils.showShort(context, "updata");
        mHandler = new Handler();
        new Thread(){
            @Override
            public void run() {
                super.run();
                uploadFile();
            }
        }.start();
    }


    public void setStopBT(){
        Jacoco.generateEcFile(false);
        ToastUtils.showShort(context, "stop");
    }


    public void setHelpBT(){
        ToastUtils.showShort(context, "help");
    }

    public void setDownloadBT(){

    }

    public void setGoonBT(){

    }

    public void startFloatWindow(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(InvenoCoverageActivity.this)) {
                Intent intent = new Intent(InvenoCoverageActivity.this, BlacklistService.class);
                ToastUtils.showShort(InvenoCoverageActivity.this,"已开启Toucher");
                startService(intent);
                finish();
            } else {
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                ToastUtils.showShort(InvenoCoverageActivity.this,"需要取得权限以使用悬浮窗");
                startActivity(intent);
            }
        } else {
            //SDK在23以下，不用管.
            Intent intent = new Intent(InvenoCoverageActivity.this, BlacklistService.class);
            startService(intent);
            finish();
        }
    }
    boolean coveragefileExist(){
        File file = new File(DEFAULT_COVERAGE_FILE_PATH);
        if(file.exists()){
            return true;
        }
        return false;
    }


        private String newName = "invenocoverage.ec";
//    private String newName = "coverage.ec";
        private String uploadFile = "/sdcard/invenocoverage.ec";// 要上传的文件
//    private String uploadFile = "/sdcard/coverage.ec";// 要上传的文件
    private String actionUrl = "http://192.168.1.244:8080/Tea/test/UploadServlet";



    /* 上传文件至Server的方法 */
    private void uploadFile() {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            // 设置http连接属性
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file1\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end);

            // 取得文件的FileInputStream
            FileInputStream fStream = new FileInputStream(uploadFile);
            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

            fStream.close();
            ds.flush();
            /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            /* 将Response显示于Dialog */
            b2=b;
            mHandler.post(new Thread(){
                @Override
                public void run() {
                    super.run();
                    showDialog("上传成功" + b2.toString().trim());
                }
            });

            /* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            e2=e;
            mHandler.post(new Thread(){
                @Override
                public void run() {
                    super.run();
                    showDialog("上传失败" + e2);
                }
            });

        }
    }
    public Exception e2;
    public Handler mHandler;
    public StringBuffer b2;
    /* 显示Dialog的method */
    private void showDialog(String mess) {
        new AlertDialog.Builder(InvenoCoverageActivity.this).setTitle("Message")
                .setMessage(mess)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

}
