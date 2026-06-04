// package com.example.springdemo;

// import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class RabbitMQ {

// @Bean
// public RabbitTemplate initRabbitMQ() {
// CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
// connectionFactory.setHost("localhost");
// connectionFactory.setPort(5672);
// connectionFactory.setUsername("guest");
// connectionFactory.setPassword("guest");
// // 设置连接超时时间，单位为毫秒
// connectionFactory.setConnectionTimeout(15000); // 15秒超时时间
// // 设置心跳时间，单位为秒
// // connectionFactory
// // connectionFactory.setRequestedHeartbeat(60); // 心跳间隔为60秒
// return connectionFactory;
// }
// }
