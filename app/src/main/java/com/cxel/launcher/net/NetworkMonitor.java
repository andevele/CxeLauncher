package com.cxel.launcher.net;

import java.io.File;
import java.lang.ref.WeakReference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import com.cxel.launcher.util.Constant;
import com.cxel.launcher.util.USBUtil;

/**
 * zhulf 20190924
 * andevele@163.com
 * 网络连接
 */
public class NetworkMonitor extends BroadcastReceiver {

    private static final String TAG = "NetworkMonitor";

    private INetworkUpdateListener mListener;
    private WeakReference<Context> mContextRef;

    // Network Kind and value
    public static final String KEY_NET_INTERFACE_ID = "NetworkMonitor.interface";
    public static final int ID_INTERFACE_ETHERNET = 0;
    public static final int ID_INTERFACE_WIFI = 1;

    // Network status and value
    public static final String KEY_NET_STATUS_ID = "NetworkMonitor.Status";
    public static final int ID_STATUS_DISCONNECTED = 0;
    public static final int ID_STATUS_UNREACHABLE = 1;
    public static final int ID_STATUS_CONNECTED = 2;

    // WIFI RSSI
    public static final String KEY_NET_WIFI_LEVEL = "NetworkMonitor.wifi.level";
    public static final int WIFI_LEVEL_COUNT = 4;

    private int mActiveInterface = ID_INTERFACE_ETHERNET;
    private int mActiveStatus = ID_STATUS_DISCONNECTED;
    private int mWifiLevel = 0;
    private int mWifiRssi;

    private boolean mEthHWConnected, mEthReachable;
    private boolean mWifiEnabled, mWifiConnected;

    // flag:0 indicate unregister; 1 register
    private boolean flag = false;

    public interface INetworkUpdateListener {
        void onUpdateNetworkConnectivity(Bundle newConnectivity);
        void onUpdateUSBConnectivity(String action);
    }

    public NetworkMonitor(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            mWifiEnabled = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, ConnectivityManager.TYPE_ETHERNET) == ConnectivityManager.TYPE_WIFI;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                mEthHWConnected = true;
                boolean isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
                if (isWifi) {
                    mWifiEnabled = true;
                }
            } else {
                mEthHWConnected = false;
            }
        } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            mWifiEnabled = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN) == WifiManager.WIFI_STATE_ENABLED;
        } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            final NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            mWifiConnected = networkInfo != null && networkInfo.isConnected();
            mWifiLevel = 0;
            mWifiRssi = -200;
        } else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
            if (mWifiConnected) {
                mWifiRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -200);
                mWifiLevel = WifiManager.calculateSignalLevel(mWifiRssi, WIFI_LEVEL_COUNT);
            }
        } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)
                || action.equals(Intent.ACTION_MEDIA_REMOVED)
                || action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)
                || action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
            if (mListener != null) {
                mListener.onUpdateUSBConnectivity(action);
            }
        }

        updateActiveNetwork();

        if (mListener != null) {
            mListener.onUpdateNetworkConnectivity(getCurrentConnectivityInfo());
        }
    }

    private Bundle getCurrentConnectivityInfo() {
        Bundle mCurrentConnectivity = new Bundle();
        mCurrentConnectivity.putInt(KEY_NET_INTERFACE_ID, mActiveInterface);
        mCurrentConnectivity.putInt(KEY_NET_STATUS_ID, mActiveStatus);
        if (mActiveInterface == ID_INTERFACE_WIFI) {
            mCurrentConnectivity.putInt(KEY_NET_WIFI_LEVEL, mWifiLevel);
        }

        Log.v(TAG, "NetworkMonitor.interface:" + mActiveInterface + "NetworkMonitor.Status:" + mActiveStatus + "NetworkMonitor.wifi.level:" + mWifiLevel);

        return mCurrentConnectivity;
    }

    public NetworkMonitor(Context context, INetworkUpdateListener mListener) {
        mContextRef = new WeakReference<Context>(context);
        this.mListener = mListener;
    }

    /**
     * register broadcast receiver
     */
    public void startMonitor() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);

        IntentFilter usbFilter = new IntentFilter();
        usbFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        usbFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        usbFilter.addDataScheme("file");

        Context context = mContextRef.get();
        if (context != null && flag == false) {
            context.registerReceiver(this, filter);
            context.registerReceiver(this, usbFilter);
            flag = true;
        }
    }

    /**
     * unregister broadcast receiver
     */
    public void stopMonitor() {
        Context context = mContextRef.get();
        if (context != null && flag == true) {
            flag = false;
            context.unregisterReceiver(this);
        }
    }

    /**
     * 检测USB状态
     */
    public void checkUsb() {
        String path = null;
        Context context = mContextRef.get();
        if(context == null) {
            return;
        }
        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        if(USBUtil.isUSBMounted(sm)) {
            if (mListener != null) {
                mListener.onUpdateUSBConnectivity(Constant.ACTION_USB_MOUNTED);
            }
        }
    }


    private void updateActiveNetwork() {
        if (mWifiEnabled) {
            mActiveInterface = ID_INTERFACE_WIFI;
            if (mWifiConnected) {
                mActiveStatus = ID_STATUS_CONNECTED;
            } else {
                mActiveStatus = ID_STATUS_UNREACHABLE;
            }
        } else {
            mActiveInterface = ID_INTERFACE_ETHERNET;
            if (mEthHWConnected) {
                mActiveStatus = ID_STATUS_CONNECTED;
//                if (mEthReachable) {
//                    mActiveStatus = ID_STATUS_CONNECTED;
//                } 
//                else {
//                    mActiveStatus = ID_STATUS_UNREACHABLE;
//                }
            } else {
                mActiveStatus = ID_STATUS_DISCONNECTED;
            }
        }
    }
}
