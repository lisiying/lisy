package org.lisy.spring;

import org.lisy.common.PrintUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

//测试BeanFactoryAware
public class PostBeforeAndAfterBean implements BeanPostProcessor,BeanFactoryAware{
	private BeanFactory beanFactory;
	
	public PostBeforeAndAfterBean(){
		PrintUtils.println("初始化PostBeforeAndAfterBean");
	}
	
	//业务初始化-前
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		PrintUtils.println("1-调用PostProcessBefore");
		return bean;
	}

	//业务初始化-后
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		PrintUtils.println("2-调用PostProcessAfter");
		return bean;
	}
	
	//业务代码
	public void doBiz(){
		PrintUtils.println("staticBean已注册：" + beanFactory.containsBean("staticBean"));
		PrintUtils.println("dynamicBean已注册：" + beanFactory.containsBean("dynamicBean"));
		PrintUtils.println("业务逻辑");
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
