package io.wisoft.seminar.vol1.service;

import io.wisoft.seminar.vol1.domain.User;

public interface UserLevelUpgradePolicy {
  boolean canUpgradeLevel(final User user);

  void upgradeLevels(User user);
}