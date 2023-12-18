package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day18Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day18::new, "2023/D18_small.txt");
    assertEquals("62", results.a);
    assertEquals("952408144115", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day18::new, "2023/D18_full.txt");
    assertEquals("56678", results.a);
    assertEquals("79088855654037", results.b);
  }
}
