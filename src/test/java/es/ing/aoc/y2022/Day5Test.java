package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day5Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day5::new, "2022/D5_small.txt");
        assertEquals("CMZ", results.a);
        assertEquals("MCD", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day5::new, "2022/D5_full.txt");
        assertEquals("QNHWJVJZW", results.a);
        assertEquals("BPCZJLFJW", results.b);
    }
}