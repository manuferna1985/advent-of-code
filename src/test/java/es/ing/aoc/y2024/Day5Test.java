package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day5Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day5::new, "2024/D5_small.txt");
    assertEquals("143", results.a);
    assertEquals("123", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day5::new, "2024/D5_full.txt");
    assertEquals("4578", results.a);
    assertEquals("6179", results.b);
  }
}
