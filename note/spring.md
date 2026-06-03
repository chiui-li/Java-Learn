## spring 七大模块

### 基础核心(spring core container)

模块描述
整个Spring 框架的基石，提供依赖注入（DI）与控制反转（IOC）的功能，管理所有Bean生命周期

主要功能

1. ioc实现
2. 管理Bean的完整生命周期（创建、配置、组装、销毁）
3. 依赖注入、支持构造器注入、setter、字段注入
4. 容器事件的发布、监听
5. 提供资源访问抽象，统一管理各种资源

### Spring AOP (面向切面编程)

提供面向切面编程支持，允许将横切关注点（事物、日志、安全等）模块化，通过动态代理切入核心业务

主要功能

1. 通过基于代理的aop实现
2. 支持前置、后置、环绕、异常和最终通知的
3. 提供声明式事物管理的基础设施
4. 支持切面优先级和排序
5. 集成AspectJ切点表达式
6. 支持引入增强类功能

### Spring Context (应用上下文)

在核心容器基础上提供企业级服务，如国际化、事件传播、数据验证、定时调度，BeanFactory 增强者

主要功能

1. 提供MessageSource 支持国际化
2. 完善的事件发布与监听机制
3. 资源加载和Environment 抽象
4. 支持Profile 和 PropertySource
5. 提供Validator数据验证框架
6. 集成JNDI、EJB等企业服务

### Spring Web MVC（同步阻塞方案）

基于 Servlet 的同步阻塞式MVC框架，采用经典的前端控制器模式，适合传统web应用开发

主要功能

1. DispatcherServlet 前端控制器
2. 注解驱动的控制器（@Controller @RequestMapping）
3. 视图解析与多种视图技术支持
4. 数据绑定与验证支持
5. 拦截器和过滤器支持
6. RESTFUL web 服务支持

## Spring 使用

基础

### 1. 获取 Spring 上下文

```java
    // 获取 Spring Context 上下文, 启动容器的时候，会自动解析 beans.xml 初始化所有的Bean对象，将其放到 Map 对象中
    try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml")) {
        User bean = (User) ctx.getBean("userBean");
        System.out.println("------> \n" + bean);
    }



    // 多个xml
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "beans.xml");

    //
```

#### 疑问🤔

1. bean id 可以重名吗？
   不能

2. bean 是怎么创建对象的，是通过反射机制调用无参数构造函数吗？
   是的，bean 需要保证有无参数构造
   支持无参构造的class

   ```java
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        Date d = ctx.getBean("nowBean", java.util.Date.class);
        System.out.println("构造支持无参数的class \n" + d);
   ```

3. BeanFactory
   BeanFactory 是 Spring 容器的超级接口，ApplicationContext是BeanFactory的子接口

   ```java
           BeanFactory ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           Date d = ctx.getBean("nowBean", java.util.Date.class);
           System.out.println("构造支持无参数的class \n" + d);
   ```

### 2. 集成日志 - logback （spring-boot 自动接入）

引入依赖

```
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.4.11</version>
```

```java
package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {

    private static Logger logger = LoggerFactory.getLogger(User.class);

    @Override
    public String toString() {
        logger.debug("11111111toString method called");

        logger.error("some error");
        return "hello user";
    }
}
```

配置文件-可以使用 ai 生成

注意 root level ，低于 root level 不会输出
**规则：只有级别 ≥ 配置级别的日志才会输出**

级别 数值 用途 示例场景
TRACE 5000 最细粒度，追踪程序执行流程 方法进入/退出、循环每次迭代
DEBUG 10000 调试信息，开发阶段使用 变量值、参数内容、中间结果
INFO 20000 关键业务流程信息 服务启动/关闭、重要操作完成
WARN 30000 潜在问题，不影响运行 配置缺失使用默认值、API 降级
ERROR 40000 错误异常，需要关注 异常捕获、操作失败

### 3. 依赖注入 set 方法

1. 声明 bean
   <bean id="xxx" class="com.xxxx.xxx" />

