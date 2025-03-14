package io.wisoft.seminar.vol1.service;

import io.wisoft.seminar.vol1.domain.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class UserServiceTx implements UserService {
  UserService userService;
  PlatformTransactionManager transactionManager;

  public void setTransactionManager(
          PlatformTransactionManager transactionManager
  ) {
    this.transactionManager = transactionManager;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void add(final User user) {
    userService.add(user);
  }

  @Override
  public void upgradeLevels() {
    TransactionStatus status = this.transactionManager
            .getTransaction(new DefaultTransactionDefinition());
    try {
      userService.upgradeLevels();

      this.transactionManager.commit(status);

    } catch (RuntimeException e) {
      this.transactionManager.rollback(status);
      throw e;
    }
  }
}
