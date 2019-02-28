package com.hyd.winlife.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.hyd.winlife.R;


/**
 * 自定义右侧带眼睛图标的EditText(可以显示/隐藏文本内容)
 */
public class EyeEditText extends EditText implements View.OnFocusChangeListener,
        TextWatcher {
    // 眼睛按钮的引用
    private Drawable mEyeCloseDrawable, mEyeOpenDrawable;
    // 是否有焦点
    private boolean hasFoucs;
    // 眼睛是否为关闭状态(默认为闭眼)
    private boolean isClose = true;

    public EyeEditText(Context context) {
        this(context, null);
    }

    public EyeEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EyeEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressWarnings("deprecation")
    private void init() {
        mEyeCloseDrawable = getResources().getDrawable(R.mipmap.ic_close_eyes_n);
        mEyeOpenDrawable = getResources().getDrawable(R.mipmap.ic_open_eyes_n);
        mEyeCloseDrawable.setBounds(0, 0,
                mEyeCloseDrawable.getIntrinsicWidth(),
                mEyeCloseDrawable.getIntrinsicHeight());
        mEyeOpenDrawable.setBounds(0, 0, mEyeOpenDrawable.getIntrinsicWidth(),
                mEyeOpenDrawable.getIntrinsicHeight());

        // 默认设置显示图标
        setEyeVisible(true);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    if (isClose) {
                        // 显示内容
                        this.setTransformationMethod(HideReturnsTransformationMethod
                                .getInstance());
                    } else {
                        // 隐藏内容
                        this.setTransformationMethod(PasswordTransformationMethod
                                .getInstance());
                    }

                    // 改变状态
                    isClose = !isClose;
                    toggleEyeIcon(isClose);
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // -- 右侧图标一直显示 start --
        this.hasFoucs = hasFocus;
//        if (hasFocus) {
//            setEyeVisible(getText().length() > 0);
//        } else {
//            setEyeVisible(false);
//        }
        // -- 右侧图标一直显示 end --
    }

    /**
     * 设置眼睛图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setEyeVisible(boolean visible) {
        // 如果你想让它一直显示DrawableRight的图标，并且还需要让它触发事件，直接把null改成mClearDrawable即可
        Drawable right = null;
        if (visible) {
            right = isClose ? mEyeCloseDrawable : mEyeOpenDrawable;
        } else {
            // 没有内容的情况
            // 隐藏内容
            this.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
            // 重置眼睛状态
            isClose = true;
            toggleEyeIcon(isClose);
        }
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 切换眼睛图标
     *
     * @param isClose 是否闭眼
     */
    protected void toggleEyeIcon(boolean isClose) {
        Drawable right = isClose ? mEyeCloseDrawable : mEyeOpenDrawable;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        // -- 右侧图标一直显示 start --
//        if (hasFoucs) {
//            setEyeVisible(s.length() > 0);
//        }
        // -- 右侧图标一直显示 end --
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
