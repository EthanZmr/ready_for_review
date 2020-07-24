# SpringIOC容器

## bean创建的前期准备工作

```java
AnnotationConfigApplicationContext ctx = 
    new AnnotationConfigApplicationContext(MainConfi.class);
```

1. 调用`AnnotationConfigApplicationContext`无参构造器
2. 在此无参构造器中调用父类的无参构造器，创建了一个`AnnotatedBeanDefinitionReader`注解`bean`定义读取器以及一个`ClassPathBeanDefinitionScanner`类路径下的`bean`定义扫描器
   1. 在父类的无参构造器中创建了一个`DefaultListableBeanFactory`类型的`ioc`容器
   2. 在`AnnotatedBeanDefinitionReader`的`bean`定义读取器中会为容器创建环境，创建一个条件计算器对象，还会为容器中注册一系列`spring`内部的`bean`定义组件
   3. 在`ClassPathBeanDefinitionScanner`类路径下的`bean`定义扫描器中，如果用户配置了`useDefaultFilters`属性，就按照用户配置的规则扫描，如果用户没有配置则按照默认的规则扫描出`Component`及其子注解的组件
3. 下一步会使用生成的`bean`定义读取器来注册配置类到容器中去
   1. 调用`register`方法
   2. 在`register`方法中调用`registerBean`方法
   3. 在`registerBean`方法中调用`doRegisterBean`将`MainConfig`配置类的`bean`定义信息注册到容器中
4. 调用`refresh`方法刷新`ioc`容器



## bean的实例化过程

在`bean`的前期准备工作做完之后，就可以开始进行`bean`的实例化了

```java
Object obj = ctx.getBean(beanName);
```

1. 在`ctx.getBean`方法内调用`doGetBean`方法来处理获取`bean`的逻辑
2. 在`doGetBean`方法中做了以下事情：
   1. 调用`transformedBeanName`转换`beanName`如别名，过滤去除`beanFactory`的前缀`&`
   2. 尝试去缓存中获取`bean`对象
      1. 如果能获取到，则对获取到的`bean`调用`getObjectForBeanInstance`方法进行后续处理之后返回
      2. 如果获取不到
         1. 首先判断该`bean`是否有`prototype`的循环依赖问题，如果有直接抛出异常
         2. 尝试从父容器中获取`bean`
            1. 如果不为空，并且当前容器不包含该`bean` 则调用父容器对象的`doGetBean`方法获取`bean`对象，进行一系列的判断之后返回调用`getBean`方法获取到的`bean`对象
            2. 如果父容器中也获取不到`bean`，则从父容器中走创建`bean`的流程
         3. 合并父`bean`定义和子`bean`定义，转换为`RootBeanDefinition`类型
         4. 检查当前`bean`的依赖
         5. 根据当前`bean`的`scope`来创建`bean`实例
         6. 单例:调用`getSingleton`方法，在此方法中进行回调`createBean`方法
         7. `getSingleton`方法中：
            1. 首先尝试从单例缓存池中获取`bean`
            2. 获取不到则将当前`bean`放入`singletonCurrentlyInCreation`中标记起来
            3. 通过调用`singletonFactory.getObject()`来触发`createBean`方法的流程
            4. 在`createBean`内部调用`doCreateBean`来实现真正的创建`bean`的逻辑
            5. 在`doCreateBean`方法中：
               1. 调用`createBeanInstance`方法创建一个当前`bean`的早期对象
               2. 判断是否需要提前暴露对象用于解决循环依赖问题，如果需要，就将该早期对象放入`singletonFactories`缓存中暴露出来
               3. 调用`populateBean`方法为`bean`的属性进行赋值，解决一下依赖的问题
               4. 调用`bean`的后置处理器以及`initializeBean`和用户自己定义的方法进行初始化
               5. 如果当前`bean`为早期对象，尝试去缓存中获取`bean`,如果没有循环依赖则获取不到
                  1. 如果获取到，检查当前`bean`在初始化方法中有没有被代理过
               6. 调用`registerDisposableBeanIfNecessary`注册`DisposableBean`，如果定义了`destroy-method`则需要在这里进行注册以便于在销毁时调用
            6. 调用`getObjectForBeanInstance`对`bean`实例进行后续处理之后返回该`bean`对象
            7. 对象创建完毕，从`singletonCurrentlyInCreation`中移除
            8. 将对象放入单例缓存池中然后返回该对象
      3. 如果获取到，就调用`getObjectForBeanInstance`对`bean`对象进行处理，然后返回`bean`

## spring解决单例bean的循环依赖

前提：`beanA`中引用`beanB`

​	   `beanB`中引用`beanA`



1. 获取`beanA`：`getBean`方法
2. `getBean`调用`doGetBean`方法
3. `doGetBean`方法中调用`getSingleton`从缓存中获取
4. 获取不到就调用`getSingleton(instA, objectFactory)`触发`createBean`流程
5. 在`createBean`中调用`doCreateBean`方法执行真正的创建`bean`的逻辑
6. `doCreateBean`中调用`createBeanInstance`创建`bean`的早期对象
7. 将`bean`的早期对象放入到`singletonFactories`缓存中暴露出来
8. 执行`populate`方法对`bean`进行属性设置，在属性设置的时候发现依赖`beanB`
   1. 就会调用`getBean`方法获取`beanB`
   2. 调用`doGetbean`
   3. 调用`getSingleton`
   4. 调用`getSingleton(instA, objectFactory)`触发`createBean`
   5. 在`createBean`中调用`doCreateBean`
   6. 在`doCreateBean`中调用`createBeanInstance`创建早期对象
   7. 将`beanB`早期对象放入`singletonFactories`缓存中暴露出来
   8. 调用`populate`进行属性赋值的时候发现依赖`beanA`
      1. 调用`getBean`获取`beanA`
      2. 调用`doGetBean`
      3. 在`doGetBean`中调用`getSingleton`尝试从缓存中获取`beanA`
      4. 由于之前已经将`beanA`放入到缓存中暴露，所以能够获取到
   9. 完成`beanB`的属性赋值
   10. 对`beanB`进行后续处理
   11. 返回完整的`beanB`对象
9. 完成`beanA`的属性赋值
10. 对`beanA`进行后续处理
11. 返回完整的`beanA`对象

**由此，以上步骤解决了spring中的循环依赖问题**

**spring中无法解决在构造器中传参的循环依赖问题**

原因：由于暴露对象到缓存中是在创建早期对象之后进行的，所以无法从缓存中获取到依赖的对象

**spring中也无法解决prototype的循环依赖问题**

原因：`spring`中`prototype`类型的对象不会进行缓存，每次都是创建一个新的对象

