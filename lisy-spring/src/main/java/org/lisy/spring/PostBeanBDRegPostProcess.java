package org.lisy.spring;

import org.lisy.common.PrintUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class PostBeanBDRegPostProcess implements BeanDefinitionRegistryPostProcessor{

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		//在这个代码里可以获取beanFactory
		//目前还不知道是干嘛用的
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		//自定义新的bean
		PrintUtils.println("动态注册dynamicBean");
		registry.registerBeanDefinition("dynamicBean",new RootBeanDefinition(PostBeforeAndAfterBean.class));
	}

}
