package org.lisy.study.concurrent;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.lisy.common.PrintUtils;
/**
 * 多生产多消费，利用选择性通知提升性能实验
 * [实验结果]:尚无结果
 * @see MultProdAndMultCust
 * @author lisy
 *
 */
public class MultProdAndMulctCustWithCondition {
	class Producer implements Runnable{
		LockList obj;
		
		public void run() {
			try {
				obj.publish();
			} catch (InterruptedException e) {}
		}
	}
	
	class Customer implements Runnable{
		LockList obj;
		
		public void run() {
			try {
				obj.consume();
			} catch (InterruptedException e) {}
		}
	}
	
	
	
	@SuppressWarnings("serial")
	class LockList extends ArrayList<String> {
		private ReentrantLock lock = new ReentrantLock();
		private Condition conP = lock.newCondition();
		private Condition conC = lock.newCondition();
		
		public void publish() throws InterruptedException {
			lock.lock();
			
			if(!this.isEmpty()) {
				PrintUtils.println("线程[{}]等待取消息,数据长度[{}],消费队列[{}]",Thread.currentThread().getName(),this.size(),lock.getWaitQueueLength(conC));
				conC.signalAll();
				conP.await();
			}
			else {
//			while(this.size()==0){
				this.add("val");
				PrintUtils.println("线程[{}]投放消息,数据长度[{}]",Thread.currentThread().getName(),this.size());
				conC.signalAll();
				lock.unlock();
			}
		}
		
		public void consume() throws InterruptedException {
			lock.lock();
			
			if(this.isEmpty()) {
				PrintUtils.println("线程[{}]等待投放消息,数据长度[{}],生产队列[{}]",Thread.currentThread().getName(),this.size(),lock.getWaitQueueLength(conP));
				conP.signalAll();
				conC.await();
			}else {
				this.remove(0);
				PrintUtils.println("线程[{}]取消息,数据长度[{}]",Thread.currentThread().getName(),this.size());
				conP.signalAll();
				lock.unlock();
				return;
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		MultProdAndMulctCustWithCondition m = new MultProdAndMulctCustWithCondition();
		
		LockList obj = m.new LockList();
		
		Random r = new Random();
		for(int i=0;i<1000;i++) {
			if(r.nextBoolean()) {
				Producer p = m.new Producer();
				p.obj = obj;
				Thread t = new Thread(p,"P"+i);
				t.setDaemon(true);
				t.start();
			}else {
				Customer c = m.new Customer();
				c.obj = obj;
				Thread t = new Thread(c,"C"+i);
				t.setDaemon(true);
				t.start();
			}
		}
		
		Thread.sleep(5000);
	}
}
