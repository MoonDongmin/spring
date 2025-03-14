package io.wisoft.seminar.vol1.service;

import io.wisoft.seminar.vol1.dao.UserLevelUpgradePolicy;
import io.wisoft.seminar.vol1.domain.Level;
import io.wisoft.seminar.vol1.domain.User;

public class DefaultUserLevelUpgradePolicy implements UserLevelUpgradePolicy {

  public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
  public static final int MIN_RECOMMEND_FOR_GOLD = 30;

  @Override
  public boolean canUpgradeLevel(final User user) {
    final Level currentLevel = user.getLevel();

    switch (currentLevel) {
      case BASIC:
        return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
      case SILVER:
        return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
      case GOLD:
        return false;
      default:
        throw new IllegalArgumentException("Unknown Level value: " + currentLevel);
    }
  }

  @Override
  public void upgradeLevel(final User user) {
    user.upgradeLevel();
  }
}