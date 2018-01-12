package org.lisy.study.multthread;

import java.util.Random;

import org.lisy.common.PrintUtils;

/** 
 * 同步块与同步方法是否同步
 * [实验结果]：同步（当同一对象监视器时）
 * @see SyncThisAndSyncObjIsTheSame
 * 
 * @author Lisy
 * @date 2017年12月11日
 * @version 1.0 
 */
public class SyncChunckAndSynMethod {
	private final static String CMD_RUN_SYNC_CHUNCK = "cmd_1";
	private final static String CMD_RUN_SYNC_METHOD = "cmd_2";
	
	public class Data{
		public synchronized void syncMethod(){
			PrintUtils.println("进程进入[syncMethod]方法：" + Thread.currentThread().getName());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			PrintUtils.println("进程离开[syncMethod]方法：" + Thread.currentThread().getName());
		}
		
		public void syncChunck(){
			PrintUtils.println("进程进入[syncChunck]方法" + Thread.currentThread().getName());
			synchronized(this){
				try {
					PrintUtils.println("进程进入同步块：" + Thread.currentThread().getName());
					Thread.sleep(500);
					PrintUtils.println("进程离开同步块：" + Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			PrintUtils.println("进程离开[syncChunck]方法：" + Thread.currentThread().getName());
		}
	}
	
	public class MyThread implements Runnable{
		private String command;
		private Data data;
		
		public MyThread(Data data,String command){
			//关键就是这个data是否同一个对象，如果是，则同步；否则，不同步
			this.data = data;
			this.command = command;
		}
		
		public void run() {
			if(this.command.equals(CMD_RUN_SYNC_CHUNCK)){
				data.syncChunck();
			}else if(this.command.equals(CMD_RUN_SYNC_METHOD)){
				data.syncMethod();
			}else{
				try {
					throw new Exception("未知的命令："+command);
				} catch (Exception e) {
					//do nothing
				}
			}
			
		}
	}
	
	public void execute(){
		//注意这行奇特的语法
		Data data = this.new Data();
		Random random = new Random();
		for(int i=0;i<10;i++){
			boolean val = random.nextBoolean();
			if(val){
				MyThread mt = new MyThread(data, CMD_RUN_SYNC_CHUNCK);
				new Thread(mt).start();
			}else{
				MyThread mt2 = new MyThread(data, CMD_RUN_SYNC_METHOD);
				new Thread(mt2).start();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new SyncChunckAndSynMethod().execute();
		Thread.sleep(5000);
	}
}