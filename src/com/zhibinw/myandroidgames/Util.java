package com.zhibinw.myandroidgames;

import java.io.File;

import android.util.Log;

public class Util {
    /**
     * print log information
     * @param msg
     */
    public static void log(String msg){
        Log.d(Constants.TAG, msg);
    }

    public static void createMyDir(){
        log("external is: " + Constants.EXTERNAL_PATH);
        File dirFile = Constants.APP_LOCATION;
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
    }

    /**
     * format size from integer of bytes  to a readable string of MB.
     * @param size
     * @return size in M 
     */
    public static  String sizeFormat(int size){
        StringBuilder sBuilder = new StringBuilder();
        float fsize = size/(1024*1024);
        sBuilder.append("Size:");
        sBuilder.append(fsize);
        sBuilder.append(" M");
        return sBuilder.toString();
    }

}
