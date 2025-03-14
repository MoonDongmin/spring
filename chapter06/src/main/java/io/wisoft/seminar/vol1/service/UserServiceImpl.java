package io.wisoft.seminar.vol1.service;

import io.wisoft.seminar.vol1.dao.UserDao;
import io.wisoft.seminar.vol1.dao.UserLevelUpgradePolicy;
import io.wisoft.seminar.vol1.domain.Level;
import io.wisoft.seminar.vol1.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Transactional
public class UserServiceImpl implements UserService {

  private UserDao userDao;
  private UserLevelUpgradePolicy upgradePolicy;
  private PlatformTransactionManager transactionManager;
  private MailSender mailSender;

  public void setUserDao(final UserDao userDao) {
    this.userDao = userDao;
  }

  public void setUserLevelUpgradePolicy(final UserLevelUpgradePolicy upgradePolicy) {
    this.upgradePolicy = upgradePolicy;
  }

  public void setTransactionManager(final PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public void setMailSender(final MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void upgradeLevels() {
    List<User> users = userDao.getAll();
    for (User user : users) {
      if (canUpgradeLevel(user)) {
        upgradeLevel(user);
      }
    }
  }

  // 한글 인코딩 생략
  private void sendUpgradeEmail(User user) {

    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setTo(user.getEmail());
    mailMessage.setFrom("useradmin@ksug.org");
    mailMessage.setSubject("Upgrade 안내");
    mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");

    this.mailSender.send(mailMessage);
  }


  public void add(final User user) {
    if (user.getLevel() == null) {
      user.setLevel(Level.BASIC);
    }
    userDao.add(user);
  }


  public boolean canUpgradeLevel(final User user) {
    return false;
  }

  public void upgradeLevel(final User user) {
    user.upgradeLevel();
    userDao.update(user);
    sendUpgradeEmail(user);

  }

  public void deleteAll() {
    userDao.deleteAll();
  }

  public User get(String id) {
    return userDao.get(id);
  }

  public List<User> getAll() {
    return userDao.getAll();
  }

  public void update(final User user) {
    userDao.update(user);
  }
}