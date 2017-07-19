package com.jihf.baby;

import com.jihf.lib.app.BaseApplication;
import com.jihf.lib.constans.UrlBean;
import com.jihf.lib.share.ShareBean;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-07-13 11:12
 * Mail：jihaifeng@raiyi.com
 */

public class MyApplication extends BaseApplication {

  @Override public void onCreate() {
    super.onCreate();
  }

  @Override public UrlBean initUrl() {
    UrlBean urlBean = new UrlBean();
    urlBean.baseUrl = ThirdConstants.baseUrl;
    urlBean.pathList = ThirdConstants.pathList;
    urlBean.pathDetails = ThirdConstants.pathDetails;
    urlBean.tagId = ThirdConstants.tagId;
    urlBean.defaultWebUrl = ThirdConstants.defaultWebUrl;
    return urlBean;
  }

  @Override public ShareBean initShare() {
    ShareBean shareBean = new ShareBean();
    shareBean.QqAppId = ThirdConstants.QqAppId;
    shareBean.QqAppKey = ThirdConstants.QqAppKey;
    shareBean.WxAppId = ThirdConstants.WxAppId;
    shareBean.WxAppSecret = ThirdConstants.WxAppSecret;
    return shareBean;
  }

  @Override public String initUmengAppKey() {
    return ThirdConstants.UmengAppKey;
  }

  @Override public String initUpdateTag() {
    return ThirdConstants.UpdateTag;
  }
}