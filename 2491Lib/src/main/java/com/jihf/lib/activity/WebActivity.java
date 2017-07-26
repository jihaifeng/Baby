package com.jihf.lib.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.jihf.lib.R;
import com.jihf.lib.app.BaseApplication;
import com.jihf.lib.base.BaseSimpleActivity;
import com.jihf.lib.constans.Constans;
import com.jihf.lib.constans.URLConfig;
import com.jihf.lib.http.OkGoHelper;
import com.jihf.lib.js.JsInterface;
import com.jihf.lib.share.QQShareType;
import com.jihf.lib.share.QQShareUtil;
import com.jihf.lib.share.WXShareType;
import com.jihf.lib.share.WXShareUtil;
import com.jihf.lib.share.WxThumUtils;
import com.jihf.lib.utils.BitmapCovListener;
import com.jihf.lib.utils.LogUtils;
import com.jihf.lib.widget.SharePopwindow;
import com.just.library.AgentWeb;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
  private Bitmap thumbBitmap;
  private String thumbUrl;
  private String shareDesc;
  private OkGoHelper okGoHelper;
  //private ShareInfoBean shareInfoBean;

  @Override protected int getLayoutId() {
    return R.layout.activity_web;
  }

  @Override protected void initViewAndEvent() {
    llWebRoot = getView(R.id.ll_web_root);
    if (null == okGoHelper) {
      okGoHelper = OkGoHelper.getInstance();
    }
    Bundle bundle = getIntent().getExtras();
    if (null != bundle) {
      webKey = bundle.getString(Constans.BUNDLE_WEB_KEY);
      webTitle = bundle.getString(Constans.BUNDLE_WEB_TITLE);
    }
    webUrl = TextUtils.isEmpty(webKey) ? URLConfig.getDefaultWebUrl() : okGoHelper.appendDetailUrl(webKey);
    openUrl();
    setIvRight(R.mipmap.ic_share_logo, v -> {
      WxThumUtils.covBitmapFromUrl(thumbUrl, new BitmapCovListener() {
        @Override public void covSuccess(Bitmap bitmap) {
          Log.i(TAG, "covSuccess: " + bitmap);
          thumbBitmap = bitmap;
        }

        @Override public void covFailure(String errorMsg) {
          Log.i(TAG, "covFailure: " + errorMsg);
          thumbBitmap = BitmapFactory.decodeResource(getResources(), BaseApplication.logo);
        }
      });
      sharePopwindow = new SharePopwindow(WebActivity.this, WebActivity.this);
      sharePopwindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    });
    //setShareInfo();
  }

  //private void setShareInfo() {
  //  if (null == shareInfoBean) {
  //    shareInfoBean = new ShareInfoBean();
  //  }
  //  shareInfoBean.shareUrl = webUrl;
  //  shareInfoBean.shareDesc = shareDesc;
  //  shareInfoBean.shareTitle = webTitle;
  //  shareInfoBean.shareThumbBitmap = thumbBitmap;
  //  shareInfoBean.shareThumbUrl = thumbUrl;
  //}

  private void openUrl() {
    mAgentWeb = AgentWeb.with(this)//传入Activity
        .setAgentWebParent(llWebRoot, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ，
        // 那么第二参数需要传入 RelativeLayout.LayoutParams
        .useDefaultIndicator()// 使用默认进度条
        .defaultProgressBarColor() // 使用默认进度条颜色
        //.setWebViewClient(new WebViewClient() {
        //  @Override public void onPageFinished(WebView view, String url) {
        //    Log.i(TAG, "onPageFinished: " + url);
        //    appendClick(view);
        //  }
        //})
        .setReceivedTitleCallback(
            (view, title) -> getToolBar().setTitle(TextUtils.isEmpty(webTitle) ? title : webTitle)) //设置 Web
        // 页面的 title 回调
        .createAgentWeb()//

        .ready()//
        .go(webUrl);
    mAgentWeb.getWebCreator().get().getSettings().setUserAgentString(URLConfig.getUA());
    mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new JsInterface(this));
    new Thread(new Runnable() {
      @Override public void run() {
        try {
          Log.i(TAG, "webUrl: " + webUrl);
          Document doc = Jsoup.connect(webUrl).get();
          Elements imgElements = doc.getElementsByTag("img");
          if (null != imgElements && imgElements.size() != 0) {
            thumbUrl = imgElements.first().absUrl("src");
          } else {
            thumbUrl = "";
          }
          Elements titleElements = doc.getElementsByTag("h1");
          Elements bodyElements = doc.getElementsByTag("p");
          StringBuilder builder = new StringBuilder();
          for (Element element : bodyElements) {
            builder.append(element.ownText());
          }
          Log.i(TAG, "run: " + builder.length());
          shareDesc = builder.length() > 100 ? builder.substring(0, 100) : builder.toString();
          Log.i(TAG, "run: " + shareDesc.length());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  private void appendClick(WebView view) {
    view.loadUrl("javascript:(function(){"
        + "var obj = document.getElementsByTagName(\"img\"); "
        + "for(var i=0;i<obj.length;i++)  "
        + "{"
        + "    obj[i].onclick=function()  "
        + "    {  "
        + " window.android.shareImg(this.src)"
        + "    }  "
        + "}"
        + "})()");
  }

  @Override public void onClick(View v) {
    sharePopwindow.dismiss();
    sharePopwindow.backgroundAlpha(WebActivity.this, 1f);
    Log.i(TAG, "onClick: " + thumbUrl + "\n" + thumbBitmap);
    if (null == thumbBitmap) {
      thumbBitmap = BitmapFactory.decodeResource(getResources(), BaseApplication.logo);
    }
    Log.i(TAG, "onClick: " + thumbUrl + "\n" + thumbBitmap + "\n" + shareDesc.length());
    if (v.getId() == R.id.tv_wx_friend) {
      // 微信分享给朋友
      WXShareUtil.getInstance(WebActivity.this)
          .shareUrl(webUrl, webTitle, thumbBitmap, shareDesc, WXShareType.WX_FRIEND);
    } else if (v.getId() == R.id.tv_wx_circle) {
      // 微信分享给朋友圈
      WXShareUtil.getInstance(WebActivity.this)
          .shareUrl(webUrl, webTitle, thumbBitmap, shareDesc, WXShareType.WX_CIRCLE);
    } else if (v.getId() == R.id.tv_qq_friend) {
      // 分享到QQ
      QQShareUtil.getInstance(WebActivity.this).shareUrl(webUrl, webTitle, shareDesc, thumbUrl, QQShareType.QQ);
    } else if (v.getId() == R.id.tv_qq_qzone) {
      // 分享到QQ空间
      QQShareUtil.getInstance(WebActivity.this).shareUrl(webUrl, webTitle, shareDesc, thumbUrl, QQShareType.QZONE);
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