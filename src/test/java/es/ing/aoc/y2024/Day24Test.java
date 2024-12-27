package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day24Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day24::new, "2024/D24_small.txt");
    assertEquals("2024", results.a);
    assertEquals("ffh,mjb,tgd,wpb,z02,z03,z05,z06,z07,z08,z10,z11", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day24::new, "2024/D24_full.txt");
    assertEquals("55544677167336", results.a);
    assertEquals("gsd,kth,qnf,tbt,vpm,z12,z26,z32", results.b);
  }
}
