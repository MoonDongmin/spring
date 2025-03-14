package io.wisoft.seminar.vol1.service;

import io.wisoft.seminar.vol1.domain.User;

public class TestUserServiceImpl extends UserServiceImpl {
  private String id = "madnite1";

  public void upgradeLevel(User user) {
    if (user.getId().equals(id)) {
      throw new TestUserServiceException();
    }
    super.upgradeLevel(user);  // Call the original method

  }

}