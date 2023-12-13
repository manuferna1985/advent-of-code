package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day13::new, "2023/D13_small.txt");
    assertEquals("405", results.a);
    assertEquals("400", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day13::new, "2023/D13_full.txt");
    assertEquals("36448", results.a);
    assertEquals("35799", results.b);
  }
}
