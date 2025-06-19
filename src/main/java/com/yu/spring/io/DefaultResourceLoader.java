package com.yu.spring.io;

/**
 * Default implementation of ResourceLoader.
 * 
 * @author yuhangbin
 * @date 2022/5/8
 **/
public class DefaultResourceLoader implements ResourceLoader {

    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    private ClassLoader classLoader;

    public DefaultResourceLoader() {
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }

    public DefaultResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Resource getResource(String location) {
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
        } else {
            // Default to classpath resource
            return new ClassPathResource(location, getClassLoader());
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}