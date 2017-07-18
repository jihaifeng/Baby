package com.jihf.lib.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.jihf.swipbackhelper.SwipeBackHelper;

/**
 * Func：支持滑动返回的基类Activity
 * Desc:
 * Author：jihf
 * Data：2017-03-06 10:40
 * Mail：jihaifeng@raiyi.com
 */
public abstract class BaseSwipeBackActivity extends AppCompatActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    //初始化滑动返回
    setSwipeBackPage();
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    SwipeBackHelper.onPostCreate(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    SwipeBackHelper.onDestroy(this);
  }

  private void setSwipeBackPage() {
    SwipeBackHelper.onCreate(this);
    SwipeBackHelper.getCurrentPage(this)// 获取实例
        .setSwipeBackEnable(initSwipeBackEnable())//设置是否可滑动
        .setSwipeSensitivity(0.5f)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
        .setSwipeRelateEnable(true)//是否与下一级activity联动(微信效果)。默认关
        .setSwipeRelateOffset(300);//activity联动时的偏移量。默认500px。
  }

  protected abstract boolean initSwipeBackEnable();
}
