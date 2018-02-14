package org.lisy.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * FactoryBean用法示例
 * 
 * FactoryBean用途：当spring发现其指向的class为一个factoryBean时，会调用其getObject生成bean(详见:)
 * 目前的问题是，这种方式比xml配置优在哪?是否有些东西(例如bean的某些属性)可以写死在代码里(或者根据代码生成)?
 * 好像是如此...例如我在此类，写死了生成的bean
 */
public class MyFactoryBean implements FactoryBean<Resource>{
	public Resource getObject() throws Exception {
		return new UrlResource("file:/Users/sasori/test/SUNLINE.data");
	}

	public Class<?> getObjectType() {
		return Resource.class;
	}

	public boolean isSingleton() {
		return true;
	}
	
}
