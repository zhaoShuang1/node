package concurrent;

import java.util.concurrent.Exchanger;

public class Demo2 {
	
	
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
//		Thread t2 = new Thread(()->{
//			try {
//				String str = exchanger.exchange("Thread-2");
//				System.out.println(Thread.currentThread().getName() + " : " + str);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		},"Thread-2");
		
		t1.start();
//		t2.start();
	}

}
