package io.wisoft.seminar.vol1.dao;

import io.wisoft.seminar.vol1.domain.User;

public interface UserLevelUpgradePolicy {
  boolean canUpgradeLevel(User user);

  void upgradeLevel(User user);
}
