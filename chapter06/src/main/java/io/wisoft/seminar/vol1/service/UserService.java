package io.wisoft.seminar.vol1.service;

import io.wisoft.seminar.vol1.domain.User;

public interface UserService {
  void add(User user);

  void upgradeLevels();
}
