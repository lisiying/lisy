package org.lisy.spring;

import org.lisy.common.PrintUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

//测试BeanFactoryAware，可用
//测试BeanPostProcessor，发现如果本类如果实现了BeanPostProcessor，
//则MyBeanPostProcessor不会生效，本类中的BeanPostProcessor也不会生效？不知道是不是bug
public class PostBeforeAndAfterBean implements BeanFactoryAware,InitializingBean{//,BeanPostProcessor{
	private BeanFactory beanFactory;
	
	public PostBeforeAndAfterBean(){
		PrintUtils.println("初始化PostBeforeAndAfterBean,构造方法");
	}
	
	//业务代码
	public void doBiz(){
		PrintUtils.println("staticBean、dynamicBean已注册：" + (beanFactory.containsBean("staticBean") && beanFactory.containsBean("dynamicBean")));
		PrintUtils.println("业务逻辑");
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void afterPropertiesSet() throws Exception {
		PrintUtils.println("初始化PostBeforeAndAfterBean中,afterPropertiesSet方法");		
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		PrintUtils.println("初始化PostBeforeAndAfterBean前,beanName[{}]",beanName);
		return this;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}
}
