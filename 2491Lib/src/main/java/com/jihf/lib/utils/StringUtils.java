package com.jihf.lib.utils;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-07-11 10:07
 * Mail：jihaifeng@raiyi.com
 */

public class StringUtils {
  /**
   * 判断字符串是否为null或长度为0
   *
   * @param s 待校验字符串
   *
   * @return {@code true}: 空<br> {@code false}: 不为空
   */
  public static boolean isEmpty(CharSequence s) {
    return s == null || s.length() == 0;
  }

  /**
   * 判断字符串是否为null或全为空格
   *
   * @param s 待校验字符串
   *
   * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
   */
  public static boolean isTrimEmpty(String s) {
    return (s == null || s.trim().length() == 0);
  }

  /**
   * 判断一组字符串中是否存在空字符串
   *
   * @param str
   *
   * @return
   */
  public static boolean isEmpty(String... str) {
    boolean flag = false;
    for (String s : str) {
      if (isTrimEmpty(s)) {
        flag = true;
      }
    }
    return flag;
  }
}
