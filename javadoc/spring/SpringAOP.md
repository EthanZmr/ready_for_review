# SpringAOP

## SpringAOP介绍

1. aop：面向切面编程，是面向对象编程oop的补充和完善
2. aop把系统软件分为两个部分：核心关注点和横切关注点
   1. 业务处理的主要流程是核心关注点
   2. 其余的与核心关注点不大的是横切关注点，它经常发生在核心关注点的多处，而且各处基本相似  比如权限，日志，性能，事务等等...
3. aop的作用就是将系统中的核心关注点和横切关注点分离开来
4. aop具有横切关注点，切面，连接点，切入点，通知，目标对象等相关的概念
5. 使用`springAOP`需要在`spring`的配置类上添加`@EnableAspectJAutoProxy`注解
6. 该注解为容器导入了一个`AspectJAutoProxyRegistrar`组件
7. 而`AspectJAutoProxyRegistrar`组件为容器注册了一个`AnnotationAwareAspectJAutoProxyCreator`组件，该组件是一个`AspectJ`注解自动代理创建器
8. 分析`AnnotationAwareAspectJAutoProxyCreator`发现它具有`BeanPostProcessor`接口特性和`InstantiationAwareBeanPostProcessor`的特性，而`InstantiationAwareBeanPostProcessor`是在实例化之前调用执行的
9. 在创建一个`bean`对象的时候，在`bean`的生命周期`createBean`环节调用`resolveBeforeInstantiation`来触发`InstantiationAwareBeanPostProcessor`中的`postProcessBeforeInstantiation`方法，在这个方法中就会把标注了`@Aspect`注解的类的信息找出来，并加入缓存中
10. 接下来在`BeanFactoryAware`接口的`postProcessBeforeInitialization`方法中创建要进行增强的对象的时候，根据方法进行匹配找自己的切面，然后把增强器和被增强的对象调用`createProxy`方法创建成一个代理对象
11. 代理对象的调用，根据用户设置的`proxyTargetClass`属性来制定使用哪种代理模式 如果为`true`则强制使用`CGLIB`代理，否则如果是接口方式就使用`JDK`动态代理，无接口则使用`CGLIB`代理
12. 通过责任链模式加递归的方式来进行调用
13. 先执行异常通知，如果抛出异常就执行`catch`中的代码
14. 再执行返回通知：返回通知的代码并没有被`try…catch`包起来，所有如果抛出异常就不会执行返回通知而是执行异常通知
15. 然后是后置通知：后置通知的代码放在`finally`块中，所以总是会被执行
16. 最后是前置通知
17. 条件满足，递归终止调用
18. 代理对象调用结束

