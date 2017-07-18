package com.jihf.lib.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.jihf.lib.R;
import com.jihf.lib.utils.ActivityUtils;
import com.jihf.lib.utils.AppUtils;
import com.jihf.lib.utils.LogUtils;
import com.jihf.lib.utils.ScreenUtils;
import com.jihf.lib.utils.snackBar.SnackBarType;
import com.jihf.lib.utils.snackBar.SnackBarUtils;
import com.jihf.lib.widget.EmptyView.EmptyLayout;
import com.jihf.lib.widget.EmptyView.Empty_status;
import com.jihf.lib.widget.EmptyView.RetryListener;
import com.jihf.swipbackhelper.SwipeBackHelper;
import com.lzy.okgo.OkGo;
import com.umeng.analytics.MobclickAgent;

/**
 * Func：
 * Desc:
 * Author：jihf
 * Data：2017-03-09 09:32
 * Mail：jihaifeng@raiyi.com
 */
public abstract class BaseSimpleActivity extends BaseSwipeBackActivity {
  public static String TAG = BaseSimpleActivity.class.getSimpleName().trim();

  private Toolbar toolbar;
  private ImageView ivRight;
  private FrameLayout contentFrame;
  private LinearLayout llViewRoot;
  /**
   * 把 EmptyLayout 放在基类统一处理，@Nullable 表明 View 可以为 null，详细可看 ButterKnife
   */
  private EmptyLayout emptyLayout;

