package io.wisoft.tutorial.chapter01.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {
  int counter = 0;
  private ConnectionMaker realConnectionMaker;

  public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
    this.realConnectionMaker = realConnectionMaker;
  }


  @Override
  public Connection makeConnection() throws ClassNotFoundException, SQLException {
    this.counter++;
    return realConnectionMaker.makeConnection();
  }

  public int getCounter() {
    return this.counter;
  }
}
