package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 extends Day {

  private static List<Pair<String, Integer>> TRANSLATIONS = List.of(
      Pair.of("one", 1),
      Pair.of("two", 2),
      Pair.of("three", 3),
      Pair.of("four", 4),
      Pair.of("five", 5),
      Pair.of("six", 6),
      Pair.of("seven", 7),
      Pair.of("eight", 8),
      Pair.of("nine", 9));


  @Override
  protected String part1(String fileContents) throws Exception {

    String[] lines = fileContents.split(System.lineSeparator());

    Pattern p = Pattern.compile("(\\d)");
    long sum = 0L;

    for (String line : lines) {

      String number = "";

      Matcher m1 = p.matcher(line);
      if (m1.find()) {
        number += m1.group();
      }

      Matcher m2 = p.matcher(StringUtils.reverse(line));
      if (m2.find()) {
        number += m2.group();
      }

      sum += Long.parseLong(number);
    }

    return String.valueOf(sum);
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    String[] lines = fileContents.split(System.lineSeparator());

    long sum = 0L;
    for (String line : lines) {
      sum += Long.parseLong(getFirstNumber(line) + getLastNumber(line));
    }

    return String.valueOf(sum);
  }

  private String getFirstNumber(String line) {

    while (true) {
      for (Pair<String, Integer> tr : TRANSLATIONS) {
        if (line.startsWith(tr.getKey()) || line.startsWith(String.valueOf(tr.getValue()))) {
          return String.valueOf(tr.getValue());
        }
      }
      line = line.substring(1);
    }
  }

  private String getLastNumber(String line) {

    while (true) {
      for (Pair<String, Integer> tr : TRANSLATIONS) {
        if (line.endsWith(tr.getKey()) || line.endsWith(String.valueOf(tr.getValue()))) {
          return String.valueOf(tr.getValue());
        }
      }
      line = line.substring(0, line.length() - 1);
    }
  }

  public static void main(String[] args) {
    Day.run(es.ing.aoc.y2023.Day1::new, "2023/D1_small.txt", "2023/D1_full.txt");
  }
}
