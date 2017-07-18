package com.jihf.lib.share;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jihf.lib.share.QQShareType.QQ;
import static com.jihf.lib.share.QQShareType.QZONE;

@IntDef ({ QQ, QZONE }) // 枚举数据
@Retention (RetentionPolicy.SOURCE) //告诉编译器在生成.class文件时不保留枚举注解数据
public @interface QQShareType {
  int QQ = 1;
  int QZONE = 2;
}