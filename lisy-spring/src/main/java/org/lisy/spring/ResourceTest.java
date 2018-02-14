package org.lisy.spring;

import java.io.IOException;

import org.lisy.common.PrintUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

public class ResourceTest {
	
	
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		BeanFactory bf = new ClassPathXmlApplicationContext("classpath:resource-configs.xml");
		
		//第一个Resource，以xml方式生成
		ResourceBean bean = bf.getBean(ResourceBean.class);
		Resource resource1 = bean.getResource();
		PrintUtils.println("文件是否存在：" + resource1.getFile().exists());
		PrintUtils.println("文件是否目录：" + resource1.getFile().isDirectory());
		PrintUtils.println("文件path：" + resource1.getFile().getPath());
		
		//第二个Resource，以BeanFactory生成
		Resource resource2 = bf.getBean(Resource.class);
		PrintUtils.println("文件是否存在：" + resource2.getFile().exists());
		PrintUtils.println("文件是否目录：" + resource2.getFile().isDirectory());
		PrintUtils.println("文件path：" + resource2.getFile().getPath());
	}
}
