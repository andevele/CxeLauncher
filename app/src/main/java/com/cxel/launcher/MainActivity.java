package com.cxel.launcher;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.cxel.launcher.adapter.InputSourceAdapter;
import com.cxel.launcher.control.ControlManager;
import com.cxel.launcher.data.DataAsyncTask;
import com.cxel.launcher.net.NetworkMonitor;
import com.cxel.launcher.util.Constant;
import com.cxel.launcher.view.RecycleViewItemDivider;
import com.cxel.launcher.view.TopBar;

import org.evilbinary.tv.widget.BorderView;
import org.evilbinary.tv.widget.RoundedFrameLayout;
import org.evilbinary.tv.widget.TvZorderRelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * zhulf 20190924
 * andevele@163.com
 * 主Activity
 */
public class MainActivity extends AppCompatActivity {

    private ViewGroup mainContainer;
    private BorderView border;
    private NetworkMonitor.INetworkUpdateListener mNetworkUpdateListener;
    private NetworkMonitor mNetworkMonitor;
    private TopBar topBar;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    private BroadcastReceiver mStorageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("zhulf", "======action: " + action);
            topBar.updateView(action);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initTopBars();
    }

    private void initViews() {
        border = new BorderView(this);
        border.setBackgroundResource(R.drawable.border_highlight);
        mainContainer = (ViewGroup) findViewById(R.id.list);
        border.attachTo(mainContainer);

        ViewClickListener listener = new ViewClickListener();
        for (int i = 0; i < mainContainer.getChildCount(); i++) {
            mainContainer.getChildAt(i).setOnClickListener(listener);
        }

        TvZorderRelativeLayout tvstoreContainer = (TvZorderRelativeLayout) findViewById(R.id.view_tvstore_container);
        border.attachTo(tvstoreContainer);
        RoundedFrameLayout tvstore = (RoundedFrameLayout) findViewById(R.id.view_tvstore);
        tvstore.setOnClickListener(listener);

        initInputSourceView();

    }

    private void initTopBars() {
        topBar = (TopBar) findViewById(R.id.topbar_container);
        mNetworkUpdateListener = (NetworkMonitor.INetworkUpdateListener) topBar;
        mNetworkMonitor = new NetworkMonitor(this, mNetworkUpdateListener);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mStorageReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNetworkMonitor.startMonitor();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mNetworkMonitor.stopMonitor();
    }

    private void initInputSourceView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.input_source_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, 1, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        recyclerView.addItemDecoration(new RecycleViewItemDivider(this, LinearLayoutManager.VERTICAL, 3, getResources().getColor(R.color.input_source_item_divide_bg)));
        border.attachTo(recyclerView);
        CreateSourceData(recyclerView, R.layout.input_soure_list_item);
    }

    class ViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == mainContainer.getChildAt(Constant.YOUTUBE_INDEX)) {
                launchApp("com.google.android.youtube.tv");
            } else if (view == mainContainer.getChildAt(Constant.NETFLIX_INDEX)) {
                launchApp("com.netflix.mediaclient");
            } else if (view == mainContainer.getChildAt(Constant.HOTSTAR_INDEX)) {
                launchApp("in.startv.hotstar");
            } else if (view == mainContainer.getChildAt(Constant.WIRELESS_DISPLAY_INDEX)) {
                launchApp("com.toptech.mitv.wfd");
            } else if (view == mainContainer.getChildAt(Constant.PRIME_VIDEO_INDEX)) {
                launchApp("com.amazon.avod.thirdpartyclient");
            } else if (view == mainContainer.getChildAt(Constant.APPS_INDEX)) {
                //launchApp("");
            } else if (view.getId() == R.id.view_tvstore) {
                launchApp("cm.aptoidetv.pt");
            }
        }
    }

    private void launchApp(String packageName) {
        if (isAppInstalled(getApplicationContext(), packageName)) {
            Intent intent = new Intent();
            intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.setPackage(null);
                //MainActivity.this.startActivity(intent);
                ControlManager.getInstance().startActivity(intent);
            }
        } else {
            goToMarket(MainActivity.this, packageName);
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
        }
    }

    private void CreateSourceData(final RecyclerView recyclerView, final int layoutId) {
        List<String> data = new ArrayList<String>();
        List<String> list = new ArrayList<String>();
//        list.add(0,"ATV");
//        list.add(1,"AV1");
//        list.add(2,"AV2");
//        list.add(3,"HDMI1");
//        list.add(4,"HDMI2");
//        list.add(5,"VGA");
//        list.add(6,"MEDIA");
//        data.addAll(list);
//        InputSourceAdapter adapter = new InputSourceAdapter(getApplicationContext(), data,layoutId);
//        recyclerView.setAdapter(adapter);
//        recyclerView.scrollToPosition(0);
//        adapter.notifyDataSetChanged();

        MainApplication application = (MainApplication) getApplication();
        Map<String, List<String>> dataMap = application.getDataMap();
        List<String> dataList = dataMap.get(Constant.INPUT_SOURCE_SECTION);
        if (dataList == null || dataList.size() < 0) {
            DataAsyncTask dataAsyncTask = new DataAsyncTask();
            dataAsyncTask.setDataTask(new DataAsyncTask.DataTask() {
                @Override
                public void excuteSuccess(List<String> dataList) {
                    updateList(recyclerView, dataList, layoutId);
                }

                @Override
                public void executeFailed() {

                }
            });
            dataAsyncTask.execute(Constant.INPUT_SOURCE_JSON_FILE, Constant.INPUT_SOURCE_SECTION);
            return;
        }

        updateList(recyclerView, dataList, layoutId);

    }

    private void updateList(RecyclerView recyclerView, final List<String> dataList, int layoutId) {
        InputSourceAdapter adapter = new InputSourceAdapter(getApplicationContext(), dataList, layoutId);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new InputSourceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int layoutPos) {
                ControlManager.getInstance().switchSource(layoutPos, dataList);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mStorageReceiver);
    }
}