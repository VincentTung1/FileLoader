# FileLoader
安卓文件浏览器(基于腾讯x5内核支持office类型文件浏览，另内置自定义图片浏览器，pdf文件浏览器和zip(rar)压缩包文件预览支持！

特性:
1.自定义文件浏览器支持侧滑关闭界面！
2.图片浏览器支持下拉关闭界面！


gradle使用方法:

android {
     defaultConfig {
       ndk{abiFilters "armeabi"}   //必须加上此配置，否则会造成x5内核初始化失败
     }
}

dependencies {
  implementation 'com.github.VincentTung1:FileLoader:-SNAPSHOT'
}
