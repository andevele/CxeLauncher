package com.cxel.launcher;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxel.launcher.adapter.AllAppsAdapter;
import com.cxel.launcher.control.ControlManager;
import com.cxel.launcher.data.AppData;
import com.cxel.launcher.model.AppInfo;
import com.cxel.launcher.net.NetworkMonitor;
import com.cxel.launcher.view.CustomDecoration;
import com.cxel.launcher.view.TopBar;

import org.evilbinary.tv.widget.BorderView;
import org.evilbinary.tv.widget.TvGridLayoutManagerScrolling;

import java.util.ArrayList;
import java.util.List;

/**
 * zhulf 20190924
 * andevele@163.com
 * 所有app页面
 */
public class AppsActivity extends AppCompatActivity {
    private List<AppInfo> appList;
    private AllAppsAdapter allAppsAdapter;
    private static final int spanCount = 4;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView appRecyclerView;
//    private BorderView border;
    private Handler handler = new Handler();
    private TopBar topBar;
    private RelativeLayout topBarLayout;
    private NetworkMonitor.INetworkUpdateListener mNetworkUpdateListener;
    private NetworkMonitor mNetworkMonitor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_main);
        initTopBars();
        initData();
        initView();

    }

    private void initTopBars() {
        topBar = (TopBar) findViewById(R.id.topbar_container);
        mNetworkUpdateListener = (NetworkMonitor.INetworkUpdateListener) topBar;
        if(mNetworkMonitor == null) {
            mNetworkMonitor = new NetworkMonitor(this, mNetworkUpdateListener);
        }
        mNetworkMonitor.checkUsb();
    }

    private void initData() {
        appList = new ArrayList<AppInfo>();
        List<AppInfo> appInfoList = AppData.getInstance().getAppInfo();
        if (appInfoList == null || appInfoList.size() < 1) {
            appInfoList = AppData.getInstance().catchAppInfo();
        }
        appList = appInfoList;
        allAppsAdapter = new AllAppsAdapter(this, appInfoList);
        gridLayoutManager = new TvGridLayoutManagerScrolling(this, spanCount);
    }

    private void initView() {
        TextView titleLabel = (TextView) findViewById(R.id.title_label_text);
        titleLabel.setText(getResources().getString(R.string.apps_page));
//        border = new BorderView(this);
        //border.setBackgroundResource(R.drawable.border_highlight);
//        border.setBackgroundResource(R.drawable.custom_border);
        appRecyclerView = (RecyclerView) findViewById(R.id.apps_list);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        appRecyclerView.setLayoutManager(gridLayoutManager);
        appRecyclerView.addItemDecoration(new CustomDecoration());
        appRecyclerView.setFocusable(false);
//        border.attachTo(appRecyclerView);
        allAppsAdapter.setHasStableIds(true);
        appRecyclerView.setItemAnimator(null);
        appRecyclerView.setAdapter(allAppsAdapter);
        appRecyclerView.scrollToPosition(0);

        allAppsAdapter.setOnItemClickListener(new AllAppsAdapter.OnItemClickListener() {

            @Override
            public void onClick(String pkgName) {
                ControlManager.getInstance().startInstalledApp(AppsActivity.this, pkgName);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNetworkMonitor.startMonitor();
        initTopBars();
        List<AppInfo> appInfoList = AppData.getInstance().getAppInfo();
        if (appInfoList == null || appInfoList.size() < 1) {
            appInfoList = AppData.getInstance().catchAppInfo();
        }
        allAppsAdapter.updateData(appInfoList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mNetworkMonitor.stopMonitor();
    }

    public void updateViews() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                appRecyclerView.setItemAnimator(null);
            }
        }, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}