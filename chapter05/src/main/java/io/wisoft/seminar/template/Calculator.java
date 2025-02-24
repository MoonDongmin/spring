package io.wisoft.seminar.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
  public Integer calcSum(String filepath) throws IOException {
    LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
      @Override
      public Integer doSomethingWithLine(final String line, final Integer value) {
        return value + Integer.valueOf(line);
      }
    };
    return linReadTemplate(filepath, sumCallback, 0);
  }

  public Integer calcMultiply(String filepath) throws IOException {
    LineCallback<Integer> multiplyCallback =
            new LineCallback<Integer>() {
              @Override
              public Integer doSomethingWithLine(final String line, final Integer value) {
                return value * Integer.valueOf(line);
              }
            };
    return linReadTemplate(filepath, multiplyCallback, 1);
  }

  public String concatenate(String filepath) throws IOException {
    LineCallback<String> concatenateCallback =
            new LineCallback<String>() {
              @Override
              public String doSomethingWithLine(final String line, final String value) {
                return value + line;
              }
            };
    return linReadTemplate(filepath, concatenateCallback, "");
  }

  public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(filepath));
      int ret = callback.doSomethingWithReader(br);

      return ret;
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw e;
    } finally {
      if (br != null) {
        try {
          br.close();
          ;
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  public <T> T linReadTemplate(String filepath, LineCallback<T> callback, T initval) throws IOException {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(filepath));
      T res = initval;
      String line = null;
      while ((line = br.readLine()) != null) {
        res = callback.doSomethingWithLine(line, res);
      }
      return res;
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw e;
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }
}
