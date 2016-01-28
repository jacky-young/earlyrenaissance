##Android Key Points

* android ant build cmd: 1.android list target 2.android create project --name %s --target android-%s --path %s --package %s --activity %s 3.vi project.properties 4.ant debug/ant release -f build.xml -Dkey.store=%s -Dkey.alias=%s -Dkey.store.password=%s -Dkey.alias.password=%s
* android ant build jar lib cmd: 1.android list target 2.android update project --target android-%s --path %s 3.ant release -Dandroid.library=true 4.bin/classes.jar
* android packaging proces：1.先编译java成class(包括生成R.java, aapt命令; 编译class, javac命令) 2.再把class和jar转化成dex(dx命令) 3.接着打包assets和res等资源文件为res.zip(aapt命令) 4.然后把dex和res.zip合并成一个未签名的apk(apkbuilder命令) 5.签名(jarsigner命令) 最终就是一个带签名的apk文件
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
* 几种屏幕适配方案
  * 通用方式是为每种dpi出一套图片资源，缺点增加设计工作量，使apk包变大
  * 基于现在主流apk的分辨率，xhdpi（dpi=320））规格的图片成为首选，当资源紧张时考虑
  * 根据设计图尺寸，将设计图上标识的px尺寸，转化为百分比，为所有主流屏幕生成对应百分比的值，每个尺寸都会有一个values文件夹。缺点产生大量文件夹，适配不了特殊尺寸。例如以480x320的分辨率为基准，将任何分辨率宽度分为320份，x1-x320，x1=1px，同理高度y1-y480，这样800x480的宽度x1=1.5px
  * 使用Android百分比布局库（percent-support-lib)及其它类似实现
* 几种Android UI布局优化，制作出高效且复用性高的UI
  * 尽量多使用RelativeLayout和LinearLayout, 不要使用绝对布局AbsoluteLayout，在布局层次一样的情况下， 建议使用LinearLayout代替RelativeLayout, 因为LinearLayout性能要稍高一点，但往往RelativeLayout可以简单实现LinearLayout嵌套才能实现的布局
  * 将可复用的组件抽取出来并通过include标签使用
  * 使用ViewStub标签来加载一些不常用的布局，常用来引入那些默认不会显示，只在特殊情况下显示的布局，如进度布局、网络失败显示的刷新布局、信息出错出现的提示布局等
  * 使用merge标签减少布局的嵌套层次，作用是合并UI布局，使用该标签能降低UI布局的嵌套层次

-------

* 回调，接口，反射

-------

* Android异步消息处理机制`Handler``Looper``Messge`异步消息处理线程启动后会进入一个无限循环体中，每循环一次，从内部的消息队列中取出一个消息，然后回调相应的消息处理函数，执行完一个消息后则继续循环。若消息队列为空，线程会堵塞等待
  * Looper主要是prepare和loop两个方法，prepare绑定当前线程，保证一个线程只有一个Looper实例，且一个Looper实例只有一个MessageQueue实例，loop不断从MessageQueue取消息，交给消息的target属性的dispatchMessage去处理
  * Handler的构造方法，会首先保存当前线程中的Looper实例，进而与MessageQueue实例相关联，Handler的sendMessage方法，会把msg的target属性赋值handler自身，放入MessageQueue中。在构造Handler时，我们会复写handleMessage方法，也就是msg.target.dispatchMessage(msg)最终会调用的方法。在Activity的启动过程中，会在当前的UI线程中调用Looper的prepare和loop方法
  * Handler的post(Runnable r)方法，r中可以更新UI，其实并没有创建什么线程，只是发送了一个message，r最为msg的callback属性回调。Message m = Message.obtain()
