package com.jihf.lib.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.jihf.lib.R;

public class ProgressDialogCommon {

    private Context context;

    private ShimmerTextView shimmerTextView;// 进度显示文本控件
    private String shimmerText;// 进度显示文本
    private ProgressBar mProgressBar = null;  
    private Dialog dialog;

    public ProgressDialogCommon(Context context) {
        this.context = context;
        init();
    }

    public ProgressDialogCommon(Context context, String shimmerText) {
        this.context = context;
        this.shimmerText = shimmerText;
        init();
    }

    private void init() {
        // LayoutInflater inflater = LayoutInflater.from(context);
        // View view = inflater.inflate(R.layout.progress_dialog_common, null);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams linear_lp = null;
        try {
        	LinearLayout view = new LinearLayout(context);
            if (null != view) {
                view.setLayoutParams(lp);
//                view.setPadding(15, 5, 15, 5);
                view.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
                view.setOrientation(LinearLayout.VERTICAL);
                view.setBackgroundResource(R.drawable.public_dialog_bg);
                //缓冲圈
                mProgressBar = new ProgressBar(context); 
                if (null != mProgressBar) {
//                    linear_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    linear_lp = new LinearLayout.LayoutParams(84,84);
                    linear_lp.bottomMargin = 5;
                    mProgressBar.setLayoutParams(linear_lp);
                    Drawable drawable = this.context.getResources().getDrawable(R.drawable.public_progress_img_style);
                    mProgressBar.setIndeterminateDrawable(drawable);
                    view.addView(mProgressBar);
                }
                
                //字体
                shimmerTextView = new ShimmerTextView(context);
                if (shimmerTextView != null) {
                    linear_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    shimmerTextView.setLayoutParams(lp);
                    shimmerTextView.setGravity(Gravity.CENTER);
                    shimmerTextView.setTextColor(Color.parseColor("#000000"));
                    shimmerTextView.setTextSize(16f);
                    shimmerTextView.setPadding(5, 5, 5, 5);
                    shimmerTextView.setText(shimmerText);
                    view.addView(shimmerTextView);
                }

                dialog = new Dialog(context,  R.style.commentDialog);
                dialog.setContentView(view);
                WindowManager.LayoutParams lpw = dialog.getWindow().getAttributes();
                lp.width = context.getResources().getDimensionPixelSize(R.dimen.progress_dialog_common_width);
                lp.height = LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lpw);
            }
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
    }

    /**
     * 设置加载信息
     */
    public void setShimmerText(String shimmerText) {
        if (shimmerText != null && shimmerTextView != null) {
            shimmerTextView.setText("正在加载...");
        }
    }

    /**
     * 显示弹出框
     */
    public void showProgressDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    /**
     * 隐藏弹出框
     */
    public void hideProgressDialog() {
        if (dialog != null) {
            dialog.hide();
        }
    }

    /**是否显示缓冲动画**/
    public void showProgressImage(boolean isShowProgressImage)
    {
        if (null != mProgressBar) {
            mProgressBar.setVisibility(isShowProgressImage?View.VISIBLE:View.GONE);
        }
    }
    /**
     * 设置点击返回键是否隐藏
     */
    public void setCancelable(boolean flag) {
        if (dialog != null) {
            dialog.setCancelable(flag);
        }
    }

    /**
     * 设置点击dialog以外区域是否隐藏
     */
    public void setCanceledOnTouchOutside(boolean flag) {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(flag);
        }
    }

    /**
     * 隐藏弹出框，并释放对话框所占的资源
     */
    public void dismissDialog() {
        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 判断弹出框是否显示
     */
    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }
}
