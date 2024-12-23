package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day23Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day23::new, "2024/D23_small.txt");
    assertEquals("7", results.a);
    assertEquals("co,de,ka,ta", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day23::new, "2024/D23_full.txt");
    assertEquals("1366", results.a);
    assertEquals("bs,cf,cn,gb,gk,jf,mp,qk,qo,st,ti,uc,xw", results.b);
  }
}
