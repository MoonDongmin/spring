package tobyspring.vol1.service;

import io.wisoft.seminar.vol1.dao.UserDaoJdbc;
import io.wisoft.seminar.vol1.dao.UserLevelUpgradePolicy;
import io.wisoft.seminar.vol1.domain.Level;
import io.wisoft.seminar.vol1.domain.User;
import io.wisoft.seminar.vol1.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

import static io.wisoft.seminar.vol1.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static io.wisoft.seminar.vol1.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(locations = "/applicationContext.xml")
class UserServiceTest {
  @Autowired
  DataSource dataSource;

  @Autowired
  private UserService userService;
  List<User> users;
  @Autowired
  private UserDaoJdbc userDao;
  @Autowired
  private PlatformTransactionManager transactionManager;
  private UserLevelUpgradePolicy upgradePolicy;

  @BeforeEach
  void setUp() {
    users = List.of(new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
            // silver 승급
            new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            // gold 승급
            new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE));

    upgradePolicy = new DefaultUserLevelUpgradePolicy();
    userService.setUserLevelUpgradePolicy(upgradePolicy);
  }

  @Test
  public void upgradeLevel() {
    Level[] levels = Level.values();
    for (Level level : levels) {
      if (level.nextLevel() == null) continue;
      user.setLevel(level);
      user.upgradeLevel();
      assertThat(user.getLevel(), is(level.nextLevel()));
    }
  }

  @Test(expected = IllegalStateException.class)
  public void cannotUpdateLevel() {
    Level[] levels = Level.values();
    for (Level level : levels) {
      if (level.nextLevel() == null) continue;
      user.setLevel(level);
      user.upgradeLevel();
    }
  }

  @Test
  public void upgradeAllOrNothing() throws Exception {
    UserService testUserService = new TestUserService(users.get(3)
            .getId());
    testUserService.setUserDao(this.userDao);
    testUserService.setTransactionManager(transactionManager);

    userDao.deleteAll();

    for (User user : users) {
      userDao.add(user);
    }

    try {
      testUserService.upgradeLevels();
      fail("TestUserServiceException expected");
    } catch (TestUserServiceException e) {
      e.getMessage();
    }

    checkLevel(users.get(1), false);
  }


  @Test
  public void upgradeLevels() throws Exception {
    userDao.deleteAll();

    for (User user : users) {
      userDao.add(user);
    }

    userService.upgradeLevels();

    checkLevel(users.get(0), false);
    checkLevel(users.get(1), true);
    checkLevel(users.get(2), false);
    checkLevel(users.get(3), true);
    checkLevel(users.get(4), false);


  }

  private void checkLevel(User user, boolean upgraded) {
    final User userUpdate = userDao.get(user.getId());

    if (upgraded) {
      assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel()
              .nextLevel());
    } else {
      assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
    }

  }

  @Test
  public void add() {
    userDao.deleteAll();

    User userWithLevel = users.get(4); // GOLD 레벨 지정, 초기화 X
    User userWithoutLevel = users.get(0); // 레벨이 비어있는 사용자, BASIC 초기화 필요
    userWithoutLevel.setLevel(null);

    userService.add(userWithLevel);
    userService.add(userWithoutLevel);

    User userWithLevelRead = userDao.get(userWithLevel.getId());
    User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

    assertThat(userWithLevelRead.getLevel()).is(userWithLevel.getLevel());
    assertThat(userWithoutLevelRead.getLevel()).is(Level.BASIC));

  }

  static class TestUserService extends UserService {
    private String id;

    private TestUserService(String id) {
      this.id = id;
    }

    protected void upgradeLevel(User user) {
      if (user.getId().equals(this.id)) throw new TestUserServiceException();
      super.upgradeLevel(user);
    }
  }


  static class TestUserServiceException extends RuntimeException {
  }

}