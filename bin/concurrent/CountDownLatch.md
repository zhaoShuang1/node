### CountDownLatch
- 允许一个或多个线程等待其他线程执行完成，CountDownLatch的构造方法接受一个int类型的参数（不能为负数）。调用await()方法阻塞当前线程
直到给定数值为0时，调用countDown()方法使给定int值减1（这个方法是原子操作），例子：
```
	public static void main(String[] args) throws Exception {
		CountDownLatch cdl = new CountDownLatch(3);
		new Thread(()->{
			for(int i = 0 ; i < 10 ; i ++ ) {
				new Thread(()->{
					//递减锁存器的计数，如果计数到达零，则释放所有等待的线程。
					cdl.countDown();
					System.out.println(Thread.currentThread().getName()+" done");
				},"Thread-"+i).start();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
		}).start();
		
		//主线程等待
		cdl.await();
		System.out.println(Thread.currentThread().getName()+" done");
	}

```
- 源码分析
```
	public class CountDownLatch {
	    /**
	     * CountDownLatch 同步器，state 代表count
	     */
	    private static final class Sync extends AbstractQueuedSynchronizer {
	        private static final long serialVersionUID = 4982264981922014374L;
	
	        Sync(int count) {
	            setState(count);
	        }
	
	        int getCount() {
	            return getState();
	        }
	        //尝试获得共享锁
	        protected int tryAcquireShared(int acquires) {
	            return (getState() == 0) ? 1 : -1;
	        }
	        //尝试释放共享锁
	        protected boolean tryReleaseShared(int releases) {
	            for (;;) {
	                int c = getState();
	                if (c == 0)
	                    return false;
	                int nextc = c-1;
	                if (compareAndSetState(c, nextc))
	                	//如果释放完成后state等于0时，就可以唤醒被阻塞的线程
	                    return nextc == 0;
	            }
	        }
	    }
	
	    private final Sync sync;
	    //唯一构造函数，count 小于 0 抛出异常
	    public CountDownLatch(int count) {
	        if (count < 0) throw new IllegalArgumentException("count < 0");
	        //实例化同步器，设置state
	        this.sync = new Sync(count);
	    }
	    
	    //使当前线程在锁存器倒计数至零之前一直等待，除非线程被中断。实际上是调用了同步器的acquireSharedInterruptibly(1)方法下边再看这个方法。
	    public void await() throws InterruptedException {
	        sync.acquireSharedInterruptibly(1);
	    }
	    public boolean await(long timeout, TimeUnit unit)
	        throws InterruptedException {
	        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
	    }
	    
	    //递减锁存器的计数，如果计数到达零，则释放所有等待的线程。实际上是调用了同步器的releaseShared(1)方法下边再看这个方法。
	    public void countDown() {
	        sync.releaseShared(1);
	    }
	    
	    //返回当前的计数，state就是count
	    public long getCount() {
	        return sync.getCount();
	    }
	    public String toString() {
	        return super.toString() + "[Count = " + sync.getCount() + "]";
	    }
	}
	
	
	public abstract class AbstractQueuedSynchronizer
	    extends AbstractOwnableSynchronizer
	    implements java.io.Serializable {
	    
	    //获取可中断的共享锁
		public final void acquireSharedInterruptibly(int arg)
	            throws InterruptedException {
	        //如果当前线程已中断则抛出异常
	        if (Thread.interrupted())
	            throw new InterruptedException();
			//tryAcquireShared()方法只有state 等于0时才可被获取，否则阻塞当前线程    
	        if (tryAcquireShared(arg) < 0)
	            doAcquireSharedInterruptibly(arg);
	    }
	    
	    //获取共享锁，可中断的
	    private void doAcquireSharedInterruptibly(int arg)
       		throws InterruptedException {
       		//把节点添加至队列
	        final Node node = addWaiter(Node.SHARED);
	        boolean failed = true;
	        try {
	            for (;;) {
	                final Node p = node.predecessor();
	                //如果前一个元素是队首（也就是说当前元素的的一个元素）
	                if (p == head) {
	                	//尝试获取锁
	                    int r = tryAcquireShared(arg);
	                    if (r >= 0) {
	                        setHeadAndPropagate(node, r);
	                        p.next = null; // help GC
	                        failed = false;
	                        return;
	                    }
	                }
	                //检测当前线程是否可以被阻塞，是的话就阻塞，如果线程已经被中断则抛出异常
	                if (shouldParkAfterFailedAcquire(p, node) &&
	                    parkAndCheckInterrupt())
	                    throw new InterruptedException();
	            }
	        } finally {
	        	//如果获取锁失败，或抛出异常，则把当前node从队列中清除
	            if (failed)
	                cancelAcquire(node);
	        }
    	}
	    
	    //把当前线程添加至队列，使用给定的模式
	    private Node addWaiter(Node mode) {
	        Node node = new Node(Thread.currentThread(), mode);
	        // 尝试直接插入节点，如果失败调用enq方法插入节点
	        Node pred = tail;
	        if (pred != null) {
	            node.prev = pred;
	            if (compareAndSetTail(pred, node)) {
	                pred.next = node;
	                return node;
	            }
	        }
	        enq(node);
	        return node;
    	}
	    
	    //插入节点，如果是第一个的话，则初始化。
	    private Node enq(final Node node) {
	        for (;;) {
	            Node t = tail;
	            //如果队列中没有节点，则初始化
	            if (t == null) { // Must initialize
	                if (compareAndSetHead(new Node()))
	                    tail = head;
	            } else {
	            	//插入节点
	                node.prev = t;
	                if (compareAndSetTail(t, node)) {
	                    t.next = node;
	                    return t;
	                }
	            }
	        }
    	}
	    
	    
	    //释放共享锁
	    public final boolean releaseShared(int arg) {
	        //尝试释放锁
	        if (tryReleaseShared(arg)) {
	        	//唤醒所有被阻塞的线程
	            doReleaseShared();
	            return true;
	        }
	        return false;
	    }
	    //唤醒线程
	    private void doReleaseShared() {
	        for (;;) {
	            Node h = head;
	            //确定CLH锁队列中有节点
	            if (h != null && h != tail) {
	                int ws = h.waitStatus;
	                //只有waitStatus为SIGNAL 时才去唤醒
	                if (ws == Node.SIGNAL) {
	                	//尝试去更新waitStatus到初始状态，更新成功才去唤醒线程，否则继续循环
	                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
	                        continue;            // loop to recheck cases
	                    //唤醒线程
	                    unparkSuccessor(h);
	                }
	                else if (ws == 0 &&
	                         !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
	                    continue;                // loop on failed CAS
	            }
	            if (h == head)                   // loop if head changed
	                break;
	        }
    	}
	    
	}
```