package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day15::new, "2024/D15_small.txt");
    assertEquals("10092", results.a);
    assertEquals("9021", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day15::new, "2024/D15_full.txt");
    assertEquals("1490942", results.a);
    assertEquals("1519202", results.b);
  }
}
