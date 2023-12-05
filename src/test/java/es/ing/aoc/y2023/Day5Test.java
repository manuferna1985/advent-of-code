package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day5Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day5::new, "2023/D5_small.txt");
    assertEquals("35", results.a);
    assertEquals("46", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day5::new, "2023/D5_full.txt");
    assertEquals("278755257", results.a);
    assertEquals("26829166", results.b);
  }
}
