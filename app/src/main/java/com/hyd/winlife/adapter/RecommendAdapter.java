package com.hyd.winlife.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyd.winlife.R;
import com.hyd.winlife.base.BaseRecyclerAdapter;
import com.hyd.winlife.base.ILoadFinish;
import com.hyd.winlife.base.RecyclerViewHolder;
import com.hyd.winlife.bean.RecommendWapper;
import com.hyd.winlife.tools.GlideImageLoader;

import java.util.List;

//赢生活美食推荐adapter
public class RecommendAdapter extends BaseRecyclerAdapter<RecommendWapper.Recommend> {

    public RecommendAdapter(Context context, List<RecommendWapper.Recommend> datas) {
        super(context, datas, R.layout.item_recommend);
        setNomore(true);
        setEmptyView("", 0);
    }

    public void updateDatas(List<RecommendWapper.Recommend> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    protected void bindViewHolder(int position, RecyclerViewHolder holder) {
        ImageView ivIcon = holder.getView(R.id.iv_icon, false);
        TextView tvName1 = holder.getView(R.id.tv_name1, false);
        TextView tvName2 = holder.getView(R.id.tv_name2, false);

        RecommendWapper.Recommend item = datas.get(position);
        tvName1.setText(item.brand_name);
        tvName2.setText(item.brand_desc);

        GlideImageLoader.getImageLoader().loadImage(this, ivIcon, item.logo_url);
    }


    @Override
    protected void loadData(int start, int rows, ILoadFinish<RecommendWapper.Recommend> iTaskFinishListener) {

    }
}
