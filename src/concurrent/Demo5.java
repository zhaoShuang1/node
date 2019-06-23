package concurrent;

import java.util.concurrent.locks.ReentrantLock;

public class Demo5 {
	public static void main(String[] args) {
		System.out.println(Integer.toBinaryString(2>>2));//1
		System.out.println(10<<3);//8
		System.out.println(Integer.toBinaryString(-2));
		System.out.println(Integer.toBinaryString(-2<<2));
		System.out.println(Integer.toBinaryString(-2>>2));
		System.out.println(Integer.toBinaryString(-2>>>10));
		System.out.println(-2>>>10);
		System.out.println(92^92);
		System.out.println(1|10);
		System.out.println(1&1);
		
		final ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			//do something
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	
	public int add(int a ,int b) {
		return a==0?b:add((a^b), (a&b)<<1);
	}
}





