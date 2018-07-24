package com.paix.invenocoverage.utils;

import java.io.File;


public class FilePro {
    public static boolean deleteFile(String path) {
        File file =new File(path);
        if(file.exists()&&file.isFile()){
            if(file.delete()){
                return true;
            }
        }
        return false;
    }
}


