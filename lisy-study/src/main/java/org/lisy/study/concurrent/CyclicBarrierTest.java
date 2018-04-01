package org.lisy.study.concurrent;

import java.util.concurrent.CyclicBarrier;

import org.lisy.common.PrintUtils;

/**
 * 循环栅栏
 * 重要方法：await、构造方法
 * 
 * 重要说明：
 * 1.与CountDownLatch的区别在于，锁会重新释放
 * 2.其底层实现是ReentrantLock+Condition(构造方法的传参count,await被调用时会递减，递减到0时，将唤醒自身)
 * 3.据说，常用于迭代计算
 * @author lisy
 */
public class CyclicBarrierTest {
	private final static int THREAD_NUMBER = 5;

	private final static CyclicBarrier cb = new CyclicBarrier(THREAD_NUMBER);

	public static void main(String[] args) {
		CyclicBarrierTest unused = new CyclicBarrierTest();
		for (int i = 0; i < THREAD_NUMBER; i++) {
			new Thread(unused.new MyRunnable()).start();
		}
	}

	class MyRunnable implements Runnable {
		private int count = 0;
		public void run() {
			while(count<3) {
				PrintUtils.println("线程[{}]即将进行运算，当前count[{}]，耗时1秒",Thread.currentThread().getName(),count);
				try {
					Thread.sleep(1000);
					cb.await();
					count++;
				} catch (Exception e) {}
			}			
		}
	}
}
