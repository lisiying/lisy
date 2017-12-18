package org.lisy.study.multthread;

import java.util.Random;

import org.lisy.common.PrintUtils;

/** 
 * 本实验证明：sync(this)跟sync(obj)当持有同一对象时，是同步的；
 * 也就是说，sync(this)其实跟sync(obj)是等同的；并且都是对象锁
 * 
 * @author Lisy
 * @date 2017年12月14日
 * @version 1.0 
 */
public class SyncThisAndSyncObjIsTheSame {
	@SuppressWarnings("unused")
	private Data data;
	public void setData(Data data){
		this.data = data;
	}
	class Data{
		public void syncThis(){
			try{
				synchronized(this){
					PrintUtils.println("线程[{}]进入[{}]方法",Thread.currentThread().getName(),"syncThis");
					Thread.sleep(500);
					PrintUtils.println("线程[{}]离开[{}]方法",Thread.currentThread().getName(),"syncThis");
				}
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public void syncObj(Data data){
		try{
			synchronized(data){
				PrintUtils.println("线程[{}]进入[{}]方法",Thread.currentThread().getName(),"syncObj");
				Thread.sleep(500);
				PrintUtils.println("线程[{}]离开[{}]方法",Thread.currentThread().getName(),"syncObj");
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		final SyncThisAndSyncObjIsTheSame s = new SyncThisAndSyncObjIsTheSame();
		final Data d = s.new Data();
		s.setData(d);
		
		Random r = new Random();
		
		for(int i=0;i<10;i++){
			Boolean b = r.nextBoolean();
			if(b){
				new Thread(new Runnable() {
					public void run() {
						d.syncThis();
					}
				}).start();;
			}else{
				new Thread(new Runnable() {
					public void run() {
						s.syncObj(d);
					}
				}).start();
			}
		}
	}
}
