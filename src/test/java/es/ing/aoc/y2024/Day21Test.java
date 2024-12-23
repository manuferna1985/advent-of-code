package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day21Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day21::new, "2024/D21_small.txt");
    assertEquals("126384", results.a);
    assertEquals("154115708116294", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day21::new, "2024/D21_full.txt");
    assertEquals("203814", results.a);
    assertEquals("248566068436630", results.b);
  }
}
