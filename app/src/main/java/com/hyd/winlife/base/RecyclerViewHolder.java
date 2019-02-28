package com.hyd.winlife.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private SparseArray<View> views;
    private View itemView;
    private OnItemViewClickListener onItemViewClickListener;

    public RecyclerViewHolder(View itemView, OnItemViewClickListener onItemViewClickListener) {
        super(itemView);
        this.itemView = itemView;
        views = new SparseArray<View>();
        this.onItemViewClickListener = onItemViewClickListener;
    }

    public <T extends View> T getView(int viewId, boolean clickEnable) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            if (clickEnable) {
                view.setFocusable(false);
                view.setOnClickListener(this);
            }
            views.put(viewId, view);
        }
        return (T) view;
    }

    public View getItemView() {
        return itemView;
    }

    public interface OnItemViewClickListener {
        void onViewClick(View view);
    }

    @Override
    public void onClick(View v) {
        if (onItemViewClickListener != null) {
            onItemViewClickListener.onViewClick(v);
        }
    }
}
