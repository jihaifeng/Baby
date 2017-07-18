package com.jihf.lib.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jihf.lib.R;
import com.jihf.lib.base.BaseSimpleActivity;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-16 13:44
 * Mail：jihaifeng@raiyi.com
 */

public class SplashActivity extends BaseSimpleActivity {
  private ImageView ivSplash;
  private TextView tvCutDownTime;
  //private MyCountDownTimer timer = new MyCountDownTimer(5 * 1000, 1000);

  @Override protected int getLayoutId() {
    return R.layout.activity_splash;
  }

  @Override protected void initViewAndEvent() {
    ivSplash = getView(R.id.iv_splash);
    tvCutDownTime = getView(R.id.tv_cut_down_time);
    ivSplash.setBackgroundResource(R.mipmap.ic_splash);
    tvCutDownTime.setOnClickListener(v -> toMain());
    tvCutDownTime.setVisibility(View.GONE);
    getToolBar().setVisibility(View.GONE);
    new Handler().postDelayed(this :: toMain, 3 * 1000);
  }

  @Override protected boolean initSwipeBackEnable() {
    return false;
  }

  /**
   * 定义一个倒计时的内部类
   */
  //private class MyCountDownTimer extends CountDownTimer {
  //  /**
  //   * @param millisInFuture 从开始调用start()到倒计时完成并 onfinish()方法被调用的毫秒数
  //   * @param countDownInterval 接收onTick(long)回调的间隔时间
  //   */
  //  MyCountDownTimer(long millisInFuture, long countDownInterval) {
  //    super(millisInFuture, countDownInterval);
  //  }
  //
  //  @Override public void onTick(long l) {
  //    SpannableString spanStr = new SpannableString("跳过\n" + l / 1000 + " s");
  //    spanStr.setSpan(new AbsoluteSizeSpan(10, true), 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
  //    spanStr.setSpan(new AbsoluteSizeSpan(8, true), 2, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
  //    spanStr.setSpan(new AbsoluteSizeSpan(10, true), 5, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
  //    tvCutDownTime.setText(spanStr);
  //  }
  //
  //  @Override public void onFinish() {
  //    toMain();
  //  }
  //}
  //
  private void toMain() {
    SplashActivity.this.finish();
    startActivity(new Intent(this, MainActivity.class));
  }

  @Override public void onBackPressed() {

  }
  //@Override protected void onDestroy() {
  //  super.onDestroy();
  //  if (null != timer) {
  //    timer.cancel();
  //    timer = null;
  //  }
  //
}