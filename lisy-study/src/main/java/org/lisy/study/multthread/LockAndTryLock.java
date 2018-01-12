package org.lisy.study.multthread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.lisy.common.PrintUtils;

/**
 * ReentrantLock的基础用法测试
 * @author lisy
 *
 */
public class LockAndTryLock {
	//Lock用法：全局变量Lock+一段用lock/unLock包围起来的同步代码
	//Lock原理（猜测）:在Lock中存放一个变量，记录currThread的“锁值”，持有/重复持有锁，则+1;否则-1;
	//直到锁值为0，currThread释放Lock，由其他thread竞争（类似sync的jvm实现）
	//本段说明旨在：解释为什么lock是被一个对象new出来的，却能"到处使用"
	private Lock lock = new ReentrantLock();
	
	public void sycnMethod() throws InterruptedException {
		PrintUtils.println("线程[{}]等待锁...",Thread.currentThread().getName());
		lock.lock();
		PrintUtils.println("线程[{}]获得锁,暂停一会...",Thread.currentThread().getName());
		Thread.sleep(1000);
		lock.unlock();
	}
	
	public static void main(String[] args) {
		//使用同一对象，使lock争对象锁
		final LockAndTryLock obj = new LockAndTryLock();
		for(int i=0;i<5;i++) {
			new Thread(new Runnable() {
				public void run() {
					try {
//						obj.sycnMethod();
						obj.tryLock();
					} catch (InterruptedException e) {}
				}
			},""+i).start();
		}
	}
	
	public void tryLock() throws InterruptedException {
		//使用whlie提升竞争次数
		while(!lock.tryLock()) {
			PrintUtils.println("线程[{}]先干一会别的事情...",Thread.currentThread().getName());
			Thread.sleep(500);
		}
		PrintUtils.println("线程[{}]获得锁,暂停一会...",Thread.currentThread().getName());
		Thread.sleep(1000);
		lock.unlock();
	}
}
