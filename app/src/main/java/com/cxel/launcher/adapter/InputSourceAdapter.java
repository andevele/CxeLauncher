package com.cxel.launcher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cxel.launcher.R;

import java.util.List;

public class InputSourceAdapter extends RecyclerView.Adapter<InputSourceAdapter.CustomViewHolder> {
    private final Context context;
    private final List<String> dataSource;
    private final int layoutId;

    public InputSourceAdapter(Context context, List<String> dataSource, int layoutId) {
        this.context = context;
        this.dataSource = dataSource;
        this.layoutId = layoutId;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutId,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.mTextView.setText(dataSource.get(position));
        viewHolder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public CustomViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.textView);
        }
    }
}
