package io.wisoft.seminar.proxy;

import io.wisoft.seminar.vol1.service.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {
  String text;

  public void setText(final String text) {
    this.text = text;
  }

  public Message getObject() throws Exception {
    return Message.newMessage(this.text);
  }

  public Class<? extends Message> getObjectType() {
    return Message.class;
  }

  public boolean isSingleton() {
    return false;
  }
}
