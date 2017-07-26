package com.jihf.lib.share;

import android.graphics.Bitmap;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-07-26 09:51
 * Mail：jihaifeng@raiyi.com
 */

public class ShareInfoBean {
  public String shareUrl; // 链接分享使用
  public String sharePic; // 图片分享使用
  public String shareText; // 文字分享使用
  public String shareTitle; // 分享标题
  public String shareDesc; // 分享描述
  public Bitmap shareThumbBitmap; // 分享缩略图 bitmap
  public String shareThumbUrl; // 分享缩略图 url
  public @ShareType int shareType; // 分享类型，1==QQ好友，2==QQ空间，3==微信好友 4==微信朋友圈
}
