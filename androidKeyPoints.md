##Android Key Points

* android ant build cmd: 1.android list target 2.android create project --name %s --target android-%s --path %s --package %s --activity %s 3.vi project.properties 4.ant debug/ant release -f build.xml -Dkey.store=%s -Dkey.alias=%s -Dkey.store.password=%s -Dkey.alias.password=%s
* android ant build jar lib cmd: 1.android list target 2.android update project --target android-%s --path %s 3.ant release -Dandroid.library=true 4.bin/classes.jar
* activity launch 4 ways: `standard`/`singleTop`/`singleTask`/`singleInstance`
  * 默认，每次激活activity就会新建activity实例，放入任务栈/如果av实例在栈顶，直接启动，否则新建/只要栈内存在av实例，它上面的实例会被移出栈，重回栈顶/在一个新栈中创建av实例，让多应用共享该实例，相当于多应用共享一个应用，不管谁激活该av都会进入该应用
  * 如果希望多次调用只有一个av实例存在，launchMode="sigleTask" + onNewIntent(intent)，再次启动av时onNewIntent-->onRestart-->onStart-->onResume，在OnNewInent()中要setIntent(intent)，这样在getIntent()时才能得到新的intent
  * `Application``Task``Process`，在android中，一个应用是一组组件的集合；task是在程序运行时只针对activity的概念，task是一组相互关联的activity的集合，是framework层的一个概念，这个task存在于一个称为back stack的数据结构中，实现应用界面的跳转，也就是说framework层是以栈的形式管理用户开启的activity的，task是可以跨应用的，为了用户操作的连贯性；进程是操作系统内核的一个概念，在应用程序的角度看，android应用运行在dalvik（现已被ART取代）虚拟机中，可以认为一个运行中的dalvik虚拟机实例占有一个进程，默认情况下，一个应用程序的所有组件是运行在同一进程中，但也有例外，可以通过manifest中android:process指定进程名称
  * Android之taskAffinity，表示当前activity所在的任务，像身份证一样，从概念上讲，相同affinity的activity属于同一任务，默认情况下所有activity的affinity都是manifest中的包名，可以设置不同应用的affinity相同，运行时就会存在同一任务中，Task不仅可以跨应用，而且可以跨进程。启动activity会设置FLAG_ACTIVITY_NEW_TASK标志，走onNewIntent()
  * singleTask/singleInstance + affinity
    * singleTask：single in task，启动时framework层会标记为可在一个新任务中启动，但不一定
    * singleInstance：这个模式的activity总是会在新任务中运行(前提是系统还不存在这样一个实例)，正因为singleInstance的全局唯一性，被它启动的activity都会运行在其它任务中，启动时会加上FLAG_ACTIVITY_NEW_TASK标志，同singleTask
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

-------

* Android布局优化，目标高效且复用性高的UI，建议：尽量多使用RelativeLayout布局，减少嵌套复杂度；可复用的组件抽出用include标签；使用ViewStub标签加载一些不常用的布局，例如只有特殊情况下才显示的进度布局，网络失败刷新布局，信息出错的提示布局等；使用merge标签减少布局的嵌套层次，一是用于布局根节点是Framelayout，二是include时作为该布局的顶节点，这样引入时子节点就会自动合并到主布局


-------

* Service与Thread的关系，答案是没有任何关系。Thread是开启一个子线程，去执行一些耗时操作就不会影响主线程的运行，而Service的理解是执行一些后台任务，一些耗时操作也可以放在里面运行。但实际上Service是运行在主线程的，后台和子线程是不同的概念，后台的运行是完全不依赖UI的，那为什么不直接在activity中创建线程呢？那是因为activity很难对Thread进行控制，activity销毁后很难再获得之前的Thread实例，而且不同activity没法对同一个thread进行操作，但Service不同，所有activity都可以与Service关联，操作其中的方法，activity finish后只要在重新与Service建立连接，就能够重新获得之前的binder实例
* Service的启动方式，使用情况，结束方式
  * startService启动服务，结束用stopService，用于开启一个后台服务执行一个耗时任务，不需要通信
  * bindService启动服务，结束用unbindService或者调用的Context finish之后
  * start和bind同时启动，结束也要都调用stop和unbind，不分先后，这样activity就可以更新Service的状态
* android:process=""，可以设置服务是否运行在另外一个进程
* IntentService，异步处理服务，使用工作线程逐一处理所有启动要求，只需实现onHandleIntent()方法即可，该方法会接收每个启动请求的Intent，使您能执行后台工作，永远不必担心多线程问题
  * 不需要主动调用stopSelf()来结束服务，当所有intent被处理完后，系统会自动关闭服务
  * 默认实现onBind()返回null
  * 默认实现onStartCommand()的目的是将intent插入到工作队列中
  * 如果要重写其他回调方法(onCreate, onStartCommand, onDestroy)，请确保调用超类实现

-------

* 横竖屏切换时activity的生命周期
  * android:configChanges="orientation"或者不设，横切竖，竖切横，生命周期都会执行一遍，onPause-->onSaveInstanceState-->onStop-->onDestroy-->onCreate-->onStart-->onRestoreInstanceState-->onResume
  * >=API13, android:configChanges="orientation|keyboardHidden|screenSize"，不会重建activity，只会执行onConfigurationChanged；<API13，只需设置前两项

-------

* android序列化Serializable, Parcelable

-------

* android broadcastreceiver，使用观察者模式，基于消息的发布/订阅事件模型，广播的发布者和接收者解耦，类似的第三方lib EventBus
  * 普通广播、系统广播、有序广播、粘性广播（5.0 deprecated），Local Broadcast（应用内广播）
  * 静态注册的广播接收器即使app已经退出，仍然能够接收到，自Android3.1后可能不再成立，对于系统广播app退出后是无法接收到的，但对于自定义的广播，可以通过设置flag为FLAG_INCLUDE_STOPPED_PACKAGES，获得接收
  * LocalBroadcastManager方式发送的广播，只能通过LocalBroadcastManger注册的ContextReceiver才能接收，静态注册或者其他方式动态注册的ContextReceiver接收不到

-------

* Android的数据存储：`Preference(通常是键值对)``文件(手机设备或外设存储，缺省只能由创建它的应用访问)``数据库(如SQLite方式，由创建的应访问)``网络(Android提供API远程在服务器上存储数据)`

-------

* Android ListView优化：
  * 利用好convertView来重用view，ViewHolder，TAG必不可少
  * ItemView的Layout层次结构尽量简单，要善用自定义的View
  * 分页加载，分批加载
  * getView()要尽量做少的事情，不要有耗时操作，特别是滑动时不要加载图片，停下来再加载
  * 尽量避免在适配器中使用线程，因为周期不可控，涉及图片加载可以使用较成熟的第三方lib，比如Picasso, glide, ImageLoader, Volley
  * 为了满足需求必须使用Context的话，尽量使用Application Context，因为Application Context生命周期比较长，不易出现内存泄露
  * 尽量避免在BaseAdapter中使用static来定义全局静态变量
  * 使用RecycleView代替，RecycleView 在性能和可定制性上都有很大的改善，推荐使用

-------

* Android子线程中更新UI的方法：
  * 主线程定义Handler，子线程发送message，通知Handler完成UI更新
  * 子线程通过runOnUiThread更新，在非上下文类中，需要传递Activity对象
  * View.post(Runnable r)，需要传递更新的view过去
* Android线程间通信，进程间通信
  * AsyncTask， Handler，
  * IPC机制Binder
