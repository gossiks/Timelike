package org.kazin.timelike.misc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

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

        ParseCrashReporting.enable(this);
        Parse.initialize(this, "vkOmdpKK7NAw6MOL1zXZrSGdZ69QtbV05NwpHV9G", "8MBAp9Rs8vYBLjOzjVOt60B7G8U731OhYWg2oJkz");
    }

    public static Context getContext() {
        return context;
    }

    public static Application getApplication(){
        return application;
    }

    public static void showToast(String message){
        Toast.makeText(context, message,Toast.LENGTH_LONG);
    }
}
