package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day19 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());
    List<String> towels = Arrays.stream(lines[0].split(", ")).toList();
    List<String> designsToCheck = IntStream.rangeClosed(2, lines.length - 1).mapToObj(i -> lines[i]).toList();
    return String.valueOf(designsToCheck.parallelStream().filter(design -> checkDesign(design, towels)).count());
  }

  private boolean checkDesign(String design, List<String> towels) {
    System.out.println("Checking "+design);
    return checkDesign(design, towels, 0);
  }

  private boolean checkDesign(String design, List<String> towels, int index) {

    if (index >= design.length()){
      return true;
    }

    for (String towel : towels){
      if (design.startsWith(towel, index)){
       boolean match = checkDesign(design, towels, index+towel.length());
       if (match){
         return true;
       }
      }
    }

    return false;
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  public static void main(String[] args) {
    Day.run(Day19::new, "2024/D19_small.txt", "2024/D19_full.txt");
  }
}
