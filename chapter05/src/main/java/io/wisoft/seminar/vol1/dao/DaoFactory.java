package io.wisoft.seminar.vol1.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

  @Bean
  public DataSource dataSource() {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

    dataSource.setDriverClass(org.postgresql.Driver.class);
    dataSource.setUrl("jdbc:postgresql://127.0.0.1:5432/springbook");
    dataSource.setUsername("postgres");
    dataSource.setPassword("postgres");

    return dataSource;
  }


  @Bean
  public UserDaoJdbc userDao() {
    UserDaoJdbc dao = new UserDaoJdbc();
    dao.setDataSource(dataSource());
    return dao;
  }


}
