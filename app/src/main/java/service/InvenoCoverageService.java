package service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import com.paix.invenocoverage.utils.FilePro;
import com.paix.invenocoverage.utils.Jacoco;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InvenoCoverageService extends Service {
    String TAG = "InvenoCoverageService";
    String filePath="";
    String doWhat="";
    String actionUrl = "http://192.168.9.126:8080/Tea/test/UploadServlet";
    String ECfileName;
    StringBuffer b2;
    Boolean run=false;

    public InvenoCoverageService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String packageName;
        if(TextUtils.isEmpty(intent.getStringExtra("packageName"))){
            packageName="empty";
        }else {
            packageName=intent.getStringExtra("packageName");
        }
        Log.i(TAG,"onStartCommand-start  packageName="+packageName);
        if(packageName.equals(getPackageName())){
            final int forIn = intent.getIntExtra("forIn",0);
            filePath = this.getFilesDir().getAbsolutePath()+"/invenocoverage.ec";
            doWhat = intent.getStringExtra("doWhat");
            if(doWhat.equals("run")){
                run=true;
                SendBroadcast("run",run,"run");
                new Thread(){
                    @Override
                    public void run() {
                        while (run){
                            Log.i(TAG,"onStartCommand-run  run="+run+" doWhat= "+doWhat+" forIn= "+forIn+ " packageName= "+packageName);
                            if(forIn<1){
                                return;
                            }
                            try {
                                sleep(forIn);
                                Jacoco.generateEcFile(filePath);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
            else if(doWhat.equals("upload")) {
                run=false;
                ECfileName = intent.getStringExtra("ECfileName");
                Jacoco.generateEcFile(filePath);
                upLoadEcFile();
            }else if(doWhat.equals("clear")){
                run=false;
                clearEc();
            }else if(doWhat.equals("stop")){
                run=false;
                SendBroadcast("stop",run,"stop");
            }else if(doWhat.equals("done")){
                run=false;
                SendBroadcast("done",run,"done");
            }
        }
        Log.i(TAG,"onStartCommand-end run="+run+" doWhat= "+doWhat+ " packageName= "+packageName);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void clearEc() {
        if (FilePro.deleteFile(filePath)) {
            SendBroadcast("sucess",true,"clear");
        } else {
            SendBroadcast("fail",false,"clear");
        }
    }

    private void upLoadEcFile() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                uploadFile();
            }
        }.start();
    }

    private void uploadFile() {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + ECfileName + "\"" + end);
            ds.writeBytes(end);
            FileInputStream fStream = new FileInputStream(filePath);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            while ((length = fStream.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            fStream.close();
            ds.flush();
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            b2=b;
            ds.close();
            SendBroadcast(b2.toString().trim(),true,"upload");

        } catch (Exception e) {
            SendBroadcast(e.toString(),false,"upload");
        }
    }

    private void SendBroadcast(String message,Boolean success,String feature){
        Log.i(TAG,"SendBroadcast() message= "+ message+" success= "+success+" feature="+feature);
        Intent intent = new Intent("com.paix.inveno.ertop.ReceiveJacocoBroadcast");
        intent.putExtra("message",message);
        intent.putExtra("success",success);
        intent.putExtra("feature",feature);
        sendBroadcast(intent);
    }
}
