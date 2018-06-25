package com.paix.invenocoverage.utils;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by paix on 2017/12/22.
 * ΢�� ColorOs
 */

public class Jacoco {

    //ec�ļ���·��
    private static String DEFAULT_COVERAGE_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/invenocoverage.ec";

    //��Ŀ·��
    private static String PROJECT_PATH;

    /**
     * ��ʼ��
     * @param projectPath  '��Ŀ·��' + '/app/build/outputs/code-coverage/'
     * @param isDebug �Ƿ��log
     */
    public static void init(String projectPath, boolean isDebug){
        PROJECT_PATH = projectPath + "  /app/build/outputs/code-coverage/";
        LogUtils.setLogEnable(isDebug);
    }

    /**
     * ����ec�ļ�
     *
     * @param isNew �Ƿ����´���ec�ļ�
     */
    public static void generateEcFile(boolean isNew) {
        if (!LogUtils.getLogEnable())
            return;

        OutputStream out = null;
        File mCoverageFilePath = new File(DEFAULT_COVERAGE_FILE_PATH);
       // File file = new File(Environment.getExternalStorageDirectory().getPath() +"/001ping.li_2.txt");

        try {
            if (isNew && mCoverageFilePath.exists()) {
                LogUtils.d("inveno_Jacoco_generateEcFile: clean old ec file");
                mCoverageFilePath.delete();
            }
            if (!mCoverageFilePath.exists()) {
                mCoverageFilePath.createNewFile();
            }
            out = new FileOutputStream(mCoverageFilePath.getPath(), true);
            Object agent = Class.forName("org.jacoco.agent.rt.RT")
                    .getMethod("getAgent")
                    .invoke(null);
            out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class)
                    .invoke(agent, false));
           // file.createNewFile();
            //FileOutputStream outputStream = new FileOutputStream(file);//���ļ������
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));//д�뵽������
           // writer.write("������Ҫд�뵽�ļ�������");//�Ӵӻ�����д��
            //writer.close();//�ر���
        } catch (Exception e) {
            LogUtils.d("inveno_Jacoco_generateEcFile: " + e.getMessage());
        } finally {
            if (out == null)
                return;
            try {
                out.close();
                LogUtils.d("inveno_Jacoco_generateEcFile: "+mCoverageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        LogUtils.d(getAdbPullCmd());
    }


    /**
     * ����jacoco���ɵ�ec�ļ�����Ŀ���Ŀ¼��
     * @return adb ����
     */
    public static String getAdbPullCmd(){
        String adb = "adb pull " + DEFAULT_COVERAGE_FILE_PATH + " " + PROJECT_PATH;
        LogUtils.d("export ec file command: "+adb);
        return adb;
    }
}
