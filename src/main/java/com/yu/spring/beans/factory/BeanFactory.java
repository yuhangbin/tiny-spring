package com.yu.spring.beans.factory;

/**
 * @author yuhangbin
 * @date 2022/5/3
 **/
public interface BeanFactory {

	Object getBean(String name);

	boolean containBean(String name);
}
