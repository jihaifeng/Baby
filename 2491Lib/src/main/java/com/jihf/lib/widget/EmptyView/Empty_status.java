package com.jihf.lib.widget.EmptyView;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_LOADING;
import static com.jihf.lib.widget.EmptyView.Empty_status.STATUS_HIDE;
import static com.jihf.lib.widget.EmptyView.Empty_status.STATUS_NO_DATA;
import static com.jihf.lib.widget.EmptyView.Empty_status.STATUS_NO_NET;

/**
 * Func：
 * Desc:
 * Author：jihf
 * Date：2017-05-09 10:35
 * Mail：jihaifeng@raiyi.com
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef ({STATUS_HIDE,STATUS_LOADING, STATUS_NO_NET, STATUS_NO_DATA})
public @interface Empty_status {
  public static final int STATUS_HIDE = 1001;
  public static final int STATUS_LOADING = 1;
  public static final int STATUS_NO_NET = 2;
  public static final int STATUS_NO_DATA = 3;
}
