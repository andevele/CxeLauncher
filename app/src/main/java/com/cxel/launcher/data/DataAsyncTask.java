package com.cxel.launcher.data;

import android.os.AsyncTask;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cxel.launcher.model.SourceJsonBean;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataAsyncTask extends AsyncTask<String, Void, List<String>> {
    private static final String TAG = "DataAnyscTask";
    private DataTask task;

    public interface DataTask {
        void excuteSuccess(List<String> list);

        void executeFailed();
    }

    public void setDataTask(DataTask task) {
        this.task = task;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        String jsonFilePath = params[0];
        String jsonFileSection = params[1];
        List<String> sourceList = new ArrayList<String>();
        File jsonFile = new File(jsonFilePath);
        if (!jsonFile.exists()) {
            Log.e(TAG, "jsonFile is not exist");
            return null;
        }
        String jsonString = "";
        try {
            jsonString = FileUtils.readFileToString(jsonFile, "utf-8");

//            Log.d(TAG, "jsonString: \n" + jsonString);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "readFileToString error");
            e.printStackTrace();
        }

        if (jsonString == null || jsonString.contentEquals("")) {
            Log.e(TAG, "jsonString is null");
            throw new NullPointerException();
        }
        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONArray jsonArray = (JSONArray) jsonObject.get(jsonFileSection);
        List<SourceJsonBean> list = JSONArray.parseArray(jsonArray.toJSONString(), SourceJsonBean.class);
        for (SourceJsonBean element : list) {
            if (element.getValue().equals("1")) {
                sourceList.add(element.getName());
            }
        }

        return sourceList;
    }

    @Override
    protected void onPostExecute(List<String> sourceList) {
        super.onPostExecute(sourceList);
        if (sourceList == null || sourceList.size() < 0) {
            Log.e(TAG, "sourceList is null");
            return;
        }
        task.excuteSuccess(sourceList);
    }
}
