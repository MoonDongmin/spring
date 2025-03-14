package proxy;

import io.wisoft.seminar.proxy.UppercaseHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DynamicProxyTest {
  @Test
  public void classNamePointcutAdvisor() {
    NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
      public ClassFilter getClassFilter() {
        return new ClassFilter() {
          public boolean matches(Class<?> clazz) {
            return clazz.getSimpleName().startsWith("HelloT");
          }
        };
      }
    };
    classMethodPointcut.setMappedName("SayH*");

    // 테스트
    checkAdviced(new HelloTarget(), classMethodPointcut, true);

    class HelloWorld extends HelloTarget {
    }

    checkAdviced(new HelloTarget(), classMethodPointcut, false);

    class HelloToby extends HelloTarget {
    }

    checkAdviced(new HelloToby(), classMethodPointcut, true);
  }

  private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
    ProxyFactoryBean pfBean = new ProxyFactoryBean();
    pfBean.setTarget(target);
    pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
    Hello proxiedHello = (Hello) pfBean.getObject();

    if (adviced) {
      assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
      assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
      assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
    } else {
      assertThat(proxiedHello.sayHello("Toby")).isEqualTo("Hello Toby");
      assertThat(proxiedHello.sayHi("Toby")).isEqualTo("Hi Toby");
      assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
    }
  }

  @Test
  public void simpleProxy() {
    Hello hello = new HelloTarget();
    assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
    assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
    assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");


//    Hello proxiedHello = new HelloUppercase(new HelloTarget());
    Hello proxiedHello = (Hello) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[]{Hello.class},
            new UppercaseHandler(new HelloTarget())
    );
    assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
    assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
    assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
  }

  @Test
  public void proxyFactoryBean() {
    ProxyFactoryBean pfBean = new ProxyFactoryBean();
    pfBean.setTarget(new HelloTarget());
    pfBean.addAdvice(new UppercaseAdvice());

    Hello proxiedHello = (Hello) pfBean.getObject();
    assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
    assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
    assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
  }

  static class UppercaseAdvice implements MethodInterceptor {
    public Object invoke(MethodInvocation invocation) throws Throwable {
      String ret = (String) invocation.proceed();
      return ret.toUpperCase();
    }
  }

  @Test
  public void pointcutAdvisor() {
    ProxyFactoryBean pfBean = new ProxyFactoryBean();
    pfBean.setTarget(new HelloTarget());

    NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
    pointcut.setMappedName("sayH*");

    pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

    Hello hello = (Hello) pfBean.getObject();
    assertThat(hello.sayHello("Toby")).isEqualTo("HELLO TOBY");
    assertThat(hello.sayHi("Toby")).isEqualTo("HI TOBY");
    assertThat(hello.sayThankYou("Toby")).isEqualTo("thank you Toby");

  }

  static interface Hello {
    String sayHello(String name);

    String sayHi(String name);

    String sayThankYou(String name);
  }

  static class HelloTarget implements Hello {
    public String sayHello(String name) {
      return "Hello " + name;
    }

    public String sayHi(String name) {
      return "Hi " + name;
    }

    public String sayThankYou(String name) {
      return "Thank You " + name;
    }
  }

}
