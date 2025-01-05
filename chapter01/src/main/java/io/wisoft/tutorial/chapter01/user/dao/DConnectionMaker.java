package io.wisoft.tutorial.chapter01.user.dao;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {
  Dotenv dotenv = Dotenv.configure().directory("/Users/moon/Developments/graduate-school/tobyspring/chapter01/.env").load();

  @Override
  public Connection makeConnection() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/tobySpring", dotenv.get("ID"), dotenv.get("PASSWORD"));

    return c;
  }
}
