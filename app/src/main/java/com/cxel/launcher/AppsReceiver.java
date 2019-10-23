package com.cxel.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cxel.launcher.data.AppData;
import com.cxel.launcher.util.ConstantResource;
import com.cxel.launcher.util.LogUtils;

/**
 * zhulf 20190924
 * andevele@163.com
 * 所有app相关广播
 */
public class AppsReceiver extends BroadcastReceiver {
    private static final String TAG = AppsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_PACKAGE_CHANGED.equals(action)
                || Intent.ACTION_PACKAGE_REMOVED.equals(action)
                || Intent.ACTION_PACKAGE_ADDED.equals(action)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
            LogUtils.v(TAG, "replacing:" + replacing + " packageName:" + packageName);
            int op = ConstantResource.OP_NONE;
            if (packageName == null || packageName.length() == 0) {
                return;
            }
            if (Intent.ACTION_PACKAGE_CHANGED.equals(action)) {
                op = ConstantResource.OP_UPDATE;
            } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                if (!replacing) {
                    op = ConstantResource.OP_REMOVE;
                }
            } else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                if (!replacing) {
                    op = ConstantResource.OP_ADD;
                } else {
                    op = ConstantResource.OP_UPDATE;
                }
            }

            if (op != ConstantResource.OP_NONE) {
                AppData.getInstance().updateData(op, new String[]{packageName});
            }
        }
    }
}
