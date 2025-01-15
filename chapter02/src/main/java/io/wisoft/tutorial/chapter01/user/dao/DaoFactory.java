package io.wisoft.tutorial.chapter01.user.dao;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {
  Dotenv dotenv = Dotenv.configure().directory("/Users/moon/Developments/graduate-school/tobyspring/chapter01/.env").load();


  @Bean
  public UserDao userDao() {
    UserDao userDao = new UserDao();
    userDao.setDataSource(dataSource());
    return userDao;
  }

  @Bean
  public DataSource dataSource() {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

    dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
    dataSource.setUrl("jdbc:mysql://localhost:3306/tobySpring");
    dataSource.setUsername(dotenv.get("ID"));
    dataSource.setPassword(dotenv.get("PASSWORD"));

    return dataSource;
  }
}
