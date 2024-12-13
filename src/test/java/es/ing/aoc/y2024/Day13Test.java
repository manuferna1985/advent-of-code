package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day13::new, "2024/D13_small.txt");
    assertEquals("480", results.a);
    assertEquals("875318608908", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day13::new, "2024/D13_full.txt");
    assertEquals("36870", results.a);
    assertEquals("78101482023732", results.b);
  }
}
