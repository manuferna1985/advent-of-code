package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Day17 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    AtomicLong a = new AtomicLong(Long.parseLong(lines[0].substring(12)));
    AtomicLong b = new AtomicLong(Long.parseLong(lines[1].substring(12)));
    AtomicLong c = new AtomicLong(Long.parseLong(lines[2].substring(12)));
    AtomicLong[] memory = new AtomicLong[]{
        new AtomicLong(0L),
        new AtomicLong(1L),
        new AtomicLong(2L),
        new AtomicLong(3L),
        a, b, c};

    List<Integer> program = Arrays.stream(lines[4].substring(9).split(","))
        .map(Integer::parseInt)
        .toList();

    List<Long> out = new ArrayList<>();
    for (int i = 0; i < program.size(); ) {
      Integer opCode = program.get(i);
      Integer comboOp = program.get(i + 1);

      boolean jump = false;
      switch (opCode) {
        // 0: adv (division, result to A)
        case 0 -> a.set((long) (a.get() / Math.pow(2, memory[comboOp].get())));
        // 1: bxl (bitwise XOR)
        case 1 -> b.set(bitwiseXor(b.get(), comboOp));
        // 2: bst (modulo 8)
        case 2 -> b.set(memory[comboOp].get() % 8);
        // 3: jnz (nothing if 0)
        case 3 -> {
          if (a.get()!=0) {
            i = comboOp;
            jump = true;
          }
        }
        // 4: bxc (bitwise XOR)
        case 4 -> b.set(bitwiseXor(b.get(), c.get()));
        // 5: out (print CSV)
        case 5 -> out.add(memory[comboOp].get() % 8);
        // 6: bdv (division, result to B)
        case 6 -> b.set((long) (a.get() / Math.pow(2, memory[comboOp].get())));
        // 7: cdv (division, result to C)
        case 7 -> c.set((long) (a.get() / Math.pow(2, memory[comboOp].get())));
        default -> throw new RuntimeException("Invalid opCode!");
      }

      if (!jump) {
        i += 2;
      }
    }
    return out.stream().map(String::valueOf).collect(Collectors.joining(","));
  }

  private long bitwiseXor(long a, long b) {
    return a ^ b;
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  public static void main(String[] args) {
    Day.run(Day17::new, "2024/D17_small.txt", "2024/D17_full.txt");
    //Day.run(Day17::new, "2024/D17_full.txt");
  }
}
