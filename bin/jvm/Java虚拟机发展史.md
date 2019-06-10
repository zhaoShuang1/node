## java虚拟机发展历史
### Sun Classic / Exact VM
- 世界上第一款商用 Java 虚拟机。只能使用纯解释器方式来执行 Java 代码，也可以使用外挂JIT编译器，JIT 会完全接管虚拟机的执行系统，
但是此时解释器不会再工作了。JDK1.2之前是 Sun JDK 中唯一的虚拟机，JDK1.4 时，完全退出商用虚拟机的历史舞台，
与 Exact VM 一起进入了Sun Labs Research VM。

### Exact VM
- 它的执行系统已经具备了现代高性能虚拟机的雏形：如两级即时编译器、编译器与解析器混合工作模式等；
		使用准确式内存管理：虚拟机可以知道内存中某个位置的数据具体是什么类型。
		有与 HotSpot 几乎一样的热点探测；
		在商业应用上只存在了很短暂的时间就被更优秀的 HotSpot VM 所取代。

### Sun HotSpot VM
- 它是 Sun JDK 和 OpenJDK 中所带的虚拟机，也是目前使用范围最广的 Java 虚拟机；
		继承了 Sun 之前两款商用虚拟机的优点（如准确式内存管理），也使用了许多自己新的技术优势，如热点代码探测技术（通过执行计数器找出最具有编译价值的代码，然后通知 JIT 编译器以方法为单位进行编译）；
		Oracle 公司分别收购了 BEA 和 Sun，并在 JDK8 的时候，整合了 JRokit VM 和 HotSpot VM，如使用了 JRokit 的垃圾回收器与 MissionControl 服务，使用了 HotSpot 的 JIT 编译器与混合的运行时系统。

### Sun Mobile-Embedded VM / Meta-Circular VM
- KVM中的K是“Kilobyte”的意思，它强调简单、轻量、高度可移植，但是运行速度比较慢。在Android、iOS等智能手机操作系统出现前曾经在手机平台上得到非常广泛的应用

### Squawk VM
- Squawk VM由Sun公司开发，运行于Sun SPOT（Sun Small Programmable Object Technology，一种手持的WiFi设备），也曾经运用于Java Card。这是一个Java代码比重很高的嵌入式虚拟机实现，其中诸如类加载器、字节码验证器、垃圾收集器、解释器、编译器和线程调度都是Java语言本身完成的，仅仅靠C语言来编写设备I/O和必要的本地代码。

### JavaInJava
- JavaInJava是Sun公司于1997年～1998年间研发的一个实验室性质的虚拟机，从名字就可以看出，它试图以Java语言来实现Java语言本身的运行环境，既所谓的“元循环”（Meta-Circular，是指使用语言自身来实现其运行环境）。它必须运行在另外一个宿主虚拟机之上，内部没有JIT编译器，代码只能以解释模式执行。在20世纪末主流Java虚拟机都未能很好解决性能问题的时代，开发这种项目，其执行速度可想而知。

### Maxine VM
- Maxine VM和上面的JavaInJava非常相似，它也是一个几乎全部以Java代码实现（只有用于启动JVM的加载器使用C语言编写）的元循环Java虚拟机。这个项目于2005年开始，到现在仍然在发展之中，比起JavaInJava，Maxine VM就显得“靠谱”很多，它有先进的JIT编译器和垃圾收集器（但没有解释器），可在宿主模式或独立模式下执行，其执行效率已经接近了HotSpot Client VM的水平。

### BEA JRockit / IBM J9 VM
- 号称“世界上速度最快的 Java 虚拟机”（广告词）。一款专门为服务器硬件和服务器端应用场景高度优化的虚拟机，由于专注于服务器端应用，可以不太关注程序启动速度，因此 JRockit 内部不包含解析器实现，全部代码靠即时编译器编译后执行，除此之外，JRockit 的垃圾收集器和 MissionControl 服务套件等部分的实现，在众多 Java 虚拟机中也一直处于领先水平。

### IBM J9 VM
- 市场定位与 Sun HotSpot 比较接近，是一款设计上从服务器端到桌面应用再到嵌入式都全面考虑的多用途虚拟机。其开发的目的：作为 IBM 公司各种 Java 产品的执行平台。

### Azul VM 
- 是在 HotSpot 基础上进行大量改进，运行于 Azul Systems 公司的专有硬件 Vega 系统上的 Java 虚拟机，每个 Azul VM 实例都可以管理至少数十个 CPU 和数百 GB 内存的硬件资源，并提供在巨大范文内实现可控的 GC 时间的垃圾收集器、为专有硬件优化的线程调度等优秀特性；

### BEA Liquid VM
- 即是现在的 JRockit VE（Virtual Edition），是 BEA 公司开发的，可以直接运行在自家的 Hypervisor 系统上的 Jrockit VM 的虚拟化版本。它不需要操作系统的支持，或者说它自己本身实现了一个专有操作系统的必要功能，如文件系统、网络支持等。由虚拟机越过通用操作系统直接控制硬件可以获得很多好处，如在线程调度时，不需要再进行核态/用户态的切换等，这样可以最大限度的发挥硬件的能力，提升 Java 程序的执行性能。Apache Harmony / Google Android Dalvik VM ，它们只能称为“虚拟机”，而不能称为“Java 虚拟机”。

### Apache Harmony
- 是一个 Apache 软件基金会旗下以 Apache License 协议开源的实际兼容于 JDK1.5 和 JDK1.6 的 Java 程序运行平台。

### Dalvik VM
- 是 Android 平台的核心组成部分之一。没有遵循 Java 虚拟机规范，不能直接执行 Java 的 Class 文件，使用的是寄存器架构而不是 JVM 中常见的栈架构。它执行的 dex（Dalvik Executable）文件可以通过 Class 文件转化而来。

### Microsoft JVM 及其他
- Microsoft JVM：微软公司为了在 IE3 中支持 Java Applets 应用而开发了自己的 Java 虚拟机，虽然这款虚拟机只有 Windows 平台的版本，却是当时 Windows 下性能最好的 Java 虚拟机。其他：JamVM、cacaovm、SableVM、Kaffe、Jelatine JVM、Nano VM、MRP、Moxie JVM、Jikes RVM。



