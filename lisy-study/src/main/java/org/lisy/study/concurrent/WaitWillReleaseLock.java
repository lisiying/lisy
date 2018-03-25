package org.lisy.study.concurrent;

import org.lisy.common.PrintUtils;

/**
 * 本实验证明，wait之后线程立即释放锁
 * [实验结果]:1.先notify后wait会使wait停不下（也就是无wait的话，notify无作用）
 * [实验结果]:2.wait()执行完后，会立即释放锁；
 * [实验结果]:3.notify()执行完后，还需要sync内容才执行完才释放锁
 * @author lisy
 */
public class WaitWillReleaseLock {
	class Wait extends Thread{
		public String str;
		
		@Override
		public void run() {
			super.run();
			synchronized (str) {
				PrintUtils.println("当前[{}]进程[{}],执行wait前，持有锁，即将释放", 
						this.getClass().getTypeName(),
						Thread.currentThread().getName());
				try {
					str.wait();
					PrintUtils.println("当前[{}]进程[{}],wait继续执行，持有锁", 
							this.getClass().getTypeName(),
							Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class Notify extends Thread{
		public String str;
		
		@Override
		public void run() {
			super.run();
			synchronized (str) {
				PrintUtils.println("当前[{}]进程[{}],执行notify前，持有锁，即将释放", 
						this.getClass().getTypeName(),
						Thread.currentThread().getName());
				str.notify();
				//暂停一会，方便观察
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				PrintUtils.println("当前[{}]进程[{}],notify后，继续持有锁", 
						this.getClass().getTypeName(),
						Thread.currentThread().getName());
			}
			PrintUtils.println("当前[{}]进程[{}],notify后，sync后，释放锁", 
					this.getClass().getTypeName(),
					Thread.currentThread().getName());
		}
	}
	
	//与notify竞争锁
	class CompleteWithNotify extends Thread{
		public String str;
		@Override
		public void run() {
			synchronized (str) {
				PrintUtils.println("当前[{}]进程[{}],将与notify竞争锁", 
						this.getClass().getTypeName(),
						Thread.currentThread().getName());
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		WaitWillReleaseLock w = new WaitWillReleaseLock();
		Wait t1 = w.new Wait();
		Notify t2 = w.new Notify();
		CompleteWithNotify t3 = w.new CompleteWithNotify();
		t1.str = "str";
		t2.str = "str";
		t3.str = "str";
		t1.start();
		//先notify，再wait：会有问题，wait停不下来
		t2.start();
		t3.start();
	}
}
