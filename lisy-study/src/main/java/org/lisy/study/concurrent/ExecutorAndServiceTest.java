package org.lisy.study.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.lisy.common.PrintUtils;

/**
 * Executor和ExecutorService用例
 * 
 * 记一道曾经被问倒的面试题...第一种实现方式：Future
 * 题目是这样的...一个主线程查询任务，去调两个线程的接口获取结果；
 * 5秒内，如果子线程均拿到结果，就合并结果并返回；
 * 如果大于5秒，则无论查询线程是否获得结果，都将返回，并将查询结果合并到主线程的结果；
 * 
 * 也可以这么理解：在两个线程上的数据库做查询，并在超时时间内将结果合并返回
 * 
 * 发现了一个有意思的事情；请看get方法
 * @author lisy
 */
public class ExecutorAndServiceTest {
	private final static ExecutorService es = new ThreadPoolExecutor
			//这个最大线程数参数值得思考一下。等于1的时候会发生有意思的事情
			(2, 2, Long.MAX_VALUE,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
	
	private final static Random r = new Random();
	public static void main(String[] args) throws Exception {
		final ExecutorAndServiceTest unused = new ExecutorAndServiceTest();
		Data target = unused.new Data(20);
		List<Future<Data>> fts = new ArrayList<Future<Data>>();
		
		for(int i=0;i<2;i++) {
			Future<Data> ft = es.submit(new Callable<Data>() {
				public Data call() throws Exception {
					//休眠小于5秒，便于观察结果
					Thread.sleep(4500);
					//休眠大于5秒，观察结果：主线程等待超时
//					Thread.sleep(6000);
					
					Data d = unused.new Data(10);
					//概率翻车，找不到任何结果
					if(r.nextBoolean()) {
						//填充结果的随便写了，反正不重要
						for(int i=0;i<10;i++) {
							d.add(r.nextInt(1000)+"");
						}
					}else {
						PrintUtils.println("本线程翻车了~");
					}
					
					return d;
				}
			});
			
			fts.add(ft);
		}
		
		Long preTime = System.currentTimeMillis();
		
		try {
			for(Future<Data> f : fts) {
				//这样写大概不对吧？应该会等待10秒
				//实验结果却出乎预期；并不会等待2倍时间；也就是说，这么写就可以达到任务目的
				//猜想：for循环->get(阻塞，立刻返回)->for循环->get(阻塞，立刻返回)->4秒后，同时唤醒两个get；关键就是get拿不到结果会立刻返回
				target.addAll(f.get(5L, TimeUnit.SECONDS));
				//值得注意的是，主线程不一定能在5秒后响应；当线程池不够时就会出现这种情况。
			}
			PrintUtils.println("主线程总共等待时长：" + (System.currentTimeMillis() - preTime)/1000 + "秒");
		//这个catch应该写在for循环外面，否则打印出来的日志有点怪
		} catch(TimeoutException e) {
			PrintUtils.println("等待超时，未获取到结果");
		}
		
		PrintUtils.println("最终长度：" + target.size());
		PrintUtils.println("最终结果：" + target.toString());
	}
	
	@SuppressWarnings("serial")
	class Data extends ArrayList<String>{
		public Data(Integer size) {
			super(size);
		}
	}
}
