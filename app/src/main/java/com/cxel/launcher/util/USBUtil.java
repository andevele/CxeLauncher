package com.cxel.launcher.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.TextUtils;
import android.util.Log;

/**
 * zhulf 20190924
 * andevele@163.com
 * USB工具
 */
public class USBUtil {
    /**
     * 检测是否有U盘已插好并挂载成功
     *
     * @param storageManager
     * @return
     */
    public static boolean isUSBMounted(StorageManager storageManager) {
        boolean result = false;
        try {
            Class clz = StorageManager.class;
            Method getVolumeList = clz.getMethod("getVolumeList");
            StorageVolume[] volumes = (StorageVolume[]) getVolumeList.invoke(storageManager);
            for (int i = 0; i < volumes.length; i++) {
                String element = volumes[i].toString();
                if (element.contains("/mnt/usb") && element.contains("mounted")) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getUSBPathInfo(StorageManager storageManager) {
        String ret = "/mnt/sdcard";
        try {
            Class clz = StorageManager.class;
            Method getVolumeList = clz.getMethod("getVolumeList");
            StorageVolume[] volumes = (StorageVolume[]) getVolumeList.invoke(storageManager);
            for (int i = 0; i < volumes.length; i++) {
                String element = volumes[i].toString();
                Log.d("zhulf","==element: " + element);
                if (element.contains("/mnt/usb/s") && element.contains("mounted")) {
                    return element;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 检测u盘路径是否存在
     * @param storageManager
     * @param path
     * @return
     */
    public static String getVolumeState(StorageManager storageManager, String path) {
        String result = "";
        if (null == storageManager || TextUtils.isEmpty(path)) {
            return result;
        }
        try {
            Class clz = StorageManager.class;
            Method getVolumeList = clz.getMethod("getVolumeState", String.class);
            result = (String) getVolumeList.invoke(storageManager, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

