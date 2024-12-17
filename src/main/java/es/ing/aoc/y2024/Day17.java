package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;

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

    for (int i=0; i < program.size(); i++) {
      printIntervals(program, memory, i);
    }

    return "";
  }

  private void printIntervals(List<Integer> program, long[] memory, int programPos){

    long n = 0;

    boolean match = false;
    long lastMatch = -1, period = -1, groupSize = -1, groupSizeCurrent = -1, firstMatching = -1;
    while (true) {
      List<Integer> out = executeCode(program, memory, n);
      System.out.printf("%-10d - %s%n", n, out);
      if (out.size() > programPos && out.get(programPos).equals(program.get(programPos))) {

        if (firstMatching == -1){
          firstMatching = n;
        }
        if (!match) {
          if (lastMatch > 0) {
            period = n - lastMatch;
          }
          lastMatch = n;
        }
        match = true;

        //System.out.printf("%-10d - %s%n", n, out);
        groupSizeCurrent++;
      } else {
        if (groupSize == -1 && match){
          groupSize = groupSizeCurrent;
        }
        groupSizeCurrent = 0;
        match = false;
      }
      n++;

      if (n > 50000) {
        break;
      }
    }
    //System.out.println(period + " " + groupSize + " " + firstMatching);

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
      Integer comboOp = program.get(i + 1);

      boolean jump = false;
      switch (opCode) {
        // 0: adv (division, result to A)
        case 0 -> memory[A] = (long) (memory[A] / Math.pow(2, memory[comboOp]));
        // 1: bxl (bitwise XOR)
        case 1 -> memory[B] = memory[B] ^ comboOp;
        // 2: bst (modulo 8)
        case 2 -> memory[B] = memory[comboOp] % 8;
        // 3: jnz (nothing if A=0)
        case 3 -> {
          if (memory[A]!=0L) {
            i = comboOp;
            jump = true;
          }
        }
        // 4: bxc (bitwise XOR)
        case 4 -> memory[B] = memory[B] ^ memory[C];
        // 5: out (print CSV)
        case 5 -> {
          out.add(((int) memory[comboOp] % 8));

        }
        // 6: bdv (division, result to B)
        case 6 -> memory[B] = (long) (memory[A] / Math.pow(2, memory[comboOp]));
        // 7: cdv (division, result to C)
        case 7 -> memory[C] = (long) (memory[A] / Math.pow(2, memory[comboOp]));
        default -> throw new RuntimeException("Invalid opCode!");
      }

      if (!jump) {
        i += 2;
      }
    }
    return out;
  }

  public static void main(String[] args) {
    //Day.run(Day17::new, "2024/D17_small.txt", "2024/D17_full.txt");
    Day.run(Day17::new, "2024/D17_full.txt");
  }
}
