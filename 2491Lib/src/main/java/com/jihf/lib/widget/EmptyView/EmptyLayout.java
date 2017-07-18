package com.jihf.lib.widget.EmptyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.jihf.lib.R;

/**
 * Func：
 * Desc:
 * Author：jihf
 * Date：2017-05-09 9:42
 * Mail：jihaifeng@raiyi.com
 */
public class EmptyLayout extends FrameLayout implements View.OnClickListener {
  private Context mContext;
  private int bgColor;
  private int bgColor_default = Color.WHITE;
  private FrameLayout flEmptyRoot;
  private RelativeLayout rlErrorRoot;
  private TextView tvError;
  private ImageView ivError;
  private SpinKitView spinKitLoading;
  private RetryListener retryListener;
  private int emptyStatus = Empty_status.STATUS_HIDE;
  private String errorMsg = null;
  private int errorIconId = -1;
  private Drawable errorIconDrawable = null;

  public EmptyLayout(@NonNull Context context) {
    this(context, null);
  }

  public EmptyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public EmptyLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.mContext = context;
    if (null == attrs) {
      throw new NullPointerException("attrs is null");
    }
    TypedArray t = mContext.obtainStyledAttributes(attrs, R.styleable.EmptyLayout);
    bgColor = t.getColor(R.styleable.EmptyLayout_background_color, bgColor_default);
    initView();
    t.recycle();
  }

  private void initView() {
    inflate(mContext, R.layout.empty_view, this);
    flEmptyRoot = getView(R.id.fl_empty_root);
    rlErrorRoot = getView(R.id.rl_error_root);
    tvError = getView(R.id.tv_error);
    tvError.setOnClickListener(this);
    ivError = getView(R.id.iv_error);
    ivError.setOnClickListener(this);
    spinKitLoading = getView(R.id.spin_kit_loading);
    flEmptyRoot.setBackgroundColor(bgColor);
    switchEmptyView();
  }

  public void hide() {
    emptyStatus = Empty_status.STATUS_HIDE;
    switchEmptyView();
  }

  public void setEmptyStatus(int status) {
    this.emptyStatus = status;
    switchEmptyView();
  }

  public int getEmptyStatus() {
    return emptyStatus;
  }

  private void switchEmptyView() {
    switch (emptyStatus) {
      case Empty_status.STATUS_HIDE:
        setVisibility(VISIBLE);
        spinKitLoading.setVisibility(GONE);
        rlErrorRoot.setVisibility(GONE);
        if (getChildCount() > 1) {
          getChildAt(1).setVisibility(VISIBLE);
        }
        break;
      case Empty_status.STATUS_LOADING:
        setVisibility(VISIBLE);
        spinKitLoading.setVisibility(VISIBLE);
        rlErrorRoot.setVisibility(GONE);
        break;
      case Empty_status.STATUS_NO_DATA:
        setVisibility(VISIBLE);
        spinKitLoading.setVisibility(GONE);
        rlErrorRoot.setVisibility(VISIBLE);
        tvError.setText(TextUtils.isEmpty(errorMsg) ? "未查到数据，点击重试..." : errorMsg);
        if (errorIconId != -1) {
          ivError.setImageResource(errorIconId);
        } else if (errorIconDrawable != null) {
          ivError.setImageDrawable(errorIconDrawable);
        } else {
          ivError.setImageResource(R.mipmap.ic_no_data);
        }
        break;
      case Empty_status.STATUS_NO_NET:
        setVisibility(VISIBLE);
        spinKitLoading.setVisibility(GONE);
        rlErrorRoot.setVisibility(VISIBLE);
        tvError.setText(TextUtils.isEmpty(errorMsg) ? "网络异常，点击重试..." : errorMsg);
        if (errorIconId != -1) {
          ivError.setImageResource(errorIconId);
        } else if (errorIconDrawable != null) {
          ivError.setImageDrawable(errorIconDrawable);
        } else {
          ivError.setImageResource(R.mipmap.ic_not_network);
        }
        break;
    }
  }

  @SuppressWarnings ("unchecked") public final <E extends View> E getView(int id) {
    try {
      return (E) findViewById(id);
    } catch (Exception e) {
      Log.e("getView", "getView: ", e);
      throw e;
    }
  }

  @Override public void onClick(View v) {
    if (v.getId() == R.id.tv_error || v.getId() == R.id.iv_error) {
      if (null != retryListener) {
        retryListener.onRetry();
      }
    }
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  /**
   * 设置图标
   *
   * @param resId 资源ID
   */
  public void setErrorIcon(int resId) {
    this.errorIconId = resId;
  }

  /**
   * 设置图标
   *
   * @param drawable drawable
   */
  public void setErrorIcon(Drawable drawable) {
    this.errorIconDrawable = drawable;
  }

  /**
   * 设置重试监听器
   *
   * @param retryListener 监听器
   */
  public void setRetryListener(RetryListener retryListener) {
    this.retryListener = retryListener;
  }

  public void setBgColor(int bgColor) {
    this.bgColor = bgColor;
    flEmptyRoot.setBackgroundColor(bgColor);
  }
}
