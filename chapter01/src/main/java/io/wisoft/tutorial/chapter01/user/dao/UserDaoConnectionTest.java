//package io.wisoft.tutorial.chapter01.user.dao;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//public class UserDaoConnectionTest {
//  public static void main(String[] args) {
//    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
//    UserDao dao = context.getBean("userDao", UserDao.class);
//
//    // DAO 사용 코드
//
//    CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
//    System.out.println("Connection counter: " + ccm.getCounter());
//  }
//}
