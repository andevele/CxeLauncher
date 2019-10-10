package com.cxel.launcher;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.SystemProperties;

import com.cxel.launcher.adapter.InputSourceAdapter;
import com.cxel.launcher.control.ControlManager;
import com.cxel.launcher.data.DataAsyncTask;
import com.cxel.launcher.net.NetworkMonitor;
import com.cxel.launcher.util.Constant;
import com.cxel.launcher.view.RecycleViewItemDivider;
import com.cxel.launcher.view.TopBar;
import com.mstar.android.tv.TvCommonManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTopBars();
        initViews();
    }

    private void initViews() {
        TextView titleLabel = (TextView) findViewById(R.id.title_label_text);
        titleLabel.setText(getResources().getString(R.string.home_page));
        border = new BorderView(this);
        //border.setBackgroundResource(R.drawable.border_highlight);
        border.setBackgroundResource(R.drawable.custom_border);
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
        mNetworkMonitor.checkUsb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //boot to launcher or TV, 0 is to launcher,1 is to TV
        SystemProperties.set("persist.sys.intvmode", "0");
        goToStorageSource();
        if (mNetworkMonitor != null) {
            mNetworkMonitor.startMonitor();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mNetworkMonitor != null) {
            mNetworkMonitor.stopMonitor();
        }
    }

    private void initInputSourceView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.input_source_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, 1, false);
//        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        recyclerView.addItemDecoration(new RecycleViewItemDivider(this, LinearLayoutManager.VERTICAL, 5, getResources().getColor(R.color.input_source_item_divide_bg)));
//        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.custom_divider));
//        recyclerView.addItemDecoration(divider);
//        recyclerView.addItemDecoration(new RecycleViewItemDivider(this,LinearLayoutManager.VERTICAL
//        , 5, getResources().getColor(R.color.input_source_item_divide_bg)));
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
                startActivity("com.cxel.launcher.action.AllAppsAdapter");
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

    private void startActivity(String action) {
        ControlManager.getInstance().startActivity(action);
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

    private void goToStorageSource() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                ControlManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        //Do not respond to the exit key
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkMonitor != null) {
            mNetworkMonitor.stopMonitor();
        }
    }
}