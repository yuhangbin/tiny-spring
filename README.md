# Tiny Spring Framework

A lightweight implementation of Spring Framework core concepts for learning purposes.

## 🎯 **Purpose**
1. Learning how to use dynamic proxies correctly
2. Understanding how Spring IoC (Inversion of Control) works
3. Exploring Spring's architecture and design patterns

## 🏗️ **Architecture Overview**

The framework follows Spring's modular architecture with these core packages:

- **`beans`**: Core IoC container functionality
- **`context`**: Application context and configuration
- **`aop`**: Aspect-Oriented Programming support
- **`io`**: Resource management and loading

## ✨ **Implemented Features**

### ✅ **Core IoC Container**
- **BeanFactory**: Core container interface for bean management
- **BeanDefinition**: Metadata holder for bean configuration
- **AbstractBeanFactory**: Template pattern implementation with singleton management
- **DefaultListableBeanFactory**: Complete bean factory implementation

### ✅ **Bean Lifecycle Management**
- **BeanPostProcessor**: Hook mechanism for custom bean processing
- **Singleton/Prototype scopes**: Bean lifecycle management
- **Initialization methods**: Support for init and destroy methods

### ✅ **Application Context**
- **ApplicationContext**: Enhanced interface extending BeanFactory
- **AbstractApplicationContext**: Template for context implementations
- **AnnotationConfigApplicationContext**: Annotation-based configuration

### ✅ **Resource Management**
- **Resource**: Abstraction for resource access
- **ResourceLoader**: Strategy for loading different resource types
- **ClassPathResource**: Classpath-based resource implementation
- **DefaultResourceLoader**: Default resource loading strategy

### ✅ **AOP (Aspect-Oriented Programming)**
- **AopProxy**: Proxy creation interface
- **JdkDynamicAopProxy**: JDK dynamic proxy implementation
- **CglibAopProxy**: CGLIB-based proxy for classes
- **Pointcut**: Method and class filtering
- **MethodMatcher & ClassFilter**: AOP matching strategies

### ✅ **Annotation Support**
- **@Component**: Component scanning annotation
- **AnnotatedBeanDefinitionReader**: Annotation processing

## 🚀 **Quick Start**

### Basic Bean Factory Usage

```java
// Create bean factory
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

// Register bean definition
BeanDefinition beanDefinition = new BeanDefinition(MyService.class);
beanFactory.registerBeanDefinition("myService", beanDefinition);

// Get bean instance
MyService service = (MyService) beanFactory.getBean("myService");
```

### Annotation-based Configuration

```java
@Component("myService")
public class MyService {
    public String getMessage() {
        return "Hello from Tiny Spring!";
    }
}

// Create context with annotated classes
AnnotationConfigApplicationContext context = 
    new AnnotationConfigApplicationContext(MyService.class);

// Get bean by type
MyService service = context.getBean(MyService.class);
```

### AOP Proxy Example

```java
// Create target object
MyService target = new MyService();

// Configure AOP
AdvisedSupport advised = new AdvisedSupport(target);
advised.setPointcut(Pointcut.TRUE); // Match all methods

// Create proxy
JdkDynamicAopProxy proxy = new JdkDynamicAopProxy(advised);
MyServiceInterface proxiedService = (MyServiceInterface) proxy.getProxy();

// Use proxy (will log before/after method calls)
proxiedService.getMessage();
```

### Bean Post Processor

```java
public class LoggingBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("Before: " + beanName);
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("After: " + beanName);
        return bean;
    }
}

// Register post processor
beanFactory.addBeanPostProcessor(new LoggingBeanPostProcessor());
```

## 🧪 **Running Examples**

### Run the Demo Application

```bash
./gradlew run
```

### Run Tests

```bash
./gradlew test
```

### Build the Project

```bash
./gradlew build
```

## 📁 **Project Structure**

```
src/main/java/com/yu/spring/
├── beans/
│   ├── BeanDefinition.java          # Bean metadata
│   ├── BeanPostProcessor.java       # Bean lifecycle hooks
│   └── factory/
│       ├── BeanFactory.java         # Core container interface
│       ├── support/
│       │   ├── AbstractBeanFactory.java
│       │   └── DefaultListableBeanFactory.java
│       └── config/
│           └── BeanFactoryPostProcessor.java
├── context/
│   ├── ApplicationContext.java      # Enhanced container interface
│   ├── AbstractApplicationContext.java
│   ├── AnnotationConfigApplicationContext.java
│   └── annotation/
│       ├── Component.java           # Component annotation
│       └── AnnotatedBeanDefinitionReader.java
├── aop/
│   ├── AopProxy.java               # Proxy creation interface
│   ├── JdkDynamicAopProxy.java     # JDK dynamic proxy
│   ├── CglibAopProxy.java          # CGLIB proxy
│   ├── Advised.java                # AOP configuration
│   ├── AdvisedSupport.java         # AOP configuration implementation
│   ├── Pointcut.java               # Method/class filtering
│   ├── MethodMatcher.java          # Method matching
│   └── ClassFilter.java            # Class filtering
└── io/
    ├── Resource.java               # Resource abstraction
    ├── ResourceLoader.java         # Resource loading strategy
    ├── ClassPathResource.java      # Classpath resources
    └── DefaultResourceLoader.java  # Default resource loader
```

## 🔧 **Dependencies**

- **CGLIB**: For class-based proxies
- **AspectJ**: For AOP support
- **JUnit 5**: For testing

## 🎓 **Learning Outcomes**

This project demonstrates:

1. **IoC Container Design**: How Spring manages object creation and dependencies
2. **Factory Pattern**: Bean factory implementation
3. **Template Method Pattern**: Abstract classes with customizable steps
4. **Strategy Pattern**: Different resource loading strategies
5. **Proxy Pattern**: AOP proxy implementations
6. **Annotation Processing**: Runtime annotation handling
7. **Singleton Pattern**: Bean lifecycle management

## 🔮 **Future Enhancements**

- [ ] Dependency Injection (constructor, setter, field injection)
- [ ] XML configuration support
- [ ] More AOP advice types (around, after-throwing, etc.)
- [ ] Bean scopes (request, session, etc.)
- [ ] Event system
- [ ] Property placeholder resolution
- [ ] More comprehensive error handling

## 📚 **References**

- [Spring Framework Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html)
- [Spring Framework Source Code](https://github.com/spring-projects/spring-framework)
- [Tiny Spring Reference Implementation](https://github.com/code4craft/tiny-spring)

---

**Note**: This is a learning project and not intended for production use. It demonstrates core Spring concepts in a simplified manner.

