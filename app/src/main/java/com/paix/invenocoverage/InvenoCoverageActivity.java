package com.paix.invenocoverage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paix.invenocoverage.utils.Jacoco;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvenoCoverageActivity extends AppCompatActivity {
    EditText et_ecName;
    Button bt_upLoad;
    Button bt_help;
    TextView tv_history;
    String ECfileName="";
    TextView tv1;
    TextView tv_ecName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invenocoverage);
        init();
    }
    public void init(){
        ECfileName = clearnStr(Build.MODEL+Build.VERSION.RELEASE);
        et_ecName = (EditText)findViewById(R.id.et_ecName);
        bt_upLoad = (Button)findViewById(R.id.bt_upLoad);
        bt_help = (Button)findViewById(R.id.bt_help);
        tv_history=(TextView)findViewById(R.id.tv_history);
        tv1 = (TextView)findViewById(R.id.tv1);
        tv_ecName = (TextView)findViewById(R.id.tv_ecName);
        bt_upLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et_ecName.getText().toString().trim();
                if(str!=""){
                    ECfileName = str;
                }
                Jacoco.generateEcFile(false);
                upLoadEcFile();
            }
        });
        bt_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpView();
            }
        });
        et_ecName.setText(ECfileName);
        et_ecName.setSelection(et_ecName.getText().toString().length());
        tv_history.setText(getStateTime());
    }


    private String clearnStr(String str){
        str=str.replace(" ","");
        str=str.replace("_","");
        str=str.replace("-","");
        str=str.replace("\\","");
        str=str.replace("/","");
        str=str.replace(":","");
        str=str.replace("*","");
        str=str.replace("?","");
        str=str.replace("\"","");
        str=str.replace("<","");
        str=str.replace(">","");
        str=str.replace("|","");
        return str;
    }
    private void helpView() {
        new AlertDialog.Builder(this)
                .setTitle("more...")
                .setMessage("��ϸ˵����192.168.1.244")
                .setPositiveButton("ǰ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(InvenoCoverageActivity.this,webviewActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void upLoadEcFile() {
        mHandler = new Handler();
        new Thread(){
            @Override
            public void run() {
                super.run();
                uploadFile();
            }
        }.start();
        savaStateTime();
        tv_history.setText(getStateTime());
        Toast.makeText(InvenoCoverageActivity.this,"upload",Toast.LENGTH_SHORT).show();
    }

    private void savaStateTime() {
        SharedPreferences.Editor editor = getSharedPreferences("timeState",MODE_PRIVATE).edit();
        editor.putLong("time",System.currentTimeMillis());
        editor.commit();
    }
    private String getStateTime(){
        SharedPreferences sp =getSharedPreferences("timeState",MODE_PRIVATE);
        long lon = sp.getLong("time",0);
        return getDateToString(lon,"yyyy/MM/dd HH:mm:ss");
    }

    private  String getDateToString(long time, String pattern) {
        if(time==0)
            return "δ�ϴ�������";
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return "���һ���ϴ�����ʱ�䣺\n"+format.format(date);
    }

    /* �ϴ��ļ���Server�ķ��� */
    private void uploadFile() {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* ����Input��Output����ʹ��Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            // ����http��������
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file1\";filename=\"" + ECfileName + "\"" + end);
            ds.writeBytes(end);

            // ȡ���ļ���FileInputStream
            FileInputStream fStream = new FileInputStream(uploadFile);
            /* ����ÿ��д��1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* ���ļ���ȡ������������ */
            while ((length = fStream.read(buffer)) != -1) {
                /* ������д��DataOutputStream�� */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

            fStream.close();
            ds.flush();
            /* ȡ��Response���� */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            b2=b;
            mHandler.post(new Thread(){
                @Override
                public void run() {
                    super.run();
                    showDialog("�ϴ��ɹ�" + b2.toString().trim());
                }
            });
            ds.close();
        } catch (Exception e) {
            e2=e;
            mHandler.post(new Thread(){
                @Override
                public void run() {
                    super.run();
                    showDialog("�ϴ�ʧ��" + e2);
                }
            });

        }
    }
    public Exception e2;
    public Handler mHandler;
    public StringBuffer b2;
    private String uploadFile = "/sdcard/invenocoverage.ec";
    private String actionUrl = "http://192.168.9.126:8080/Tea/test/UploadServlet";

    private void showDialog(String mess) {
        new android.support.v7.app.AlertDialog.Builder(InvenoCoverageActivity.this).setTitle("Message")
                .setMessage(mess)
                .setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

}
