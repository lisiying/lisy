package org.lisy.study.concurrent;

import java.util.Random;

import org.lisy.common.PrintUtils;

/** 
 * 本实验证明线程的原子性：即线程的事务不会部分成功
 * [实验结论]：会部分成功，这并不是线程的原子性的解释
 * [实验结论]：异常会导致sync原子性被破坏？也就是说，需要预期会抛异常，而做数据清理
 * 
 * @author Lisy
 * @date 2017年12月15日
 * @version 1.0 
 */
public class SyncIsAtomic {
	public String userName;
	public String pwd;
	
	public void changeData(String userName,String pwd,Boolean throwException) throws InterruptedException{
		synchronized (this) {
			//此处sleep是为了保证catch的日志在下面的这行日志前打印；
			//防止catch可能晚于线程进入sync块而导致看上去异步执行了
			Thread.sleep(50);
			PrintUtils.println("线程[{}]获得锁，将抛异常[{}]",Thread.currentThread().getName(),throwException);
			PrintUtils.println("线程" + Thread.currentThread().getName() + "：Before change : UserName[{}]，Pwd[{}]",this.userName,this.pwd);
			this.userName = userName;
			if(throwException) throw new RuntimeException();
			this.pwd = pwd;
		}
	}
	
	public static void main(String[] args) {
		final SyncIsAtomic s = new SyncIsAtomic();
		final Random r = new Random();
		for(int i=0;i<10;i++){
			new Thread(new Runnable() {
				
				public void run() {
					Integer num = r.nextInt(1000);
					try{
						s.changeData(num +"",num +"",r.nextBoolean());
						PrintUtils.println("线程" + Thread.currentThread().getName() + "：After change : UserName[{}]，Pwd[{}]",s.userName,s.pwd);
					}catch(Exception e){
						PrintUtils.println("线程" + Thread.currentThread().getName() + "：After change : UserName[{}]，Pwd[{}]",s.userName,s.pwd);
					}
				}
			}).start();
		}
	}
}
