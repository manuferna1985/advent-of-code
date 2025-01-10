package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day10::new, "2015/D10_small.txt");
    assertEquals("82350", results.a);
    assertEquals("1166642", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day10::new, "2015/D10_full.txt");
    assertEquals("360154", results.a);
    assertEquals("5103798", results.b);
  }
}
