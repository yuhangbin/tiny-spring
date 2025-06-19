package com.yu.spring.io;

/**
 * Strategy interface for loading resources (e.g., class path or file system
 * resources).
 * 
 * @author yuhangbin
 * @date 2022/5/8
 **/
public interface ResourceLoader {

    /**
     * Load a resource from the given location.
     * 
     * @param location the resource location
     * @return the resource
     */
    Resource getResource(String location);

    /**
     * Get the ClassLoader used for loading resources.
     * 
     * @return the ClassLoader
     */
    ClassLoader getClassLoader();
}
