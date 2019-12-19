package com.cxel.launcher.util;

import com.cxel.launcher.R;

/**
 * zhulf 20190924
 * andevele@163.com
 * 网络相关常量
 */
public class ConstantResource {
    /**
     * wifi connection signal levels image resources, the array index denote the
     * signal level</br> <b>0</b> means non-signal and <b>4</b> means full level
     */
    public static int WIFI_SIGNAL_LEVLES[] = {
            R.drawable.ic_wifi_level_1,
            R.drawable.ic_wifi_level_2,
            R.drawable.ic_wifi_level_3,
            R.drawable.ic_wifi_level_4};
    /**
     * wire network connect state image resource<br/>
     * <b>0</b> connected<br/>
     * <b>1</b> disconnected<br/>
     * <b>2</b> unreachable
     */
    public static int WIRE_CONNECT_STATES[] = {
            R.drawable.ic_eth_connected,
            R.drawable.ic_eth_disconnected,
            R.drawable.ic_eth_unreachable};

    /**
     * Network states, if networkType is wifi, the state is the signal level and
     * if the networkType is wire, it indicates current connected state,
     * connected, disconnected or unreachable.
     *
     * @author mutter
     */
    public static class NetworkStates {
        public static final int WIFI_SIGNAL_0 = 0;
        public static final int WIFI_SIGNAL_1 = 1;
        public static final int WIFI_SIGNAL_2 = 2;
        public static final int WIFI_SIGNAL_3 = 3;
        public static final int WIFI_SIGNAL_4 = 4;
        public static final int WIRE_CONNECTED = 5;
        public static final int WIRE_DISCONNECTED = 6;
        public static final int WIRE_UNREACHABLE = 7;
    }

    public static final int OP_NONE = 0;
    public static final int OP_ADD = 1;
    public static final int OP_CHANGED = 2;
    public static final int OP_REMOVE = 3; // uninstlled
    public static final int OP_UNAVAILABLE = 4; // external media unmounted
}
