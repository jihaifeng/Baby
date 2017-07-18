package com.jihf.lib.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.jihf.lib.R;
import com.jihf.lib.app.BaseApplication;
import com.jihf.lib.base.BaseSimpleActivity;
import com.jihf.lib.constans.Constans;
import com.jihf.lib.constans.URLConfig;
import com.jihf.lib.http.OkGoHelper;
import com.jihf.lib.share.QQShareType;
import com.jihf.lib.share.QQShareUtil;
import com.jihf.lib.share.WXShareType;
import com.jihf.lib.share.WXShareUtil;
import com.jihf.lib.utils.LogUtils;
import com.jihf.lib.widget.SharePopwindow;
import com.just.library.AgentWeb;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-20 18:13
 * Mail：jihaifeng@raiyi.com
 */

public class WebActivity extends BaseSimpleActivity implements View.OnClickListener {
  public static final String TAG = WebActivity.class.getSimpleName();
  private LinearLayout llWebRoot;
  private AgentWeb mAgentWeb;
  private String webKey;
  private String webTitle;
  private String webUrl = URLConfig.getDefaultWebUrl();
  private SharePopwindow sharePopwindow;
  private Bitmap shareBitmap;

  @Override protected int getLayoutId() {
    return R.layout.activity_web;
  }

  @Override protected void initViewAndEvent() {
    llWebRoot = getView(R.id.ll_web_root);
    Bundle bundle = getIntent().getExtras();
    if (null != bundle) {
      webKey = bundle.getString(Constans.BUNDLE_WEB_KEY);
      webTitle = bundle.getString(Constans.BUNDLE_WEB_TITLE);
    }
    webUrl =
        TextUtils.isEmpty(webKey) ? URLConfig.getDefaultWebUrl() : OkGoHelper.getInstance().appendDetailUrl(webKey);
    openUrl();
    setIvRight(R.mipmap.ic_share_logo, v -> {
      sharePopwindow = new SharePopwindow(WebActivity.this, WebActivity.this);
      sharePopwindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    });
  }

  private void openUrl() {
    mAgentWeb = AgentWeb.with(this)//传入Activity
        .setAgentWebParent(llWebRoot, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ，
        // 那么第二参数需要传入 RelativeLayout.LayoutParams
        .useDefaultIndicator()// 使用默认进度条
        .defaultProgressBarColor() // 使用默认进度条颜色
        .setReceivedTitleCallback(
            (view, title) -> getToolBar().setTitle(TextUtils.isEmpty(webTitle) ? title : webTitle)) //设置 Web
        // 页面的 title 回调
        .createAgentWeb()//
        .ready()//
        .go(webUrl);
    mAgentWeb.getWebCreator().get().getSettings().setUserAgentString(URLConfig.getUA());
  }

  @Override public void onClick(View v) {
    sharePopwindow.dismiss();
    sharePopwindow.backgroundAlpha(WebActivity.this, 1f);
    shareBitmap = BitmapFactory.decodeResource(getResources(), BaseApplication.logo);
    if (v.getId() == R.id.tv_wx_friend) {
      // 微信分享给朋友
      WXShareUtil.getInstance(WebActivity.this).shareUrl(webUrl, webTitle, shareBitmap, null, WXShareType.WX_FRIEND);
    } else if (v.getId() == R.id.tv_wx_circle) {
      // 微信分享给朋友圈
      WXShareUtil.getInstance(WebActivity.this).shareUrl(webUrl, webTitle, shareBitmap, null, WXShareType.WX_CIRCLE);
    } else if (v.getId() == R.id.tv_qq_friend) {
      // 分享到QQ
      QQShareUtil.getInstance(WebActivity.this).shareUrl(webUrl, webTitle, null, QQShareType.QQ);
    } else if (v.getId() == R.id.tv_qq_qzone) {
      // 分享到QQ空间
      QQShareUtil.getInstance(WebActivity.this).shareUrl(webUrl, webTitle, null, QQShareType.QZONE);
    }
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (null != mAgentWeb) {
      return mAgentWeb.handleKeyEvent(keyCode, event) || super.onKeyDown(keyCode, event);
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override protected void onResume() {
    super.onResume();
    if (null != mAgentWeb) {
      mAgentWeb.getWebLifeCycle().onResume();
    }
  }

  @Override protected void onPause() {
    super.onPause();
    if (null != mAgentWeb) {
      mAgentWeb.getWebLifeCycle().onPause();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (null != mAgentWeb) {
      mAgentWeb.getWebLifeCycle().onDestroy();
    }
    cancelTag(Constans.DETAILS_TAG);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    LogUtils.i(TAG, "onActivityResult: " + requestCode + "  " + resultCode);
    QQShareUtil.getInstance(WebActivity.this).onActivityResult(requestCode, resultCode, data);
  }
}