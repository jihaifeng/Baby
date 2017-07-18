package com.jihf.lib.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.jihf.lib.R;
import com.jihf.lib.utils.LogUtils;

public class SharePopwindow extends PopupWindow {
  private static final String TAG = SharePopwindow.class.getSimpleName().trim();
  private TextView tvWxFriend;
  private TextView tvWxFriendCircle;
  private TextView tvQqFriend;
  private TextView tvQqQzone;
  private Button btnCancelShare;

  public SharePopwindow(Activity context, View.OnClickListener itemsOnClick) {
    super(context);
    initView(context, itemsOnClick);
  }

  private void initView(final Activity context, View.OnClickListener itemsOnClick) {
    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View mView = mInflater.inflate(R.layout.share_view, null);
    findView(mView);
    btnCancelShare.setOnClickListener(v -> {
      //销毁弹出框
      dismiss();
      backgroundAlpha(context, 1f);
    });
    //设置按钮监听 
    tvWxFriend.setOnClickListener(itemsOnClick);
    tvWxFriendCircle.setOnClickListener(itemsOnClick);
    tvQqFriend.setOnClickListener(itemsOnClick);
    tvQqQzone.setOnClickListener(itemsOnClick);
    //设置SelectPicPopupWindow的View 
    this.setContentView(mView);
    //设置SelectPicPopupWindow弹出窗体的宽 
    this.setWidth(WindowManager.LayoutParams.FILL_PARENT);
    //设置SelectPicPopupWindow弹出窗体的高 
    this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    //设置SelectPicPopupWindow弹出窗体可点击 
    this.setFocusable(true);
    //设置PopupWindow可触摸 
    this.setTouchable(true);
    //设置非PopupWindow区域是否可触摸 
    this.setOutsideTouchable(true);
    //设置SelectPicPopupWindow弹出窗体动画效果 
    //this.setAnimationStyle(R.style.select_anim);
    //实例化一个ColorDrawable颜色为半透明 
    ColorDrawable dw = new ColorDrawable(0x00000000);
    //设置SelectPicPopupWindow弹出窗体的背景 
    this.setBackgroundDrawable(dw);
    backgroundAlpha(context, 0.5f);//0.0-1.0 
    this.setOnDismissListener(() -> backgroundAlpha(context, 1f));
  }

  private void findView(View mView) {
    tvWxFriend = getView(mView, R.id.tv_wx_friend);
    tvWxFriendCircle = getView(mView, R.id.tv_wx_circle);
    tvQqFriend = getView(mView, R.id.tv_qq_friend);
    tvQqQzone = getView(mView, R.id.tv_qq_qzone);
    btnCancelShare = getView(mView, R.id.btn_cancel_share);
  }

  @Override public void dismiss() {
    super.dismiss();
  }

  /**
   * 设置添加屏幕的背景透明度
   *
   * @param bgAlpha
   */
  public void backgroundAlpha(Activity context, float bgAlpha) {
    WindowManager.LayoutParams lp = context.getWindow().getAttributes();
    lp.alpha = bgAlpha;
    context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    context.getWindow().setAttributes(lp);
  }

  @SuppressWarnings ("unchecked") public final <E extends View> E getView(View viewParent, int id) {
    if (null == viewParent) {
      throw new NullPointerException("parent view is null.");
    }
    try {
      return (E) viewParent.findViewById(id);
    } catch (Exception e) {
      LogUtils.i(TAG, "Can not cast view to concrete class" + e);
      throw e;
    }
  }
} 
