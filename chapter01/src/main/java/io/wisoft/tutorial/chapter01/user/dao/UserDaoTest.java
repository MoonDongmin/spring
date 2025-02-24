package io.wisoft.tutorial.chapter01.user.dao;

import io.wisoft.tutorial.chapter01.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
  public static void main(String[] args) throws SQLException, ClassNotFoundException {

    ApplicationContext context =
            new AnnotationConfigApplicationContext(DaoFactory.class);

    UserDao dao = context.getBean("userDao", UserDao.class);

    User user = new User();
    user.setId("whiteship");
    user.setName("백기선");
    user.setPassword("married");

    dao.add(user);

    System.out.println(user.getId() + " 등록 성공");

    User user2 = dao.get(user.getId());
    System.out.println(user2.getName());

    System.out.println(user2.getPassword());
    System.out.println(user2.getId() + "조회 성공");
  }
}
