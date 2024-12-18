package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day4Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day4::new, "2024/D4_small.txt");
    assertEquals("18", results.a);
    assertEquals("9", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day4::new, "2024/D4_full.txt");
    assertEquals("2434", results.a);
    assertEquals("1835", results.b);
  }
}
