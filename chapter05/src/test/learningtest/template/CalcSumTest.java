package learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CalcSumTest {
  Calculator calculator;
  String numFilepath;

  @BeforeEach
  public void setUp() {
    this.calculator = new Calculator();
    this.numFilepath = getClass().getClassLoader().getResource("numbers.txt").getPath();
  }

  @Test
  public void sumOfNumbers() throws IOException {
    assertThat(calculator.calcSum(this.numFilepath), is(10));
  }

  @Test
  public void multiplyOfNumbers() throws IOException {
    assertThat(calculator.calcMultiply(this.numFilepath), is(24));
  }

  @Test
  public void concatenateStrings() throws IOException {
    assertThat(calculator.concatenate(this.numFilepath), is("1234"));
  }
}
