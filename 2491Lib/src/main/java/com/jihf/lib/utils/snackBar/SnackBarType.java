package com.jihf.lib.utils.snackBar;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jihf.lib.utils.snackBar.SnackBarType.Confirm;
import static com.jihf.lib.utils.snackBar.SnackBarType.ErrorT;
import static com.jihf.lib.utils.snackBar.SnackBarType.Info;
import static com.jihf.lib.utils.snackBar.SnackBarType.Warning;

/**
 * Func：
 * Desc:
 * Author：jihf
 * Data：2017-03-27 14:18
 * Mail：jihaifeng@raiyi.com
 */
@IntDef ({ Info, Confirm, Warning, ErrorT }) // 枚举数据
@Retention (RetentionPolicy.SOURCE) //告诉编译器在生成.class文件时不保留枚举注解数据
public @interface SnackBarType {
  int Info = 1;
  int Confirm = 2;
  int Warning = 3;
  int ErrorT = 4;
}
