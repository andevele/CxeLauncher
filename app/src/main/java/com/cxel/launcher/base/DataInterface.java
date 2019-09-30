package com.cxel.launcher.base;

public class DataInterface {
    public interface AppTaskCallBack {
        public void onAppInfoAdded(int size);

        public void onAppInfoRemoved(int size);
    }
}
