package io.wisoft.seminar.proxy;

public class Message {
  String text;

  private Message(final String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public static Message newMessage(final String text) {
    return new Message(text);
  }
}
