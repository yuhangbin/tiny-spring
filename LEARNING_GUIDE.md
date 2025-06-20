# Tiny Spring Learning Guide

## 🎯 **How to Master This Project**

### **Phase 1: Core Concepts Understanding**

#### 1. **IoC (Inversion of Control) - The Foundation**
- **What it is**: Instead of objects creating their dependencies, a container creates and manages them
- **Why it matters**: Decouples classes, makes testing easier, enables flexibility
- **In our project**: `BeanFactory` is the IoC container

**Exercise**: 
- Create a simple service class with dependencies
- Show how IoC eliminates tight coupling

#### 2. **Bean Lifecycle - Understanding Object Management**
- **Bean Definition**: Metadata about how to create a bean
- **Bean Creation**: Instantiation, dependency injection, initialization
- **Bean Destruction**: Cleanup when context closes

**Exercise**:
- Add init/destroy methods to a bean
- Observe the lifecycle in action

#### 3. **AOP (Aspect-Oriented Programming) - Cross-cutting Concerns**
- **What it solves**: Logging, transactions, security across multiple classes
- **How it works**: Proxies intercept method calls to add behavior
- **In our project**: `JdkDynamicAopProxy` and `CglibAopProxy`

**Exercise**:
- Create a logging aspect
- Apply it to multiple services

### **Phase 2: Hands-On Experiments**

#### **Experiment 1: Build Your Own Bean**
```java
// Create a simple service
public class UserService {
    private String message = "Hello from UserService";
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}

// Register it in the container
BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
beanFactory.registerBeanDefinition("userService", beanDefinition);

// Get it from the container
UserService service = (UserService) beanFactory.getBean("userService");
```

#### **Experiment 2: Bean Post Processor**
```java
public class LoggingBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("Creating bean: " + beanName);
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("Created bean: " + beanName);
        return bean;
    }
}
```

#### **Experiment 3: AOP Proxy**
```java
// Create target
UserService target = new UserService();

// Configure AOP
AdvisedSupport advised = new AdvisedSupport(target);
advised.setPointcut(Pointcut.TRUE); // Match all methods

// Create proxy
JdkDynamicAopProxy proxy = new JdkDynamicAopProxy(advised);
UserServiceInterface proxiedService = (UserServiceInterface) proxy.getProxy();

// Use proxy (will log before/after)
proxiedService.getMessage();
```

### **Phase 3: Deep Dive Questions**

#### **IoC Questions to Answer:**
1. How does `AbstractBeanFactory` manage singleton beans?
2. What happens when you call `getBean()` for the first time vs. subsequent times?
3. How do `BeanPostProcessor` hooks work in the bean creation process?

#### **AOP Questions to Answer:**
1. What's the difference between JDK dynamic proxy and CGLIB proxy?
2. How does the proxy know which methods to intercept?
3. What happens if a method doesn't match the pointcut?

#### **Resource Loading Questions:**
1. How does `DefaultResourceLoader` decide which type of resource to create?
2. What's the difference between `classpath:` and no prefix?
3. How could you add support for file system resources?

### **Phase 4: Extension Projects**

#### **Project 1: Add Dependency Injection**
- Implement constructor injection
- Add `@Autowired` annotation support
- Handle circular dependencies

#### **Project 2: Add More Resource Types**
- Implement `FileSystemResource`
- Add URL resource support
- Create a resource chain (fallback mechanism)

#### **Project 3: Enhanced AOP**
- Add different advice types (before, after, around)
- Implement pointcut expressions
- Add advice ordering

### **Phase 5: Compare with Real Spring**

#### **Key Differences to Understand:**
1. **Real Spring** has much more sophisticated dependency injection
2. **Real Spring** supports XML configuration
3. **Real Spring** has extensive AOP support with AspectJ
4. **Real Spring** includes transaction management, security, etc.

#### **Similarities to Appreciate:**
1. **Same core concepts**: IoC, AOP, resource abstraction
2. **Same design patterns**: Factory, Template Method, Strategy
3. **Same architecture**: Modular, extensible design

### **Phase 6: Practical Exercises**

#### **Exercise 1: Build a Simple Web Framework**
```java
@Component
public class WebController {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
```

#### **Exercise 2: Create a Configuration System**
```java
@Configuration
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        return new SimpleDataSource();
    }
}
```

#### **Exercise 3: Implement Event System**
```java
@Component
public class UserService {
    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        // Handle event
    }
}
```

### **Phase 7: Debugging and Understanding**

#### **Add Debug Logging:**
```java
// In AbstractBeanFactory
protected Object createBean(String beanName, BeanDefinition beanDefinition) {
    System.out.println("Creating bean: " + beanName);
    // ... existing code
    System.out.println("Created bean: " + beanName);
    return bean;
}
```

#### **Trace Method Calls:**
```java
// In JdkDynamicAopProxy
@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    System.out.println("Intercepting method: " + method.getName());
    // ... existing code
}
```

### **Phase 8: Documentation and Teaching**

#### **Write Your Own Documentation:**
1. Explain each package's purpose
2. Document the bean lifecycle
3. Create diagrams of how AOP works
4. Write tutorials for others

#### **Teach Someone Else:**
1. Explain IoC to a colleague
2. Show how AOP solves cross-cutting concerns
3. Demonstrate the resource abstraction

### **Success Metrics:**

✅ **You understand the project when you can:**
- Explain IoC without looking at code
- Draw the bean lifecycle on a whiteboard
- Implement a new feature without help
- Debug issues in the framework
- Explain how AOP proxies work
- Compare your implementation to real Spring
- Teach the concepts to others

### **Common Pitfalls to Avoid:**

❌ **Don't just read the code** - experiment with it
❌ **Don't skip the fundamentals** - understand IoC before AOP
❌ **Don't copy without understanding** - write your own implementations
❌ **Don't ignore the design patterns** - they're key to understanding

### **Resources for Deeper Learning:**

1. **Spring Framework Documentation**: Compare your implementation
2. **Design Patterns Book**: Understand the patterns used
3. **Java Reflection API**: Essential for AOP and dependency injection
4. **CGLIB Documentation**: For understanding class-based proxies

---

**Remember**: The goal isn't to build a production-ready framework, but to understand how Spring works internally. Focus on the concepts, not the completeness of the implementation. 