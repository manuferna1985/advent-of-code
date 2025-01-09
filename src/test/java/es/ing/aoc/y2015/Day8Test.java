package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day8Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day8::new, "2015/D8_small.txt");
    assertEquals("12", results.a);
    assertEquals("19", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day8::new, "2015/D8_full.txt");
    assertEquals("1333", results.a);
    assertEquals("2046", results.b);
  }
}
