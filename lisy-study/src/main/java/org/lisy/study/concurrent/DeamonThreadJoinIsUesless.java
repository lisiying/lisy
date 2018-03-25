package org.lisy.study.concurrent;

/**
 * 本实验证明，后台进程在前台进程结束后会立即结束；后台进程join可能不会被执行
 * [实验结果]:后台进程在前台进程后join，可能会导致不执行
 * @author lisy
 *
 */
public class DeamonThreadJoinIsUesless {
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			
			public void run() {
				try {
					System.out.println("我将在main之后执行");
					Thread.sleep(1000);
					System.out.println("我很可能无法被打印出来");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		t.join();
		t.setDaemon(true);
		t.start();
		//如果是后台进程，join很可能没用，在main之后可能就会结束
	}
}
