package com.jihf.lib.utils.snackBar;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jihf.lib.R;

import static com.jihf.lib.utils.snackBar.SnackBarType.Confirm;
import static com.jihf.lib.utils.snackBar.SnackBarType.ErrorT;
import static com.jihf.lib.utils.snackBar.SnackBarType.Info;
import static com.jihf.lib.utils.snackBar.SnackBarType.Warning;

/**
 * Func：
 * User：jihf
 * Data：2016-12-20
 * Time: 10:30
 * Mail：jihaifeng@raiyi.com
 */
public class SnackBarUtils {

  private static int msgColor = Color.WHITE;

  private static Snackbar snackbar;

  /**
   * 创建短时间显示的snackBar
   *
   * @param view 显示snackBar依附的窗口
   * @param msg snackBar显示的内容
   *
   * @return snackBar
   */
  public static SnackBarUtils creatShort(View view, String msg) {
    return creatIndefinite(view, msg, Snackbar.LENGTH_SHORT);
  }

  /**
   * 创建长时间显示的nackBar
   *
   * @param view 显示snackBar依附的窗口
   * @param msg snackBar显示的内容
   *
   * @return snackBar
   */
  public static SnackBarUtils creatLong(View view, String msg) {
    return creatIndefinite(view, msg, Snackbar.LENGTH_LONG);
  }

  /**
   * 创建自定义时间时间显示的nackBar
   *
   * @param view 显示snackBar依附的窗口
   * @param msg snackBar显示的内容
   *
   * @return snackBar
   */
  public static SnackBarUtils creatIndefinite(View view, String msg, int duration) {
    if (null == view || TextUtils.isEmpty(msg)) {
      throw new NullPointerException("view or msg is null!");
    }
    snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE).setDuration(duration);
    return new SnackBarUtils();
  }

  /**
   * 创建长时间显示的nackBar
   *
   * @param messageColor snackBar显示的文字颜色
   *
   * @return snackBar
   */
  public SnackBarUtils setMsgColor(int messageColor) {
    return setColor(messageColor, 0);
  }

  /**
   * 创建长时间显示的nackBar
   *
   * @param backgroundColor snackBar背景颜色
   *
   * @return snackBar
   */
  public SnackBarUtils setBgColor(int backgroundColor) {
    return setColor(0, backgroundColor);
  }

  /**
   * 创建长时间显示的nackBar
   *
   * @param messageColor snackBar显示的文字颜色
   * @param backgroundColor snackBar背景颜色
   *
   * @return snackBar
   */
  public SnackBarUtils setColor(int messageColor, int backgroundColor) {
    if (null == snackbar) {
      throw new NullPointerException("the snackBar is null!");
    }
    View view = snackbar.getView();
    if (0 != backgroundColor) {
      view.setBackgroundColor(backgroundColor);
    }
    if (0 != messageColor) {
      ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
    }

    msgColor = messageColor;
    return this;
  }

  /**
   * 创建长时间显示的nackBar
   *
   * @param maxLines snackBar显示的文字最大行数
   *
   * @return snackBar
   */
  public SnackBarUtils setMsgMaxLines(int maxLines) {
    View view = snackbar.getView();
    ((TextView) view.findViewById(R.id.snackbar_text)).setMaxLines(5);
    return this;
  }

  /**
   * 设置snackBar类型
   *
   * @param type 预设类型的snackBar
   *
   * @return snackBar
   */
  public SnackBarUtils setType(@SnackBarType int type) {
    if (null == snackbar) {
      throw new NullPointerException("the snackBar is null!");
    }
    switch (type) {
      case Info:
        int blue = 0xff2195f3;
        setBgColor(blue);
        break;
      case Confirm:
        int green = 0xff4caf50;
        setBgColor(green);
        break;
      case Warning:
        int orange = 0xffffc107;
        setBgColor(orange);
        break;
      case ErrorT:
        int red = 0xfff44336;
        setColor(Color.YELLOW, red);
        break;
    }
    return this;
  }

  /**
   * 设置msg文字大小
   *
   * @param size 文字大小
   *
   * @return SnackBarUtils
   */
  public SnackBarUtils setMsgSize(float size) {
    View view = snackbar.getView();
    ((TextView) view.findViewById(R.id.snackbar_text)).setTextSize(size);
    return this;
  }

  /**
   * 向Snackbar中添加view
   *
   * @param layoutId 新加布局
   * @param index 新加布局在Snackbar中的位置
   */
  public SnackBarUtils addView(int layoutId, int index) {
    if (null == snackbar) {
      throw new NullPointerException("the snackBar is null!");
    }
    View snackBarView = snackbar.getView();
    Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackBarView;

    View add_view = LayoutInflater.from(snackBarView.getContext()).inflate(layoutId, null);

    LinearLayout.LayoutParams p =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    p.gravity = Gravity.CENTER_VERTICAL;
    snackBarLayout.addView(add_view, index, p);
    return this;
  }

  /**
   * 设置action
   *
   * @param actionStr action的文字
   * @param listener action事件
   *
   * @return SnackBarUtils
   */
  public SnackBarUtils setAction(String actionStr, View.OnClickListener listener) {
    if (null == snackbar) {
      throw new NullPointerException("the snackBar is null!");
    }
    snackbar.setAction(actionStr, listener);
    return this;
  }

  /**
   * 设置action的文字颜色
   *
   * @param textColor action的文字颜色
   *
   * @return SnackBarUtils
   */
  public SnackBarUtils setActionTextColor(int textColor) {
    int actionTextColor = msgColor;
    if (0 != actionTextColor) {
      actionTextColor = textColor;
    }
    snackbar.setActionTextColor(actionTextColor);
    return this;
  }

  /**
   * 设置action的文字大小
   *
   * @param size action的文字大小
   *
   * @return SnackBarUtils
   */
  public SnackBarUtils setActionTextSize(float size) {
    Button button = (Button) snackbar.getView().findViewById(R.id.snackbar_action);
    button.setTextSize(size);
    return this;
  }

  /**
   * snackbar显示
   */
  public void show() {
    if (null == snackbar) {
      throw new NullPointerException("the snackBar is null!");
    }
    snackbar.show();
  }

  /**
   * snackbar关闭
   */
  public static void dismiss() {
    if (null == snackbar) {
      throw new NullPointerException("the snackBar is null!");
    }
    if (snackbar.isShown()) {
      snackbar.dismiss();
    }
  }
}
