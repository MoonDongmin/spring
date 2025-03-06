package io.wisoft.seminar.vol1.dao;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

public interface MailSender {
  void send(SimpleMailMessage simpleMailMessage) throws MailException;

  void send(SimpleMailMessage[] simpleMailMessages) throws MailException;
}
