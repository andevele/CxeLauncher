package com.cxel.launcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cxel.launcher.adapter.AllAppsAdapter;
import com.cxel.launcher.data.AppData;
import com.cxel.launcher.model.AppInfo;
import com.cxel.launcher.view.CustomDecoration;

import org.evilbinary.tv.widget.BorderView;

import java.util.ArrayList;
import java.util.List;

public class AppsActivity extends AppCompatActivity {
    private List<AppInfo> appList;
    private AllAppsAdapter allAppsAdapter;
    private static final int spanCount = 4;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView appRecyclerView;
    private BorderView border;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_main);
        initData();
        initView();
    }

    private void initData() {
        appList = new ArrayList<AppInfo>();
        List<AppInfo> appInfoList = AppData.getInstance().getAppInfo();
        if (appInfoList == null || appInfoList.size() < 1) {
            appInfoList = AppData.getInstance().catchAppInfo();
        }
        allAppsAdapter = new AllAppsAdapter(this, appInfoList);
        gridLayoutManager = new GridLayoutManager(this, spanCount);
    }

    private void initView() {
        border = new BorderView(this);
        border.setBackgroundResource(R.drawable.border_highlight);
        appRecyclerView = (RecyclerView) findViewById(R.id.apps_list);
        appRecyclerView.setLayoutManager(gridLayoutManager);
        appRecyclerView.addItemDecoration(new CustomDecoration());
        appRecyclerView.setAdapter(allAppsAdapter);
        border.attachTo(appRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
