### Guava
- Guava���̰��������ɱ�Google�� Java��Ŀ�㷺���� �ĺ��Ŀ⣬���磺���� [collections] ������ [caching] ��ԭ������֧�� [primitives support] �������� [concurrency libraries] ��
ͨ��ע�� [common annotations] ���ַ������� [string processing] ��I/O �ȵȡ�
	 - 1. �������� [Basic utilities]
	 	- Joiner 
	 		- ����ָ���ķָ���ƴ���ַ���
	 		```
	 			List<String> list = Arrays.asList("a","b","c");
				String string = Joiner.on("#").join(list);
				System.out.println(string);
				//���  a#b#c
	 		```
	 		- ����ָ���ķָ���ƴ���ַ��� ��������ֵ
	 		```
			 	List<String> list = Arrays.asList("a","b","c",null);
				String string = Joiner.on("#").skipNulls().join(list);
				System.out.println(string);
				//���  a#b#c
	 		```
	 		- ����ָ���ķָ���ƴ���ַ�����Ϊ��ֵָ��һ��Ĭ��ֵ
	 		```
		 		List<String> list = Arrays.asList("a","b","c",null);
				String string = Joiner.on("#").useForNull("null").join(list);
				System.out.println(string);
	 			//���	a#b#c#null
	 		```
	 		- ����ָ���ķָ���ƴ���ַ���������׷�ӵ������Ķ�����棨����������ʵ��Appendable�ӿڣ�
	 		```
	 			StringBuilder sb = new StringBuilder("sb");
				List<String> list = Arrays.asList("a","b","c",null);
				StringBuilder newSb = Joiner.on("#").useForNull("null").appendTo(sb, list);
				System.out.println(newSb.toString());
				System.out.println(sb.equals(newSb));
				//���
				//		sba#b#c#null
				//		true
	 		```
	 		-  ��Map ���зָ�
	 		```
		 		Map<String,String> map = new HashMap<String,String>();
				map.put("a", "1");
				map.put("b", "2");
				String string = Joiner.on("#").withKeyValueSeparator("=").join(map);
				System.out.println(string);
		 		//���	a=1#b=2
	 		```
	 		- ��mapƴ�ӵ��ļ�
	 		```
	 			try(FileWriter writer = new FileWriter(new File("d://joiner-file-map.txt"))){
					Map<String,String> map = new HashMap<String,String>();
					map.put("a", "1");
					map.put("b", "2");
					Joiner.on("#").withKeyValueSeparator("=").appendTo(writer, map);
				}catch(Exception e) {}
				//�ļ�����	a=1#b=2
				
	 		```
	 		- ��listƴ�ӵ��ļ�
	 		```
	 			try(FileWriter writer = new FileWriter(new File("d://joiner-file-list.txt"))){
					List<String> list = Arrays.asList("a","b","c",null);
					Joiner.on("#").useForNull("null").appendTo(writer, list);
				}catch(Exception e) {}
				//�ļ�����	a#b#c#null
	 		```
	 	- Spliter 
	 		- ����ָ���ķָ����ָ��ַ���
	 		```
	 			List<String> list = Splitter.on("=").splitToList("hello=world");
				list.forEach(System.out::println);
				//���
				//hello
				//world
	 		```
	 		- ����ָ���ķָ����ָ��ַ��������˿��ַ���
	 		```
	 			List<String> list = Splitter.on("=").omitEmptyStrings().splitToList("hello=world===");
				list.forEach(System.out::println);
				//���
				//hello
				//world
	 		```
	 		- ����ָ���ķָ����ָ��ַ�����trim �ָ�֮����ַ���
	 		```
	 			List<String> list = Splitter.on("=").trimResults().splitToList(" hello =world ===");
				list.forEach(System.out::println);
	 			//���
				//hello
				//world
	 		```
	 		- ���չ̶��ĳ��ȷָ��ַ���
	 		```
		 		List<String> list = Splitter.fixedLength(3).splitToList(" aaabbbcccddd");
				list.forEach(System.out::println);
				//���
				//aaa
				//bbb
				//ccc
				//ddd
	 		```
	 		- ����ָ���ķָ����ָ��ַ��������Ʒָ��
	 		```
	 			List<String> list = Splitter.on("=").limit(3).splitToList("a=b=c=d=e");
				list.forEach(System.out::println);
				//���
				//a
				//b
				//c=d=e
	 		```
	 		- ����������ʽ�ָ�
	 		```
	 			List<String> list = Splitter.onPattern("\\d").splitToList("a1b1c1d1e");
				list.forEach(System.out::println);
				//����
				List<String> list = Splitter.on(Pattern.compile("\\d")).splitToList("a1b1c1d1e");
				list.forEach(System.out::println);
				
				//���
				//a
				//b
				//c
				//d
				//e
	 		```
	 		- �ָ����ָ��ַ���Ϊmap
	 		```
	 			Map<String, String> map = Splitter.on("|").withKeyValueSeparator("=").split("a=b|c=d");
				map.forEach((k,v)->{
					System.out.println(k+":"+v);
				});
				//���
				//a:b
				//c:d
	 		```
	 	- Preconditions
	 	```
	 		List<String> list = new ArrayList<>();
			Object obj = null;
			//����Ƿ�Ϊ�գ��׳�NullPointerException 
			Preconditions.checkNotNull(obj, "this object shoud not be null: %s","obj" );
			//���������׳�IllegalArgumentException
			Preconditions.checkArgument(obj != null);
			//������״̬���׳�IllegalStateException
			Preconditions.checkState(obj != null, "this object shoud not be null: %s","obj");
			//��⼯���±꣬�׳�IndexOutOfBoundException
			Preconditions.checkElementIndex(3, list.size());
			//��⼯����Ԫ��λ�ã��׳�IndexOutOfBoundException
			Preconditions.checkPositionIndex(3, list.size());
	 	```
	 	- Objects��MoreObjects��ComparisonChain
	 		- MoreObjects ����toString()����
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
	 		- Objects ����HashCode equals����
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
	 		- ComparisonChain ����compareTo����
	 		```
	 			@Override
				public int compareTo(Person p) {
					return ComparisonChain.start().compare(this.name, p.name)
							.compare(this.age, this.age)
							.compare(this.gender, p.gender)
							.result();
				}
	 		```
	 	- Strings��Charsets��CharMatcher
	 		- Strings �ַ���������
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
	 		- Charsets �ַ�������
	 		```
	 			assertThat(Charsets.UTF_8, equalTo(Charset.forName("utf-8")));
	 		```
	 		- CharMatcher �ַ�ƥ����
	 		```
	 			assertThat(CharMatcher.javaDigit().matches('1'), equalTo(true));
				assertThat(CharMatcher.javaDigit().matches('b'), equalTo(false));
				assertThat(CharMatcher.is('a').countIn("aabbcc"), equalTo(2));
				assertThat(CharMatcher.breakingWhitespace().collapseFrom("    abc  d", ' '), equalTo(" abc d"));
				assertThat(CharMatcher.javaDigit().or(CharMatcher.whitespace()).removeFrom("123  asd123"), equalTo("asd"));
				assertThat(CharMatcher.javaDigit().or(CharMatcher.whitespace()).retainFrom("123  asd123"), equalTo("123  123"));
	 		```
	 - 2. ����[Collections]
	 - 3. ����[Caches]
	 - 4. ����ʽ���[Functional idioms]
	 - 5. ����[Concurrency]
	 - 6. �ַ�������[Strings]
	 - 7. ԭ������[Primitives]
	 - 8. ����[Ranges]
	 - 9. I/O
	 - 10. ɢ��[Hash]
	 - 11. �¼�����[EventBus]
	 - 12. ��ѧ����[Math]
	 - 13. ����[Reflection]