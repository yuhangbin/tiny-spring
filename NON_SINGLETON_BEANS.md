# Non-Singleton Beans in Tiny Spring

## 🎯 **What are Non-Singleton Beans?**

Non-singleton beans (also called **prototype beans**) are beans that get a **new instance created every time** you request them from the container. Unlike singletons, they are not cached and are not shared.

## 🔄 **How They Work in the Code**

### **Key Code in AbstractBeanFactory:**

```java
protected Object doGetBean(String name) {
    BeanDefinition beanDefinition = beanDefinitionMap.get(name);
    
    // Check singleton cache first
    if (beanDefinition.isSingleton()) {
        Object singletonBean = singletonObjects.get(name);
        if (singletonBean != null) {
            return singletonBean;  // Return cached instance
        }
    }
    
    // Create new bean instance (ALWAYS for prototypes)
    Object bean = createBean(name, beanDefinition);
    
    // Cache singleton ONLY
    if (beanDefinition.isSingleton()) {
        singletonObjects.put(name, bean);
    }
    
    return bean;
}
```

**Key Points:**
- **Prototype beans skip the cache check** (they're never cached)
- **New instance created every time** `getBean()` is called
- **No caching** - each request gets a fresh object

## 📊 **Singleton vs Prototype Comparison**

| Aspect                | Singleton                 | Prototype                        |
| --------------------- | ------------------------- | -------------------------------- |
| **Instance Creation** | Once, then cached         | Every time `getBean()` is called |
| **Memory Usage**      | Low (shared instance)     | Higher (multiple instances)      |
| **Performance**       | Fast (cached)             | Slower (creation overhead)       |
| **State Sharing**     | Shared across all callers | Each caller gets own state       |
| **BeanPostProcessor** | Called once               | Called for every instance        |
| **Init Method**       | Called once               | Called for every instance        |

## 🧪 **Demonstration from Exercise 7**

### **Singleton Behavior:**
```
1. Getting singleton bean first time:
   Creating DetailedService instance #1
   Before initialization: singletonService (ID: 1)
   Initializing DetailedService #1
   After initialization: singletonService (ID: 1)
   Singleton ID: 1

2. Getting singleton bean second time:
   Singleton ID: 1
   Same instance? true
```

**What happens:**
- ✅ **First call**: Creates instance, runs lifecycle, caches it
- ✅ **Second call**: Returns cached instance, no lifecycle

### **Prototype Behavior:**
```
1. Getting prototype bean first time:
   Creating DetailedService instance #2
   Before initialization: prototypeService (ID: 2)
   Initializing DetailedService #2
   After initialization: prototypeService (ID: 2)
   Prototype ID: 2

2. Getting prototype bean second time:
   Creating DetailedService instance #3
   Before initialization: prototypeService (ID: 3)
   Initializing DetailedService #3
   After initialization: prototypeService (ID: 3)
   Prototype ID: 3
   Same instance? false
```

**What happens:**
- ✅ **Every call**: Creates new instance, runs full lifecycle
- ✅ **No caching**: Each request gets fresh object

## 🎯 **When to Use Prototype Beans**

### **Use Prototype When:**
- **Stateful beans** - Each caller needs own state
- **Short-lived objects** - Temporary objects that don't need sharing
- **Thread-specific data** - Each thread needs its own instance
- **User sessions** - Each user needs separate instance
- **Request-scoped data** - Each HTTP request needs fresh instance

### **Use Singleton When:**
- **Stateless services** - No state to share
- **Configuration objects** - Shared configuration
- **Utility classes** - Stateless utilities
- **Database connections** - Shared connection pools
- **Cached data** - Shared cache

## 💻 **Code Examples**

### **Setting Bean Scope:**

```java
// Singleton (default)
BeanDefinition singletonDef = new BeanDefinition(MyService.class);
singletonDef.setScope("singleton"); // or just leave default

// Prototype
BeanDefinition prototypeDef = new BeanDefinition(MyService.class);
prototypeDef.setScope("prototype");
```

### **Checking Scope:**

```java
BeanDefinition def = beanFactory.getBeanDefinition("myBean");
if (def.isSingleton()) {
    System.out.println("This is a singleton bean");
} else if (def.isPrototype()) {
    System.out.println("This is a prototype bean");
}
```

### **Practical Example:**

```java
// User session bean - should be prototype
@Component
@Scope("prototype") // Each user gets own session
public class UserSession {
    private String userId;
    private List<String> cartItems = new ArrayList<>();
    
    public void addToCart(String item) {
        cartItems.add(item);
    }
}

// Configuration service - should be singleton
@Component
public class AppConfig {
    private final String appName = "MyApp";
    private final int maxUsers = 1000;
    
    public String getAppName() { return appName; }
    public int getMaxUsers() { return maxUsers; }
}
```

## ⚠️ **Important Considerations**

### **1. Memory Management**
- **Prototypes use more memory** - each instance consumes memory
- **No automatic cleanup** - container doesn't manage prototype lifecycle
- **Manual cleanup needed** - you must handle prototype destruction

### **2. Performance Impact**
- **Creation overhead** - each prototype requires full lifecycle
- **BeanPostProcessor calls** - runs for every prototype instance
- **Init method calls** - runs for every prototype instance

### **3. Lifecycle Differences**
- **No destroy method** - prototypes aren't managed by container
- **No dependency injection** - each instance gets fresh dependencies
- **No circular dependency resolution** - each instance resolves independently

## 🔍 **Debugging Prototype Issues**

### **Common Problems:**

1. **Memory Leaks**: Too many prototype instances
   ```java
   // BAD: Creating many prototypes without cleanup
   for (int i = 0; i < 10000; i++) {
       MyPrototypeBean bean = context.getBean("prototypeBean");
       // bean is never cleaned up
   }
   ```

2. **Performance Issues**: Creating prototypes in loops
   ```java
   // BAD: Expensive in loops
   for (User user : users) {
       UserSession session = context.getBean("userSession");
       // Each iteration creates new instance
   }
   ```

3. **State Confusion**: Expecting shared state
   ```java
   // BAD: Expecting prototype to share state
   UserSession session1 = context.getBean("userSession");
   UserSession session2 = context.getBean("userSession");
   session1.addToCart("item");
   // session2 won't have the item!
   ```

## 🎓 **Learning Exercises**

### **Exercise 1: State Management**
```java
// Create prototype bean with state
BeanDefinition def = new BeanDefinition(CounterService.class);
def.setScope("prototype");
beanFactory.registerBeanDefinition("counter", def);

// Test state isolation
CounterService counter1 = (CounterService) beanFactory.getBean("counter");
CounterService counter2 = (CounterService) beanFactory.getBean("counter");

counter1.increment(); // Should be 1
counter2.increment(); // Should be 1 (not 2!)
```

### **Exercise 2: Lifecycle Observation**
```java
// Add BeanPostProcessor to observe lifecycle
beanFactory.addBeanPostProcessor(new LifecycleObserver());

// Get multiple prototypes
for (int i = 0; i < 3; i++) {
    beanFactory.getBean("prototypeBean");
    // Should see full lifecycle for each instance
}
```

## 📚 **Real Spring Comparison**

### **In Real Spring:**
- **More scopes**: request, session, application, websocket
- **Scope proxies**: Can inject prototype into singleton
- **Custom scopes**: Can define your own scopes
- **Scope resolution**: Automatic scope resolution

### **In Tiny Spring:**
- **Two scopes**: singleton and prototype only
- **No scope proxies**: Direct instantiation
- **Simplified lifecycle**: Basic creation and initialization

## 🎯 **Key Takeaways**

1. **Prototype beans are never cached** - new instance every time
2. **Full lifecycle runs for each instance** - BeanPostProcessor, init methods
3. **Use for stateful, short-lived objects** - each caller needs own state
4. **Memory management is your responsibility** - no automatic cleanup
5. **Performance impact** - creation overhead for each instance

---

**Remember**: Prototype beans are powerful but come with trade-offs. Use them when you need instance isolation, but be mindful of memory and performance implications! 