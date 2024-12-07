package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day7::new, "2024/D7_small.txt");
    assertEquals("3749", results.a);
    assertEquals("11387", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day7::new, "2024/D7_full.txt");
    assertEquals("3598800864292", results.a);
    assertEquals("340362529351427", results.b);
  }
}
