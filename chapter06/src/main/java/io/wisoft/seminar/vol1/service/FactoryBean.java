package io.wisoft.seminar.vol1.service;

public interface FactoryBean<T> {
  T getObject() throws Exception;

  Class<? extends T> getObjectType();

  boolean isSingleton();
}
