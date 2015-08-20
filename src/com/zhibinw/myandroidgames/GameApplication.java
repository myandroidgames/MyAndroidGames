package com.zhibinw.myandroidgames;

import com.zhibinw.myandroidgames.Util;
import android.app.Application;

public class GameApplication extends Application {

    @Override
    public void onCreate() {
        Util.createAppDirIfNeed();
        super.onCreate();
    }

}
