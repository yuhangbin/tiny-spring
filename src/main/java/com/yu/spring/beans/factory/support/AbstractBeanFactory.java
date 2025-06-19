package com.yu.spring.beans.factory.support;

import com.yu.spring.beans.BeanDefinition;
import com.yu.spring.beans.BeanPostProcessor;
import com.yu.spring.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for bean factories that provides basic functionality.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public abstract class AbstractBeanFactory implements BeanFactory {

	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
	private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

	@Override
	public Object getBean(String name) {
		return doGetBean(name);
	}

	@Override
	public boolean containBean(String name) {
		return beanDefinitionMap.containsKey(name);
	}

	protected Object doGetBean(String name) {
		BeanDefinition beanDefinition = beanDefinitionMap.get(name);
		if (beanDefinition == null) {
			throw new RuntimeException("Bean definition not found for: " + name);
		}

		// Check singleton cache first
		if (beanDefinition.isSingleton()) {
			Object singletonBean = singletonObjects.get(name);
			if (singletonBean != null) {
				return singletonBean;
			}
		}

		// Create new bean instance
		Object bean = createBean(name, beanDefinition);

		// Cache singleton
		if (beanDefinition.isSingleton()) {
			singletonObjects.put(name, bean);
		}

		return bean;
	}

	protected Object createBean(String beanName, BeanDefinition beanDefinition) {
		Object bean = instantiateBean(beanDefinition);

		// Apply bean post processors before initialization
		bean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

		// Initialize bean
		initializeBean(bean, beanName, beanDefinition);

		// Apply bean post processors after initialization
		bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);

		return bean;
	}

	protected Object instantiateBean(BeanDefinition beanDefinition) {
		try {
			return beanDefinition.getBeanClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate bean: " + beanDefinition.getClassName(), e);
		}
	}

	protected void initializeBean(Object bean, String beanName, BeanDefinition beanDefinition) {
		// Call init method if specified
		String initMethodName = beanDefinition.getInitMethodName();
		if (initMethodName != null && !initMethodName.isEmpty()) {
			try {
				Method initMethod = bean.getClass().getMethod(initMethodName);
				initMethod.invoke(bean);
			} catch (Exception e) {
				throw new RuntimeException("Failed to invoke init method: " + initMethodName, e);
			}
		}
	}

	protected Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) {
		Object result = bean;
		for (BeanPostProcessor processor : beanPostProcessors) {
			try {
				result = processor.postProcessBeforeInitialization(result, beanName);
				if (result == null) {
					return result;
				}
			} catch (Exception e) {
				throw new RuntimeException("BeanPostProcessor failed on bean: " + beanName, e);
			}
		}
		return result;
	}

	protected Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) {
		Object result = bean;
		for (BeanPostProcessor processor : beanPostProcessors) {
			try {
				result = processor.postProcessAfterInitialization(result, beanName);
				if (result == null) {
					return result;
				}
			} catch (Exception e) {
				throw new RuntimeException("BeanPostProcessor failed on bean: " + beanName, e);
			}
		}
		return result;
	}

	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		beanDefinitionMap.put(beanName, beanDefinition);
	}

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		beanPostProcessors.add(beanPostProcessor);
	}

	public BeanDefinition getBeanDefinition(String beanName) {
		return beanDefinitionMap.get(beanName);
	}

	public String[] getBeanDefinitionNames() {
		return beanDefinitionMap.keySet().toArray(new String[0]);
	}
}
