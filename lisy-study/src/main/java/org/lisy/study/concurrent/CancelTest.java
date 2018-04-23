package org.lisy.study.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.lisy.common.PrintUtils;

/**
 * 主要试试看FutureTask的取消
 * 实验结果：取消操作其实跟interrupt差不多
 * 1.当任务已经开始了，取消就无效(在没有中断策略的前提下)，FutureTask会执行到结束(联系被废弃的Thread.stop来看，这还是比较安全的策略)
 * 2.否则任务不会开始，并且会抛一个CancellationException异常
 * @author sasori
 *
 */
public class CancelTest {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		FutureTask<Object> task1 = new FutureTask<Object>(new Callable<Object>() {

			public Object call() throws Exception {
				PrintUtils.println("任务1：即将休眠2秒；但这行代码将不会被执行到，除非cancel操作在后面执行");
				try {
					Thread.sleep(2000);
				}catch(InterruptedException e) {}
				return new Object();
			}
		});
		//用这种方式启动FutureTask
		new Thread(task1).start();
		//这里休眠多少，很大程度上会决定上面的日志会不会被打印出来
		Thread.sleep(1000);
		//当休眠时间相等时，可能取消失败，也可能取消成功
//		Thread.sleep(2000);
//		Thread.sleep(3000);

		//用false，具体忘了为什么（好像是只有ft具有完善的中断策略？）
		task1.cancel(false);
		
		try {
			if(task1.get() == null) {
				PrintUtils.println("任务1成功被取消，任务内容也没做完");
			}else {
				PrintUtils.println("任务1取消操作失败，任务已经完成");
			}
		}catch(CancellationException e) {
			PrintUtils.println("任务1成功被直接取消");
		}
			
		Thread task2 = new Thread(new Runnable() {

			public void run() {
				PrintUtils.println("任务2：即将休眠2秒；但这行代码将不会被执行到，除非interrupt操作在后面执行");
				try {
					Thread.sleep(2000);
				}catch(InterruptedException e) {}
			}
			
		});
		
		Thread.sleep(500);
		task2.interrupt();
		Thread.sleep(500);
		if(!task2.isAlive()) 
			PrintUtils.println("任务2被告知中断，但不一定能被执行");
	}
}
