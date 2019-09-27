package com.cxel.launcher.view;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
            outRect.left = 0;
            outRect.right = 15;
        } else if (parent.getChildAdapterPosition(view) % 4 == 1) {
            //第二列
            outRect.left = 5;
            outRect.right = 10;
        } else if (parent.getChildAdapterPosition(view) % 4 == 2) {
            //第三列
            outRect.left = 10;
            outRect.right = 5;
        } else if (parent.getChildAdapterPosition(view) % 4 == 3) {
            //第四列
            outRect.left = 15;
            outRect.right = 0;
        }
        if(visualPos - 4 < 0) {
            outRect.top = 0;
        } else {
            outRect.top = 20;
        }

    }
}
