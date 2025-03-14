package io.wisoft.seminar.vol1.service;

import io.wisoft.seminar.vol1.domain.User;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public interface UserService {

  void add(User user);

  @Transactional
  User get(String id);
  @Transactional
  List<User> getAll();

  void deleteAll();

  void update(User user);

  void upgradeLevels();
}
