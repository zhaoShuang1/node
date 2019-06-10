## jvm存活对象分析
### 引用计数法
- 引用计数法的逻辑是：在堆中存储对象时，在对象头处维护一个counter计数器，如果一个对象增加了一个引用与之相连，则将counter++。如果一个引用关系失效则counter–。
如果一个对象的counter变为0，则说明该对象已经被废弃，不处于存活状态。 
- 当对象没有外部对其进行关联时，自己相互关联则不会被标记为废弃状态。下边这种情况o1、o2 不会被标记为废弃状态。
```
		Object o1 = new Object();
		Object o2 = new Object();
		o1.obj=o2;
		o2.obj=o1;
		
		o1=null;
		o2=null;
```


### 可达性分析
- 这个算法的基本思路就是通过一系列名为GC Roots的对象作为起始点，从这些节点开始向下搜索，
	 搜索所走过的路径称为引用链(Reference Chain)，当一个对象到GC Roots没有任何引用链相连时，
	 则证明此对象是不可用的，下图对象object5, object6, object57虽然有互相判断，但它们到GC Roots是不可达的，
	 所以它们将会判定为是可回收对象。
	 
	![alt](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559643920837&di=de79a2e216ad257514de12452c2ce2ab&imgtype=0&src=http%3A%2F%2Fss.csdn.net%2Fp%3Fhttps%3A%2F%2Fmmbiz.qpic.cn%2Fmmbiz_png%2FWGyNiboAjLV5WtT7YvpoKEmVwt1O82dK12ibqibytHOibepMAt5F5V21pIVhwV6beHsib7jic1JO1VrhBco6FpzRkERQ%2F640%3Fwx_fmt%3Dpng)
	
- 如下情况的对象可以作为GC Roots：
	- 虚拟机栈(栈桢中的本地变量表)中的引用的对象
	- 方法区中的类静态属性引用的对象
	- 方法区中的常量引用的对象
	- 本地方法栈中JNI（Native方法）的引用的对象
	
	
- 在从gc root向下查找引用链时，可作为GC ROOT的节点主要在全局性引用（常量、静态变量）和执行上下文（栈帧中的本地变量表），通常方法区就有数百兆，逐个检查消耗会很大
	在查找引用链过程中，需要保证引用链的一致性，即在分析过程中对象的引用关系不能再变化，否则分析准确性则无法得到保证
　	因此通常GC执行时会stop the world，停止所有执行线程，即使几乎不发生停顿的CMS收集器中，枚举根节点也是需要停顿的。
- OopMap：在HotSpot中，使用的是一种称为OopMap的数据结构来存储对象内什么偏移量存储的是什么类型的数据的映射关系，在JIT编译
　 	过程中，也会在特定位置记录下栈和寄存器中的那些位置和引用的，这样GC在扫描时就可以直接获得这些信息。
	
- 实际上，HotSpot并没有为每一个对象都创建OopMap，只在特定的位置上创建了这些信息，这些位置称为安全点（Safepoints）。
为了保证虚拟机中安全点的个数不算太多也不是太少，主要决定安全点是否被建立的因素是时间。当进行了耗时的操作时，比如方法调用、循环跳转等时会产生安全点。此外，HotSpot虚拟机在安全点的基础上还增加了安全区域的概念，安全区域是安全点的扩展。在一段安全区域中能够实现安全点不能达成的效果。

