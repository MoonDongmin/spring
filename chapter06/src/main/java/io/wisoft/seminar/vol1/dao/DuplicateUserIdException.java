package io.wisoft.seminar.vol1.dao;

public class DuplicateUserIdException extends RuntimeException {
  public DuplicateUserIdException(Throwable cause) {
    super(cause);
  }
}
