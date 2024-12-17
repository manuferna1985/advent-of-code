package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day17::new, "2024/D17_small.txt");
    assertEquals("5,7,3,0", results.a);
    assertEquals("117440", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day17::new, "2024/D17_full.txt");
    assertEquals("2,3,6,2,1,6,1,2,1", results.a);
    assertEquals("90938893795561", results.b);
  }
}
