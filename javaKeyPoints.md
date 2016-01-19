##JAVA Key Points

* 面向对象编程语言有封装、继承、抽象、多态4个主要的特征
* 接口：接口中可以包含变量和方法，但变量会被隐式地指定为且只能是public static final，方法则指定为且只能是public abstract。如果一个非抽象类遵循了该接口，则必须实现接口中所有的方法，而抽象类则可以不用实现
* 接口和抽象类的区别：
  * 接口是用来实现的implements，而抽象类是别继承的extends
  * 接口可以继承多个接口，组成一个新的接口
  * 抽象类可以继承一个别的类，或实现一个或多个接口
  * 接口是一个100%的抽象类；而抽象类中可以包含非抽象的方法实现
  * Java接口和Java抽象类的最大一个区别在于，Java抽象类可以提供某些方法的部分实现，而接口不行。抽象类的优点是当向一个抽象类中加入一个新的具体方法时，它的所有子类一下子都得到了这个新方法，但由于Java的单继承性，限制了它的能效，但恰恰体现了接口的优势，可以任意实现多个接口
  * 结合上述两者的优点，经典的设计模式-缺省适配模式就出来了：声明类型的工作仍然由Java接口承担，但是同时给出一个Java抽象类，且实现了这个接口，而其他同属于这个抽象类型的具体类可以选择实现这个Java接口，也可以选择继承这个抽象类。Java接口和Java抽象类的存在就是为了用于具体类的实现和继承的
* 重载Overload和重写Override
  * 重载是一个类中多态性的一种表现，是一个参数多态机制
  * 重写体现的是父类和子类之间的多态性，对父类的函数进行重新定义
* 单例设计模式(保证一个类在内存中的对象唯一)
```java
class Single{ //饿汉式
  private Single() {}
  private static Single s = new Single();
  public static Single getInstance() {
    return s;
  }
}
class Single2{ //懒汉式
  private Single2() {}
  private static Single2 s = null;
  public static Single2 getInstance() {
    if (s==null) s = new Single2();
    return s;
  }
}
```

*******

* &和&&的区别：都可以做逻辑与运算，&&具有短路功能，&还可以用作位运算符
* switch(expr)语句中的expr，只能是一个整数表达式或者枚举常量，long, float, String类型都不符合
* 2*8最快的计算方式：2<<3 一个数向左移位n位就等于乘以2的n次方
* 使用final关键字修饰一个变量时，是指的引用变量不能变，引用变量指向的对象中的内容还是可以改变的
* ==和equals的区别：equals比较的是两个对象中的内容是否相等；==比较的是两个变量的值是否相等，也就是比较变量所对应的内存中所存储的数值是否相同
* Integer和int的区别：int是Java提供的原始数据类型之一，Integer是对int的封装类(数据+操作)，int默认值是0，Integer默认是null
* 对两个变量的数据进行互换，不需要第三方变量
```java
int a = 3, b = 5;
//a = a + b;
//b = a - b;
//b = a - b;
a = a ^ b;
b = a ^ b;
a = a ^ b;
```

*******

* 多线程有两种实现方式：分别是继承Thread类和实现Runnable接口
* 同步的实现有两种：分别是synchronized，wait和notify
* 异常分为Error和Exception，Throwable是所有异常类的父类，Exception包含RuntimeException和RuntimeException之外的异常

*******

```
Collection
├List
│├LinkedList
│├ArrayList
│└Vector
│　└Stack
└Set
Map
├Hashtable
├HashMap
└WeakHashMap
```
* List是有序的Collection。LinkedList双向链表，允许null元素，插入删除速度快，非同步；ArrayList线性表，可变大小的数组，非同步；Vector类似ArrayList，同步的
* Set是不包含重复元素的Collection
* Map没有持续Collection接口，提供key-value映射。Hashtable是一个实现key-value映射的哈希表，不允许null元素，同步的；HashMap长短同步的，允许null
