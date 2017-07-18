package com.jihf.lib.http;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-20 16:16
 * Mail：jihaifeng@raiyi.com
 */

public interface HttpListener<T> {

  public void onSuccess(T bean);

  public void onDataError(String msg);

  public void onNetError(String msg);
}
