package org.lisy.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 本类的两个用途：
 * 1.测试配置文件注册bean(staticBean)
 * 2.测试动态注册bean用法(dynamicBean)
 * 
 * @author lisy
 */
public class PostBeforeAndAfterTest {
	private static BeanFactory bf;
	public static void main(String[] args) {
		bf = new ClassPathXmlApplicationContext("classpath:beanRegistTest-configs.xml");
		//普通方式(xml)配置的bean
		PostBeforeAndAfterBean bean = (PostBeforeAndAfterBean) bf.getBean("staticBean");
		bean.doBiz();
		//用于验证，当第二次获取postBean的时候，spring是否会从缓存里取；验证结果为是
		bean = (PostBeforeAndAfterBean) bf.getBean("staticBean");
		bean.doBiz();
		//动态注册方式(使用BeanDefinitionRegistryPostProcessor接口注册bean)
		bean = (PostBeforeAndAfterBean) bf.getBean("dynamicBean");
		bean.doBiz();
	}
}
