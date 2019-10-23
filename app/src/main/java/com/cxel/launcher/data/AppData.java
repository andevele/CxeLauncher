package com.cxel.launcher.data;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.cxel.launcher.MainApplication;
import com.cxel.launcher.adapter.AllAppsAdapter;
import com.cxel.launcher.model.AppInfo;
import com.cxel.launcher.thread.AppExecutors;
import com.cxel.launcher.util.ConstantResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * zhulf 20190924
 * andevele@163.com
 * 所有app获取的数据类
 */
public class AppData {
    private static AppData INSTANCE = null;
    private final Context mContext;
    private final PackageManager packageManager;
    private AppExecutors appExecutors;
    private List<AppInfo> appInfoList = new ArrayList<AppInfo>();
    private int opt;
    private String[] packages;
    private AllAppsAdapter callBack;

    public AppData() {
        mContext = MainApplication.getContext();
        packageManager = MainApplication.getContext().getPackageManager();
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        for(AppInfo appInfo : list) {
//            Log.d("zhulf","===pkg: " + appInfo.getPackageName());
//        }
        appInfoList.addAll(list);
        return list;
    }

    public List<AppInfo> getAppInfo() {
        return appInfoList;
    }

    public void updateData(int op, String[] pkgs) {
        this.opt = op;
        this.packages = pkgs;
        final int N = packages.length;
        switch (opt) {
            case ConstantResource.OP_ADD:
                for (int i = 0; i < N; i++) {
                    addPackage(mContext, packages[i]);
                }
                break;
            case ConstantResource.OP_UPDATE:
                for (int i = 0; i < N; i++) {
                    //will be to do
                }
                break;
            case ConstantResource.OP_REMOVE:
            case ConstantResource.OP_UNAVAILABLE:
                for (int i = 0; i < N; i++) {
                    removePackage(packages[i]);
                }
                break;
        }
    }

    private void addPackage(Context mContext, String packageName) {
        List<ResolveInfo> list = findActivitiesForPackage(mContext, packageName);
        List<AppInfo> newAppList = new ArrayList<AppInfo>();
        if (list.size() > 0) {
            for (ResolveInfo info : list) {
                AppInfo appInfo = new AppInfo();
                appInfo.setAppIcon(info.loadIcon(packageManager));
                appInfo.setAppName((String) info.loadLabel(packageManager));
                appInfo.setPackageName(info.activityInfo.packageName);
                addApps(appInfo);
            }
        }
    }

    private void addApps(AppInfo appInfo) {
        if (findActivity(appInfoList, appInfo)) {
            return;
        }
        appInfoList.add(appInfo);
        Collections.sort(appInfoList,new AppInfo.AppComparator());
        callBack.onAppInfoAdded(appInfoList.size());
    }

    private void removePackage(String packageName) {
        final List<AppInfo> data = appInfoList;
        for (int i = data.size() - 1; i >= 0; i--) {
            AppInfo info = data.get(i);
            final String pkgName = info.getPackageName();
            if (packageName.equals(pkgName)) {
                appInfoList.remove(i);
            }
        }
        callBack.onAppInfoRemoved(appInfoList.size());
    }

    public void setCallBack(AllAppsAdapter allAppsAdapter) {
        this.callBack = allAppsAdapter;
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
            appInfoList.clear();
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

    static List<ResolveInfo> findActivitiesForPackage(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(packageName);

        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        return apps != null ? apps : new ArrayList<ResolveInfo>();
    }

    private static boolean findActivity(List<AppInfo> apps, AppInfo appsInfo) {
        final int N = apps.size();
        for (int i = 0; i < N; i++) {
            final AppInfo info = apps.get(i);
            if (info.getPackageName().equals(appsInfo.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
