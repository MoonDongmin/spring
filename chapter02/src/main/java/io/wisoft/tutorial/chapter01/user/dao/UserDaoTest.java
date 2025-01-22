package io.wisoft.tutorial.chapter01.user.dao;

import io.wisoft.tutorial.chapter01.user.domain.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThrows;

@RunWith(SpingJUnit4ClassRunner.class)
@ContextConfiguratoin(location = "/applicationContext.xml")
public class UserDaoTest {
  private static final Log log = LogFactory.getLog(UserDaoTest.class);
  private UserDao dao;
  private User user1;
  private User user2;
  private User user3;

  @Before
  public void setUp() {
    ApplicationContext context =
            new AnnotationConfigApplicationContext(DaoFactory.class);

    UserDao dao = context.getBean("userDao", UserDao.class);

    this.user1 = new User("dongmin1", "문동민1", "springno1");
    this.user2 = new User("dongmim2", "문동민2", "springno2");
    this.user3 = new User("dongmin3", "문동민3", "springno3");
  }

  @Test
  public void addAndGet() throws SQLException {

    ApplicationContext context =
            new AnnotationConfigApplicationContext(DaoFactory.class);

    UserDao dao = context.getBean("userDao", UserDao.class);
    User user1 = new User("Dognmin", "문동민", "springno1");
    User user2 = new User("Dognmin2", "문동민2", "springno2");

    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    User userget1 = dao.get(user1.getId());
    assertThat(userget1.getName(), is(user1.getName()));
    assertThat(userget1.getPassword(), is(user1.getPassword()));

    User userget2 = dao.get(user2.getId());
    assertThat(userget2.getName(), is(user2.getName()));
    assertThat(userget2.getPassword(), is(user2.getPassword()));

  }

  @Test
  public void count() throws SQLException {
    ApplicationContext context =
            new AnnotationConfigApplicationContext(DaoFactory.class);

    UserDao dao = context.getBean("userDao", UserDao.class);
    User user1 = new User("dongmin1", "문동민1", "springno1");
    User user2 = new User("dongmim2", "문동민2", "springno2");
    User user3 = new User("dongmin3", "문동민3", "springno3");

    dao.deleteAll();
    assertThat(dao.getCount(), is(1));

    dao.add(user1);
    assertThat(dao.getCount(), is(1));

    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    dao.add(user3);
    assertThat(dao.getCount(), is(3));
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void getUserFailure() throws SQLException {
    ApplicationContext context =
            new AnnotationConfigApplicationContext(DaoFactory.class);

    UserDao dao = context.getBean("userDao", UserDao.class);
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.get("unknown_id");
  }
}
