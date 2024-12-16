package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day16::new, "2024/D16_small.txt");
    assertEquals("7036", results.a);
    assertEquals("45", results.b);
  }

  @Test
  void testSmall2Problem() {
    Pair<String, String> results = Day.run(Day16::new, "2024/D16_small2.txt");
    assertEquals("11048", results.a);
    assertEquals("64", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day16::new, "2024/D16_full.txt");
    assertEquals("65436", results.a);
    assertEquals("489", results.b);
  }
}
