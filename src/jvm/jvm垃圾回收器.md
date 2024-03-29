## jvm垃圾回收器
## Serial
- 新生代收集器，可以说Serial是最基础的收集器。
- 特点：
	- 单线程，当他收集内存的时候，所有其他的线程都要等待，直到他完成。
	- 复制算法

## ParNew
- 新生代收集器，可以说ParNew是Serial的一个多线程版本。
- 特点：
	- 多线程
	- 复制算法
	- 回收的时候暂停所有用户的线程
	- 只能配合CMS工作

## Parallel Seaverage
- 新生代收集器，多线程，复制算法，更加关注吞吐量。
- 特点：
	- 多线程
	- 复制算法
	- 可控制的吞吐量
	- 自适应调节策略

- 吞吐量 = 用户代码运行时间/（用户代码运行时间+垃圾回收时间）自适应调节策略：UseAdaptiveSizePolicy参数，这个参数打开后，
不用手动设置Eden和s1、s2的大小比例，收集器会根据当前虚拟机运行情况收集性能监控信息，动态的调整这些参数以供最合适的停顿时间和最大的吞吐量。
可以说，自适应调节策略是Parallel Scaverage和ParNew的主要区别。

## Serial Old
- 老年代回收器。是一个单线程，使用标记-整理算法的垃圾收集器。
- 特点：
	- 单线程
	- 标记-整理算法
	- 暂停所有用户线程

## Parallel Old
- 老年代收集器。是Parallel Scaverage的老年代版本。使用多线程和标记-整理算法。
- 特点：
	- 多线程
	- 标记-整理算法

## CMS
- 老年代收集器。Concurrent Mark Sweep，是一种获取最短停顿时间为目标的回收器。现在很多业务场合都要求挺短时间短，CMS非常适合这种业务场景。
- 从Mark Sweep可以看出，这是一种标记清除算法，步骤：
	- 1.初始标记
	- 2.并发标记
	- 3.重新标记
	- 4.并发清除
- 特点：
	- 多线程
	- 标记清除算法
	- 对CPU资源敏感
	- 尽可能小的缩短停顿时间
	- 无法处理浮动垃圾
	- 产生内存碎片

![alt](https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1041430851,1856407024&fm=26&gp=0.jpg)


## G1
- G1垃圾收集器是面向服务端应用的垃圾收集器。在垃圾收集过程中，是可以替换掉CMS的
- 特点：
	- 并行与并发：充分利用cpu，缩短STW停顿时间，不用停顿GC操作，并发执行。
	- 分代收集
	- 空间整合：整体上基于标记-整理算法，局部上是基于复制算法实现，不会产生内存碎片。
	- 可预测的停顿

![alt](https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3437179383,1778218564&fm=26&gp=0.jpg)