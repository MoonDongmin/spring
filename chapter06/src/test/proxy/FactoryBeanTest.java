package proxy;

import io.wisoft.seminar.proxy.Message;
import jakarta.annotation.security.RunAs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration(locations = "/FactoryBeanTest-context.xml")
public class FactoryBeanTest {
  @Autowired
  ApplicationContext context;

  @Test
  public void getMessageFromFactoryBean() {
    Object message = context.getBean("&message");
    assertThat(message).isEqualTo(Message.class);
    assertThat(((Message) message).getText()).isEqualTo("Factory Bean");
  }
}
