package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day4Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day4::new, "2015/D4_small.txt");
    assertEquals("609043", results.a);
    assertEquals("6742839", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day4::new, "2015/D4_full.txt");
    assertEquals("282749", results.a);
    assertEquals("9962624", results.b);
  }
}
