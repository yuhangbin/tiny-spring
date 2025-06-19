package com.yu.spring.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Resource implementation for classpath resources.
 * 
 * @author yuhangbin
 * @date 2022/5/8
 **/
public class ClassPathResource implements Resource {

    private final String path;
    private final ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = classLoader.getResourceAsStream(path);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + path);
        }
        return inputStream;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ClassPathResource{" + "path='" + path + '\'' + '}';
    }
}