package com.cxel.launcher.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cxel.launcher.R;
import com.cxel.launcher.net.NetworkMonitor;
import com.cxel.launcher.view.TopBar;

/**
 * zhulf 20190924
 * andevele@163.com
 * 父类Activity
 */
public class BaseActivity extends AppCompatActivity {
    protected NetworkMonitor mNetworkMonitor;
    private NetworkMonitor.INetworkUpdateListener mNetworkUpdateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TopBar topBar = (TopBar) findViewById(R.id.topbar_container);
        mNetworkUpdateListener = (NetworkMonitor.INetworkUpdateListener) topBar;
        if (mNetworkMonitor == null) {
            mNetworkMonitor = new NetworkMonitor(this, mNetworkUpdateListener);
        }
        mNetworkMonitor.startMonitor();
        mNetworkMonitor.checkUsb();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkMonitor != null) {
            mNetworkMonitor.stopMonitor();
        }
    }
}
