package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day12::new, "2023/D12_small.txt");
    assertEquals("21", results.a);
    assertEquals("525152", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day12::new, "2023/D12_full.txt");
    assertEquals("7195", results.a);
    assertEquals("33992866292225", results.b);
  }
}
