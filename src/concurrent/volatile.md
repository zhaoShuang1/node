### volatile 作者 Doug lea [原文](http://gee.cs.oswego.edu/dl/cpj/jmm.html) [译文](http://ifeve.com/syn-jmm-volatile/)
- 从原子性，可见性和有序性的角度分析，声明为volatile字段的作用相当于一个类通过get/set同步方法保护普通字段，如下：
    ```
        final class VFloat {
            private float value;
            final synchronized void set(float f) { value = f; }
            final synchronized float get()       { return value; }
        }
    ```


- 与使用synchronized相比，声明一个volatile字段的区别在于没有涉及到锁操作。但特别的是对volatile字段进行“++”这样的读写操作不会被当做原子操作执行。

- 另外，有序性和可见性仅对volatile字段进行一次读取或更新操作起作用。声明一个引用变量为volatile，不能保证通过该引用变量访问到的非volatile变量的可见性。同理，声明一个数组变量为volatile不能确保数组内元素的可见性。volatile的特性不能在数组内传递，因为数组里的元素不能被声明为volatile。

- 由于没有涉及到锁操作，声明volatile字段很可能比使用同步的开销更低，至少不会更高。但如果在方法内频繁访问volatile字段，很可能导致更低的性能，这时还不如锁住整个方法。

- 如果你不需要锁，把字段声明为volatile是不错的选择，但仍需要确保多线程对该字段的正确访问。可以使用volatile的情况包括：

    - 该字段不遵循其他字段的不变式。
    - 对字段的写操作不依赖于当前值。
    - 没有线程违反预期的语义写入非法值。
    - 读取操作不依赖于其它非volatile字段的值。
- 当只有一个线程可以修改字段的值，其它线程可以随时读取，那么把字段声明为volatile是合理的。例如，一个名叫Thermometer(中文：体温计)的类，可以声明temperature字段为volatile。正如在3.4.2节所讨论，一个volatile字段很适合作为完成某些工作的标志。另一个例子在4.4节有描述，通过使用轻量级的执行框架使某些同步工作自动化，但是仍需把结果字段声明为volatile，使其对各个任务都是可见的。