package com.yu.spring.beans.factory;

/**
 * @author yuhangbin
 * @date 2022/5/3
 **/
public abstract class AbstractBeanFactory implements BeanFactory {

	@Override public Object getBean(String name) {
		return null;
	}

	@Override public boolean containBean(String name) {
		return false;
	}
}
