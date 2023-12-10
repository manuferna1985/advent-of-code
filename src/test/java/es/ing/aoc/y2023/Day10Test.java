package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {

  @Test
  void testSmall1Problem() {
    Pair<String, String> results = Day.run(Day10::new, "2023/D10_small1.txt");
    assertEquals("4", results.a);
    assertEquals("1", results.b);
  }

  @Test
  void testSmall2Problem() {
    Pair<String, String> results = Day.run(Day10::new, "2023/D10_small2.txt");
    assertEquals("8", results.a);
    assertEquals("1", results.b);
  }

  @Test
  void testSmall3Problem() {
    Pair<String, String> results = Day.run(Day10::new, "2023/D10_small3.txt");
    assertEquals("23", results.a);
    assertEquals("4", results.b);
  }

  @Test
  void testSmall4Problem() {
    Pair<String, String> results = Day.run(Day10::new, "2023/D10_small4.txt");
    assertEquals("22", results.a);
    assertEquals("4", results.b);
  }

  @Test
  void testSmall5Problem() {
    Pair<String, String> results = Day.run(Day10::new, "2023/D10_small5.txt");
    assertEquals("70", results.a);
    assertEquals("8", results.b);
  }

  @Test
  void testSmall6Problem() {
    Pair<String, String> results = Day.run(Day10::new, "2023/D10_small6.txt");
    assertEquals("80", results.a);
    assertEquals("10", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day10::new, "2023/D10_full.txt");
    assertEquals("6838", results.a);
    assertEquals("451", results.b);
  }
}
