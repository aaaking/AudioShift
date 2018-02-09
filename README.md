# CoolDialog

 [简书地址](https://www.jianshu.com/p/a668576bd1af)
 
 最近完成了一个开源项目：CoolDialog，代码很少，只有两个kotlin类：`CoolDialog.kt`和`CoolStyle.kt`。

CoolDialog能够满足大部分样式需求，包括：
1. 自定义背景`withBgAndTopbg`
2. 自定义头部背景`withBgAndTopbg`
3. 自定义标题和icon `withTitle`、`withIcon`
4. 自定义“取消”和“确定”按钮`withNegativeBtn`、`withPositiveBtn`
5. 自定义对话框内容`withMsg`、`withMsgSub`、`withContentCustom`
6. 自定义对话框动画`withCustomAnim`
7. 自定义Gravity和左上右下的边距`show(gravity: Int = Gravity.CENTER, paddingLeft: Int = mPadding, paddingTop: Int = mPadding, paddingRight: Int = mPadding, paddingBottom: Int = mPadding)`

<p align="center">
 
 <img src ="https://upload-images.jianshu.io/upload_images/1323444-3143631c174c261e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700" />
 
 <img src ="https://upload-images.jianshu.io/upload_images/1323444-d48b94c4bdd13264.gif" />
 
 <p align="center"> coolAnim.gif </p>
 
</p>

### 使用
#### maven:
```
<dependency>
  <groupId>com.zzh.cooldialog</groupId>
  <artifactId>cooldialog</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

#### gradle:
```
compile 'com.zzh.cooldialog:cooldialog:1.0.0'
```


#### Ivy:
```
<dependency org='com.zzh.cooldialog' name='cooldialog' rev='1.0.0'>
  <artifact name='cooldialog' ext='pom' ></artifact>
</dependency>
```
