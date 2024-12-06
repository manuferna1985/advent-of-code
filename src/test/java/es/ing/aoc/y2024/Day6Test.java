package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day6Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day6::new, "2024/D6_small.txt");
    assertEquals("41", results.a);
    assertEquals("6", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day6::new, "2024/D6_full.txt");
    assertEquals("5153", results.a);
    assertEquals("1711", results.b);
  }
}
