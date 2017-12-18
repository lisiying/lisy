package org.lisy.common;
/** 
 * 我决定命名为Log4Lee
 * 
 * @author Lisy
 * @date 2017年12月11日
 * @version 1.0 
 */
public class PrintUtils {
	public static void println(Object obj){
		System.out.println(obj);
	}
	
	public static void println(String str,Object... objs){
		if(objs == null){
			System.out.println(str);
			return;
		}
		//诡异的replaceFirst
//		for(Object obj : objs){
//			str = str.replaceFirst("[{}]", obj==null?"[]":"[" + obj.toString() +"]");
//		}
//		System.out.println(str);
		
		//spilt也诡异
//		String[] strArr = str.split("{}");
//		StringBuilder target = new StringBuilder();
//		int i = 0;
//		for(Object obj : objs){
//			target.append(strArr[i]);
//			target.append(obj==null? "": obj.toString());
//		}
//		
//		for(;i<strArr.length;i++){
//			target.append(strArr[i]);
//		}
//		
//		System.out.println(target.toString());
		
		for(Object obj : objs){
			int index = str.indexOf("[{}]");
			if(index==-1){
				System.out.println(str);
				return;
			}
			str = new StringBuilder().append(str.substring(0,index+1))
					.append(obj==null?"":obj.toString())
					.append(str.substring(index+3,str.length())).toString();
		}
		
		System.out.println(str);
	}
}
