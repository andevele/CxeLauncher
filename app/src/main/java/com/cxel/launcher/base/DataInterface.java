package com.cxel.launcher.base;

import com.cxel.launcher.model.AppInfo;

import java.util.List;

/**
 * zhulf 20190924
 * andevele@163.com
 * app任务执行接口
 */
public class DataInterface {
    public interface AppTaskCallBack {
        public void onAppInfoAdded(List<AppInfo> list);

        public void onAppInfoRemoved(List<AppInfo> list);
    }
}
