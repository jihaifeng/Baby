### 项目使用教程：
1. 新建空项目并将该项目引用为lib工程；
2. 在项目主工程的gradle文件下加入以下代码,引入lambda以及maven仓库：
```
buildscript {

   ...
  dependencies {
     ...
     // 添加这行
    classpath 'me.tatarka:gradle-retrolambda:3.4.0'
    
  }
}
  allprojects {
    repositories {
      ...
      // 添加这行
      maven { url "https://jitpack.io" }
    }
  }
```
3. 自定义主工程的application类集成自BaseApplication,并实现相关方法;
  - 在抽象方法initUrl，initShare和initUmeng中设置自己的相关配置，如不需要，返回null即可；
  - baseApplication中还提供了自定义页面数据的setPageNum和设置userAgent的setUa方法可供使用；
4. 将以下代码复制到配置文件manifest中并修改包名：
```
 <manifest xmlns:android="http://schemas.android.com/apk/res/android"
     package="你的包名"
     >
      <!--package是你项目的包名-->
      <!--name是你的自定义Application继承自BaseApplication-->
   <application
       android:name="你的自定义Application继承自BaseApplication"
       android:icon="@mipmap/ic_launcher"
       android:label="@string/app_name"
       android:supportsRtl="true"
       android:theme="@style/AppLibTheme"
       >
    <!--微信回调-->
     <activity
         android:name=".wxapi.WXEntryActivity"
         android:exported="true"
         />
   </application>
 
 </manifest>

```
5. 将wxapi文件夹复制到主工程包名目录下，接受微信分享回调
6. 最后设置logo和splash图片，ic_launcher，ic_splash