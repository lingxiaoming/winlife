package com.hyd.winlife.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hyd.winlife.R;
import com.hyd.winlife.bean.AdvListWapper;
import com.hyd.winlife.manager.UserStatusManager;
import com.hyd.winlife.tools.GlideImageLoader;
import com.hyd.winlife.tools.UrlUtils;
import com.hyd.winlife.ui.LoginActivity;
import com.hyd.winlife.ui.WebViewActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 所有的banner适配器
 * Created by lingxiaoming on 2018-08-03.
 */
public class BannerAdapter extends PagerAdapter {
    private int count = 100;
    private Queue<View> views;
    private List<AdvListWapper.AdvList.HomeAd> m_lstAds = new ArrayList<>();
    private LayoutInflater lay;
    private Activity context;
    private boolean isRound;//是否是圆角图片

    public BannerAdapter(Activity ct, List<AdvListWapper.AdvList.HomeAd> listAds, boolean isRound) {
        views = new LinkedList<>();
        m_lstAds.clear();
        m_lstAds.addAll(listAds);
        lay = LayoutInflater.from(ct);
        context = ct;
        this.isRound = isRound;
    }

    public BannerAdapter(Activity ct, List<AdvListWapper.AdvList.HomeAd> listAds) {
        views = new LinkedList<>();
        m_lstAds.clear();
        if (listAds != null && listAds.size() > 0)
            m_lstAds.addAll(listAds);
        lay = LayoutInflater.from(ct);
        context = ct;
    }

    public void updateDatas(List<AdvListWapper.AdvList.HomeAd> listAds) {
        if (listAds == null) return;
        m_lstAds.clear();
        m_lstAds.addAll(listAds);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return m_lstAds.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View mage = views.poll();
        if (mage == null) {
            if (isRound) {
                mage = lay.inflate(R.layout.item_image_round, null);
            } else {
                mage = lay.inflate(R.layout.item_image, null);
            }
            mage.setId(count++);
        }

        final AdvListWapper.AdvList.HomeAd homeAd = m_lstAds.get(position);

        ImageView iv = mage.findViewById(R.id.imageview);
        /*
         * FIT_CONTER 安比例显示在中间
         */
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setOnClickListener(view -> {
            if (m_lstAds.get(position).is_link_enabled) {
                if (homeAd.is_login && !UserStatusManager.hasLogin()) {
                    LoginActivity.go(context);
                } else {
                    WebViewActivity.goToLoginTarget(context, m_lstAds.get(position).url);
                }
            }
        });

        if (!TextUtils.isEmpty(homeAd.image_filename)) {
            GlideImageLoader.getImageLoader().loadImage(context, iv, UrlUtils.getImageUrl(homeAd.image_filename));
        }

        container.addView(mage);
        return mage;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View mage = (View) object;
        views.add(mage);
//        Log.d("** des", mage.getId() + "  id" + " size " + views.size());
        container.removeView(mage);
    }

}
