package com.zhibinw.myandroidgames;

import java.io.File;

import android.os.Environment;

public class Constants {
    public static String TAG = "myandroidgames";
    
    public static String ADDRESS_BASE = "https://raw.githubusercontent.com/myandroidgames/myandroidgames.github.io/master/";
    public static String ADDRESS_JSON = "apps/com.king.candycrushsaga.json";
    
    public  static final int BUFF_SIZE = 1024;
    public static final String EXTERNAL_PATH = Environment.getExternalStorageDirectory().toString();
    public static final String MY_FOLDER = "myandroidgames";
    public static final File APP_LOCATION = new File(EXTERNAL_PATH + "/"+MY_FOLDER);
    public static final String TYPE_APPLICATION = "application/vnd.android.package-archive";

    public static String JSON_KEY_NAME = "name";
    public static String jSON_KEY_PACKAGE ="packagename";
    public static String JSON_KEY_SIZE = "size";
    public static String JSON_KEY_ICON = "iconlink";
    public static String JSON_KEY_LINK = "downloadlink";
    public static String JSON_KEY_VERSION = "version";
}
