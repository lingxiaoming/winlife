package com.hyd.winlife.base;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyd.winlife.R;
import com.hyd.winlife.tools.DialogManager;

import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements View.OnClickListener, RecyclerViewHolder.OnItemViewClickListener {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_HEADER = 3;
    public static final int TYPE_EMPTY = 4;

    private OnItemClickListener onItemClickListener;
    private OnViewClickListener onViewClickListener;

    private LayoutInflater inflater;

    protected View headerView;
    protected View footerView;
    protected View emptyView;

    protected Context context;
    protected List<T> datas;

    protected TextView mTvNomore;//没有更多了控件
    protected View mVLoading;//没有更多了控件
    protected String nomoreContent;//没有更多了文字
    protected int headerViewCount;
    protected int footerViewCount = 1;
    protected int itemRes;
    protected int defaultRadius = 12;//默认图片圆角大小

    // 分页加载
    private int currentPage;//当前显示第几页
    private int pages;//总共的页数
    private final int NUMS = 10;//每页显示数据条数
    private boolean noMore = false;//没有更多数据了
    private boolean isLoadingData = false;//正在加载数据，不要在请求了

    private MyTaskFinishListener myTaskFinishListener;

    public Dialog progressDialog;

    public BaseRecyclerAdapter(Context context, List<T> datas, int itemLayoutResourse) {
        this.datas = datas;
        this.context = context;
        this.itemRes = itemLayoutResourse;
        inflater = LayoutInflater.from(context);
        myTaskFinishListener = new MyTaskFinishListener();
        footerView = inflater.inflate(R.layout.layout_footer, null);//这里默认就让每个recyclerview有footer
        mTvNomore = footerView.findViewById(R.id.footer_text_view);
        mVLoading = footerView.findViewById(R.id.footer_pb_view);
        emptyView = inflater.inflate(R.layout.layout_empty, null);//空数据显示的空布局

        progressDialog = DialogManager.getLoadingDialog(context, "加载中");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_NORMAL:
                view = inflater.inflate(itemRes, parent, false);
                break;
            case TYPE_FOOTER:
                view = footerView;
                break;
            case TYPE_HEADER:
                view = headerView;
                break;
            case TYPE_EMPTY:
                view = emptyView;
                break;
            default:
                view = inflater.inflate(itemRes, parent, false);
                break;
        }
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        if (isEmpty()) {
            bindEmpty(holder);
        } else if (isFooter(position)) {
            bindFooter(holder);
        } else if (isHeader(position)) {
            bindHeader(holder);
        } else {
            final int index = position - headerViewCount;
            if (index < datas.size()) {
                bindViewHolder(index, holder);
                View itemView = holder.getItemView();
                final T data = datas.get(index);
                itemView.setTag(data);
            }
        }
    }

    public String TAG = "BaseRecyclerAdapter";

    @Override
    public int getItemCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size() + headerViewCount + footerViewCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) {
            return TYPE_EMPTY;
        } else if (isFooter(position)) {
            return TYPE_FOOTER;
        } else if (isHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
    }

    public T getItem(int position) {
        return datas.get(position);
    }

    public int getPosition(T data) {
        return datas.indexOf(data) + headerViewCount;
    }

    protected abstract void bindViewHolder(int position, RecyclerViewHolder holder);

    protected abstract void loadData(int start, int rows, ILoadFinish<T> iTaskFinishListener);

    protected void bindHeader(RecyclerViewHolder holder) {
        holder.getItemView().setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    protected void bindFooter(RecyclerViewHolder holder) {
        holder.getItemView().setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (!noMore && !isLoadingData && currentPage != 1) {
            isLoadingData = true;
            loadData(currentPage, NUMS, myTaskFinishListener);
        }
        if (noMore || datas.size() == 0) {
            if(!TextUtils.isEmpty(nomoreContent)){//请将加载中改为没有更多数据了哦
                holder.getItemView().setVisibility(View.VISIBLE);
                mTvNomore.setText(nomoreContent);
                mVLoading.setVisibility(View.GONE);
            }else {
                holder.getItemView().setVisibility(View.GONE);
            }
        } else {
            holder.getItemView().setVisibility(View.VISIBLE);
            mVLoading.setVisibility(View.VISIBLE);
            mTvNomore.setText("正在加载更多");
        }
    }

    protected void bindEmpty(RecyclerViewHolder holder) {
        holder.getItemView().setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private class MyTaskFinishListener implements ILoadFinish<T> {

        @Override
        public void success(T object) {
            dismissProgressDialog();
            if (object != null && object instanceof List) {
                List<T> list = (List<T>) object;
                if (list != null) {
                    if (list.size() < NUMS) {
                        noMore = true;
                    }

                    if (list.size() == 0 && currentPage == 1) {//第一页,没有数据时,清空Datas并刷新adapter
                        datas.clear();
                        notifyDataSetChanged();

                    } else if (list.size() > 0) {
                        currentPage++;

                        if (currentPage == 2) {
                            datas.clear();
                        }
//                        int beforeItemCounts = datas.size();
                        datas.addAll(list);
//                        if(currentPage >= 2){
////                            notifyItemRangeInserted(beforeItemCounts, list.size());
//                        notifyItemRangeChanged(0, list.size());
//                        }else
                            notifyDataSetChanged();
                    } else {
                        notifyDataSetChanged();
                    }
                }
            }
            isLoadingData = false;
            loadDataFinish(true);
        }

        @Override
        public void fail(String errorMsg) {
            noMore = true;
            notifyDataSetChanged();
            dismissProgressDialog();
            isLoadingData = false;
            loadDataFinish(false);
        }
    }

    /**
     * 首次加载数据
     */
    public void loadDataFirstTime() {
        if (!isLoadingData) {
            noMore = false;
            currentPage = 1;
            showProgressDialog();
            isLoadingData = true;
            loadData(1, NUMS, myTaskFinishListener);
        }
    }

    protected boolean isFooter(int position) {
        if (footerView == null) {
            return false;
        }
        return position == getItemCount() - 1;
    }

    protected boolean isHeader(int position) {
        if (headerView == null) {
            return false;
        }
        return position == 0;
    }

    protected boolean isEmpty() {
        if (emptyView == null) {
            return false;
        }

        if (!noMore) {
            return false;
        }
        return datas.size() <= 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    public void setHeaderView(View headerView) {
        if (this.headerView == null) {
            if (headerView != null) {
                this.headerView = headerView;
                this.headerViewCount++;
                notifyItemInserted(0);
            }
        }
    }

    public void setFooterNoMore(String nomoreContent){
        this.nomoreContent = nomoreContent;
    }

    public void setNomore(boolean noMore){
        this.noMore = noMore;
    }


    public void removeHeaderView() {
        if (headerViewCount > 0) {
            this.headerView = null;
            this.headerViewCount--;
            this.notifyItemRemoved(0);
        }
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, v.getTag());
        }
    }

    @Override
    public void onViewClick(View view) {
        if (onViewClickListener != null) {
            onViewClickListener.onViewClick(view, view.getTag());
        }
    }

    /**
     * 整个Item点击事件
     *
     * @param <T>
     */
    public interface OnItemClickListener<T> {
        void onItemClick(View view, T data);
    }

    /**
     * 整个Item中的子view设置点击事件
     * 注意：必须在Adapter中setTag，否则这里data没有数据，目前还没有更好的解决办法，如有请告知
     *
     * @param <T>
     */
    public interface OnViewClickListener<T> {
        void onViewClick(View view, T data);
    }

    public void setEmptyView(String emptyText, int emptyImageSource) {
        if (emptyView != null) {
            if (emptyText != null) {
                ((TextView) emptyView.findViewById(R.id.tv_msg)).setText(emptyText);
            }else {
                emptyView.findViewById(R.id.tv_msg).setVisibility(View.GONE);
            }
            if (emptyImageSource > 0) {
                ((ImageView) emptyView.findViewById(R.id.iv_icon)).setImageResource(emptyImageSource);
            }else {
                emptyView.findViewById(R.id.iv_icon).setVisibility(View.GONE);
            }
        }
    }

    private OnDataLoadFinish onDataLoadFinish;

    public void setOnDataLoadFinish(OnDataLoadFinish onDataLoadFinish) {
        this.onDataLoadFinish = onDataLoadFinish;
    }

    public interface OnDataLoadFinish {
        void loadDataFinished(int datasLength);
    }

    private void loadDataFinish(boolean success) {
        if (onDataLoadFinish != null) {
            onDataLoadFinish.loadDataFinished(datas.size());
        }
    }

    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing())
            try {
                progressDialog.show();
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
