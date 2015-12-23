##Android Key Points

* android ant build cmd: 1.android list target 2.android create project --name %s --target android-%s --path %s --package %s --activity %s 3.vi project.properties 4.ant debug/ant release -f build.xml -Dkey.store=%s -Dkey.alias=%s -Dkey.store.password=%s -Dkey.alias.password=%s
* android ant build jar lib cmd: 1.android list target 2.android update project --target android-%s --path %s 3.ant release -Dandroid.library=true 4.bin/classes.jar
* activity launch 4 ways: `standard`/`singleTop`/`singleTask`/`singleInstance`
  * 默认，每次激活activity就会新建activity实例，放入任务栈/如果av实例在栈顶，直接启动，否则新建/只要栈内存在av实例，它上面的实例会被移出栈，重回栈顶/在一个新栈中创建av实例，让多应用共享该实例，相当于多应用共享一个应用，不管谁激活该av都会进入该应用
  * 如果希望多次调用只有一个av实例存在，launchMode="sigleTask" + onNewIntent(intent)，再次启动av时onNewIntent-->onRestart-->onStart-->onResume，在OnNewInent()中要setIntent(intent)，这样在getIntent()时才能得到新的intent
* onCreate-->onStart-->onResume-->Running-->onPause-->onStop-->onDestroy, av可见区onResume-->Running-->onPause
* android应用程序的生命周期，系统根据进程内运行的组件和这些组件的状态，把这些进程分为前端进程，可视进程，服务进程，后台进程，空进程

------

* android屏幕适配，屏幕尺寸即对角线长度(英寸1inch=2.54cm)/屏幕密度density=(dpi/160)/分辨率即总像素数(width*height)/dpi即每英寸点数/dp设备独立像素，在屏幕密度=160dpi时，1dp=1px，px=dp*(dpi/160)/sp同dp，用来设置字体
* drawable-mdpi(dpi=160 density=1) drawable-xhdpi (dpi=320 density=2) drawable-xxhdpi (dpi=480 density=3)，资源紧张时可以考虑只用xhdpi，wrap_content，自动渲染
* 另一个android屏幕适配方案，基准百分比的方式布局，google支持库android-percent-support-lib

-------

* 回调，接口，反射
