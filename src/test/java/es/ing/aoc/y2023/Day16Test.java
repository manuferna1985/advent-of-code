package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day16::new, "2023/D16_small.txt");
    assertEquals("46", results.a);
    assertEquals("51", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day16::new, "2023/D16_full.txt");
    assertEquals("6902", results.a);
    assertEquals("7697", results.b);
  }
}