2. 声明 依赖关系
   通过 xml的 ref标签 / 或者 ref 属性来装配

   ```xml
   <bean>
        <property name="xxx" ref="xxx" />
   </bean>

   <bean>
        <property name="xxx">
                <ref bean="beanId"/>
        </property>
   </bean>
   ```

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
		https://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 声明/定义一个 Bean, Spring 容器会自动管理这个Bean -->
    <!-- 通过全限定类名字符串获取class  Class.forName("com.example.User") -->
    <bean id="userBean" class="com.example.User" />
    <bean id="parent" class="com.example.Parent" />
    <!-- <bean id ="studentBean" class="com.example.Student">
      <!-- 这里 name 其实是找的是 setParent 而不是找的parent 属性 -->
      <property name="parent" ref="parent"/>
    </bean> -->
    <bean id ="studentBean" class="com.example.Student">
      <!-- 这里 name 其实是找的是 setParent 而不是找的parent 属性 -->
      <property name="parent" >
        <ref bean="parent"/>
      </property>
    </bean>
</beans>
```

### 4. 构造注入

有循坏依赖问题，很难解决，不建议使用
index 是参数位置
比较健壮，支持多种传参方式
`<constructor-arg index="0" ref="grandBean" />`
`<constructor-arg ref="grandBean" />`
`<constructor-arg ref="grandBean" />`

```xml
    <bean id="grandBean" class="com.example.GrandParent" />

    <bean id="parent" class="com.example.Parent">
      <constructor-arg index="0" ref="grandBean" />
    </bean>

    <bean id="parent" class="com.example.Parent">
      <constructor-arg  ref="grandBean" />
    </bean>
```

### 内部 bean

一般是外部 bean ，内部 bean 也是支持的，支持 bean 嵌套

### 注入简单类型

通过 value 属性或者标签
集成mybatis 可能会涉及

### 级联属性赋值 - 了解就行

### 注入 list map set

```xml
      <property name="list">
        <list>
          <value>a</value>
          <value>b</value>
          <value>c</value>
        </list>
      </property>
      <property name="set">
        <set>
          <value>a</value>
          <value>b</value>
          <value>c</value>
        </set>
      </property>
      <property name="map">
        <map>
          <entry key="a" value="1"></entry>
          <entry key="b" value="2"></entry>
          <entry key="c" value="3"></entry>
        </map>
      </property>
```

### 注入 "" 空字符和 null

注入 null

```xml
<bean id="xxx"  class="com.xxxx"/>

<null />
```

注入 ""

```xml
<proprety name="a">
   <value />
</proprety>

<proprety name="a" value="">
</proprety>
```

### 注入 包含特殊符号 " ' < > &

查表转义

### p 命名注入

简化xml配置,基于 setter 注入

```xml
<!-- 传统方式：需要写多个<property>标签 -->
<bean id="employee" class="com.example.Employee">
    <property name="id" value="1001"/>
    <property name="name" value="王五"/>
    <property name="address" ref="addrBean"/>
</bean>

<!-- p命名空间：一行搞定 -->
<bean id="employee" class="com.example.Employee"
      p:id="1001"
      p:name="王五"
      p:address-ref="addrBean"/>
```

### c 命名空间注入

基于 constructor 注入

```xml
<bean id="userService" class="com.example.UserService"
      c:userDao-ref="userDao"
      c:timeout="5000"/>
```

### 自动装配 bean 类型和名称

### 引入.propretis 文件注入 context

```xml
<context:property-placeholder location="jdbc.properties"/>
```

### bean 实例化方式 4 中

### bean 生命周期

1. 5个阶段
   实例化 -> 属性赋值 > 初始化-> 使用 -> 销毁
   对应 ----------
   构造方法 -> set -> init -> 使用 -> 销毁

2. 7个阶段

### 循环依赖解决？setter 注入可解 constructor 不可解

### 声明 bean 注解

#### 对象创建 bean 注解

负责声明 Bean 的注解，常见四个
@Component
@Controller
@Service
@Repository
都是 其他三个都是 Component 的语义化

1. 先加 aop 依赖
2. 在配置文件中添加 context 命名空间
3. 在配置文件中添加要扫的包
4. 在 bean 上使用注解

#### 可以选择性需要实例化的注解
