package com.hyd.winlife.tools;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

import com.hyd.winlife.R;

/**
 * 弹框统一类
 * Created by lingxiaoming on 2018-10-31.
 */
public class DialogManager {
    /**
     * 可配置提示文字的gif圆形动图弹框
     *
     * @param context
     * @param loadingTextRes
     * @return
     */
    public static Dialog getLoadingDialog(Context context, Object loadingTextRes) {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_dialog_progress);
        Window window = dialog.getWindow();
        // WindowManager.LayoutParams lp = window.getAttributes();
        // lp.width = getScreenWidth(context) - dpToPx(context, 100);
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置

        TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialog_tv);
        if (loadingTextRes instanceof String) {
            titleTxtv.setText((String) loadingTextRes);
        }
        if (loadingTextRes instanceof Integer) {
            titleTxtv.setText((Integer) loadingTextRes);
        }

        return dialog;
    }
}
