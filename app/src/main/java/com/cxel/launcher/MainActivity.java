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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.cxel.launcher.util.Constant;
import org.evilbinary.tv.widget.BorderView;

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

        for(int i = 0; i < mainContainer.getChildCount();i++) {
            mainContainer.getChildAt(i).setOnClickListener(new ViewClickListener());
        }

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
            if(view == mainContainer.getChildAt(Constant.INPUT_SOURCE_INDEX)) {
                launchApp("com.ydg.tvplayer");
            } else if(view == mainContainer.getChildAt(Constant.YOUTUBE_INDEX)) {
                launchApp("com.google.android.youtube.tv");
            } else if(view == mainContainer.getChildAt(Constant.NETFLIX_INDEX)) {
                launchApp("com.netflix.mediaclient");
            } else if(view == mainContainer.getChildAt(Constant.HOTSTAR_INDEX)) {
                launchApp("in.startv.hotstar");
            } else if(view == mainContainer.getChildAt(Constant.TV_STORE_INDEX)) {
                launchApp("cm.aptoidetv.pt");
            } else if(view == mainContainer.getChildAt(Constant.WIRELESS_DISPLAY_INDEX)) {
                launchApp("com.toptech.mitv.wfd");
            } else if(view == mainContainer.getChildAt(Constant.PRIME_VIDEO_INDEX)) {
                launchApp("com.amazon.avod.thirdpartyclient");
            } else if(view == mainContainer.getChildAt(Constant.APPS_INDEX)) {
                //launchApp("");
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

    }
}