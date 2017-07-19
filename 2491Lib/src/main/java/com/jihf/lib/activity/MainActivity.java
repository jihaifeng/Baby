package com.jihf.lib.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.jihf.lib.R;
import com.jihf.lib.adapter.FunAdapter;
import com.jihf.lib.base.BaseSimpleActivity;
import com.jihf.lib.constans.Constans;
import com.jihf.lib.constans.URLConfig;
import com.jihf.lib.constans.UmengConfig;
import com.jihf.lib.entity.FunBean;
import com.jihf.lib.http.HttpListener;
import com.jihf.lib.http.OkGoHelper;
import com.jihf.lib.update.VersionUpdate;
import com.jihf.lib.utils.AppUtils;
import com.jihf.lib.utils.LogUtils;
import com.jihf.lib.utils.NetworkUtils;
import com.jihf.lib.utils.ScreenUtils;
import com.jihf.lib.utils.snackBar.SnackBarType;
import com.jihf.lib.utils.snackBar.SnackBarUtils;
import com.jihf.lib.widget.CircleView;
import com.jihf.lib.widget.recyclerview.DividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-16 13:44
 * Mail：jihaifeng@raiyi.com
 */
public class MainActivity extends BaseSimpleActivity implements HttpListener<List<FunBean>> {

  private RecyclerView rvFunData;
  private TwinklingRefreshLayout sfRoot;
  private FrameLayout fabToTop;
  private boolean isLoadMore = false;
  private boolean isOnRefresh = true;
  private FunAdapter funAdapter;
  private int page = 0;
  private LinearLayoutManager layoutManager;
  private boolean curDataIsNull = true;
  private int clickNum = 0;
  private long clickTime;
  boolean isChecked = false;

  @Override protected int getLayoutId() {
    return R.layout.activity_main;
  }