* AsyncTask，轻量级的android异步类，内部是一个线程池，每个后台任务交给线程池中的线程去完成，抽象出了一个后台任务的5种状态，对应5个回调接口方便需求实现，线程池最多128个工作线程，5个核心工作线程。缺点：AsyncTask可能存在新开大量线程消耗系统资源和导致应用FC的风险，当线程池已满，缓冲队列已满时，再向线程提交任务会导致RejectedExecutionException。解决：由一个控制线程来处理AsyncTask的调用判断线程池是否满了，如果满了则线程睡眠否则请求AsyncTask继续处理
* AsyncTask与Thread对比：如果是简单的操作UI或者耗时不多且较简单的任务，应该选用AsyncTask，如果用来处理数据不参与界面操作时，应该使用Thrad，为了方便管理大量繁重的Thread任务，应该引入线程池ThreadPoolExecutor，统一分配、调度、管理，

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
  * Serializable只需要实现当前接口，不需要重写任何方法，比较简单，适合本地长时间存储，Parcelable效率比Serializable高，内存存储，适合应用内及应用之间传递数据，需要实现writeToParcel、describeContents函数以及静态的CREATOR变量，实际上就是将如何打包和解包的工作自己来定义，而序列化的这些操作完全由底层实现

-------

* android broadcastreceiver，使用观察者模式，基于消息的发布/订阅事件模型，广播的发布者和接收者解耦，类似的第三方lib EventBus Otto
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
  * AsyncTask， Handler，synchronized机制，当两个并发程序访问同一个object的synchronized(this)同步代码时，一个时间内只有一个线程得到执行，另一个线程必须等到当前线程执行完代码后才能执行
  * IPC机制Binder，通过AIDL（Android Interface Definition Language）接口文件
-------

* Android处理崩溃异常：
  * 继承Thread.UncaughtExceptionHandler, 重写uncaughtException()方法，退出程序，收集信息，保存log文件
  * 自定义Application，注册未捕获异常处理器
  * 异常退出后提示用户是否上传log信息，可以通过service实现
* App调试的几个命令：
  * logcat：logcat -v time *:E
  * bugreport：存储开机之后详细的dumpsys,dumpstate和logcat信息，是一份完整的日志记录
  * dumpsys：查看系统信息
  * top：cpu信息
  * 配置文件local.prop：可用于调试数据库

-------

* Intent是Android系统各组件之间进行数据传递的数据负载者，当我们需要做一个调用动作，就可以通过Intent告诉Android系统来完成这个过程。Intent有几个重要的属性
  * action：要执行的动作，<intent-filter>中声明<action>，new Intent(Action action)中指定目标action。除自定义action外，Intent中内含许多默认的action，如ACTION_MAIN，ACTION_VIEW，ACTION_WEB_SEARCH
  * data和extras：执行动作操作的数据和传递到目标的附加信息，为intent指定一个data属性，其实就是指定要操作的数据，要符合<data/>元素的匹配规则；extras涉及到bundle对象，bundle主要负责为intent保存附加参数信息，它实现了Paracelable接口，内部维护一个Map类型的属性，以键值对的方式存放参数信息，通过intent.putExtras()/getExtras()存取bundle实例
  * category：要执行动作的目标所具有的特质或行为归类，几种常见的category，如Intent.LAUNCHER，Intent.CATEGORY_DEFAULT，Intent.CATEGORY_PREFERENCE，Intent.CATEGORY_BROWSABLE
  * type：要执行动作的目标activity所能处理的MIME数据类型，例如<data android:mimeType="image/*">
  * component：目标组件的包名或类名，值得注意的是，如果在intent中指定了component属性，系统将不会再对action、data/type、category进行匹配。e.g. intent.setComponent(new ComponentName(getApplicationContext(), TargetActivity.class))

-------

