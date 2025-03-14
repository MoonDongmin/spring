package io.wisoft.seminar.proxy;

public class HelloUppercase implements Hello {
  Hello hello;

  public HelloUppercase(Hello hello) {
    this.hello = hello;
  }

  @Override
  public String sayHello(final String name) {
    return hello.sayHello(name).toUpperCase();
  }

  @Override
  public String sayHi(final String name) {
    return hello.sayHi(name).toUpperCase();
  }

  @Override
  public String sayThankYou(final String name) {
    return hello.sayThankYou(name).toUpperCase();
  }
}