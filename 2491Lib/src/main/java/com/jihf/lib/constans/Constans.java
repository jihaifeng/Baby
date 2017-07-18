package com.jihf.lib.constans;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-21 12:50
 * Mail：jihaifeng@raiyi.com
 */

public class Constans {
  public static final String FUN_TAG = "fun_tag";
  public static final String DETAILS_TAG = "details_tag";
  public static final String BUNDLE_WEB_KEY = "web_url";
  public static final String BUNDLE_WEB_TITLE = "web_title";
  private static int LIST_PAGE_NUM = 20;

  public static int getListPageNum() {
    return LIST_PAGE_NUM;
  }

  public static void setListPageNum(int listPageNum) {
    if (listPageNum > 0) {
      LIST_PAGE_NUM = listPageNum;
    }
  }
}
