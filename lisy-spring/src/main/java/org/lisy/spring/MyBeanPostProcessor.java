package org.lisy.spring;

import org.lisy.common.PrintUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 本类用于测试BeanPostProcessor接口的用法
 * 本bean也需要注册到beanFactory；
 * 
 * @author lisy
 */
public class MyBeanPostProcessor implements BeanPostProcessor{
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof PostBeforeAndAfterBean) {
			PrintUtils.println("初始化PostBeforeAndAfterBean前,beanName[{}]，来自MyBeanPostProcessor",beanName);
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof PostBeforeAndAfterBean) {
			PrintUtils.println("初始化PostBeforeAndAfterBean后,beanName[{}]，来自MyBeanPostProcessor",beanName);
		}
		return bean;
	}
}
