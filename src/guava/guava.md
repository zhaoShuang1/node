### Guava
- Guava工程包含了若干被Google的 Java项目广泛依赖 的核心库，例如：集合 [collections] 、缓存 [caching] 、原生类型支持 [primitives support] 、并发库 [concurrency libraries] 、
通用注解 [common annotations] 、字符串处理 [string processing] 、I/O 等等。
	 - 1. 基本工具 [Basic utilities]
	 	- Joiner 
	 		- 根据指定的分隔符拼接字符串
	 		```
	 			List<String> list = Arrays.asList("a","b","c");
				String string = Joiner.on("#").join(list);
				System.out.println(string);
				//结果  a#b#c
	 		```
	 		- 根据指定的分隔符拼接字符串 ，跳过空值
	 		```
			 	List<String> list = Arrays.asList("a","b","c",null);
				String string = Joiner.on("#").skipNulls().join(list);
				System.out.println(string);
				//结果  a#b#c
	 		```
	 		- 根据指定的分隔符拼接字符串，为空值指定一个默认值
	 		```
		 		List<String> list = Arrays.asList("a","b","c",null);
				String string = Joiner.on("#").useForNull("null").join(list);
				System.out.println(string);
	 			//结果	a#b#c#null
	 		```
	 		- 根据指定的分隔符拼接字符串，并且追加到给定的对象后面（这个对象必须实现Appendable接口）
	 		```
	 			StringBuilder sb = new StringBuilder("sb");
				List<String> list = Arrays.asList("a","b","c",null);
				StringBuilder newSb = Joiner.on("#").useForNull("null").appendTo(sb, list);
				System.out.println(newSb.toString());
				System.out.println(sb.equals(newSb));
				//结果
				//		sba#b#c#null
				//		true
	 		```
	 		-  对Map 进行分割
	 		```
		 		Map<String,String> map = new HashMap<String,String>();
				map.put("a", "1");
				map.put("b", "2");
				String string = Joiner.on("#").withKeyValueSeparator("=").join(map);
				System.out.println(string);
		 		//结果	a=1#b=2
	 		```
	 		- 把map拼接到文件
	 		```
	 			try(FileWriter writer = new FileWriter(new File("d://joiner-file-map.txt"))){
					Map<String,String> map = new HashMap<String,String>();
					map.put("a", "1");
					map.put("b", "2");
					Joiner.on("#").withKeyValueSeparator("=").appendTo(writer, map);
				}catch(Exception e) {}
				//文件内容	a=1#b=2
				
	 		```
	 		- 把list拼接到文件
	 		```
	 			try(FileWriter writer = new FileWriter(new File("d://joiner-file-list.txt"))){
					List<String> list = Arrays.asList("a","b","c",null);
					Joiner.on("#").useForNull("null").appendTo(writer, list);
				}catch(Exception e) {}
				//文件内容	a#b#c#null
	 		```
	 	- Spliter 
	 		- 根据指定的分隔符分割字符串
	 		```
	 			List<String> list = Splitter.on("=").splitToList("hello=world");
				list.forEach(System.out::println);
				//结果
				//hello
				//world
	 		```
	 		- 根据指定的分隔符分割字符串，过滤空字符串
	 		```
	 			List<String> list = Splitter.on("=").omitEmptyStrings().splitToList("hello=world===");
				list.forEach(System.out::println);
				//结果
				//hello
				//world
	 		```
	 		- 根据指定的分隔符分割字符串，trim 分割之后的字符串
	 		```
	 			List<String> list = Splitter.on("=").trimResults().splitToList(" hello =world ===");
				list.forEach(System.out::println);
	 			//结果
				//hello
				//world
	 		```
	 		- 按照固定的长度分割字符串
	 		```
		 		List<String> list = Splitter.fixedLength(3).splitToList(" aaabbbcccddd");
				list.forEach(System.out::println);
				//结果
				//aaa
				//bbb
				//ccc
				//ddd
	 		```
	 		- 根据指定的分隔符分割字符串，限制分割长度
	 		```
	 			List<String> list = Splitter.on("=").limit(3).splitToList("a=b=c=d=e");
				list.forEach(System.out::println);
				//结果
				//a
				//b
				//c=d=e
	 		```
	 		- 根据正则表达式分割
	 		```
	 			List<String> list = Splitter.onPattern("\\d").splitToList("a1b1c1d1e");
				list.forEach(System.out::println);
				//或者
				List<String> list = Splitter.on(Pattern.compile("\\d")).splitToList("a1b1c1d1e");
				list.forEach(System.out::println);
				
				//结果
				//a
				//b
				//c
				//d
				//e
	 		```
	 		- 分隔符分割字符串为map
	 		```
	 			Map<String, String> map = Splitter.on("|").withKeyValueSeparator("=").split("a=b|c=d");
				map.forEach((k,v)->{
					System.out.println(k+":"+v);
				});
				//结果
				//a:b
				//c:d
	 		```
	 	- Preconditions
	 	```
	 		List<String> list = new ArrayList<>();
			Object obj = null;
			//检测是否为空，抛出NullPointerException 
			Preconditions.checkNotNull(obj, "this object shoud not be null: %s","obj" );
			//检测参数，抛出IllegalArgumentException
			Preconditions.checkArgument(obj != null);
			//检测参数状态，抛出IllegalStateException
			Preconditions.checkState(obj != null, "this object shoud not be null: %s","obj");
			//检测集合下标，抛出IndexOutOfBoundException
			Preconditions.checkElementIndex(3, list.size());
			//检测集合中元素位置，抛出IndexOutOfBoundException
			Preconditions.checkPositionIndex(3, list.size());
	 	```
	 	- Objects、MoreObjects、ComparisonChain
	 		- MoreObjects 生成toString()方法
	 		```
	 		public static class Person{
				private String name;
				private String gender;
				private int age;
				@Override
				public String toString() {
					return MoreObjects.toStringHelper(this)
					.omitNullValues()
					.add("name", this.name)
					.add("gender", this.gender)
					.add("age", this.age)
					.toString();
				}
			}
	 		```
	 		- Objects 生成HashCode equals方法
	 		```
	 			@Override
				public int hashCode() {
					return Objects.hashCode(this.name,this.gender,this.age);
				}
				@Override
				public boolean equals(Object obj) {
					if(!(obj instanceof Person))
						return false;
					Person p = (Person)obj;
					return Objects.equal(this.name, p.name)
							&&Objects.equal(this.gender, p.gender)
							&&Objects.equal(this.age, p.age);
				}
	 		```
	 		- ComparisonChain 生成compareTo方法
	 		```
	 			@Override
				public int compareTo(Person p) {
					return ComparisonChain.start().compare(this.name, p.name)
							.compare(this.age, this.age)
							.compare(this.gender, p.gender)
							.result();
				}
	 		```
	 	- Strings、Charsets、CharMatcher
	 		- Strings 字符串工具类
	 		```
	 			assertThat(Strings.emptyToNull(""), nullValue());
				assertThat(Strings.nullToEmpty(null), equalTo(""));
				assertThat(Strings.commonPrefix("abc", "ab"), equalTo("ab"));
				assertThat(Strings.commonSuffix("abc", "ac"), equalTo("c"));
				assertThat(Strings.repeat("a", 3), equalTo("aaa"));
				assertThat(Strings.isNullOrEmpty(""), equalTo(true));
				assertThat(Strings.padEnd("aaa", 3, 'b'), equalTo("aaa"));
				assertThat(Strings.padEnd("aaa", 5, 'b'), equalTo("aaabb"));
				assertThat(Strings.padStart("aaa", 3, 'b'), equalTo("aaa"));
				assertThat(Strings.padStart("aaa", 5, 'b'), equalTo("bbaaa"));
	 		
	 		```
	 		- Charsets 字符集常量
	 		```
	 			assertThat(Charsets.UTF_8, equalTo(Charset.forName("utf-8")));
	 		```
	 		- CharMatcher 字符匹配器
	 		```
	 			assertThat(CharMatcher.javaDigit().matches('1'), equalTo(true));
				assertThat(CharMatcher.javaDigit().matches('b'), equalTo(false));
				assertThat(CharMatcher.is('a').countIn("aabbcc"), equalTo(2));
				assertThat(CharMatcher.breakingWhitespace().collapseFrom("    abc  d", ' '), equalTo(" abc d"));
				assertThat(CharMatcher.javaDigit().or(CharMatcher.whitespace()).removeFrom("123  asd123"), equalTo("asd"));
				assertThat(CharMatcher.javaDigit().or(CharMatcher.whitespace()).retainFrom("123  asd123"), equalTo("123  123"));
	 		```
	 	- StopWatch 用于测量程序运行时间的一个工具类
	 		```
	 			//启动一个秒表
				Stopwatch stopwatch = Stopwatch.createStarted();
				TimeUnit.MILLISECONDS.sleep(1500);
				//停止秒表,以毫秒为单位显示
				System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
	 		```
	 - 2. 集合[Collections]
	 - 3. 缓存[Caches]
	 - 4. 函数式风格[Functional idioms]
	 - 5. 并发[Concurrency]
	 	* Monitor 监听器根据ReentrantLock 的进一步封装，可读性更好
	 	```
	 		/**
	 		 *一个生产者消费者模式的例子
	 		 */
			public class MonitorExample {
				//库存
				private static volatile int count = 0;
				//库存限制
				private static final int limit = 100;
				public static void main(String[] args) {
					//创建监视器
					Monitor monitor = new Monitor();
					//不为空条件类似于Condition
					Guard notEmpty = monitor.newGuard(() -> count > 0 );
					//未到达限制个数条件
					Guard notFull = monitor.newGuard(() -> count < limit);
					
					//创建生产者
					new Thread(()->{
						for (int i = 0; i < 10; i++) {
							new Thread(new Consumer(monitor, notEmpty),"provider-"+(i+1)).start();
						}
					}).start();;
					
					//创建消费者
					new Thread(()->{
						for (int i = 0; i < 10; i++) {
							new Thread(new Provider(monitor, notFull),"provider-"+(i+1)).start();
						}
					}).start();;
					
					
					
				}
				
				public static class Consumer implements Runnable{
					private Monitor monitor;
					private Guard notEmpty;
					
					public Consumer(Monitor monitor, Guard notEmpty) {
						this.monitor = monitor;
						this.notEmpty = notEmpty;
					}
					
					@Override
					public void run() {
						while(true) {
							try {
								//当条件成立时才能获取锁，否则阻塞
								monitor.enterWhen(notEmpty);
								count--;
								System.out.println(Thread.currentThread().getName() + "consumed ，当前值 ： " + count);
							}catch(Exception e) {
								e.printStackTrace();
							}finally {
								//释放锁
								monitor.leave();
							}
							try {
								//休眠10毫秒
								TimeUnit.MILLISECONDS.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				
				public static class Provider implements Runnable{
					private Monitor monitor;
					private Guard notFull;
					public Provider(Monitor monitor, Guard notFull) {
						this.monitor = monitor;
						this.notFull = notFull;
					}
					
					@Override
					public void run() {
						while(true) {
							try {
								//当条件成立时才能获取锁，否则阻塞
								monitor.enterWhen(notFull);
								count++;
								System.out.println(Thread.currentThread().getName() + "provided ，当前值 ： " + count);
							}catch(Exception e) { 
								e.printStackTrace();
							}finally {
								//释放锁
								monitor.leave();
							}
							try {
								//休眠10毫秒
								TimeUnit.MILLISECONDS.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
			}
	 	``` 
	 	* ListenableFuture 以回调的方式获取异步任务执行结果，jdk8中有更简洁的实现
	 		- jdk8 之前的处理方式
	 		```
			public static void main(String[] args) throws Exception {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				Future<Integer> future = executor.submit(()->{
					try {
						//模拟任务处理
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//返回结果
					return 10;
				});
				System.out.println(future.get());
			}	 		
	 		```
	 		- jdk8 之后的处理方式
	 		```
 			public static void main(String[] args) {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()->{
					try {
						//模拟任务处理
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//返回结果
					return 10;
				}, executor);
				//创建回调方法
				future.whenComplete((t,u)->{
					System.out.println("result : " + t  + " , 异常信息：" + u);
				});
			}
	 		```
	 		- 使用 ListenableFuture
	 		```
	 		public static void main(String[] args) {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				//装饰executor
				ListeningExecutorService decoratorExecutor = MoreExecutors.listeningDecorator(executor);
				
				ListenableFuture<Integer> future = decoratorExecutor.submit(()->{
					try {
						//模拟任务处理
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//返回结果
					return 10;
				});
				
				//适用于无返回值的任务
				//future.addListener(()->{
				//		System.out.println("done");
				//}, decoratorExecutor);
				
				//用于有返回值的任务
				Futures.addCallback(future, new FutureCallback<Integer>() {
					@Override
					public void onSuccess(Integer result) {
						System.out.println("result : " + result);
					}
		
					@Override
					public void onFailure(Throwable t) {
						System.out.println("failure");
						t.printStackTrace();
					}
				},decoratorExecutor);
			}
	 		
	 		```
	 	
	 	
	 	
	 - 6. 字符串处理[Strings]
	 - 7. 原生类型[Primitives]
	 - 8. 区间[Ranges]
	 - 9. I/O
	 - 10. 散列[Hash]
	 - 11. 事件总线[EventBus]
	 	- 通过EventBus可以轻松的实现一个简单消息订阅与发布，类似于MQ，一个简单的例子。
	 		- 一个简单的例子，订阅者（也可以称为监听器）
	 	```
		 	public class SimpleListenerExample {
				//通过@Subscribe 注解标识这个方法为一个'订阅者' ，这个方法必须是public的而且参数只能有一个。
				//String 类型的参数代表这个方法订阅一个String类型的事件
				@Subscribe
				public void method(String event) {
					System.out.println("event : " + event );
				}
			
			}
	 	
	 	```
	 		 事件总线
	 		
	 	``` 
	 		public class SimpleEventBusExample {
				public static void main(String[] args) {
					//创建一个事件总线
					EventBus eventBus = new EventBus();
					//注册一个监听器
					SimpleListenerExample simpleListenerExample = new SimpleListenerExample();
					eventBus.register(simpleListenerExample);
					//向事件总线发送一个String类型的事件
					eventBus.post("test event");
					//注销监听器
					eventBus.unregister(simpleListenerExample);
					
				}
			}
	 	```
	 	
	 - 12. 数学运算[Math]
	 - 13. 反射[Reflection]