package io.wisoft.seminar.vol1.domain;

import org.hibernate.AssertionFailure;

public enum Level {
  BASIC(1, SILVER), SILVER(2, GOLD), GOLD(3, null);

  private final int value;
  private final Level next;

  Level(int value, Level next) {
    this.value = value;
    this.next = next;
  }

  public int intValue() {
    return value;
  }

  public Level nextLevel() {
    return this.next;
  }

  public static Level valueOf(int value) {
    switch (value) {
      case 1:
        return BASIC;
      case 2:
        return SILVER;
      case 3:
        return GOLD;
      default:
        throw new AssertionError("Unknown value: " + value);
    }
  }
}
