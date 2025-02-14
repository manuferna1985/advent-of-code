package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day12::new, "2024/D12_small.txt");
    assertEquals("1184", results.a);
    assertEquals("368", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day12::new, "2024/D12_full.txt");
    assertEquals("1483212", results.a);
    assertEquals("897062", results.b);
  }
}
