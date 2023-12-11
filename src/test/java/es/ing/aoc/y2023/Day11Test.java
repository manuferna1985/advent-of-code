package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day11::new, "2023/D11_small.txt");
    assertEquals("374", results.a);
    assertEquals("82000210", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day11::new, "2023/D11_full.txt");
    assertEquals("9795148", results.a);
    assertEquals("650672493820", results.b);
  }
}
