package org.kazin.timelike.misc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Created by Alexey on 16.06.2015.
 */
public class TimelikeApp extends Application {
    private static Context context;
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        application = TimelikeApp.this;
    }

    public static Context getContext() {
        return context;
    }

    public static Application getApplication(){
        return application;
    }
}
