package com.cxel.launcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.evilbinary.tv.widget.BorderView;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BorderView border = new BorderView(this);
        border.setBackgroundResource(R.drawable.border_highlight);
        mainContainer = (ViewGroup) findViewById(R.id.list);
        border.attachTo(mainContainer);
    }
}
