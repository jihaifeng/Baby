package com.jihf.lib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import java.util.Stack;

/**
 * Func：
 * User：jihf
 * Data：2016-12-15
 * Time: 15:39
 * Mail：jihaifeng@raiyi.com
 */
public class ActivityUtils {
  private static ActivityUtils instance;
  //Activity栈
  private Stack<Activity> activityStack = new Stack<>();

  public synchronized static ActivityUtils getInstance() {
    if (null == instance) {
      synchronized (ActivityUtils.class) {
        if (null == instance) {
          instance = new ActivityUtils();
        }
      }
    }
    return instance;
  }

  /**
   * 添加activity,入栈
   * @param activity activity
   */
  public void addActivity(Activity activity) {
    if (null != activity) {
      activityStack.add(activity);
    }
  }

  /**
   * 移除activity，出栈
   * @param activity activity
   */
  public void removeActivity(Activity activity) {
    if (null != activity && activityStack.contains(activity)) {
      activityStack.remove(activity);
    }
  }

  /**
   * 获取当前Activity（栈顶Activity）
   *
   * @return activity
   */
  public Activity getCurrentActivity() {
    return activityStack.lastElement();
  }

  /**
   * 结束当前activity,栈顶activity
   */
  public void finishCurrentActivity() {
    Activity activity = activityStack.lastElement();
    finishActivity(activity);
  }

  /**
   * 结束指定的Activity
   * @param activity activity
   */
  public void finishActivity(Activity activity) {
    if (null != activity) {
      activityStack.remove(activity);
      activity.finish();
      activity = null;
    }
  }

  /**
   * 结束指定类名的Activity
   * @param cls class
   */
  public void finishActivityByName(Class<?> cls) {
    for (Activity activity : activityStack) {
      if (activity.getClass().equals(cls)) {
        finishActivity(activity);
      }
    }
  }

  /**
   * 结束所有Activity
   */
  public void finishAllActivity() {
    for (Activity activity : activityStack) {
      finishActivity(activity);
    }
    activityStack.clear();
  }

  /**
   * 获取当前activity数量
   * @return int
   */
  public int getActivityNum() {
    if (null != activityStack) {
      return activityStack.size();
    }
    return 0;
  }

  /**
   *   退出应用
   * @param context 上下文
   */
  public void AppExit(Context context) {
    try {
      finishAllActivity();
      ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
      activityMgr.killBackgroundProcesses(context.getPackageName());
      System.exit(0);
      Process.killProcess(Process.myPid());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
