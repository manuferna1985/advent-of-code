package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day7::new, "2015/D7_small.txt");
    assertEquals("65510", results.a);
    assertEquals("49158", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day7::new, "2015/D7_full.txt");
    assertEquals("3176", results.a);
    assertEquals("14710", results.b);
  }
}
