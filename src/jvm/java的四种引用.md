### Java �е���������
- ǿ����
	- ֱ��ͨ��new�ؼ��ִ���һ���������˵һ���������ǿ���ü�ʹ����OOM�������Ҳ���ᱻ���ա�
		```
			Object obj = new Object();
		
		```
		
- ������
	- ���ڴ治���ʱ�������������������л��գ�������ͨ���������ڴ����еĻ��档
	```
		SoftReference<Object> softReference = new SoftReference<Object>(new Object());
		Object obj = softReference.get();
	```
- ������
	- ÿ�ν�����������ʱ������������ö��󣬴�����ڻ��档
	```
		WeakReference<Object> weakReference = new WeakReference<Object>(new Object());
		Object obj = weakReference.get();
	```
	- �����ÿ��Ժ�ReferenceQueue����ʹ�ã�����Ӧ�ñ�����ʱjvm�ͻ����������ö��������֮�����Ķ�����ȥ�������������ٶ�����������ڡ�
	```
		ReferenceQueue<Object> queue = new ReferenceQueue<>();
		WeakReference<Object> weakReference = new WeakReference<Object>(new Object(),queue);
		Object obj = weakReference.get();
		System.gc();
		//��������ᱻ����ֱ�����󱻻���
		Reference<? extends Object> reference = queue.remove();
		Object object = reference.get();
	
	```
- ������
	- �������൱��û�����ã����κ�ʱ�򶼿��ܻᱻ�������������ա�����Ҫ���ReferenceQueue����ʹ�ã������ڶ�����ɵ�һЩ������������Բο�Apache ��common-io �е�org.apache.commons.io.FileCleaningTracker ���е�ʵ�֡�
	```
		ReferenceQueue<Object> queue = new ReferenceQueue<>();
		PhantomReference<Object> phantomReference = new PhantomReference<Object>(new Object(), queue);
		
		//���ɱ���ȡ����Զ��null
		Object object = phantomReference.get();
		System.out.println(object);
	
	```
	
	