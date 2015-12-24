##Android Key Points

* android ant build cmd: 1.android list target 2.android create project --name %s --target android-%s --path %s --package %s --activity %s 3.vi project.properties 4.ant debug/ant release -f build.xml -Dkey.store=%s -Dkey.alias=%s -Dkey.store.password=%s -Dkey.alias.password=%s
* android ant build jar lib cmd: 1.android list target 2.android update project --target android-%s --path %s 3.ant release -Dandroid.library=true 4.bin/classes.jar
* activity launch 4 ways: `standard`/`singleTop`/`singleTask`/`singleInstance`
  * 默认，每次激活activity就会新建activity实例，放入任务栈/如果av实例在栈顶，直接启动，否则新建/只要栈内存在av实例，它上面的实例会被移出栈，重回栈顶/在一个新栈中创建av实例，让多应用共享该实例，相当于多应用共享一个应用，不管谁激活该av都会进入该应用
  * 如果希望多次调用只有一个av实例存在，launchMode="sigleTask" + onNewIntent(intent)，再次启动av时onNewIntent-->onRestart-->onStart-->onResume，在OnNewInent()中要setIntent(intent)，这样在getIntent()时才能得到新的intent
* onCreate-->onStart-->onResume-->Running-->onPause-->onStop-->onDestroy, av可见区onResume-->Running-->onPause
* android应用程序的生命周期，系统根据进程内运行的组件和这些组件的状态，把这些进程分为前端进程，可视进程，服务进程，后台进程，空进程

-------

* android屏幕适配，屏幕尺寸即对角线长度(英寸1inch=2.54cm)/屏幕密度density=(dpi/160)/分辨率即总像素数(width*height)/dpi即每英寸点数/dp设备独立像素，在屏幕密度=160dpi时，1dp=1px，px=dp*(dpi/160)/sp同dp，用来设置字体
* drawable-mdpi(dpi=160 density=1) drawable-xhdpi (dpi=320 density=2) drawable-xxhdpi (dpi=480 density=3)，资源紧张时可以考虑只用xhdpi，wrap_content，自动渲染
* 另一个android屏幕适配方案，基准百分比的方式布局，google支持库android-percent-support-lib

-------

* 回调，接口，反射

-------

* Android异步消息处理机制`Handler``Looper``Messge`异步消息处理线程启动后会进入一个无限循环体中，每循环一次，从内部的消息队列中取出一个消息，然后回调相应的消息处理函数，执行完一个消息后则继续循环。若消息队列为空，线程会堵塞等待
  * Looper主要是prepare和loop两个方法，prepare绑定当前线程，保证一个线程只有一个Looper实例，且一个Looper实例只有一个MessageQueue实例，loop不断从MessageQueue取消息，交给消息的target属性的dispatchMessage去处理
  * Handler的构造方法，会首先保存当前线程中的Looper实例，进而与MessageQueue实例相关联，Handler的sendMessage方法，会把msg的target属性赋值handler自身，放入MessageQueue中。在构造Handler时，我们会复写handleMessage方法，也就是msg.target.dispatchMessage(msg)最终会调用的方法。在Activity的启动过程中，会在当前的UI线程中调用Looper的prepare和loop方法
  * Handler的post(Runnable r)方法，r中可以更新UI，其实并没有创建什么线程，只是发送了一个message，r最为msg的callback属性回调。Message m = Message.obtain()
* AsyncTask，轻量级的android异步类，内部是一个线程池，每个后台任务交给线程池中的线程去完成，抽象出了一个后台任务的5种状态，对应5个回调接口方便需求实现，线程池最多128个工作线程，5个核心工作线程。缺点：AsyncTask可能存在新开大量线程消耗系统资源和导致应用FC的风险，当线程池已满，缓冲队列已满时，再向线程提交任务会导致RejectedExecutionException。解决：由一个控制线程来处理AsyncTask的调用判断线程池是否满了，如果满了则线程睡眠否则请求AsyncTask继续处理

-------

* `Android单元测试``Android依赖管理``公共库选型(依赖注入，ORM，网络类)``渠道打包``开发效率`
  * Android Http请求API: JAVA的HttpURLConnection, Apache的HttpClient，2.3之后推荐使用前者，默认开启gzip，connection keepAlive
  * 第三方的Http网络库Volley，OKHttp，Retrofit，客户端与服务器交互的数据格式主流是json，推荐使用google-gson
