package com.cxel.launcher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cxel.launcher.R;
import java.util.List;

/**
 * zhulf 20190924
 * andevele@163.com
 * 主页通道列表适配器
 */
public class InputSourceAdapter extends RecyclerView.Adapter<InputSourceAdapter.CustomViewHolder> {
    private final Context context;
    private final List<String> dataSource;
    private final int layoutId;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int layoutPos);
    }

    public InputSourceAdapter(Context context, List<String> dataSource, int layoutId) {
        this.context = context;
        this.dataSource = dataSource;
        this.layoutId = layoutId;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d("zhulf","===viewType: " + viewType);
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder viewHolder, int position) {
        viewHolder.mTextView.setText(dataSource.get(position));
        viewHolder.itemView.setTag(position);
        if(onItemClickListener == null) {
            return;
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int layoutPos = viewHolder.getLayoutPosition();
                onItemClickListener.onItemClick(layoutPos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
