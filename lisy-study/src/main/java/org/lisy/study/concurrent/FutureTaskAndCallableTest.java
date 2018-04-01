package org.lisy.study.concurrent;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.lisy.common.PrintUtils;

/**
 * FutureTask用例
 * 最重要的方法：get()、call()
 * 
 * 几个重要的点：
 * 1.FutureTask有2个构造方法：Callable(看起来灵活性高一些，否则需要定义一个全局变量，如本例的result?)或Runnable
 * 2.FutureTask的get方法是阻塞方法(也就是说，调用了get的话，会阻塞直到callable拿到结果),非常重要
 * 3.FutureTask(Runnable,V)底层是用Excutor实现的
 * 4.(最重要)业务代码应该是写在Callable.call里的(如果构造方法是Callable的话)
 * @author lisy
 *
 */
public class FutureTaskAndCallableTest {
	//总感觉使用全局变量不好。可能有安全问题？代码灵活性低？
	private static MyResult result2;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		FutureTaskAndCallableTest unused = new FutureTaskAndCallableTest();
		//任务1：配合Callable使用
		FutureTask<MyResult> ft1 = new FutureTask<MyResult>(unused.new MyCallable());
		new Thread(ft1).start();
		//正常说，下面这行代码会先执行
		PrintUtils.println("等待产品A组装完成");
		MyResult result1 = ft1.get();
		PrintUtils.println(result1);
		
		//任务2：Runnable模式
		result2 = unused.new MyResult();
		FutureTask<MyResult> ft2 = new FutureTask<MyResult>(unused.new MyRunnable(),result2);
		new Thread(ft2).start();
		PrintUtils.println("等待产品B组装完成");
		PrintUtils.println(ft2.get());
	}
	
	class MyRunnable implements Runnable{
		public void run() {
			PrintUtils.println("即将进行产品B组装，耗时2秒...");
			try {
				Thread.sleep(2000);
				result2.setValue(new StringBuilder()
					.append("完成任务，产品B编号:")
					.append(UUID.randomUUID()).toString());
			} catch (InterruptedException e) {}
			
		}
		
	}
	
	class MyCallable implements Callable<MyResult>{
		public MyResult call() throws Exception {
			PrintUtils.println("即将进行A产品组装，耗时2秒...");
			Thread.sleep(2000);
			return new MyResult().setValue(new StringBuilder()
					.append("完成任务，产品A编号:")
					.append(UUID.randomUUID()).toString());
		}
		
	}
	
	class MyResult{
		String value;
		
		public String toString() {
			return value;
		}
		
		public MyResult setValue(String value) {
			this.value = value;
			return this;
		}
	}
}
