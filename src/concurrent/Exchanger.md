### Exchanger 
- Exchanger用于线程之间交换数据，其内部提供了一个同步点，用于交换数据。当一个线程先调用了exchange方法，则会一直等待另外一下线程调用exchange方法（除非当前线程被其他线程中断）。当两个线程都到达同步点之后就可以交换数据了。例子：
```
	public static void main(String[] args) {
		Exchanger<String> exchanger = new Exchanger<>();
		Thread t1 = new Thread(()->{
			try {
				String str = exchanger.exchange("Thread-1");
				System.out.println(Thread.currentThread().getName() + " : " + str);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"Thread-1");
		Thread t2 = new Thread(()->{
			try {
				String str = exchanger.exchange("Thread-2");
				System.out.println(Thread.currentThread().getName() + " : " + str);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"Thread-2");
		
		t1.start();
		t2.start();
	}
```