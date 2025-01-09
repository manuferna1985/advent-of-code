package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;

import java.util.Arrays;

public class Day8 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(Arrays.stream(fileContents.split(System.lineSeparator()))
        .mapToInt(str -> str.length() - getMemoryStr(str).length()).sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(Arrays.stream(fileContents.split(System.lineSeparator()))
        .mapToInt(str -> getRealStr(str).length() - str.length()).sum());
  }

  private String getMemoryStr(String str) {
    return str.replace("\\\\", "$")
        .replace("\\\"", "#")
        .replaceAll("\\\\x\\w{2}", "%")
        .replace("\"", "");
  }

  private String getRealStr(String str) {
    return str.replace("\\\\", "$$$$")
        .replace("\\\"", "####")
        .replaceAll("\\\\x\\w{2}", "%%%%%")
        .replace("\"", "^^^");
  }

  public static void main(String[] args) {
    Day.run(Day8::new, "2015/D8_small.txt", "2015/D8_full.txt");
  }
}