* Android手机自动化测试工具
  * Monkey：Android SDK自带的测试工具，测试过程中向系统发送伪随机的用户事件流，一般用于对应用程序的压力测试
  * MonkeyRunner：Android SDK提供的测试工具，其实是一套API工具包，使用python编写测试脚本定义数据，事件
  * Instrumentation：Google提供的Android自动化测试工具类，结合JUnit，允许对应用程序做更复杂的测试，甚至是框架层面。Instrumentation是通过把主程序和测试程序运行在同一进程中来实现功能，可以将其看作是类似activity或者service并且不带界面的组件，在程序运行期间监控你的主程序
  * Espresso：Google的开源自动化测试框架，testing UI for a single app，其测试框架基于instrumentation，不能跨App，其特点是规模小，更简洁，API更加准确
  * UiAutomator：Android提供的自动化测试框架，基本上支持所有的Android事件操作，不需要测试人员了解代码实现细节，测试代码结构简单，可以跨App，缺点只支持Android 4.1+。另有基于此的开源python框架uiautomator实现
  * 其它的测试框架：Selendroid，Robotium，Appium，商业工具如testin

-------

* RESTful(Representational State Transfer): 面向资源，是一种架构，不是协议，只要Web Service能够满足REST的几个条件，通常就称这个系统是RESTful的。条件包括C/S结构，无状态，可以cache，分层系统，统一接口，与HTTP协议近似，三个要素是唯一的资源标识, 简单的方法(此处的方法是个抽象的概念), 一定的表达方式.
* RPC(Remote procedure call): 所谓的远程过程调用（面向方法），像调用本地服务(方法)一样调用服务器的服务(方法)。通常实现由XML-RPC， JSON-RPC。记住一点RPC是以动词为中心的, REST是以名词为中心的。

-------

* 图像占用内存的公式是：numBytes = width * height * bitsPerPixel / 8。OpenGL ES纹理的宽和高都要是2次幂数，所以start.png本身是480x480，载入内存后就会变成512x512的纹理，对于每一个像素点使用4byte表示--1个byte（8位）代表red，另外3个byte分别表示green、blue、alpha，这个简称为RGBA8888。一个512x512的图片耗费内存为512x512x4=1MB
* android退出程序往往会调用以下方法：
  * 关闭掉所有界面
  * 干掉当前进程，android.os.Process.killProcess(android.os.Process.myPid());
  * 结束JVM，System.exit(0);
* Context.MODE_PRIVATE: 默认操作模式，代表文件是私有数据，只能被应用本身访问，该模式下写入的内容会覆盖原有内容
* Context.MODE_APPEND: 模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件
* Context.MODE_WORLD_READABLE：表示当前文件可以被其他应用读取 MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入

-------

* Android实现Tab类型的界面方式有如下几种：
  * 传统的ViewPager实现
  * FragmentManager+Fragment实现
  * ViewPager+FragmentPagerAdapter实现
  * TabPageIndicator+ViewPager+FragmentPagerAdapter实现
* Fragment比Activity多了几个额外的生命周期，onAttach-->onCreate-->onCreateView-->onActivityCreated-->onStart-->onResume-->onPause-->onStop-->onDestoryView()-->onDestory-->onDetach，FragmentManager=getFragmentManager()，FragmentTransaction transaction=fm.benginTransatcion(), transaction.add/remove/replace/hide/show/commit/addToBackStack
* Fragment与Activity通信
  * 如果你Activity中包含自己管理的Fragment的引用，可以通过引用直接访问所有的Fragment的public方法
  * 如果Activity中未保存任何Fragment的引用，那么没关系，每个Fragment都有一个唯一的TAG或者ID,可以通过getFragmentManager.findFragmentByTag()或者findFragmentById()获得任何Fragment实例，然后进行操作
  * 在Fragment中可以通过getActivity得到当前绑定的Activity的实例，然后进行操作
  * 声明接口，处理回调
* 屏幕旋转时防止Fragment重叠，加入savedInstanceState==null判断即可

-------

* 提高Android程序运行效率的方法：
  * 降低执行时间，使用缓存（图片，listview，网络，IO，layout）；优化数据存储，选择合适的数据类型和数据结构；算法优化；JNI（Java Native Interface）；逻辑优化
  * 异步，利用多线程
  * 提前或延迟操作
  * 网络优化
