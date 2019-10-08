package com.cxel.launcher.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxel.launcher.R;
import com.cxel.launcher.base.DataInterface;
import com.cxel.launcher.data.AppData;
import com.cxel.launcher.model.AppInfo;

import java.util.List;

/**
 * zhulf 20190924
 * andevele@163.com
 * 所有app宫格适配器
 */
public class AllAppsAdapter extends RecyclerView.Adapter<AllAppsAdapter.ViewHolder> implements DataInterface.AppTaskCallBack {

    private View mView;
    private List<AppInfo> mlist;
    private Context mContext;
    private ViewHolder mVH;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;
    private AppInfo appInfo = null;

    public AllAppsAdapter(Context context, List<AppInfo> list) {
        this.mlist = list;
        this.mContext = context;
        AppData.getInstance().setCallBack(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_item_view,
                parent, false);
        mVH = new ViewHolder(mView);
        return mVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        appInfo = mlist.get(position);
        holder.icon.setBackground(appInfo.getAppIcon());
        holder.name.setText(appInfo.getAppName());

        final int itemposition = position;
        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String pkg = mlist.get(position).getPackageName();
                    listener.onClick(pkg);
                }
            }
        });
        holder.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(itemposition);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public void onAppInfoAdded(int size) {
        //this.notifyDataSetChanged();
        this.notifyItemInserted(size);
        //((AppsActivity)mContext).updateViews();
    }

    @Override
    public void onAppInfoRemoved(int size) {
        //this.notifyItemRemoved(size);
        this.notifyDataSetChanged();
    }

    public void updateData(List<AppInfo> list) {
        this.mlist = list;
        //this.notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(String pkgName);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout itemlayout;
        public TextView name;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_app_icon);
            itemlayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
            name = (TextView) itemView.findViewById(R.id.item_app_name);
        }
    }
}
