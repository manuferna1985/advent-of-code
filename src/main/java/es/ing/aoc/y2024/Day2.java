package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());
    int counter = 0;
    for (String line : lines) {
      int[] numbers = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
      counter += checkReportRules(numbers) ? 1:0;
    }
    return String.valueOf(counter);
  }

  private boolean checkReportRules(int[] numbers) {
    boolean increasing = numbers[0] < numbers[1];
    boolean isValid = true;

    for (int i = 0; i < numbers.length - 1 && isValid; i++) {
      if (increasing && numbers[i] < numbers[i + 1]) {
        isValid = between(numbers[i + 1] - numbers[i], 1, 3);
      } else if (!increasing && numbers[i] > numbers[i + 1]) {
        isValid = between(numbers[i] - numbers[i + 1], 1, 3);
      } else {
        isValid = false;
      }
    }
    return isValid;
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());
    int counter = 0;
    for (String line : lines) {
      int[] numbers = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();

      if (checkReportRules(numbers)) {
        counter++;
      } else {
        if (generateCombinations(numbers).stream().anyMatch(this::checkReportRules)){
          counter++;
        }
      }
    }
    return String.valueOf(counter);
  }

  private boolean between(int n, int lower, int upper) {
    return n >= lower && n <= upper;
  }

  private static List<int[]> generateCombinations(int[] numbers) {
    List<int[]> combs = new ArrayList<>();

    for (int i = 0; i < numbers.length; i++) {
      int[] newComb = new int[numbers.length - 1];
      for (int j = 0, k = 0; j < numbers.length; j++) {
        if (i!=j) {
          newComb[k++] = numbers[j];
        }
      }
      combs.add(newComb);
    }
    return combs;
  }

  public static void main(String[] args) {
    Day.run(Day1::new, "2024/D2_small.txt", "2024/D2_full.txt");
  }
}
