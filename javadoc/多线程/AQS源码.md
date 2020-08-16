## AQS源码

核心是一个`volatile`修饰的`state`标志位

 内部维护一个队列，这个队列是一个双向链表，链表的每一个结点都是一个线程。



## ThreadLocal

当前线程独占空间

* `set`
  * `Thread.currentThread.map(ThreadLocal, person)`
  * 设到了当前线程的map中 key是`ThreadLocal` value是类型参数
* `ThreadLocal`用途
  * 声明式事务，保证同一个`Connection`

## Java对象引用

* 强：强引用(`new`出来的)
* 软：软引用`SoftReference` ，GC的时候如果发现空间不足则会回收软引用 主要用于缓存
* 弱：弱引用(`WeakReference`)，只要GC发现弱引用就会回收。 一般用在容器(`WeakHashMap`)，`ThreadLocal`就是弱引用
* 虚：管理堆外内存(`PhantomReference`)

## 容器

* `ConcurrentHashMap` 写的效率相对低，读的效率很高
* `ConcurrentLinkedQueue` 多线程环境下 单个元素多考虑用`Queue`，少考虑`List`
* `ConcurrentSkipListMap` 基于跳表的`map`

## 队列

