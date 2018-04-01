package org.lisy.study.concurrent;

import java.util.concurrent.CountDownLatch;

import org.lisy.common.PrintUtils;

/**
 * 计数闭锁用例
 * CountDownLatch特性：若其count减到0，他会notifyAll那些await的线程
 * @author lisy
 *
 */
public class CountDownLatchTest {
	private final static int THREAD_NUMBER = 5;
	private final static CountDownLatch startGate = new CountDownLatch(1);//启动门闭锁
	private final static CountDownLatch endGate = new CountDownLatch(THREAD_NUMBER);//到达门闭锁
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatchTest t = new CountDownLatchTest();
		for(int i=0;i<THREAD_NUMBER;i++) {
			new Thread(t.new MyThread()).start();
		}
		//主线程暂停一会，再打开启动门闭锁
		Thread.sleep(1000);
		//打开闭锁，子线程将犹如脱缰的野猪
		startGate.countDown();
		PrintUtils.println("主线程等待end闭锁...");
		endGate.await();
		PrintUtils.println("end闭锁打开，唤醒主线程");
	}

	/*
	 * + + + + +
	 * + 主线程 + --->start.count=0------->等待end
	 * + + + + +				|		        ↑
	 * 					   唤醒	     	   唤醒
	 * + + + + +				↓			    |
	 * + 子线程 + ------->等待start----->end.count=0
	 * + + + + +
	 * 
	 */
	class MyThread implements Runnable{
		public void run() {
			//子线程等待启动指令
			try {
				PrintUtils.println("子线程[{}]等待start闭锁...",Thread.currentThread().getName());
				startGate.await();
				PrintUtils.println("start闭锁打开，唤醒子线程[{}]",Thread.currentThread().getName());
			} catch (InterruptedException e) {}
			try {
				Thread.sleep(500);
				//通知到达门闭锁递减，直到为0的时候，endGate会唤醒所有await的线程(就是主线程)
				endGate.countDown();
			} catch (InterruptedException e) {}
		}
	}
}
