package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day6Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day6::new, "2023/D6_small.txt");
    assertEquals("288", results.a);
    assertEquals("71503", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day6::new, "2023/D6_full.txt");
    assertEquals("2374848", results.a);
    assertEquals("39132886", results.b);
  }
}
