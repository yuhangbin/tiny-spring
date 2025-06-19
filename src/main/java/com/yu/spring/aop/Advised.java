package com.yu.spring.aop;

/**
 * Interface to be implemented by classes that hold the configuration
 * of a factory of AOP proxies.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public interface Advised {

    /**
     * Return the target object.
     * 
     * @return the target object
     */
    Object getTarget();

    /**
     * Return the target class.
     * 
     * @return the target class
     */
    Class<?> getTargetClass();

    /**
     * Return the pointcut.
     * 
     * @return the pointcut
     */
    Pointcut getPointcut();

    /**
     * Set the target object.
     * 
     * @param target the target object
     */
    void setTarget(Object target);

    /**
     * Set the pointcut.
     * 
     * @param pointcut the pointcut
     */
    void setPointcut(Pointcut pointcut);
}
