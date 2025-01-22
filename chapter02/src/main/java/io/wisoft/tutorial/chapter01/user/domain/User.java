package io.wisoft.tutorial.chapter01.user.domain;

public class User {
  String id;
  String name;
  String password;

  public User(final String id, final String name, final String password) {
    this.id = id;
    this.name = name;
    this.password = password;
  }

  public User() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
