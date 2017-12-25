package org.lisy.study.multthread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lisy.common.PrintUtils;

/**
 * 本类作废，当我没写
 * 消费者与生产者模型：使用多个锁优化性能问题
 * [实验结果]:似乎会造成死锁?
 * @author lisy
 *
 */
@SuppressWarnings("unused")
public class MultProdAndMultCust {
	private final Random random = new Random();
	class Producer extends Thread{ 
		private Object pLock;
		private Object cLock;
		private List<String> list;
		
		public void push() throws InterruptedException {
			synchronized (cLock) {
				while(list.size()!=0) {
					cLock.wait();
				}
				Thread.sleep(100);
				list.add(Thread.currentThread().getName()+random.nextInt(10));
				PrintUtils.println("生产了消息[{}]，list长度[{}]", Thread.currentThread().getName(),list.size());
				//通知所有的消费者
				cLock.notifyAll();
			}
		}
		
		@Override
		public void run() {
			super.run();
			try {
				this.push();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	class Customer extends Thread{
		private Object pLock;
		private Object cLock;
		private List<String> list;
		
		public void pop() throws InterruptedException {
			synchronized (pLock) {
				while(list.size()==0) {
					pLock.wait();
				}
				Thread.sleep(100);
				list.remove(list.size()-1);
				PrintUtils.println("消费了消息[{}]，list长度[{}]", Thread.currentThread().getName(),list.size());
				//通知所有的生产者
				pLock.notifyAll();
			}
		}
		
		@Override
		public void run() {
			super.run();
			try {
				this.pop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		MultProdAndMultCust m = new MultProdAndMultCust();
		Producer p1 = m.new Producer();
		Producer p2 = m.new Producer();
		Customer c1 = m.new Customer();
		Customer c2 = m.new Customer();
		Object cLock = new Object();
		Object pLock = new Object();
		List<String> list = new ArrayList<String>();
		p1.cLock = cLock;p1.list = list;p1.setName("P1");
		p2.cLock = cLock;p2.list = list;p2.setName("P2");
		c1.pLock = pLock;c1.list = list;c1.setName("C1");
		c2.pLock = pLock;c2.list = list;c2.setName("C2");
		p1.start();
		p2.start();
		c1.start();
		c2.start();
		Thread.sleep(2000);
	}
}
