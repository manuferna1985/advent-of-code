package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7 extends Day {

  record Operation(Long result, List<Long> factors) {
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(readOperations(fileContents).parallelStream()
        .filter(op -> this.hasAnyValidResult(op, 2))
        .mapToLong(op -> op.result)
        .sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(readOperations(fileContents).parallelStream()
        .filter(op -> this.hasAnyValidResult(op, 3))
        .mapToLong(op -> op.result)
        .sum());
  }

  private List<Operation> readOperations(String fileContents) {
    String[] lines = fileContents.split(System.lineSeparator());
    List<Operation> ops = new ArrayList<>();
    for (String line : lines) {
      String[] parts = line.split(":");
      ops.add(new Operation(
          Long.parseLong(parts[0]),
          Arrays.stream(parts[1].trim().split(" ")).map(Long::parseLong).toList()));
    }
    return ops;
  }

  private boolean hasAnyValidResult(Operation op, int base) {
    int n = op.factors.size() - 1;
    for (int i = 0; i < Math.pow(base, n); i++) {
      String sequence = StringUtils.leftPad(Integer.toString(i, base), n, "0");
      Long currentResult = op.factors.get(0);
      for (int j = 0; j < sequence.length(); j++) {
        switch (sequence.charAt(j)) {
          case '0' -> currentResult += op.factors.get(j + 1);
          case '1' -> currentResult *= op.factors.get(j + 1);
          default -> currentResult = Long.parseLong(currentResult + String.valueOf(op.factors.get(j + 1)));
        }
      }
      if (currentResult.equals(op.result)) {
        return true;
      }
    }
    return false;
  }

  public static void main(String[] args) {
    Day.run(Day7::new, "2024/D7_small.txt", "2024/D7_full.txt");
  }
}
