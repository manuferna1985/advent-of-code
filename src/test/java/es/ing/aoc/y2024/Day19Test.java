package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day19Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day19::new, "2024/D19_small.txt");
    assertEquals("6", results.a);
    assertEquals("16", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day19::new, "2024/D19_full.txt");
    assertEquals("272", results.a);
    assertEquals("1041529704688380", results.b);
  }
}
