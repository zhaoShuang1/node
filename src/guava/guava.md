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
	- 2. 集合[Collections]
	- 3. 缓存[Caches]
	- 4. 函数式风格[Functional idioms]
	- 5. 并发[Concurrency]
	- 6. 字符串处理[Strings]
	- 7. 原生类型[Primitives]
	- 8. 区间[Ranges]
	- 9. I/O
	 	- guava 提供了一系列用于操作流的工具类。ByteStreams 操作字节流，CharStreams 用于操作字符流。ByteSource ByteSink  CharSink CharSource。
	 		- ByteStreams 字节流操作
		 		```
		 			FileInputStream in = new FileInputStream(SOURCE_FILE);
					FileOutputStream out = new FileOutputStream(TARGET_FILE);
		 			//文件复制
					ByteStreams.copy(in,out );
					//把流读到内存，然后扔掉（可以理解为计算流的长度）
					long exhaust = ByteStreams.exhaust(in);
					System.out.println(exhaust);
					//限制流的读取个数
					InputStream limitedIn = ByteStreams.limit(in, 5);
					//创建一个DataInput（用于从二进制流中读取字节，并根据所有 Java 基本类型数据进行重构） 的一个扩展，用于操作内存中的byte数组
					ByteArrayDataInput dataInput = ByteStreams.newDataInput(new byte[1024]);
					String str = dataInput.readLine();
					//创建一个DataOuput（用于将数据从任意 Java 基本类型转换为一系列字节，并将这些字节写入二进制流） 的扩展，把基本数据写入字节流
					ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
					dataOutput.write("string".getBytes());
					//创建一个空的OutputStream，会丢弃所有写入的字节
					ByteStreams.nullOutputStream();
					//向字节数组中读数据 
					ByteStreams.read(in, new byte[1024], 0, 1024);
					//把输入流中的数据全部读到字节数组中
					ByteStreams.readFully(in, new byte[1024*1024]);
		 		
		 		```
		 		
	 		- CharStreams字符流操作
		 		```
		 			FileReader reader = new FileReader(SOURCE_FILE);
					FileWriter writer = new FileWriter(TARGET_FILE);
					
					//文件拷贝
					CharStreams.copy(reader, writer);
					
					//返回一个Writer，并把所有写入到Writer的字节流拼接至 传入的Appendable中
					StringBuilder sb = new StringBuilder();
					Writer asWriter = CharStreams.asWriter(sb);
					asWriter.write("1111111111111111");
					System.out.println(sb);
					
					//把流读到内存，然后扔掉（可以理解为计算流的长度）
					long l = CharStreams.exhaust(reader);
					
					//返回一个丢弃所有写入的writer
					Writer nullWriter = CharStreams.nullWriter();
					//读取所有内容
					List<String> lines = CharStreams.readLines(reader);
					lines.forEach(System.out::println);
					
					
					//读取所有内容，分别处理每一行
					List<String> result = CharStreams.readLines(reader, new LineProcessor<List<String>>() {
						List<String> lines = new ArrayList<String>();
						//处理每一行，遇到false直接结束不往后读
						public boolean processLine(String line) throws IOException {
							if(line.length() > 10) {
								lines.add(line);
								return true;
							}
							return false;
						}
						//返回结果
						public List<String> getResult() {
							return this.lines;
						}
					});
					result.forEach(System.out::println);
					
					//丢弃指定的字符数
					CharStreams.skipFully(reader, 3);
					
					//读取流中的所有的字符
					String content = CharStreams.toString(reader);
					System.out.println(content);
		 		```
		 		
		 	- Files 提供创建Source Sink 的方法和一些简单的操作文件的方法
		 		```
			 		File file = new File("C:\\Users\\user\\Desktop\\test\\1.txt");
			
					//读取字节流
					ByteSource byteSource = Files.asByteSource(new File(SOURCE_FILE));
					//写入字节流，以追加的方式
					ByteSink byteSink = Files.asByteSink(new File(SOURCE_FILE), FileWriteMode.APPEND);
					//读取字符流
					CharSource charSource = Files.asCharSource(new File(SOURCE_FILE), Charsets.UTF_8);
					//写入字符流，以追加的方式
					CharSink charSink = Files.asCharSink(new File(SOURCE_FILE), Charsets.UTF_8, FileWriteMode.APPEND);
					//必要时为文件创建父目录
					Files.createParentDirs(file);
					//在操作系统的临时目录下创建一个临时文件夹
					System.out.println(Files.createTempDir().getAbsolutePath());
					//判断是否为同一个文件
					assertThat(Files.equal(file, file),equalTo(true));
					//返回文件扩展名
					assertThat(Files.getFileExtension("C:\\Users\\user\\Desktop\\test\\1.txt"),equalTo("txt"));
					//返回去除扩展名的文件名
					assertThat(Files.getNameWithoutExtension("C:\\Users\\user\\Desktop\\test\\1.txt"),equalTo("1"));
					//规范文件路径，并不总是与文件系统一致
					String simplifyPath = Files.simplifyPath("C:\\Users\\user\\Desktop\\test\\1.txt");
					System.out.println(simplifyPath);
					
					
					System.out.println("=================================================");
					//TreeTraverser用于遍历文件树
					TreeTraverser<File> traverser = Files.fileTreeTraverser();
					//前序遍历
					FluentIterable<File> postOrder = traverser.postOrderTraversal(new File("C:\\Users\\user\\Desktop\\doc\\trade\\交易公共需求"));
					postOrder.forEach((f)->System.out.println(f.getAbsolutePath()));
					
					//后序遍历
					System.out.println("=================================================");
					FluentIterable<File> preOrder = traverser.preOrderTraversal(new File("C:\\Users\\user\\Desktop\\doc\\trade\\交易公共需求"));
					preOrder.forEach((f)->System.out.println(f.getAbsolutePath()));
	
		 		```
	- 10. 散列[Hash]
		- Hashing提供了若干散列函数
		```
			System.out.println(Hashing.md5().hashString("aaaaaaaaa", Charsets.UTF_8));
			System.out.println(Hashing.sha1().hashString("aaaaaaaaa", Charsets.UTF_8));
			System.out.println(Hashing.sha256().hashString("aaaaaaaaa", Charsets.UTF_8));
			System.out.println(Hashing.murmur3_128().hashString("aaaaaaaaa", Charsets.UTF_8));
			System.out.println(Hashing.murmur3_32().hashString("aaaaaaaa", Charsets.UTF_8));
			System.out.println(Hashing.goodFastHash(256).hashString("aaaaaaaaa", Charsets.UTF_8));
		```
		- BloomFilter 布鲁姆过滤器是一种概率数据结构，用来检测对象是不是不存在与集合中（我说你不在你肯定不在，我说你在你可能在）
		```
			/**
			 * 创建一个期待插入数量为 1000000 ，期待错误率为0.01的BloomFilter
			 */
			BloomFilter<String> bloomFilter = BloomFilter.create(new Funnel<String>() {
				@Override
				public void funnel(String from, PrimitiveSink into) {
					into.putString(from, Charsets.UTF_8);
				}
			}, 1000000,0.01);
			
			//保存100W数据
			for(int i = 0;i < 1000000;i++) {
				bloomFilter.put(i+"");
			}
			//验证数据
			System.out.println(bloomFilter.mightContain("100000"));
			System.out.println(bloomFilter.mightContain("123asdasd"));
			System.out.println(bloomFilter.mightContain("111111111"));
			System.out.println(bloomFilter.mightContain("123"));
		
		```
	- 11. 事件总线[EventBus]
	- 12. 数学运算[Math]
	- 13. 反射[Reflection]