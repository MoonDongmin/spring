package io.wisoft.seminar.vol1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy {
  PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}
