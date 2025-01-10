package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;

import java.util.HashSet;
import java.util.Set;

public class Day11 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    return changePassword(
        fileContents.split(System.lineSeparator())[0]);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return changePassword(
        changePassword(
            fileContents.split(System.lineSeparator())[0]));
  }

  private String changePassword(String pwd) {
    do {
      pwd = nextPassword(pwd);
    } while (!isValidPassword(pwd));
    return pwd;
  }

  private String nextPassword(String pwd) {
    boolean inc = false;
    char[] pwdArray = pwd.toCharArray();
    int index = pwd.length() - 1;
    while (!inc) {
      char nextC = next(pwdArray[index]);
      pwdArray[index] = nextC;
      if (nextC!='a') {
        inc = true;
      } else {
        index--;
      }
    }
    return new String(pwdArray);
  }

  private char next(char c) {
    int nextC = c + 1;
    if (nextC==123) {
      nextC = 97;
    }
    return (char) nextC;
  }

  private boolean isValidPassword(String pwd) {
    return firstRule(pwd) && secondRule(pwd) && thirdRule(pwd);
  }

  private boolean firstRule(String pwd) {
    int i = 0;
    boolean found = false;
    while (i < pwd.length() - 2 && !found) {
      if (pwd.charAt(i) + 1==pwd.charAt(i + 1) && pwd.charAt(i + 1) + 1==pwd.charAt(i + 2)) {
        found = true;
      }
      i++;
    }
    return found;
  }

  private boolean secondRule(String pwd) {
    return !pwd.contains("i") && !pwd.contains("o") && !pwd.contains("l");
  }

  private boolean thirdRule(String pwd) {
    Set<Character> reps = new HashSet<>();
    int i = 0;
    while (i < pwd.length() - 1) {
      if (pwd.charAt(i)==pwd.charAt(i + 1)) {
        reps.add(pwd.charAt(i));
        i++;
      }
      i++;
    }
    return reps.size() >= 2;
  }

  public static void main(String[] args) {
    Day.run(Day11::new, "2015/D11_small.txt", "2015/D11_full.txt");
  }
}
