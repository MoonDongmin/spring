package pointcut;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.context.annotation.Bean;

import java.lang.annotation.Target;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class PointcutExpressionTest {
  @Test
  public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("execution(public int " +
            "io.wisoft.vo1.pointcut.Target.minus(int, int)" +
            "throws java.lang.RuntimeException)");

    assertThat(pointcut.getClassFilter().matches(Target.class) &&
            pointcut.getMethodMatcher().matches(
                    Target.class.getMethod("minus", int.class, int.class), null
            )).isEqualTo(true);

    assertThat(pointcut.getClassFilter().matches(Target.class) &&
            pointcut.getMethodMatcher().matches(
                    Target.class.getMethod("plus", int.class, int.class), null
            )).isEqualTo(false);

    assertThat(pointcut.getClassFilter().matches(Bean.class) &&
            pointcut.getMethodMatcher().matches(
                    Target.class.getMethod("method", int.class, int.class), null
            )).isEqualTo(false);
  }

  @Test
  public void pointcut() throws Exception {
    targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);
  }

  public void pointcutMatches(String expression, Boolean expected, Class<?> clazz,
                              String methodName, Class<?>... args) throws Exception {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(expression);

    assertThat(pointcut.getClassFilter().matches(clazz)
            && pointcut.getMethodMatcher().matches(clazz.getMethod(methodName,
            args), null)).isEqualTo(expected);
  }

  public void targetClassPointcutMatches(String expression, boolean... expected) throws Exception {
    pointcutMatches(expression, expected[0], Target.class, "hello");
    pointcutMatches(expression, expected[1], Target.class, "hello", String.class.);
    pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
    pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
    pointcutMatches(expression, expected[4], Target.class, "method");
    pointcutMatches(expression, expected[5], Bean.class, "meethod");
  }
}
