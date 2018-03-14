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
 * 微信 ColorOs
 */

public class Jacoco {

    //ec文件的路径
    private static String DEFAULT_COVERAGE_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/invenocoverage.ec";

    //项目路径
    private static String PROJECT_PATH;

    /**
     * 初始化
     * @param projectPath  '项目路径' + '/app/build/outputs/code-coverage/'
     * @param isDebug 是否打开log
     */
    public static void init(String projectPath, boolean isDebug){
        PROJECT_PATH = projectPath + "  /app/build/outputs/code-coverage/";
        LogUtils.setLogEnable(isDebug);
    }

    /**
     * 生成ec文件
     *
     * @param isNew 是否重新创建ec文件
     */
    public static void generateEcFile(boolean isNew) {
        if (!LogUtils.getLogEnable())
            return;

        OutputStream out = null;
        File mCoverageFilePath = new File(DEFAULT_COVERAGE_FILE_PATH);
        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/001ping.li_2.txt");

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
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);//打开文件输出流
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));//写入到缓存流
            writer.write("这里是要写入到文件的数据");//从从缓存流写入
            writer.close();//关闭流
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
     * 导出jacoco生成的ec文件到项目相关目录下
     * @return adb 命令
     */
    public static String getAdbPullCmd(){
        String adb = "adb pull " + DEFAULT_COVERAGE_FILE_PATH + " " + PROJECT_PATH;
        LogUtils.d("export ec file command: "+adb);
        return adb;
    }
}
