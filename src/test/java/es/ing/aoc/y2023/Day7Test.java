package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day7::new, "2023/D7_small.txt");
    assertEquals("6440", results.a);
    assertEquals("5905", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day7::new, "2023/D7_full.txt");
    assertEquals("250957639", results.a);
    assertEquals("251515496", results.b);
  }
}
