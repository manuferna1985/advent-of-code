package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day12::new, "2015/D12_small.txt");
    assertEquals("6", results.a);
    assertEquals("4", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day12::new, "2015/D12_full.txt");
    assertEquals("119433", results.a);
    assertEquals("68466", results.b);
  }
}
