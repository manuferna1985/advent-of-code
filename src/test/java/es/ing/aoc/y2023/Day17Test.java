package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day17::new, "2023/D17_small.txt");
    assertEquals("102", results.a);
    assertEquals("94", results.b);
  }

  @Test
  void testFullProblem() {
//    Pair<String, String> results = Day.run(Day17::new, "2023/D17_full.txt");
//    assertEquals("668", results.a);
//    assertEquals("788", results.b);
  }
}
