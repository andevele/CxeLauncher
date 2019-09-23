package com.cxel.launcher;

import android.app.Application;

import com.cxel.launcher.data.DataAsyncTask;
import com.cxel.launcher.util.Constant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainApplication extends Application {
    Map<String, List<String>> dataMap = new HashMap<String, List<String>>();

    @Override
    public void onCreate() {
        super.onCreate();
        DataAsyncTask dataAsyncTask = new DataAsyncTask();
        dataAsyncTask.setDataTask(new DataAsyncTask.DataTask() {
            @Override
            public void excuteSuccess(List<String> list) {
                dataMap.put(Constant.INPUT_SOURCE_SECTION, list);
            }

            @Override
            public void executeFailed() {

            }
        });
        dataAsyncTask.execute(Constant.INPUT_SOURCE_JSON_FILE, Constant.INPUT_SOURCE_SECTION);
    }

    public Map<String, List<String>> getDataMap() {
        return dataMap;
    }
}
