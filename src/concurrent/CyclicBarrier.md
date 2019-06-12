### CyclicBarrier
- 使一组线程相互等待，直到最后一个线程到达屏障处才放行，CyclicBarrier支持一个可选的Runnable命令，在最后一个线程到达屏障处执行这个命令。
CyclicBarrier 使用了一种要么全部要么全不 (all-or-none) 的破坏模式：如果因为中断、失败或者超时等原因，导致线程过早地离开了屏障点，那么在该屏障点等待的其他所有线程也将通过 BrokenBarrierException（如果它们几乎同时被中断，则用 InterruptedException）以反常的方式离开。
还有这个屏障是可重用的，例子：
```
	public static void main(String[] args) {
		//创建一个数量为3的屏障，到达屏障时打印done
		CyclicBarrier barrier = new CyclicBarrier(3,() -> {System.out.println("done");});
		new Thread(()-> {
			for(int i = 0 ; i < 7 ; i ++ ) {
				new Thread(()-> {
					System.out.println(Thread.currentThread().getName() + " done");
					try {
						//在所有线程都已经在此 barrier 上调用 await 方法之前，将一直等待。
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				},"Thread-"+i).start();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();;
		
	}


```
```
	输出结果
		Thread-0 done
		Thread-1 done
		Thread-2 done
		done
		Thread-3 done
		Thread-4 done
		Thread-5 done
		done
		Thread-6 done
```
- 源码分析
```
	//关键代码
	private int dowait(boolean timed, long nanos)
        throws InterruptedException, BrokenBarrierException,
               TimeoutException {
        final ReentrantLock lock = this.lock;
        //加锁
        lock.lock();
        try {
        	//拿到当前代
            final Generation g = generation;
			//如果当前代的屏障已被打破抛出异常
            if (g.broken)
                throw new BrokenBarrierException();
			//当前线程被中断抛出异常
            if (Thread.interrupted()) {
            	//打破屏障，并且唤醒所有在屏障处等待的线程
                breakBarrier();
                throw new InterruptedException();
            }
			
			//如果当前线程是最后一个到达屏障处的线程
            int index = --count;
            if (index == 0) {  // tripped
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    //如果在实例化时传入的Runable不为空，则执行此命令
                    if (command != null)
                        command.run();
                    ranAction = true;
                    //创建新代，并唤醒所有线程
                    nextGeneration();
                    //结束
                    return 0;
                } finally {
                	//如果执行命令时抛出异常，则打破屏障，唤醒所有在屏障处等待的线程。
                    if (!ranAction)
                        breakBarrier();
                }
            }

            
			//自旋等待
            for (;;) {
                try {
                	//如果没有设置超时时间，直接等待
                    if (!timed)
                        trip.await();
                    else if (nanos > 0L)
                    	//设置超时时间的等待
                        nanos = trip.awaitNanos(nanos);
                } catch (InterruptedException ie) {
                	//如果等待期间线程被中断，则打破屏障，唤醒线程
                    if (g == generation && ! g.broken) {
                        breakBarrier();
                        throw ie;
                    } else {
                        Thread.currentThread().interrupt();
                    }
                }

                if (g.broken)
                    throw new BrokenBarrierException();

                if (g != generation)
                    return index;
				
				//如果在指定时间内还没有被唤醒，打破屏障唤醒线程，抛出异常
                if (timed && nanos <= 0L) {
                    breakBarrier();
                    throw new TimeoutException();
                }
            }
        } finally {
        	//解锁
            lock.unlock();
        }
    }


```

