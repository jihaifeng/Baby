package com.jihf.lib.app;

import android.app.Application;
import android.support.annotation.DrawableRes;
import android.util.Log;
import com.jihf.lib.R;
import com.jihf.lib.constans.Constans;
import com.jihf.lib.constans.URLConfig;
import com.jihf.lib.constans.UmengConfig;
import com.jihf.lib.constans.UrlBean;
import com.jihf.lib.http.OkGoHelper;
import com.jihf.lib.share.ShareBean;
import com.jihf.lib.share.ShareConfig;
import com.jihf.lib.update.VersionUpdate;
import com.jihf.lib.utils.AppUtils;
import com.jihf.lib.utils.DeviceUtils;
import com.jihf.lib.utils.LogUtils;
import com.jihf.lib.utils.NetworkUtils;
import com.jihf.lib.utils.ScreenUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-20 14:27
 * Mail：jihaifeng@raiyi.com
 */

public abstract class BaseApplication extends Application {
  private static final String TAG = BaseApplication.class.getSimpleName().trim();
  private static BaseApplication instance;
  public static @DrawableRes int logo;
  public static @DrawableRes int splash;

  @Override public void onCreate() {
    super.onCreate();
    initConfig();
  }

  public static BaseApplication getInstance() {
    return instance;
  }

  private static void setInstance(BaseApplication instance) {
    BaseApplication.instance = instance;
  }

  private void initConfig() {
    setInstance(this);
    // 设置日志类是否显示
    LogUtils.setLogSwitch(true);
    // 屏幕尺寸
    ScreenUtils.createInstance(this);
    // App信息
    AppUtils.init(this);
    // 设备信息
    DeviceUtils.init(this);
    // 网络状态
    NetworkUtils.init(this);
    // 网络请求
    OkGoHelper.getInstance().init(this);

    //初始化配置
    logo = R.mipmap.ic_launcher;
    splash = R.mipmap.ic_splash;
    UmengConfig.init(initUmengAppKey());
    setShare(initShare());
    setUrl(initUrl());
    VersionUpdate.setUpdateTag(initUpdateTag());
    /*
     * umeng统计
     *
     * UMAnalyticsConfig(Context context, String appkey, String channelId, EScenarioType eType,Boolean isCrashEnable)
     * 构造意义：
     * String appkey:官方申请的Appkey
     * String channel: 渠道号
     * EScenarioType eType: 场景模式，包含统计、游戏、统计盒子、游戏盒子
     * Boolean isCrashEnable: 可选初始化. 是否开启crash模式
     */
    Log.i(TAG, "initConfig: " + UmengConfig.getUmengChannel());
    MobclickAgent.UMAnalyticsConfig umAnalyticsConfig =
        new MobclickAgent.UMAnalyticsConfig(this, UmengConfig.getUmengAppkey(), UmengConfig.getUmengChannel(),
            MobclickAgent.EScenarioType.E_UM_NORMAL, false);
    MobclickAgent.startWithConfigure(umAnalyticsConfig);
    MobclickAgent.setDebugMode(false);
  }

  // 设置每一页请求的数量
  public void setPageNum(int num) {
    Constans.setListPageNum(num);
  }

  // 设置请求的UserAgent
  public void setUa(String ua) {
    URLConfig.setUA(ua);
  }

  // 设置请求的UserAgent
  public void setUa(String port, String appName) {
    URLConfig.setUA(port, appName);
  }

  //数组长度为定长5，分别对应baseUrl,pathList,pathDetails,tagId,defaultWebUrl
  private void setUrl(UrlBean urlBean) {
    if (null == urlBean) {
      Log.i(TAG, "url配置参数异常");
      return;
    }
    URLConfig.init(urlBean.baseUrl, urlBean.pathList, urlBean.pathDetails, urlBean.tagId, urlBean.defaultWebUrl);
  }

  //数组长度为定长4，分别对应qq分享的appId,appKey,微信分享的appId,appSecret
  private void setShare(ShareBean shareBean) {
    if (null == shareBean) {
      Log.i(TAG, "app分享参数异常 ");
      return;
    }
    ShareConfig.initQQShare(shareBean.QqAppId, shareBean.QqAppKey);
    ShareConfig.initWxShare(shareBean.WxAppId, shareBean.WxAppSecret);
  }

  public void setLogo(@DrawableRes int id) {
    BaseApplication.logo = id != 0 ? id : R.mipmap.ic_launcher;
  }

  public void setSplash(@DrawableRes int splash) {
    BaseApplication.splash = splash != 0 ? splash : R.mipmap.ic_splash;
  }

  //数组长度为定长5，分别对应baseUrl,pathList,pathDetails,tagId,defaultWebUrl
  public abstract UrlBean initUrl();

  // 数组长度为定长4，分别对应qq分享的appId,appKey,微信分享的appId,appSecret
  public abstract ShareBean initShare();

  // 数组长度为定长4，分别对应umeng的Key,channel
  public abstract String initUmengAppKey();

  // 升级Tag
  public abstract String initUpdateTag();
}
