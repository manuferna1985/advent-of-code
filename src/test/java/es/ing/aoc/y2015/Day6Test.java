package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day6Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day6::new, "2015/D6_small.txt");
    assertEquals("442083", results.a);
    assertEquals("660236", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day6::new, "2015/D6_full.txt");
    assertEquals("569999", results.a);
    assertEquals("17836115", results.b);
  }
}
