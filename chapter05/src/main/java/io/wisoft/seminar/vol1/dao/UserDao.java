package io.wisoft.seminar.vol1.dao;

import io.wisoft.seminar.vol1.domain.User;

import java.sql.Connection;
import java.util.List;

public interface UserDao {

  void add(User user);

  User get(String id);

  List<User> getAll();

  void deleteAll();

  int getCount();

  public void update(User user);

}