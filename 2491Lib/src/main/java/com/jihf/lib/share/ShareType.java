package com.jihf.lib.share;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jihf.lib.share.ShareType.QQ;
import static com.jihf.lib.share.ShareType.QZONE;
import static com.jihf.lib.share.ShareType.WX_CIRCLE;
import static com.jihf.lib.share.ShareType.WX_FRIEND;

@IntDef ({ QQ, QZONE ,WX_FRIEND, WX_CIRCLE }) // 枚举数据
@Retention (RetentionPolicy.SOURCE) //告诉编译器在生成.class文件时不保留枚举注解数据
public @interface ShareType {
  int QQ = 1;
  int QZONE = 2;
  int WX_FRIEND = 3;
  int WX_CIRCLE = 4;
}