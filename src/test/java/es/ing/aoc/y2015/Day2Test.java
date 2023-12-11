package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day2Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day2::new, "2015/D2_small.txt");
    assertEquals("101", results.a);
    assertEquals("48", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day2::new, "2015/D2_full.txt");
    assertEquals("1586300", results.a);
    assertEquals("3737498", results.b);
  }
}
