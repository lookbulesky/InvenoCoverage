package com.paix.invenocoverage.utils;


import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by paix on 2017/12/22.
 * weChat ColorOs
 */

public class Jacoco {

    public static void generateEcFile(String filePath) {
        OutputStream out = null;
        File mCoverageFilePath = new File(filePath);

        try {
            if (mCoverageFilePath.exists()) {
                mCoverageFilePath.delete();
            }
            if (!mCoverageFilePath.exists()) {
                mCoverageFilePath.createNewFile();
            }
            out = new FileOutputStream(mCoverageFilePath.getPath(), true);
            Object agent = Class.forName("org.jacoco.agent.rt.RT").getMethod("getAgent").invoke(null);
            out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class).invoke(agent, false));
        } catch (Exception e) {
            Log.e("Jacoco_paix","inveno_Jacoco_generateEcFile: " + e.getMessage());
        } finally {
            if (out == null)
                return;
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
