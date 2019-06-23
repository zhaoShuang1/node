package concurrent;

import java.util.concurrent.Semaphore;


public class Demo3 {
	public static void main(String[] args) {
		Semaphore semaphore = new Semaphore(3);
		new Thread(() -> {
			for(int i = 0 ; i < 10 ; i ++ ) {
				new Thread(()-> {
					try {
						//获取一个许可
						semaphore.acquire();
						System.out.println(Thread.currentThread().getName());
						Thread.sleep(10000000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally {
						//释放一个许可
						semaphore.release();
					}
					
				},"Thread-" + i).start();
			}
		}).start();
		
		boolean tryAcquire = semaphore.tryAcquire();
		System.out.println(tryAcquire);
		
		
	}

}
