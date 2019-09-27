package com.cxel.launcher.data;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.cxel.launcher.MainApplication;
import com.cxel.launcher.base.DataInterface;
import com.cxel.launcher.model.AppInfo;
import com.cxel.launcher.thread.AppExecutors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class AppData implements DataInterface.AppTaskCallBack {
    private static final String TAG = "AppData";
    private static AppData INSTANCE = null;
    private AppExecutors appExecutors;
    private List<AppInfo> appInfoList = new ArrayList<AppInfo>();

    public AppData() {
        if (appExecutors == null) {
            appExecutors = new AppExecutors();
        }
    }

    public static AppData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppData();
        }
        return INSTANCE;
    }

    public List<AppInfo> catchAppInfo() {
        List<AppInfo> list = new ArrayList<AppInfo>();
        FutureTask<List<AppInfo>> futureTask = new FutureTask<List<AppInfo>>(new AppCallable());
        appExecutors.diskIO().submit(futureTask);
        try {
            list = futureTask.get();
            Log.d("zhulf", "===size: " + list.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        appInfoList.addAll(list);
        return list;
    }

    public List<AppInfo> getAppInfo() {
        return appInfoList;
    }

    @Override
    public void onAppInfoObtainedSuccess() {

    }

    @Override
    public void onAppInfoObtainedFailed() {

    }

    class AppThread implements Runnable {

        @Override
        public void run() {
            ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
            PackageManager manager = MainApplication.getContext().getPackageManager();
            Intent mainIntent = new Intent("android.intent.action.MAIN", null);
            mainIntent.addCategory("android.intent.category.LAUNCHER");
            List<ResolveInfo> resolveInfos = manager.queryIntentActivities(mainIntent, 0);
            Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(manager));
            if (appInfos != null) {
                appInfos.clear();
                for (ResolveInfo resolveInfo : resolveInfos) {
                    if (resolveInfo.activityInfo.packageName.equals("com.mstar.tv.tvplayer.ui")) {
                        continue;
                    }
                    AppInfo appInfo = new AppInfo();
                    appInfo.setAppIcon(resolveInfo.loadIcon(manager));
                    appInfo.setAppName((String) resolveInfo.loadLabel(manager));
                    appInfo.setPackageName(resolveInfo.activityInfo.packageName);
                    appInfos.add(appInfo);
                }
            }

        }
    }

    class AppCallable implements Callable<List<AppInfo>> {

        @Override
        public List<AppInfo> call() throws Exception {
            ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
            PackageManager manager = MainApplication.getContext().getPackageManager();
            Intent mainIntent = new Intent("android.intent.action.MAIN", null);
            mainIntent.addCategory("android.intent.category.LAUNCHER");
            List<ResolveInfo> resolveInfos = manager.queryIntentActivities(mainIntent, 0);
            Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(manager));
            if (appInfos != null) {
                appInfos.clear();
                for (ResolveInfo resolveInfo : resolveInfos) {
                    if (resolveInfo.activityInfo.packageName.equals("com.ydg.tvplayer")) {
                        continue;
                    }
                    AppInfo appInfo = new AppInfo();
                    appInfo.setAppIcon(resolveInfo.loadIcon(manager));
                    appInfo.setAppName((String) resolveInfo.loadLabel(manager));
                    appInfo.setPackageName(resolveInfo.activityInfo.packageName);
                    appInfos.add(appInfo);
                }
            }
            return appInfos;
        }
    }
}
