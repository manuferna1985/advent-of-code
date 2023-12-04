package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day3::new, "2023/D3_small.txt");
    assertEquals("4361", results.a);
    assertEquals("467835", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day3::new, "2023/D3_full.txt");
    assertEquals("521601", results.a);
    assertEquals("80694070", results.b);
  }
}
