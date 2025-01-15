package io.wisoft.tutorial.chapter01.user.dao;

import io.wisoft.tutorial.chapter01.user.domain.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.sql.SQLException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class UserDaoTest {
  private static final Log log = LogFactory.getLog(UserDaoTest.class);

  @Test
  public void addAndGet() throws SQLException {

    ApplicationContext context =
            new AnnotationConfigApplicationContext(DaoFactory.class);

    UserDao dao = context.getBean("userDao", UserDao.class);
    User user = new User();
    user.setId("gyumee");
    user.setName("박성철");
    user.setPassword("springno1");

    dao.add(user);


    User user2 = dao.get(user.getId());

    assertThat(user2.getName(), is(user.getName()));
    assertThat(user2.getPassword(), is(user.getPassword()));

  }
}
