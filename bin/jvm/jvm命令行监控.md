## jvm命令行监控
### jps
- 命令用法: jps [options] [hostid]
	* options:命令选项，用来对输出格式进行控制
	* hostid:指定特定主机，可以是ip地址和域名, 也可以指定具体协议，端口。
- 功能描述: jps是用于查看有权访问的hotspot虚拟机的进程. 当未指定hostid时，默认查看本机jvm进程，否者查看指定的hostid机器上的jvm进程，此时hostid所指机器必须开启jstatd服务。 jps可以列出jvm进程lvmid，主类类名，main函数参数, jvm参数，jar名称等信息。
- 没添加option的时候，默认列出pid和主类名称.如下:
```
	16128 
	22452 
	23228 Jps
	9596 Application
```
- jps -q 只显示pid信息，如下
```
	16128
	22452
	28164
	9596
```
- jsp -m 输出main函数传入的参数
```
	16128 
	22452 
	33740 Jps -m
	9596 Application --spring.output.ansi.enabled=always
```
- jps -l 输出主类的完整信息
```
	16128 
	22452 
	23672 sun.tools.jps.Jps
	9596 com.ebill.eps.Application
```
- jps -v 输出jvm参数
```
	16128  -Dosgi.requiredJavaVersion=1.8 -Xms40m -Dosgi.module.lock.timeout=10 -Dorg.eclipse.swt.browser.IEVersion=10001 -Xmx1200m -javaagent:D:\tools\spring-tool-suite-3.9.4.RELEASE-e4.7.3a-win32-x86_64\sts-bundle\sts-3.9.4.RELEASE\lombok.jar
	22452  -Dosgi.requiredJavaVersion=1.8 -XX:+UseG1GC -XX:+UseStringDeduplication -Dosgi.requiredJavaVersion=1.8 -Xms256m -Xmx1024m
	23700 Jps -Dapplication.home=C:\Program Files\Java\jdk1.8.0_172 -Xms8m
	9596 EpsApplication -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=57654 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true -Xverify:none -XX:TieredStopAtLevel=1 -Dfile.encoding=UTF-8
``` 
- jps -V 输出通过.hotsportrc或-XX:Flags=<filename>指定的jvm参数
### jstack
- Prints Java thread stack traces for a Java process, core file, or remote debug server. This command is experimental and unsupported.
- 用法：
	- jstack [ options ] pid
	- jstack [ options ] executable core
	- jstack [ options ] [ server-id@ ] remote-hostname-or-IP
	- executable 生成的core dump java可执行程序
	- server-id 多个调试程序运行在同一台远程主机上的一个可选唯一标识
	- core 将被打印信息的核心文件
- jstack -F  没有响应的时候强制打印栈信息,如果直接jstack无响应时，用于强制jstack
- jstack -l 长列表. 打印关于锁的附加信息,例如属于java.util.concurrent的ownable synchronizers列表，会使得JVM停顿得长久得多，相当于一次full gc。
- stack -m 打印本地方法的堆栈信息（native 方法）
### jhat
- Analyzes the Java heap. This command is experimental and unsupported. 用来分析jmap 导出的 heap dump文件，在浏览器中展示。
支持OQL（object query language） 类似于SQL。
- usage
	- jhat [ options ] heap-dump-file
	- heap-dump-file 二进制的java堆dump文件。对于一个dump文件包含多个堆dump信息的，可以通过在文件名后面附加#<number>来指定文件中的哪个转储，例子 myfile.hprof#3。
- -J  指定此命令在虚拟机运行时的jvm参数 ，例子 jhat -J-Xmx512m ...
- 常用方式 jhat dump-file ，更多详细参数查看官网https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jhat.html

### jmap
- You use the jmap command to print details of a specified process. This command is experimental and unsupported.
- usage
	- jmap [ options ] pid
	- jmap [ options ] executable core
	- jmap [ options ] [ pid ] server-id@ ] remote-hostname-or-IP
- <no option> 当不使用任何选项时，jmap命令打印共享对象映射。
- jmap -dump:<dump-options> 导出java堆信息到二进制文件，可以通过jhat命令查看。
	- live 打印活着的对象，如果未指定打印所有
	- format=b 二进制文件
	- file=a.bin 文件名
	- 示例 jmap -dump:live,format=b,file=a.bin 15076
- jmap -heap 打印堆的摘要信息。
- jmap -clstats 打印java类加载器的统计信息。
- jmap -finalizerinfo 打印等待结束的对象信息（执行finalize方法）
- -F 强制dump ，用于 -dump 或 -histo ，此模式不支持live参数。
- jmap -histo:[live] 以柱状图的方式打印java类、对象、内存、全限定类名信息。
- -J 指定此命令在虚拟机运行时的jvm参数 ，例子 jmap -J-Xmx512m .....
### jstat
- jstat显示一个仪表化的HotSpot Java虚拟机(JVM)的性能统计数据。
- 示例 jstat -gc 15076 1000 20 , 打印进程pid为15076 的gc统计信息，每隔1000毫秒打印一次，打印20次。
- 详细选项查看官网 https://docs.oracle.com/javase/7/docs/technotes/tools/share/jstat.html	

### jinfo 
- jinfo打印给定Java进程或核心文件或远程调试服务器的Java配置信息。配置信息包括Java系统属性和Java虚拟机命令行标志。如果给定的进程在64位VM上运行，你可能需要指定-J-d64选项，例如:jinfo -J-d64 -sysprops pid
- usage 
	- jinfo [ option ] pid
	- jinfo [ option ] executable core
	- jinfo [ option ] [server-id@]remote-hostname-or-IP 
- <no option> 打印命令行标识和系统属性键值对
- flag 打印指定命令行标识键值对，例子jinfo -flag MaxHeapSize 15076
- flag[+|-]name 启用或关闭VM标识，例子jinfo -flag +PrintGC 15076
- flag name=value 设置指定的命令行标识等于指定值 ，例子 jinfo -flag PrintGC=1 15076
- falgs 打印所有命令行标识
- sysprops 打印所有系统属性
- 并不是所有的参数都可以通过这种方式修改 ，可以使用一下命令查看可以动态修改的参数java -XX:+PrintFlagsFinal -version | grep manageable
```
	intx CMSAbortablePrecleanWaitMillis            = 100             {manageable}         
     intx CMSWaitDuration                           = 2000            {manageable}         
     bool HeapDumpAfterFullGC                       = false           {manageable}         
     bool HeapDumpBeforeFullGC                      = false           {manageable}         
     bool HeapDumpOnOutOfMemoryError                = false           {manageable}         
    ccstr HeapDumpPath                              =                 {manageable}         
     bool PrintClassHistogram                       = false           {manageable}         
     bool PrintClassHistogramAfterFullGC            = false           {manageable}         
     bool PrintClassHistogramBeforeFullGC           = false           {manageable}         
     bool PrintConcurrentLocks                      = false           {manageable}         
     bool PrintGC                                   = false           {manageable}         
     bool PrintGCDateStamps                         = false           {manageable}         
     bool PrintGCDetails                            = false           {manageable}         
     bool PrintGCTimeStamps                         = false           {manageable} 
```