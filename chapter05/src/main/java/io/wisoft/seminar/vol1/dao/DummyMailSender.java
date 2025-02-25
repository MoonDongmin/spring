package io.wisoft.seminar.vol1.dao;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {
  @Override
  public void send(final SimpleMailMessage simpleMailMessage) throws MailException {
  }

  @Override
  public void send(final SimpleMailMessage[] simpleMailMessages) throws MailException {

  }
}
