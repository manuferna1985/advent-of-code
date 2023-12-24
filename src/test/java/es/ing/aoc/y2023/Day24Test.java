package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day24Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day24::new, "2023/D24_small.txt");
    assertEquals("6", results.a);
    assertEquals("", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day24::new, "2023/D24_full.txt");
    assertEquals("17244", results.a);
    assertEquals("", results.b);
  }
}
