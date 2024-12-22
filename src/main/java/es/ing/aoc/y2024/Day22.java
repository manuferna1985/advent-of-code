package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Day22 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    List<AtomicLong> numbers = Arrays.stream(fileContents.split(System.lineSeparator())).map(Long::parseLong)
        .map(AtomicLong::new)
        .toList();
    evolvePrices(numbers, false);
    return String.valueOf(numbers.stream().mapToLong(AtomicLong::get).sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    List<AtomicLong> numbers = Arrays.stream(fileContents.split(System.lineSeparator())).map(Long::parseLong)
        .map(AtomicLong::new)
        .toList();

    // We have to get the sequence from the 'bestSequences' map with the better overall aggregated price
    return String.valueOf(evolvePrices(numbers, true).values().stream().mapToInt(Integer::intValue).max().orElse(0));
  }

  private  Map<List<Integer>, Integer> evolvePrices(List<AtomicLong> numbers, boolean calculateIncrementals){

    Map<List<Integer>, Integer> bestSequences = new HashMap<>();

    int price;
    int previousPrice;
    for (AtomicLong n : numbers) {
      price = getPrice(n);

      Deque<Integer> queue = new ArrayDeque<>();
      Map<List<Integer>, Integer> priceSequences = new HashMap<>();
      for (int i = 0; i < 2000; i++) {
        evolveNumber(n);
        if (calculateIncrementals) {
          previousPrice = price;
          price = getPrice(n);
          queue.add(price - previousPrice);

          if (queue.size() > 4) {
            queue.removeFirst();
          }

          if (queue.size() >= 4) {
            List<Integer> key = queue.stream().toList();
            if (!priceSequences.containsKey(key)) {
              priceSequences.put(key, price);
            }
          }
        }
      }

      if (calculateIncrementals) {
        for (var entry : priceSequences.entrySet()) {
          if (bestSequences.containsKey(entry.getKey())) {
            bestSequences.put(entry.getKey(), bestSequences.get(entry.getKey()) + entry.getValue());
          } else {
            bestSequences.put(entry.getKey(), entry.getValue());
          }
        }
      }
    }
    return bestSequences;
  }

  private int getPrice(AtomicLong n) {
    return (int)(n.get() % 10);
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

  public static void main(String[] args) {
    Day.run(Day22::new, "2024/D22_small.txt", "2024/D22_full.txt");
  }
}
