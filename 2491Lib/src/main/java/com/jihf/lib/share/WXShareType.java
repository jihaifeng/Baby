package com.jihf.lib.share;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jihf.lib.share.WXShareType.WX_CIRCLE;
import static com.jihf.lib.share.WXShareType.WX_FRIEND;

@IntDef ({ WX_FRIEND, WX_CIRCLE }) // 枚举数据
@Retention (RetentionPolicy.SOURCE) //告诉编译器在生成.class文件时不保留枚举注解数据
public @interface WXShareType {
  int WX_FRIEND = 1;
  int WX_CIRCLE = 2;
}