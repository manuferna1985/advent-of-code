package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day1::new, "2015/D1_small.txt");
    assertEquals("-3", results.a);
    assertEquals("23", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day1::new, "2015/D1_full.txt");
    assertEquals("74", results.a);
    assertEquals("1795", results.b);
  }
}
