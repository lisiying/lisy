package org.lisy.study.concurrent;

import java.util.concurrent.Semaphore;

import org.lisy.common.PrintUtils;

/**
 * 信号量用例
 * 最重要方法：release()、acquire()
 * 使用场景：常用于构建对象池
 * @author lisy
 *
 */
public class SemaphoreTest {
	private final static Semaphore objPool = new Semaphore(5);

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				public void run() {
					PrintUtils.println("线程[{}]尝试获取对象池资源",Thread.currentThread().getName());
					try {
						objPool.acquire();
						PrintUtils.println("线程[{}]获得对象，开始处理...",Thread.currentThread().getName());
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					finally {
						//异常或处理结束，则释放对象池资源
						objPool.release();
					}
				}
			}).start();
		}
	}
}
