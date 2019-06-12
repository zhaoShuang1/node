### Semaphore
- Semaphore内部维护了一个许可集，可以通过acquire方法获取许可，release方法释放许可。同一时间只能获取指定数量的许可，当许可被其它线程获取完时，之后的线程再尝试获取许可时会被阻塞，直到有可用许可时才能被唤醒（如果在此线程在阻塞过程中被中断会抛出InterruptedException）。例子：
```
	public static void main(String[] args) {
		Semaphore semaphore = new Semaphore(3);
		new Thread(() -> {
			for(int i = 0 ; i < 10 ; i ++ ) {
				new Thread(()-> {
					try {
						//获取一个许可
						semaphore.acquire();
						System.out.println(Thread.currentThread().getName());
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally {
						//释放一个许可
						semaphore.release();
					}
					
				},"Thread-" + i).start();
			}
		}).start();
	
	}
```