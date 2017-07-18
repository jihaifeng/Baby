package com.jihf.lib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jihf.lib.R;
import com.jihf.lib.utils.LogUtils;
import com.jihf.lib.widget.EmptyView.EmptyLayout;
import com.jihf.lib.widget.EmptyView.Empty_status;
import com.jihf.lib.widget.EmptyView.RetryListener;

/**
 * Func：
 * Desc:
 * Author：jihf
 * Data：2017-03-09 10:12
 * Mail：jihaifeng@raiyi.com
 */
public abstract class BaseSimpleFragment extends Fragment {
  public static String TAG = BaseSimpleFragment.class.getSimpleName().trim();
  public View view;
  /**
   * 把 EmptyLayout 放在基类统一处理，@Nullable 表明 View 可以为 null，详细可看 ButterKnife
   */
  private EmptyLayout emptyLayout;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(getLayoutId(), container, false);
    ViewGroup parent = (ViewGroup) view.getParent();
    if (null != parent) {
      parent.removeView(view);
    }
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
    TAG = getClass().getSimpleName().trim();
    LogUtils.i(TAG, "cur：" + getClass().getSimpleName().trim());
    initViewAndEvent();
  }

  private void initView() {
    emptyLayout = getView(view, R.id.empty_layout_root);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  public void showLoading() {
    if (null != emptyLayout) {
      emptyLayout.setEmptyStatus(Empty_status.STATUS_LOADING);
    }
  }

  public void hideLoading() {
    if (null != emptyLayout) {
      emptyLayout.hide();
    }
  }

  public void showNetError(RetryListener retryListener) {
    if (null != emptyLayout) {
      emptyLayout.setEmptyStatus(Empty_status.STATUS_NO_NET);
      emptyLayout.setRetryListener(retryListener);
    }
  }

  public void showDataError(String msg, RetryListener retryListener) {
    if (null != emptyLayout) {
      emptyLayout.setEmptyStatus(Empty_status.STATUS_NO_DATA);
      emptyLayout.setRetryListener(retryListener);
    }
    Snackbar.make(view, TextUtils.isEmpty(msg) ? "数据异常" : msg, Snackbar.LENGTH_SHORT).show();
    LogUtils.i(TAG, TextUtils.isEmpty(msg) ? "数据异常" : msg);
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

  protected abstract int getLayoutId();

  protected abstract void initViewAndEvent();
}
