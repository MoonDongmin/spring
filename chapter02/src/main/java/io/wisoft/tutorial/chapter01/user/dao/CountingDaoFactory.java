package io.wisoft.tutorial.chapter01.user.dao;//package io.wisoft.tutorial.chapter01.user.dao;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CountingDaoFactory {
//  @Bean
//  public UserDao userDao() {
//    return new UserDao(connectionMaker());
//  }
//
//  @Bean
//  public ConnectionMaker connectionMaker() {
//    return new CountingConnectionMaker(realConnectionMaker());
//  }
//
//  @Bean
//  public ConnectionMaker realConnectionMaker() {
//    return new DConnectionMaker();
//  }
//}
