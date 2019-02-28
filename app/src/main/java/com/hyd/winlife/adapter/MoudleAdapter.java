package com.hyd.winlife.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyd.winlife.R;
import com.hyd.winlife.bean.MoudleWapper;
import com.hyd.winlife.manager.UserStatusManager;
import com.hyd.winlife.tools.GlideImageLoader;
import com.hyd.winlife.tools.ToastUtils;
import com.hyd.winlife.tools.UrlUtils;
import com.hyd.winlife.ui.LoginActivity;
import com.hyd.winlife.ui.WebViewActivity;

import java.util.List;

//垂直模块布局
public class MoudleAdapter extends BaseAdapter {

    List<MoudleWapper.Modules> data;
    Activity context;
    private LayoutInflater inflater;

    public MoudleAdapter(Activity context, List<MoudleWapper.Modules> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_moudle, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MoudleWapper.Modules info = data.get(position);
        if (!TextUtils.isEmpty(info.title)) {
            holder.tv_title.setText(info.title);
        }

        if (TextUtils.isEmpty(info.link)) {
            holder.tv_title.setTextColor(Color.parseColor("#BBBBBB"));
        } else {
            holder.tv_title.setTextColor(Color.parseColor("#333333"));
        }

        GlideImageLoader.getImageLoader().loadImage(context,  holder.iv_icon, UrlUtils.getImageUrl(info.icon));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.is_login) {
                    if (!UserStatusManager.hasLogin()) {
                        LoginActivity.go(context);
                        return;
                    }
                }

                if (TextUtils.isEmpty(info.link)) {
                    ToastUtils.showText("即将上线，敬请期待");
                    return;
                }

                if (info.link.startsWith("gotohyd://net.huayingdai")) {//跳转到app原生页面
                    WebViewActivity.goToTargetNoHttp(context, info.link);
                } else {
                    WebViewActivity.goToLoginTarget(context, info.link);
                }
            }
        });
        return convertView;
    }

    public void updateDatas(List<MoudleWapper.Modules> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tv_title;
        ImageView iv_icon;
    }
}