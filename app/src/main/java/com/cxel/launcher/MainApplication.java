package com.cxel.launcher;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.cxel.launcher.data.AppData;
import com.cxel.launcher.data.DataAsyncTask;
import com.cxel.launcher.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * zhulf 20190924
 * andevele@163.com
 * apk主Application
 */
public class MainApplication extends Application {
    Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
    private static Context context = null;
    private List<Activity> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
//        MultiDex.install(this);
        context = getApplicationContext();
//        DataAsyncTask dataAsyncTask = new DataAsyncTask();
//        dataAsyncTask.setDataTask(new DataAsyncTask.DataTask() {
//            @Override
//            public void excuteSuccess(List<String> list) {
//                dataMap.put(Constant.INPUT_SOURCE_SECTION, list);
//            }
//
//            @Override
//            public void executeFailed() {
//
//            }
//        });
//        dataAsyncTask.execute(Constant.INPUT_SOURCE_JSON_FILE, Constant.INPUT_SOURCE_SECTION);
//        AppData.getInstance().catchAppInfo();
    }

    public Map<String, List<String>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(List<String> list) {
        dataMap.put(Constant.INPUT_SOURCE_SECTION, list);
    }

    public static Context getContext() {
        return context;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
