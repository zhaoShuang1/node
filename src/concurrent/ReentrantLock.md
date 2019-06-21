### ReentrantLock
- 一个通过AQS同步器实现的可重入锁，类似于synchronized，但是比synchronized更灵活。
	```
		final ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			//do something
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	```
- 源码解析
    ```
      public class ReentrantLock implements Lock, java.io.Serializable {
           private static final long serialVersionUID = 7373984872572414699L;
           private final Sync sync;
        
           abstract static class Sync extends AbstractQueuedSynchronizer {
               private static final long serialVersionUID = -5179523762034025860L;
        
               abstract void lock();
        
               final boolean nonfairTryAcquire(int acquires) {
                   final Thread current = Thread.currentThread();
                   int c = getState();
                   
                   //尝试获取锁
                   if (c == 0) {
                       if (compareAndSetState(0, acquires)) {
                           setExclusiveOwnerThread(current);
                           return true;
                       }
                   }
                   else if (current == getExclusiveOwnerThread()) {//判断是否可以重入
                       int nextc = c + acquires;
                       if (nextc < 0) // overflow
                           throw new Error("Maximum lock count exceeded");
                       setState(nextc);
                       return true;
                   }
                   return false;
               }
               
               
               protected final boolean tryRelease(int releases) {
                   int c = getState() - releases;
                   if (Thread.currentThread() != getExclusiveOwnerThread())	//判断释放锁的线程是否为线程的持有者
                       throw new IllegalMonitorStateException();
                   boolean free = false;
                   if (c == 0) {	//如果当前线程已经完全释放锁了，返回true ，并把锁的持有者设置为空
                       free = true;
                       setExclusiveOwnerThread(null);
                   }
                   setState(c);
                   return free;
               }
               
               //判断当前线程是否是获取锁的线程
               protected final boolean isHeldExclusively() {
                   return getExclusiveOwnerThread() == Thread.currentThread();
               }
               
               //创建一个Condition
               final ConditionObject newCondition() {
                   return new ConditionObject();
               }
        
               //获取锁的持有线程
               final Thread getOwner() {
                   return getState() == 0 ? null : getExclusiveOwnerThread();
               }
               
               //获取锁被获取的次数
               final int getHoldCount() {
                   return isHeldExclusively() ? getState() : 0;
               }
               //判断锁是否已被获取
               final boolean isLocked() {
                   return getState() != 0;
               }
        
               private void readObject(java.io.ObjectInputStream s)
                   throws java.io.IOException, ClassNotFoundException {
                   s.defaultReadObject();
                   setState(0); // reset to unlocked state
               }
           }
           
           static final class NonfairSync extends Sync {
               private static final long serialVersionUID = 7316153563782823691L;
        
               final void lock() {
                   if (compareAndSetState(0, 1))	//直接尝试获取锁
                       setExclusiveOwnerThread(Thread.currentThread());
                   else
                       acquire(1);				//获取失败后再次尝试（调用tryAcquire方法），如果还是失败，则把当前线程加入阻塞队列，并且阻塞当前线程
               }
        
               protected final boolean tryAcquire(int acquires) {
                   return nonfairTryAcquire(acquires);
               }
           }
        
           static final class FairSync extends Sync {
               private static final long serialVersionUID = -3000897897090466540L;
        
               final void lock() {
                   acquire(1);
               }
        
               protected final boolean tryAcquire(int acquires) {
                   final Thread current = Thread.currentThread();
                   int c = getState();
                   if (c == 0) {//如果锁不被任何线程持有，则尝试获取锁
                       if (!hasQueuedPredecessors() &&	//判断CLH队列有没有正在等待获取锁的线程
                           compareAndSetState(0, acquires)) {// 更新锁状态
                           setExclusiveOwnerThread(current); //把当前线程设置为持有者
                           return true;
                       }
                   }
                   else if (current == getExclusiveOwnerThread()) {	//判断是否可重入
                       int nextc = c + acquires;
                       if (nextc < 0)
                           throw new Error("Maximum lock count exceeded");
                       setState(nextc);
                       return true;
                   }
                   return false;
               }
           }
           
           /**
            * 默认创建的是非公平锁，非公平锁和公平锁唯一的区别的是，公平锁在尝试获取锁时会判断一下当前有没有正在排队等待获取锁的线程。
            * 如果有直接获取失败，如果没有的话才会去尝试获取锁。非公平锁直接就尝试获取，不管有没有在等待获取锁的线程。相对来说非公平锁的效率
            * 要高于公平锁
            */
           public ReentrantLock() {
               sync = new NonfairSync();
           }
        
           public ReentrantLock(boolean fair) {
               sync = fair ? new FairSync() : new NonfairSync();
           }
           public void lock() {
               sync.lock();
           }
        
           public void lockInterruptibly() throws InterruptedException {
               sync.acquireInterruptibly(1);
           }
        
           public boolean tryLock() {
               return sync.nonfairTryAcquire(1);
           }
        
           public boolean tryLock(long timeout, TimeUnit unit)
                   throws InterruptedException {
               return sync.tryAcquireNanos(1, unit.toNanos(timeout));
           }
           
           /**
            * 释放锁，如果锁已经被完全释放了（锁的重入问题），则唤醒在队列中等待的线程
            */
           public void unlock() {
               sync.release(1);
           }
           
           /**
            * 创建一个Condition
            */
           public Condition newCondition() {
               return sync.newCondition();
           }
           
           public int getHoldCount() {
               return sync.getHoldCount();
           }
        
           public boolean isHeldByCurrentThread() {
               return sync.isHeldExclusively();
           }
        
           public boolean isLocked() {
               return sync.isLocked();
           }
        
           public final boolean isFair() {
               return sync instanceof FairSync;
           }
        
           protected Thread getOwner() {
               return sync.getOwner();
           }
           
           //判断等待队列中是否有线程在等待
           public final boolean hasQueuedThreads() {
               return sync.hasQueuedThreads();
           }
           
           //判断指定线程是否在排队
           public final boolean hasQueuedThread(Thread thread) {
               return sync.isQueued(thread);
           }
        
           //获取等待队列的长度
           public final int getQueueLength() {
               return sync.getQueueLength();
           }
           
           //得到等待队列中的线程
           protected Collection<Thread> getQueuedThreads() {
               return sync.getQueuedThreads();
           }
           
           //判断这个condition上是否有等待的线程
           public boolean hasWaiters(Condition condition) {
               if (condition == null)
                   throw new NullPointerException();
               if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
                   throw new IllegalArgumentException("not owner");
               return sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject)condition);
           }
        
           public int getWaitQueueLength(Condition condition) {
               if (condition == null)
                   throw new NullPointerException();
               if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
                   throw new IllegalArgumentException("not owner");
               return sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject)condition);
           }
        
           protected Collection<Thread> getWaitingThreads(Condition condition) {
               if (condition == null)
                   throw new NullPointerException();
               if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
                   throw new IllegalArgumentException("not owner");
               return sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject)condition);
           }
        
           public String toString() {
               Thread o = sync.getOwner();
               return super.toString() + ((o == null) ?
                                          "[Unlocked]" :
                                          "[Locked by thread " + o.getName() + "]");
           }
        }
    ```