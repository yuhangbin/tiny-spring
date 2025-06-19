package com.yu.spring.beans;

/**
 * Bean definition holds the configuration metadata for a bean.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class BeanDefinition {

	private Class<?> beanClass;
	private String className;
	private String scope = "singleton";
	private boolean lazyInit = false;
	private boolean prototype = false;
	private String initMethodName;
	private String destroyMethodName;

	public BeanDefinition() {
	}

	public BeanDefinition(Class<?> beanClass) {
		this.beanClass = beanClass;
		this.className = beanClass.getName();
	}

	public BeanDefinition(String className) {
		this.className = className;
		try {
			this.beanClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot find class: " + className, e);
		}
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
		this.className = beanClass.getName();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
		try {
			this.beanClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot find class: " + className, e);
		}
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
		this.prototype = "prototype".equals(scope);
	}

	public boolean isLazyInit() {
		return lazyInit;
	}

	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	public boolean isPrototype() {
		return prototype;
	}

	public boolean isSingleton() {
		return !prototype;
	}

	public String getInitMethodName() {
		return initMethodName;
	}

	public void setInitMethodName(String initMethodName) {
		this.initMethodName = initMethodName;
	}

	public String getDestroyMethodName() {
		return destroyMethodName;
	}

	public void setDestroyMethodName(String destroyMethodName) {
		this.destroyMethodName = destroyMethodName;
	}
}
