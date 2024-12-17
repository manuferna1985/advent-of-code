package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day17 extends Day {

  private static final Integer A = 4;
  private static final Integer B = 5;
  private static final Integer C = 6;

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());
    long[] memory = readMemory(lines);

    List<Integer> program = Arrays.stream(lines[4].substring(9).split(","))
        .map(Integer::parseInt)
        .toList();

    return executeCode(program, memory, memory[A]).stream().map(String::valueOf).collect(Collectors.joining(","));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());
    long[] memory = readMemory(lines);

    List<Integer> program = Arrays.stream(lines[4].substring(9).split(","))
        .map(Integer::parseInt)
        .toList();

    return String.valueOf(findMatching(program, memory, 0, program.size() - 1, 0));
  }

  private long findMatching(List<Integer> program, long[] memory, long a, int matchingGroup, int tab) {

    if (matchingGroup < 0) {
      return a;
    }

    for (int i = 0; i < 8; i++) {
      List<Integer> results = executeCode(program, memory, a);
      System.out.printf("%s[%15d] - %s%n", StringUtils.leftPad("", tab * 2, "."), a, results.stream().map(String::valueOf).collect(Collectors.joining(",")));

      if (results.size() < matchingGroup || !results.get(matchingGroup).equals(program.get(matchingGroup))) {
        a += (long) Math.pow(2, matchingGroup * 3.0);
      } else {
        long match = findMatching(program, memory, a, matchingGroup - 1, tab + 1);
        if (match == -1) {
          a += (long) Math.pow(2, matchingGroup * 3.0);
        } else {
          return match;
        }
      }
    }
    return -1;
  }

  private long[] readMemory(String[] lines) {
    return new long[]{0L, 1L, 2L, 3L,
        Long.parseLong(lines[0].substring(12)),
        Long.parseLong(lines[1].substring(12)),
        Long.parseLong(lines[2].substring(12))};
  }

  private List<Integer> executeCode(List<Integer> program, long[] originalMemory, long aValue) {

    long[] memory = Arrays.copyOf(originalMemory, originalMemory.length);
    memory[A] = aValue;

    List<Integer> out = new ArrayList<>();

    for (int i = 0; i < program.size(); ) {
      Integer opCode = program.get(i);
      Integer literalOp = program.get(i + 1);
      long comboOp = memory[literalOp];

      boolean jump = false;
      switch (opCode) {
        // 0: adv (division, result to A)
        case 0 -> memory[A] = (long) (memory[A] / Math.pow(2, comboOp));
        // 1: bxl (bitwise XOR)
        case 1 -> memory[B] = memory[B] ^ literalOp;
        // 2: bst (modulo 8)
        case 2 -> memory[B] = comboOp % 8;
        // 3: jnz (nothing if A=0)
        case 3 -> {
          if (memory[A]!=0L) {
            i = literalOp;
            jump = true;
          }
        }
        // 4: bxc (bitwise XOR)
        case 4 -> memory[B] = memory[B] ^ memory[C];
        // 5: out (print CSV)
        case 5 -> out.add(((int) (comboOp % 8)));
        // 6: bdv (division, result to B)
        case 6 -> memory[B] = (long) (memory[A] / Math.pow(2, comboOp));
        // 7: cdv (division, result to C)
        case 7 -> memory[C] = (long) (memory[A] / Math.pow(2, comboOp));
        default -> throw new RuntimeException("Invalid opCode!");
      }

      if (!jump) {
        i += 2;
      }
    }
    return out;
  }

  public static void main(String[] args) {
    Day.run(Day17::new, "2024/D17_small.txt", "2024/D17_full.txt");
  }
}
