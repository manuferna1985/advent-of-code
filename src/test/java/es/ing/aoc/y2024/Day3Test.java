package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day3::new, "2024/D3_small.txt");
    assertEquals("161", results.a);
    assertEquals("48", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day3::new, "2024/D3_full.txt");
    assertEquals("167090022", results.a);
    assertEquals("89823704", results.b);
  }
}
