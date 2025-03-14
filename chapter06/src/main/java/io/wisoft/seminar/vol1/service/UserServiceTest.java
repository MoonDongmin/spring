package io.wisoft.seminar.vol1.service;

import io.wisoft.seminar.vol1.dao.UserDao;
import io.wisoft.seminar.vol1.dao.UserDaoJdbc;
import io.wisoft.seminar.vol1.dao.UserLevelUpgradePolicy;
import io.wisoft.seminar.vol1.domain.Level;
import io.wisoft.seminar.vol1.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.wisoft.seminar.vol1.service.DefaultUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static io.wisoft.seminar.vol1.service.DefaultUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(locations = "/applicationContext.xml")
class UserServiceTest {

  @Autowired
  ApplicationContext context;
  @Autowired
  UserService testUserService;

  @Autowired
  UserService userService;

  @Autowired
  UserServiceImpl userServiceImpl;

  List<User> users;

  @Autowired
  private UserDaoJdbc userDao;

  @Autowired
  private PlatformTransactionManager transactionManager;

  private UserLevelUpgradePolicy upgradePolicy;

  @Autowired
  private MailSender mailSender;

  @BeforeEach
  void setUp() {
    users = List.of(new User("bumjin", "박범진", "p1", "a@email.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
            // silver 승급
            new User("joytouch", "강명성", "p2", "b@email.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("erwins", "신승한", "p3", "c@email.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            // gold 승급
            new User("madnite1", "이상호", "p4", "d@email.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            new User("green", "오민규", "p5", "e@email.com", Level.GOLD, 100, Integer.MAX_VALUE));

    upgradePolicy = new DefaultUserLevelUpgradePolicy();
    userServiceImpl.setUserLevelUpgradePolicy(upgradePolicy);
  }


  @Test
  @DirtiesContext
  public void upgradeAllOrNothing() throws Exception {
    final UserServiceImpl testUserService = new TestUserService(users.get(3).getId());
    testUserService.setUserDao(this.userDao);
    testUserService.setMailSender(this.mailSender);

    ProxyFactoryBean txProxyFactoryBean =
            context.getBean("&userService", ProxyFactoryBean.class);
    txProxyFactoryBean.setTarget(testUserService);
    UserService txUserService =
            (UserService) txProxyFactoryBean.getObject();

    userDao.deleteAll();

    for (User user : users) {
      userDao.add(user);
    }

    try {
      txUserService.upgradeLevels();
      fail("TestUserServiceException expected");
    } catch (TestUserServiceException e) {
      e.getMessage();
    }

    checkLevel(users.get(3), false);
  }


  @Test
  @DirtiesContext
  public void upgradeLevels() {
    UserServiceImpl userServiceImpl = new UserServiceImpl();

    UserDao mockUserDao = mock(UserDao.class);
    when(mockUserDao.getAll()).thenReturn(this.users);
    userServiceImpl.setUserDao(mockUserDao);


    MailSender mockMailSender = mock(MailSender.class);
    userServiceImpl.setMailSender(mockMailSender);

    userServiceImpl.upgradeLevels();

    verify(mockUserDao, times(2)).update(any(User.class));
    verify(mockUserDao, times(2)).update(any(User.class));
    verify(mockUserDao).update(users.get(1));
    assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
    verify(mockUserDao).update(users.get(3));
    assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);

    ArgumentCaptor<SimpleMailMessage> mailMessageArg =
            ArgumentCaptor.forClass(SimpleMailMessage.class);
    verify(mockMailSender, times(2)).send(mailMessageArg.capture());
    List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
    assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
    assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());
  }

  private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
    assertThat(updated.getId()).isEqualTo(expectedId);
    assertThat(updated.getLevel()).isEqualTo(expectedLevel);
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

    final User userWithLevel = users.get(4); // GOLD 레벨 지정, 초기화 X
    final User userWithoutLevel = users.get(0); // 레벨이 비어있는 사용자, BASIC 초기화 필요
    userWithoutLevel.setLevel(null);

    userService.add(userWithLevel);
    userService.add(userWithoutLevel);

    final User userWithLevelRead = userDao.get(userWithLevel.getId());
    final User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

    assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
    assertThat(userWithoutLevelRead.getLevel()).isEqualTo(userWithoutLevel.getLevel());

  }

  static class TestUserServiceImpl extends UserServiceImpl {
    private String id = "madnite1";

    public void upgradeLevel(User user) {
      if (user.getId().equals(id)) {
        throw new TestUserServiceException();
      }
      super.upgradeLevel(user);  // Call the original method

    }

  }


  static class TestUserServiceException extends RuntimeException {
  }

  static class DummyMailSender implements MailSender {

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {

    }
  }

  static class MockMailSender implements MailSender {

    private List<String> requests = new ArrayList<>();

    public List<String> getRequest() {
      return requests;
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
      // 전송 요청 받은 이메일 주소 저장
      requests.add(Objects.requireNonNull(simpleMessage.getTo())[0]);
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }

  }

  static class MockUserDao implements UserDao {
    private List<User> users;
    private List<User> updated = new ArrayList();

    private MockUserDao(List<User> users) {
      this.users = users;
    }

    @Override
    public void add(final User user) {
      throw new UnsupportedOperationException();
    }

    @Override
    public User get(final String id) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
      throw new UnsupportedOperationException();

    }

    @Override
    public int getCount() {
      throw new UnsupportedOperationException();
    }

    public List<User> getUpdated() {
      return this.updated;
    }

    @Override
    public List<User> getAll() {
      return this.users;
    }

    @Override
    public void update(final User user) {
      updated.add(user);
    }
  }

}