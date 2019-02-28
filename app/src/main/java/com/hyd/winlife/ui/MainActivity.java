package com.hyd.winlife.ui;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyd.winlife.R;
import com.hyd.winlife.adapter.BannerAdapter;
import com.hyd.winlife.adapter.MoudleAdapter;
import com.hyd.winlife.adapter.RecommendAdapter;
import com.hyd.winlife.arch.ModuleResult;
import com.hyd.winlife.base.BaseActivity;
import com.hyd.winlife.base.BaseRecyclerAdapter;
import com.hyd.winlife.bean.AdvListWapper;
import com.hyd.winlife.bean.MoudleWapper;
import com.hyd.winlife.bean.RecommendWapper;
import com.hyd.winlife.bean.User;
import com.hyd.winlife.module.MainModule;
import com.hyd.winlife.tools.DeviceUtils;
import com.hyd.winlife.tools.ToastUtils;
import com.hyd.winlife.viewmodel.MainViewModel;
import com.hyd.winlife.widget.AutoLoopViewPager;
import com.hyd.winlife.widget.GridDivider;
import com.hyd.winlife.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 * Created by lingxiaoming on 2018-10-31.
 */
public class MainActivity extends BaseActivity {
    private MainViewModel mMainViewModel;


    @BindView(R.id.refreshlayout)
    public SwipeRefreshLayout refreshlayout;//下拉刷新

    @BindView(R.id.autoLoopViewPager1)
    public AutoLoopViewPager mAutoLoopViewPager1;//顶部banner

    @BindView(R.id.autoLoopViewPager2)
    public AutoLoopViewPager mAutoLoopViewPager2;//中间杭品优选1

    @BindView(R.id.autoLoopViewPager3)
    public AutoLoopViewPager mAutoLoopViewPager3;//中间杭品优选2

    @BindView(R.id.viewPager)
    public ViewPager mViewPagerModule;//中间功能模块

    @BindView(R.id.ll_viewpager_line)
    public LinearLayout mLlModuleLine;//中间功能模块下面指示线

    @BindView(R.id.recyclerview)
    public RecyclerView mRvRecommon;//美食甄选

    @BindView(R.id.tv_score)
    public TextView mTvScore;//我的华赢币


    private List<MyGridView> gridViewList = new ArrayList<>();//首页中间模块


