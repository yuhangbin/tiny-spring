# Tiny Spring

## Purpose
1. Learning how to use dynamics proxy in correct way.
2. Learning how spring IoC works.

## Key Points
- [ ] Resource Management 
- [ ] Bean Definition Management
- [ ] Bean initialize

## The IoC Container
### Objects
    1. A container for beans (BeanFactory)
    2. Beans registered
    3. Hooks for beans registered.(BeanPostProcessor)
    4. Resolve bean's dependency problem
    5. Better interface for operating Beans (ApplicationContext)

## AOP

### Questions
Q1. How AOP works in IoC Container? \
A1. The IoC Container support the hook (BeanPostProcessor) which you can use any process you want when initial beans.
    So you can use AOP before bean initialize aware some custom logic.

Q2. How to manage Resources? 
A2. 
### Objects
    1. dynamic proxy
    2. Implementing BeanPostProcessor


Reference:
- https://docs.spring.io/spring-framework/docs/current/reference/html/core.html
- https://github.com/code4craft/tiny-spring
- https://github.com/spring-projects/spring-framework

