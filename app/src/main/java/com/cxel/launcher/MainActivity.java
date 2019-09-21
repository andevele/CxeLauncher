package com.cxel.launcher;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.ViewGroup;
import com.cxel.launcher.adapter.InputSourceAdapter;
import com.cxel.launcher.util.Constant;
import org.evilbinary.tv.widget.BorderView;
import org.evilbinary.tv.widget.RoundedFrameLayout;
import org.evilbinary.tv.widget.TvZorderRelativeLayout;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mainContainer;
    private BorderView border;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    private void initViews(){
        border = new BorderView(this);
        border.setBackgroundResource(R.drawable.border_highlight);
        mainContainer = (ViewGroup) findViewById(R.id.list);
        border.attachTo(mainContainer);

        ViewClickListener listener = new ViewClickListener();
        for(int i = 0; i < mainContainer.getChildCount();i++) {
            mainContainer.getChildAt(i).setOnClickListener(listener);
        }

        TvZorderRelativeLayout tvstoreContainer = (TvZorderRelativeLayout)findViewById(R.id.view_tvstore_container);
        border.attachTo(tvstoreContainer);
        RoundedFrameLayout tvstore = (RoundedFrameLayout)findViewById(R.id.view_tvstore);
        tvstore.setOnClickListener(listener);

        initInputSourceView();

    }

    private void initInputSourceView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.input_source_RecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        border.attachTo(recyclerView);
        CreateSourceData(recyclerView,R.layout.input_soure_list_item);
    }

    class ViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(view == mainContainer.getChildAt(Constant.YOUTUBE_INDEX)) {
                launchApp("com.google.android.youtube.tv");
            } else if(view == mainContainer.getChildAt(Constant.NETFLIX_INDEX)) {
                launchApp("com.netflix.mediaclient");
            } else if(view == mainContainer.getChildAt(Constant.HOTSTAR_INDEX)) {
                launchApp("in.startv.hotstar");
            } else if(view == mainContainer.getChildAt(Constant.WIRELESS_DISPLAY_INDEX)) {
                launchApp("com.toptech.mitv.wfd");
            } else if(view == mainContainer.getChildAt(Constant.PRIME_VIDEO_INDEX)) {
                launchApp("com.amazon.avod.thirdpartyclient");
            } else if(view == mainContainer.getChildAt(Constant.APPS_INDEX)) {
                //launchApp("");
            } else if(view.getId() == R.id.view_tvstore) {
                launchApp("cm.aptoidetv.pt");
            }
        }
    }
    private void launchApp(String packageName) {
        if (isAppInstalled(getApplicationContext(), packageName)) {
            Intent intent = new Intent();
            intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName);
            if(intent != null) {
                intent.setPackage(null);
                MainActivity.this.startActivity(intent);
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

    private void CreateSourceData(RecyclerView recyclerView,int layoutId) {
        List<String> data = new ArrayList<String>();
        List<String> list = new ArrayList<String>();
        list.add(0,"ATV");
        list.add(1,"AV1");
        list.add(2,"AV2");
        list.add(3,"HDMI1");
        list.add(4,"HDMI2");
        list.add(5,"VGA");
        list.add(6,"MEDIA");
        data.addAll(list);
        InputSourceAdapter adapter = new InputSourceAdapter(getApplicationContext(), data,layoutId);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
    }
}