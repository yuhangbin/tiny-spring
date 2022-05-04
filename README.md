# Tiny Spring

## Purpose
1. Learning how to use dynamics proxy in correct way.
2. More know about Spring Core Technologies.
3. Learning designs in Spring framework.

## The IoC Container
### Objects
    1. A container for beans (BeanFactory)
    2. Beans registered
    3. Hooks for beans registered.(BeanPostProcessor)
    4. Resolve bean's dependency problem
    5. Better interface for operating Beans (ApplicationContext)

## AOP

### Questions
Q1. How to combine with IoC Container? \
A1. Implementing Hooks (BeanPostProcessor), return Proxy after initialize.

### Objects
    1. dynamic proxy
    2. Implementing BeanPostProcessor


Reference:
- https://docs.spring.io/spring-framework/docs/current/reference/html/core.html
- https://github.com/code4craft/tiny-spring
- https://github.com/spring-projects/spring-framework

