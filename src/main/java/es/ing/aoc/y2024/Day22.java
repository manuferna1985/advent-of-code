package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Day22 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    List<AtomicLong> numbers = Arrays.stream(
            fileContents.split(System.lineSeparator()))
        .map(Long::parseLong)
        .map(AtomicLong::new)
        .toList();

    for (AtomicLong n : numbers) {
      for (int i = 0; i < 2000; i++) {
        //System.out.println(n.get());
        evolveNumber(n);
      }
    }


    return String.valueOf(numbers.stream().mapToLong(ai -> ai.get()).sum());
  }

  private void evolveNumber(AtomicLong n) {



    mix(n, n.get() * 64L);
    prune(n);

    mix(n, (long)Math.floor(n.get() / 32.0));
    prune(n);

    mix(n, n.get() * 2048);
    prune(n);


  }

  private void mix(AtomicLong number, long value){
    number.set(number.get() ^ value);
  }

  private void prune(AtomicLong number){
    number.set(number.get() % 16777216);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  public static void main(String[] args) {
    Day.run(Day22::new, "2024/D22_small.txt", "2024/D22_full.txt");
  }
}
