package io.wisoft.seminar.template;

public interface LineCallback<T> {
  T doSomethingWithLine(String line, T value);
}
