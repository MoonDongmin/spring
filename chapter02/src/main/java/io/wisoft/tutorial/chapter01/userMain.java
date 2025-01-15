package io.wisoft.tutorial.chapter01;

import io.wisoft.tutorial.chapter01.user.dao.DaoFactory;
import io.wisoft.tutorial.chapter01.user.dao.UserDao;
import io.wisoft.tutorial.chapter01.user.domain.User;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class userMain {
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    DaoFactory factory = new DaoFactory();
    UserDao dao1 = factory.userDao();
    UserDao dao2 = factory.userDao();

    System.out.println(dao1);
    System.out.println(dao2);

    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    UserDao dao3 = context.getBean("userDao", UserDao.class);
    UserDao dao4 = context.getBean("userDao", UserDao.class);

    System.out.println(dao3);
    System.out.println(dao4);
  }
}
