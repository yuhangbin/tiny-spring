package com.yu.spring.aop;

/**
 * Base class for AOP proxy configuration managers.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class AdvisedSupport implements Advised {

    private Object target;
    private Pointcut pointcut;

    public AdvisedSupport() {
    }

    public AdvisedSupport(Object target) {
        this.target = target;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Class<?> getTargetClass() {
        return target != null ? target.getClass() : null;
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }
}