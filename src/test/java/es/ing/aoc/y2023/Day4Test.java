package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day4Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day4::new, "2023/D4_small.txt");
    assertEquals("13", results.a);
    assertEquals("30", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day4::new, "2023/D4_full.txt");
    assertEquals("15268", results.a);
    assertEquals("6283755", results.b);
  }
}
