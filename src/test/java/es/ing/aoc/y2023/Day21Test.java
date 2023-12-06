package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day21Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day21::new, "2023/D21_small.txt");
    assertEquals("", results.a);
    assertEquals("", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day21::new, "2023/D21_full.txt");
    assertEquals("", results.a);
    assertEquals("", results.b);
  }
}