  @Override protected void initViewAndEvent() {
    rvFunData = getView(R.id.rv_fun_data);
    sfRoot = getView(R.id.sf_root);
    fabToTop = getView(R.id.fab_to_top);
    CircleView circleView = getView(R.id.iv_flow_refresh_bg);
    circleView.setBackgroundColor(ContextCompat.getColor(this, R.color.backTop));
    getToolBar().setTitle(getResources().getString(R.string.app_name));
    getToolBar().setOnClickListener(v -> onClickTime());
    featchData(true, false);
    sfRoot.setOnRefreshListener(new RefreshListenerAdapter() {
      @Override public void onRefresh(TwinklingRefreshLayout refreshLayout) {
        page = 0;
        featchData(false, false);
      }

      @Override public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
        page++;
        featchData(false, true);
      }
    });
    fabToTop.setVisibility(View.GONE);
    fabToTop.setOnClickListener(v -> rvFunData.scrollToPosition(0));
    layoutManager = new LinearLayoutManager(this);
    rvFunData.setLayoutManager(layoutManager);
    funAdapter = new FunAdapter();
    DividerItemDecoration decoration =
        new DividerItemDecoration(this, R.color.gray_light, DividerItemDecoration.VERTICAL_LIST);
    decoration.setDividerSize(ScreenUtils.dip2px((float) 0.3));
    rvFunData.removeItemDecoration(decoration);
    rvFunData.addItemDecoration(decoration);
    rvFunData.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    rvFunData.setAdapter(funAdapter);
    funAdapter.setOnItemClickListener((adapter, view, position) -> {
      if (!NetworkUtils.isNetworkConnected()) {
        SnackBarUtils.creatShort(getRootView(), "网络拥堵，请稍后再试...").setType(SnackBarType.Warning).show();
        return;
      }
      if (null == adapter || adapter.getData().size() < position || null == adapter.getData().get(position)) {
        SnackBarUtils.creatShort(getRootView(), "没有更多信息").setType(SnackBarType.Warning).show();
        return;
      }
      FunBean funBean = (FunBean) adapter.getData().get(position);
      Bundle bundle = new Bundle();
      bundle.putString(Constans.BUNDLE_WEB_KEY, funBean.getRowKey());
      bundle.putString(Constans.BUNDLE_WEB_TITLE, funBean.getTitle());
      jumpTo(WebActivity.class, bundle);
    });
    rvFunData.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LogUtils.i(TAG, "onScrolled: " + layoutManager.findFirstVisibleItemPosition());
        if (layoutManager.findFirstVisibleItemPosition() > 7) {
          fabToTop.setVisibility(View.VISIBLE);
        } else {
          fabToTop.setVisibility(View.GONE);
        }
      }
    });
    isChecked = false;
    checkUpdate();
  }

  private void checkUpdate() {
    if (isChecked) {
      return;
    }
    VersionUpdate.getInstance(this).checkForUpdate(false, true, false);
    isChecked = true;
  }

  private void onClickTime() {
    int timeSpan = 3 * 1000;
    int maxNum = 5;
    boolean isTimeOut = (System.currentTimeMillis() - clickTime) > timeSpan;
    LogUtils.i(TAG, "onClickTime: " + isTimeOut);
    if (isTimeOut) {
      clickNum = 0;
    }
    clickNum++;
    if (clickNum >= maxNum && !isTimeOut) {
      clickNum = 0;
      showAppInfoDialog();
    }
    clickTime = System.currentTimeMillis();
  }

  @Override protected void onRestart() {
    super.onRestart();
  }

  private void showAppInfoDialog() {

    String msg = "项目名称："
        + AppUtils.getAppName()
        + "\n版本号："
        + AppUtils.getAppVersionCode()
        + "\n版本名称："
        + AppUtils.getAppVersionName()
        + "\n渠道号："
        + UmengConfig.getUmengChannel()
        + "\nUA："
        + URLConfig.getUA();
    LogUtils.i(TAG, "showAppInfoDialog: " + msg);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("软件信息").setMessage(msg).setPositiveButton("知道了", (dialog, which) -> dialog.dismiss());
    Dialog dialog = builder.create();
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();
  }

  private void featchData(boolean needLoading, boolean loadMore) {
    if (needLoading) {
      showLoading();
    }
    OkGoHelper.getInstance().getFunData(page, MainActivity.this);
    isOnRefresh = !loadMore;
    isLoadMore = loadMore;
  }

  @Override protected boolean isNeedBackButton() {
    return false;
  }

  @Override protected boolean initSwipeBackEnable() {
    return false;
  }

  @Override protected void onPause() {
    super.onPause();
  }

  @Override protected void onResume() {
    super.onResume();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    cancelTag(Constans.FUN_TAG);
  }

  @Override public void onSuccess(List<FunBean> bean) {
    if (isLoadMore) {
      funAdapter.addData(bean);
    }
    if (isOnRefresh) {
      funAdapter.setNewData(bean);
    }
    showSuccess();
    curDataIsNull = false;
    endGetData();
    LogUtils.i(TAG, "onSuccess：" + bean.size());
  }

  @Override public void onDataError(String msg) {
    endGetData();
    if (curDataIsNull) {
      showDataError(msg, () -> featchData(true, false));
    } else {
      SnackBarUtils.creatShort(getRootView(), TextUtils.isEmpty(msg) ? "数据异常" : msg)
          .setType(SnackBarType.ErrorT)
          .show();
    }

    LogUtils.i(TAG, "onDataError：" + msg);
  }

  @Override public void onNetError(String msg) {
    endGetData();
    if (curDataIsNull) {
      showNetError(msg, () -> featchData(true, false));
    } else {
      SnackBarUtils.creatShort(getRootView(), TextUtils.isEmpty(msg) ? "数据异常" : msg)
          .setType(SnackBarType.Warning)
          .show();
    }
  }

  private void endGetData() {
    try {
      hideLoading();
      sfRoot.finishRefreshing();
      sfRoot.finishLoadmore();
      isLoadMore = false;
      isOnRefresh = false;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}