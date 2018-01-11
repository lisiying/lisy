package org.lisy.study.multthread;

/**
 * 本实验验证threadLocal的作用，与基本属性
 * @author lisy
 *
 */
public class SimpleLocalThread {
	class CurrThread extends Thread{
		//暂时用string吧，简单点
		//用static达到"线程共享"（但因为这个为了方便，写成内部类，故无法使用static）
		private ThreadLocal<String> t = new ThreadLocal<String>();
		
		@Override
		public void run() {
			super.run();
			t.set("设置val"+Thread.currentThread().getName());
		}
	}
	
	public static void main(String[] args) {
		SimpleLocalThread ltt = new SimpleLocalThread();
		CurrThread[] tArr = new CurrThread[10];
		for(int i=0;i<10;i++) {
			CurrThread t = ltt.new CurrThread();
			tArr[i] = t;
			t.start();
		}
		
		for(CurrThread t : tArr) {
			System.out.println(t.getName()+":"+t.t.get());
		}
	}
}