    public static void go(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void mOnCreate() {
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.userResult.observe(this, mUserObserver);
        mMainViewModel.advListResult.observe(this, mAdvObserver);
        mMainViewModel.moudleResult.observe(this, mMoudleObserver);
        getDatas();

        initBanner();
        initModule();
        initRecommen();
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDatas();
                refreshlayout.setRefreshing(false);
            }
        });
    }

    public void getDatas() {
        mMainViewModel.getUserInfo();
        mMainViewModel.getBanner();
        mMainViewModel.getMoudle();
    }


    PagerAdapter mViewPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return gridViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(gridViewList.get(position));
            return gridViewList.get(position);
        }

        //销毁一个页卡(即ViewPager的一个item)
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(gridViewList.get(position));
        }
    };

    private Observer<ModuleResult<User>> mUserObserver = (result -> {
        if (result.data == null) {
            ToastUtils.showText("获取用户信息失败:" + result.error.getMessage());
        } else {
            User user = result.data;
            ToastUtils.showText("获取用户信息成功 用户名:" + user.realityName);
//            DataManager.setUser();//TODO 保存用户信息
        }
    });

    private Observer<ModuleResult<AdvListWapper>> mAdvObserver = (result -> {
        if (result.data == null) {
            ToastUtils.showText("获取广告图信息失败:" + result.error.getMessage());
        } else {
            AdvListWapper advListWapper = result.data;
            if (advListWapper == null || advListWapper.advInfo == null || advListWapper.advInfo.size() <= 0) {
                return;
            }
            List<AdvListWapper.AdvList.HomeAd> home_top_list = null;
            List<AdvListWapper.AdvList.HomeAd> home_middle_list1 = null;
            List<AdvListWapper.AdvList.HomeAd> home_middle_list2 = null;

            for (AdvListWapper.AdvList advList : advListWapper.advInfo) {
                if (TextUtils.equals(MainModule.BANNER_LIFE_TOP, advList.advName)) {
                    home_top_list = advList.advertise;
                } else if (TextUtils.equals(MainModule.BANNER_HOME_PREFERENCE_ONE, advList.advName)) {
                    home_middle_list1 = advList.advertise;
                } else if (TextUtils.equals(MainModule.BANNER_HOME_PREFERENCE_TWO, advList.advName)) {
                    home_middle_list2 = advList.advertise;
                }
            }

            mAutoLoopViewPager1.setAdapter(new BannerAdapter(this, home_top_list, true));
            mAutoLoopViewPager2.setAdapter(new BannerAdapter(this, home_middle_list1));
            mAutoLoopViewPager3.setAdapter(new BannerAdapter(this, home_middle_list2));
        }
    });

    private Observer<ModuleResult<MoudleWapper>> mMoudleObserver = (result -> {
        if (result.data == null) {
            ToastUtils.showText("获取模块信息失败:" + result.error.getMessage());
        } else {
            MoudleWapper moudleWapper = result.data;
            if (moudleWapper == null || moudleWapper.moduleInfo == null || moudleWapper.moduleInfo.size() <= 0) {
                return;
            }

            List<MoudleWapper.Modules> home_middle_category = moudleWapper.moduleInfo.get(MainModule.MODULE_HOME_MIDDLE);

            mLlModuleLine.removeAllViews();
            gridViewList.clear();
            int page = (home_middle_category.size() - 1) / 8 + 1;
            for (int i = 0; i < page; i++) {
                MyGridView myGridView = new MyGridView(this);
                myGridView.setNumColumns(4);
                myGridView.setVerticalScrollBarEnabled(false);
                myGridView.setAdapter(new MoudleAdapter(this, new ArrayList<>(home_middle_category.subList(i * 8, (i * 8 + 8) > home_middle_category.size() ? home_middle_category.size() : (i * 8 + 8)))));
                gridViewList.add(myGridView);

                if (page > 1) {
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(i == 0 ? R.mipmap.ic_page_show : R.mipmap.ic_page_unshow);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(DeviceUtils.dp2px(3), 0, DeviceUtils.dp2px(3), 0);
                    imageView.setLayoutParams(params);
                    mLlModuleLine.addView(imageView);
                }
            }
            mViewPagerAdapter.notifyDataSetChanged();
            mViewPagerModule.setCurrentItem(0);
        }
    });


    private void initBanner() {
        int width = DeviceUtils.getScreenWidth();
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, width * 135 / 343);
        mAutoLoopViewPager1.setLayoutParams(params1);
        mAutoLoopViewPager1.setBoundaryCaching(true);
        mAutoLoopViewPager1.setAutoScrollDurationFactor(2d); //切换图片时，图片的移动速度
        mAutoLoopViewPager1.setInterval(3000);
        mAutoLoopViewPager1.startAutoScroll();


        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, width * 6 / 25);
        mAutoLoopViewPager2.setLayoutParams(params2);
        mAutoLoopViewPager2.setBoundaryCaching(true);
        mAutoLoopViewPager2.setAutoScrollDurationFactor(2d); //切换图片时，图片的移动速度
        mAutoLoopViewPager2.setInterval(3000);
        mAutoLoopViewPager2.startAutoScroll();

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, width * 6 / 25);
        mAutoLoopViewPager3.setLayoutParams(params3);
        mAutoLoopViewPager3.setBoundaryCaching(true);
        mAutoLoopViewPager3.setAutoScrollDurationFactor(2d); //切换图片时，图片的移动速度
        mAutoLoopViewPager3.setInterval(3000);
        mAutoLoopViewPager3.startAutoScroll();
    }

    private void initModule() {
        mViewPagerModule.setAdapter(mViewPagerAdapter);
        mViewPagerModule.addOnPageChangeListener(mPageChangerListener);
    }


    private void initRecommen() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        mRvRecommon.setLayoutManager(gridLayoutManager);
        mRvRecommon.addItemDecoration(new GridDivider(2, this.getResources().getColor(R.color.gray_f7f7f7), 1));
        mRvRecommon.setHasFixedSize(true);
        mRvRecommon.setNestedScrollingEnabled(false);

        RecommendAdapter recommendAdapter = new RecommendAdapter(this, new ArrayList<>());
        mRvRecommon.setAdapter(recommendAdapter);
        recommendAdapter.setOnItemClickListener(itemClickListener);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.btn_activate)
    public void onClick(View view) {

    }

    BaseRecyclerAdapter.OnItemClickListener itemClickListener = (view, data) -> {
        RecommendWapper.Recommend recommend = (RecommendWapper.Recommend) data;
        if (!TextUtils.isEmpty(recommend.h5_url)) {
//                CommonWebViewActivity.goToLoginTarget(getActivity(), recommend.h5_url);
            ToastUtils.showText("click url = " + recommend.h5_url);
        }
    };

    ViewPager.OnPageChangeListener mPageChangerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mLlModuleLine.getChildCount(); i++) {
                ImageView view = (ImageView) mLlModuleLine.getChildAt(i);
                if (i == position) view.setImageResource(R.mipmap.ic_page_show);
                else view.setImageResource(R.mipmap.ic_page_unshow);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