  private Context mBaseContext;
  private Activity mCurrentActivity;
  private long exitTime;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //布局注入
    setContentView(getLayoutId());
    initView();
    setActivityStatus(this);
    initViewAndEvent();
    mBaseContext = this;
    setToolBar();
    LogUtils.i(TAG, "--------onCreate--------");
  }

  private void initView() {
    toolbar = getView(R.id.toolbar);
    ivRight = getView(R.id.iv_right);
    contentFrame = getView(R.id.content_frame);
    llViewRoot = getView(R.id.ll_view_root);
    emptyLayout = getView(R.id.empty_layout_root);
  }

  @Override protected void onStart() {
    super.onStart();
    LogUtils.i(TAG, "-------onStart------");
  }

  @Override protected void onPause() {
    super.onPause();
    MobclickAgent.onPause(mBaseContext);
    LogUtils.i(TAG, "-------onPause------");
  }

  @Override protected void onResume() {
    super.onResume();
    MobclickAgent.onResume(mBaseContext);
    LogUtils.i(TAG, "-------onResume------" + this);
  }

  @Override protected void onStop() {
    super.onStop();
    LogUtils.i(TAG, "-------onStop------");
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    SwipeBackHelper.onPostCreate(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    // 在Activity栈中移除Activity
    ActivityUtils.getInstance().removeActivity(this);
    LogUtils.i(TAG, "-------onDestroy------");
  }

  @Override protected void onRestart() {
    super.onRestart();
    LogUtils.i(TAG, "-------onRestart------");
  }

  @Override public void finish() {
    super.finish();
    LogUtils.i(TAG, "-------finish------");
    ActivityUtils.getInstance().removeActivity(this);
  }

  @Override public void setContentView(@LayoutRes int layoutResID) {
    View view = getLayoutInflater().inflate(R.layout.activity_base, null);
    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.content_frame);
    // 将传入的layout加载到activity_base的content_frame里面
    getLayoutInflater().inflate(layoutResID, frameLayout, true);
    super.setContentView(view);
  }

  protected void setToolBar() {
    toolbar.setTitleTextColor(Color.WHITE);
    setSupportActionBar(toolbar);
    // 设置返回键可用
    assert null != getSupportActionBar();
    getSupportActionBar().setHomeButtonEnabled(isNeedBackButton());
    getSupportActionBar().setDisplayHomeAsUpEnabled(isNeedBackButton());
  }

  public void setIvRight(@NonNull Integer icon, View.OnClickListener btnClick) {
    ivRight.setBackgroundResource(icon);
    ViewGroup.LayoutParams linearParams = ivRight.getLayoutParams();
    linearParams.height = ScreenUtils.dip2px(26);
    linearParams.width = ScreenUtils.dip2px(26);
    ivRight.setLayoutParams(linearParams);
    ivRight.setOnClickListener(btnClick);
  }

  protected boolean isNeedBackButton() {
    return true;
  }

  public Toolbar getToolBar() {
    return toolbar;
  }

  public View getRootView() {
    return llViewRoot;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        if (canGoBack()) {
          finish();
        }
        break;
    }
    return true;
  }

  /**
   * Activity 统一配置
   *
   * @param activity 需要配置的Activity
   */
  private void setActivityStatus(Activity activity) {
    //设置当前栈顶Activity
    mCurrentActivity = activity;
    //设置Activity强制竖屏显示
    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    // 向Acitivty栈（数据类型为List）中添加Activity
    ActivityUtils.getInstance().addActivity(activity);
    //设置TAG
    TAG = getClass().getSimpleName().trim();
  }

  /**
   * 是否有上一级的Activity可以返回
   *
   * @return true===可返回，false====不可返回
   */
  public static boolean canGoBack() {
    return ActivityUtils.getInstance().getActivityNum() > 1;
  }

  /**
   * 获取当前显示的Activity
   *
   * @return 当前Activity
   */
  public Activity getCurrentActivity() {
    if (null == mCurrentActivity) {
      mCurrentActivity = this;
    }
    return mCurrentActivity;
  }

  @Override protected boolean initSwipeBackEnable() {
    return true;
  }

  /**
   * 无参数的跳转
   *
   * @param to 目标Activity
   */
  public void jumpTo(Class to) {
    jumpTo(to, null);
  }

  /**
   * 带参数的跳转
   *
   * @param to 目标Activity
   * @param bundle 参数
   */
  public void jumpTo(Class to, Bundle bundle) {
    Intent intent = new Intent();
    intent.setClass(mCurrentActivity, to);
    if (null != bundle) {
      intent.putExtras(bundle);
    }
    mCurrentActivity.startActivity(intent);
  }

  public void jumpToWithResultCode(Class to, Bundle bundle, int resultCode) {
    Intent intent = new Intent();
    intent.setClass(mCurrentActivity, to);
    if (null != bundle) {
      intent.putExtras(bundle);
    }
    mCurrentActivity.startActivityForResult(intent, resultCode);
  }

  @SuppressWarnings ("unchecked") public final <E extends View> E getView(int id) {
    try {
      return (E) findViewById(id);
    } catch (Exception e) {
      LogUtils.i(TAG, "Can not cast view to concrete class" + e);
      throw e;
    }
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

  public void jumpToWithResultCode(Class to, int resultCode) {
    jumpToWithResultCode(to, null, resultCode);
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

  public void showSuccess() {
    if (null != contentFrame) {
      contentFrame.setVisibility(View.VISIBLE);
    }
  }

  public void showNetError(String msg, RetryListener retryListener) {
    Log.i(TAG, "emptyLayout.getChildCount():" + emptyLayout.getChildCount());
    if (null != emptyLayout) {
      emptyLayout.setEmptyStatus(Empty_status.STATUS_NO_NET);
      emptyLayout.setRetryListener(retryListener);
    }
    if (null != contentFrame) {
      contentFrame.setVisibility(View.GONE);
    }
    SnackBarUtils.creatShort(llViewRoot, TextUtils.isEmpty(msg) ? "数据异常" : msg).setType(SnackBarType.Warning).show();
  }

  public void showDataError(String msg, RetryListener retryListener) {
    if (null != emptyLayout) {
      emptyLayout.setEmptyStatus(Empty_status.STATUS_NO_DATA);
      emptyLayout.setRetryListener(retryListener);
    }
    if (null != contentFrame) {
      contentFrame.setVisibility(View.GONE);
    }
    SnackBarUtils.creatShort(llViewRoot, TextUtils.isEmpty(msg) ? "数据异常" : msg).setType(SnackBarType.ErrorT).show();
    LogUtils.i(TAG, TextUtils.isEmpty(msg) ? "数据异常" : msg);
  }

  protected abstract int getLayoutId();

  protected abstract void initViewAndEvent();

  @Override public void onBackPressed() {
    if (canGoBack()) {
      super.onBackPressed();
    } else {
      exit();
    }
  }

  public void exit() {
    if ((System.currentTimeMillis() - exitTime) > 2000) {
      SnackBarUtils.creatShort(llViewRoot, "再按一次退出" + AppUtils.getAppName()).setType(SnackBarType.Confirm).show();
      exitTime = System.currentTimeMillis();
    } else {
      finish();
      System.exit(0);
    }
  }

  public void cancelTag(String tag) {
    OkGo.getInstance().cancelTag(tag);
  }
}
