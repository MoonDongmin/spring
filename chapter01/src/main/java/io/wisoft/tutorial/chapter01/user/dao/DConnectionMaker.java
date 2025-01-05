package io.wisoft.tutorial.chapter01.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DConnectionMaker implements  ConnectionMaker{

  @Override
  public Connection makeConnection() throws ClassNotFoundException, SQLException {
    return null;
  }
}
