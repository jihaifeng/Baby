package com.jihf.lib.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jihf.lib.R;
import com.jihf.lib.entity.FunBean;
import com.jihf.lib.utils.DefaultBgUtils;
import com.jihf.lib.utils.LogUtils;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-20 17:22
 * Mail：jihaifeng@raiyi.com
 */

public class FunAdapter extends BaseQuickAdapter<FunBean, BaseViewHolder> {
  private static final String TAG = FunAdapter.class.getSimpleName().trim();

  public FunAdapter() {
    super(R.layout.fun_item);
  }

  @Override protected void convert(BaseViewHolder helper, FunBean item) {
    String time = formatTime(item.getTime());
    helper.setText(R.id.tv_title, item.getTitle()).setText(R.id.tv_time, TextUtils.isEmpty(time) ? "" : time);
    Glide.with(mContext).load(item.getImageUrl()).asBitmap().placeholder(DefaultBgUtils.provideDrawable())
        //.error(R.mipmap.ic_error)
        .into((ImageView) helper.getView(R.id.iv_left));
  }

  private String formatTime(String time) {
    LogUtils.i(TAG, "formatTime: " + time);
    if (TextUtils.isEmpty(time) || time.length() < 10) {
      return null;
    }
    return time.substring(0, 10);
  }
}