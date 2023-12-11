package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;

public class Day0 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());
    return String.valueOf("");
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());
    return String.valueOf("");
  }

  public static void main(String[] args) {
    Day.run(Day0::new, "2015/D0_small.txt", "2015/D0_full.txt");
  }
}
