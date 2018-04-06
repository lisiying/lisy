package org.lisy.study.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.lisy.common.PrintUtils;

/**
 * 一道多线程查询的面试题的第二种实现方式...InvokeAll
 * 
 * 凭良心讲，其实跟前一种实现方式区别不大...
 * 只是前例稍底层一点,而本例更安全一点(因为invokeAll写得比较好)
 * 
 * @see ExecutorAndServiceTest
 * @author lisy
 *
 */
public class ExecutorServiceInvokeAllTest {
	private final static ExecutorService es = 
			new ThreadPoolExecutor(2, 3, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	
	public static void main(String args[]) throws InterruptedException, ExecutionException {
		ExecutorServiceInvokeAllTest unused = new ExecutorServiceInvokeAllTest();
		
		List<Callable<Data>> tasks = new ArrayList<Callable<Data>>(2);
		tasks.add(unused.new MyCallable());
		tasks.add(unused.new MyCallable());
		Data target = unused.new Data(20);

		//特别注意，下面这行代码为什么放在这里，因为invokeAll方法的实现，会直接建立一个RunnanbleFuture并调用get()
		Long preTime = System.currentTimeMillis();
		List<Future<Data>> fts = es.invokeAll(tasks,5,TimeUnit.SECONDS);
		for(Future<Data> ft : fts) {
			target.addAll(ft.get());
		} 
		PrintUtils.println("主线程总共等待时长：" + (System.currentTimeMillis() - preTime)/1000 + "秒");
		
		PrintUtils.println("最终长度：" + target.size());
		PrintUtils.println("最终结果：" + target.toString());
	}
	
	class MyCallable implements Callable<Data>{
		private final Random r = new Random();
		
		public Data call() throws Exception {
			Thread.sleep(4500);
			
			Data d = new Data(10);
			if(r.nextBoolean()) {
				for(int i = 0;;) {
					d.add(r.nextInt(1000)+"");
					i++;
					if(i==10) break;
				}
			}else {
				PrintUtils.println("本线程翻车了~");
			}
			return d;
		}
	}
	
	@SuppressWarnings("serial")
	class Data extends ArrayList<String>{
		public Data(int size){
			super(size);
		}
	}
}
