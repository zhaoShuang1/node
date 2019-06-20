### Java 中的四种引用
- 强引用
	- 直接通过new关键字创建一个对象，如果说一个对象具有强引用即使发生OOM这个对象也不会被回收。
		```
			Object obj = new Object();
		
		```
		
- 软引用
	- 当内存不足的时候，垃圾回收器会对其进行回收，软引用通常被用作内存敏感的缓存。
	```
		SoftReference<Object> softReference = new SoftReference<Object>(new Object());
		Object obj = softReference.get();
	```
- 弱引用
	- 每次进行垃圾回收时都会回收弱引用对象，大多用于缓存。
	```
		WeakReference<Object> weakReference = new WeakReference<Object>(new Object());
		Object obj = weakReference.get();
	```
	- 弱引用可以和ReferenceQueue联合使用，当弱应用被回收时jvm就会把这个弱引用对象放入与之关联的队列中去，可以用来跟踪对象的生命周期。
	```
		ReferenceQueue<Object> queue = new ReferenceQueue<>();
		WeakReference<Object> weakReference = new WeakReference<Object>(new Object(),queue);
		Object obj = weakReference.get();
		System.gc();
		//这个方法会被阻塞直到对象被回收
		Reference<? extends Object> reference = queue.remove();
		Object object = reference.get();
	
	```
- 虚引用
	- 虚引用相当于没有引用，在任何时候都可能会被垃圾回收器回收。必须要配合ReferenceQueue联合使用，常用于对象完成的一些清理操作，可以参考Apache 的common-io 中的org.apache.commons.io.FileCleaningTracker 类中的实现。
	```
		ReferenceQueue<Object> queue = new ReferenceQueue<>();
		PhantomReference<Object> phantomReference = new PhantomReference<Object>(new Object(), queue);
		
		//不可被获取，永远是null
		Object object = phantomReference.get();
		System.out.println(object);
	
	```
	
	