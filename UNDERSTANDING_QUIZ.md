# Tiny Spring Understanding Quiz

## 🧠 **Test Your Knowledge**

Answer these questions to gauge your understanding. Try to answer without looking at the code first!

---

### **Section 1: IoC (Inversion of Control)**

#### **Question 1.1: What is IoC?**
- A) A design pattern where objects create their own dependencies
- B) A design pattern where a container creates and manages object dependencies
- C) A way to make code run faster
- D) A type of database connection

**Answer**: B

#### **Question 1.2: What happens when you call `beanFactory.getBean("myBean")` for the first time?**
- A) It returns null
- B) It creates a new instance of the bean
- C) It throws an exception
- D) It returns a cached instance

**Answer**: B

#### **Question 1.3: What happens when you call `beanFactory.getBean("myBean")` for the second time?**
- A) It creates another new instance
- B) It returns the same instance as before (if singleton)
- C) It throws an exception
- D) It returns null

**Answer**: B

---

### **Section 2: Bean Lifecycle**

#### **Question 2.1: What is a BeanDefinition?**
- A) An actual bean instance
- B) Metadata about how to create a bean
- C) A type of database table
- D) A configuration file

**Answer**: B

#### **Question 2.2: In what order do these happen during bean creation?**
1. Apply BeanPostProcessor before initialization
2. Instantiate the bean
3. Apply BeanPostProcessor after initialization
4. Call init method

- A) 2, 1, 4, 3
- B) 1, 2, 3, 4
- C) 2, 1, 3, 4
- D) 1, 2, 4, 3

**Answer**: A

#### **Question 2.3: What is the default scope for beans?**
- A) Prototype
- B) Singleton
- C) Request
- D) Session

**Answer**: B

---

### **Section 3: AOP (Aspect-Oriented Programming)**

#### **Question 3.1: What is the main purpose of AOP?**
- A) To make code run faster
- B) To handle cross-cutting concerns like logging and transactions
- C) To create database connections
- D) To manage memory

**Answer**: B

#### **Question 3.2: What's the difference between JDK dynamic proxy and CGLIB proxy?**
- A) JDK proxy is faster
- B) JDK proxy works with interfaces, CGLIB works with classes
- C) CGLIB proxy is easier to use
- D) There's no difference

**Answer**: B

#### **Question 3.3: What does a Pointcut do?**
- A) It creates new objects
- B) It defines which methods should be intercepted
- C) It manages database connections
- D) It handles exceptions

**Answer**: B

---

### **Section 4: Resource Management**

#### **Question 4.1: What does the Resource interface represent?**
- A) A database connection
- B) An abstraction over different types of resources (files, classpath, etc.)
- C) A network connection
- D) A memory location

**Answer**: B

#### **Question 4.2: What happens when you call `resourceLoader.getResource("classpath:config.txt")`?**
- A) It creates a file on disk
- B) It loads a resource from the classpath
- C) It throws an exception
- D) It returns null

**Answer**: B

#### **Question 4.3: What's the difference between `classpath:` prefix and no prefix?**
- A) No difference
- B) `classpath:` explicitly loads from classpath, no prefix defaults to classpath
- C) No prefix loads from filesystem
- D) `classpath:` loads from URL

**Answer**: B

---

### **Section 5: Annotation Support**

#### **Question 5.1: What does the @Component annotation do?**
- A) It creates a database table
- B) It marks a class as a Spring bean to be managed by the container
- C) It makes a method run faster
- D) It creates a new thread

**Answer**: B

#### **Question 5.2: How does the container know about @Component classes?**
- A) It scans all classes automatically
- B) It uses AnnotatedBeanDefinitionReader to process annotated classes
- C) It reads XML configuration
- D) It uses reflection to find them

**Answer**: B

---

### **Section 6: Code Analysis**

#### **Question 6.1: Look at this code and predict the output:**
```java
DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
BeanDefinition def = new BeanDefinition(MyService.class);
factory.registerBeanDefinition("service", def);

MyService s1 = (MyService) factory.getBean("service");
MyService s2 = (MyService) factory.getBean("service");
System.out.println(s1 == s2);
```

- A) true
- B) false
- C) Compilation error
- D) Runtime exception

**Answer**: A (because beans are singletons by default)

#### **Question 6.2: What will this code output?**
```java
AdvisedSupport advised = new AdvisedSupport(target);
advised.setPointcut(Pointcut.TRUE);
JdkDynamicAopProxy proxy = new JdkDynamicAopProxy(advised);
Object proxied = proxy.getProxy();
```

- A) A new instance of the target class
- B) A proxy that wraps the target
- C) null
- D) An exception

**Answer**: B

---

### **Section 7: Design Patterns**

#### **Question 7.1: Which design pattern is used in AbstractBeanFactory?**
- A) Singleton
- B) Template Method
- C) Factory Method
- D) Observer

**Answer**: B

#### **Question 7.2: Which design pattern is used in DefaultResourceLoader?**
- A) Strategy
- B) Singleton
- C) Factory
- D) Decorator

**Answer**: A

---

### **Section 8: Advanced Concepts**

#### **Question 8.1: What would happen if you tried to create a JDK proxy for a class that doesn't implement any interfaces?**
- A) It would work fine
- B) It would throw an exception
- C) It would create a CGLIB proxy instead
- D) It would return null

**Answer**: B

#### **Question 8.2: How could you add support for file system resources?**
- A) Create a FileSystemResource class and update DefaultResourceLoader
- B) Use only classpath resources
- C) Modify the existing ClassPathResource
- D) It's not possible

**Answer**: A

---

## 📊 **Scoring Guide**

- **0-10 correct**: You need to review the fundamentals
- **11-20 correct**: Good understanding, but room for improvement
- **21-30 correct**: Strong understanding of the concepts
- **31+ correct**: Excellent! You really understand the framework

---

## 🔍 **Review Areas Based on Your Score**

### **If you scored low (0-10):**
1. Re-read the basic concepts in the README
2. Run the UnderstandingExercises
3. Focus on IoC and bean lifecycle first

### **If you scored medium (11-20):**
1. Practice with the exercises more
2. Try implementing small features
3. Compare with real Spring documentation

### **If you scored high (21+):**
1. Try implementing advanced features
2. Read Spring source code
3. Teach others about the concepts

---

## 🎯 **Next Steps After the Quiz**

1. **Run the exercises**: Execute `UnderstandingExercises` to see concepts in action
2. **Experiment**: Modify the code and see what happens
3. **Implement features**: Try adding dependency injection or more resource types
4. **Compare with Spring**: Read Spring documentation and compare implementations

---

**Remember**: The goal is understanding, not memorization. Focus on the "why" behind each concept! 