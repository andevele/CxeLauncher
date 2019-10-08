package com.cxel.launcher.view;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * zhulf 20190924
 * andevele@163.com
 * 所有app页面,自定义间距
 */
public class CustomDecoration extends RecyclerView.ItemDecoration {
    private int count;

    public CustomDecoration() {
        super();
    }

    public CustomDecoration(int count) {
        this.count = count;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int visualPos = parent.getChildAdapterPosition(view);

        if (parent.getChildAdapterPosition(view) % 4 == 0) {
            //第一列
            outRect.left = 90;
            outRect.right = -30;
        } else if (parent.getChildAdapterPosition(view) % 4 == 1) {
            //第二列
            outRect.left = 50;
            outRect.right = 10;
        } else if (parent.getChildAdapterPosition(view) % 4 == 2) {
            //第三列
            outRect.left = 10;
            outRect.right = 50;
        } else if (parent.getChildAdapterPosition(view) % 4 == 3) {
            //第四列
            outRect.left = -30;
            outRect.right = 90;
        }
        if (visualPos - 4 < 0) {
            outRect.top = 20;
        }
//        else {
//            outRect.top = 20;
//        }
        outRect.bottom = 20;

    }
}
