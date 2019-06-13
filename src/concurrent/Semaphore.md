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

- 源码
```
	public class Semaphore implements java.io.Serializable {
	    private static final long serialVersionUID = -3222578661600680210L;
	    //AQS同步器的内部实现类
	    private final Sync sync;
	
	    abstract static class Sync extends AbstractQueuedSynchronizer {
	        private static final long serialVersionUID = 1192457210091910933L;
	        
	        
	        //state值代表许可数量
	        Sync(int permits) {
	            setState(permits);
	        }
	
	        final int getPermits() {
	            return getState();
	        }
	        
	        
	        //以不公平的方式尝试获取许可
	        final int nonfairTryAcquireShared(int acquires) {
	            for (;;) {
	                int available = getState();
	                int remaining = available - acquires;
	                //有可用许可，CAS更新。没有可用许可直接返回
	                if (remaining < 0 || compareAndSetState(available, remaining))
	                    return remaining;
	            }
	        }
	
	        protected final boolean tryReleaseShared(int releases) {
	            for (;;) {
	                int current = getState();
	                int next = current + releases;
	                if (next < current) // 防止releases 为负数
	                    throw new Error("Maximum permit count exceeded");
	                //cas 更新
	                if (compareAndSetState(current, next))
	                    return true;
	            }
	        }
	        
	        //	减去指定许可
	        final void reducePermits(int reductions) {
	            for (;;) {
	                int current = getState();
	                int next = current - reductions;
	                if (next > current) // 防止减去负数
	                    throw new Error("Permit count underflow");
	                if (compareAndSetState(current, next))
	                    return;
	            }
	        }
	        
	        //把可用许可设置为0
	        final int drainPermits() {
	            for (;;) {
	                int current = getState();
	                if (current == 0 || compareAndSetState(current, 0))
	                    return current;
	            }
	        }
	    }
	
	    //不公平的同步器实现，在等待获取许可 可用时先来的线程不一定先获取许可（效率高）
	    static final class NonfairSync extends Sync {
	        private static final long serialVersionUID = -2694183684443567898L;
	
	        NonfairSync(int permits) {
	            super(permits);
	        }
	
	        protected int tryAcquireShared(int acquires) {
	            return nonfairTryAcquireShared(acquires);
	        }
	    }
	
	    //公平的同步器实现
	    static final class FairSync extends Sync {
	        private static final long serialVersionUID = 2014338818796000944L;
	
	        FairSync(int permits) {
	            super(permits);
	        }
	
	        protected int tryAcquireShared(int acquires) {
	            for (;;) {
	            	//判断此线程前边是否有其他的线程在等待
	                if (hasQueuedPredecessors())
	                    return -1;
	                int available = getState();
	                int remaining = available - acquires;
	                if (remaining < 0 ||
	                	//cas更新许可数量
	                    compareAndSetState(available, remaining))
	                    return remaining;
	            }
	        }
	    }
	    
	    //构造方法
	    public Semaphore(int permits) {
	        sync = new NonfairSync(permits);
	    }
	    public Semaphore(int permits, boolean fair) {
	        sync = fair ? new FairSync(permits) : new NonfairSync(permits);
	    }
	    
	    //获取许可，如果没有可用许可会被阻塞
	    public void acquire() throws InterruptedException {
	        sync.acquireSharedInterruptibly(1);
	    }
	    public void acquireUninterruptibly() {
	        sync.acquireShared(1);
	    }
	    public boolean tryAcquire() {
	        return sync.nonfairTryAcquireShared(1) >= 0;
	    }
	    public boolean tryAcquire(long timeout, TimeUnit unit)
	        throws InterruptedException {
	        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
	    }
	    //是否许可
	    public void release() {
	        sync.releaseShared(1);
	    }
	    
	    public void acquire(int permits) throws InterruptedException {
	        if (permits < 0) throw new IllegalArgumentException();
	        sync.acquireSharedInterruptibly(permits);
	    }
	
	    public void acquireUninterruptibly(int permits) {
	        if (permits < 0) throw new IllegalArgumentException();
	        sync.acquireShared(permits);
	    }
	
	    public boolean tryAcquire(int permits) {
	        if (permits < 0) throw new IllegalArgumentException();
	        return sync.nonfairTryAcquireShared(permits) >= 0;
	    }
	
	    public boolean tryAcquire(int permits, long timeout, TimeUnit unit)
	        throws InterruptedException {
	        if (permits < 0) throw new IllegalArgumentException();
	        return sync.tryAcquireSharedNanos(permits, unit.toNanos(timeout));
	    }
	
	    public void release(int permits) {
	        if (permits < 0) throw new IllegalArgumentException();
	        sync.releaseShared(permits);
	    }
	
	    public int availablePermits() {
	        return sync.getPermits();
	    }
	
	    public int drainPermits() {
	        return sync.drainPermits();
	    }
	
	    protected void reducePermits(int reduction) {
	        if (reduction < 0) throw new IllegalArgumentException();
	        sync.reducePermits(reduction);
	    }
	
	    public boolean isFair() {
	        return sync instanceof FairSync;
	    }
	
	    public final boolean hasQueuedThreads() {
	        return sync.hasQueuedThreads();
	    }
	
	    public final int getQueueLength() {
	        return sync.getQueueLength();
	    }
	
	    protected Collection<Thread> getQueuedThreads() {
	        return sync.getQueuedThreads();
	    }
	
	    public String toString() {
	        return super.toString() + "[Permits = " + sync.getPermits() + "]";
	    }
	}
```