package com.jihf.lib.utils;

import java.util.regex.Pattern;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-20 18:33
 * Mail：jihaifeng@raiyi.com
 */

public class RegexUtils {
  /**
   * 验证URL
   *
   * @param input 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isURL(CharSequence input) {
    return isMatch(RegexConstants.REGEX_URL, input);
  }
  /**
   * 判断是否匹配正则
   *
   * @param regex 正则表达式
   * @param input 要匹配的字符串
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isMatch(String regex, CharSequence input) {
    return input != null && input.length() > 0 && Pattern.matches(regex, input);
  }
}
