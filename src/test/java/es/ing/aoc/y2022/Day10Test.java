package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day10Test {

    private static final String PART1_IMG = "\n" +
            "##..##..##..##..##..##..##..##..##..##..\n" +
            "###...###...###...###...###...###...###.\n" +
            "####....####....####....####....####....\n" +
            "#####.....#####.....#####.....#####.....\n" +
            "######......######......######......####\n" +
            "#######.......#######.......#######.....\n";

    private static final String PART2_IMG = "\n" +
            "###..###..###...##..###...##...##..####.\n" +
            "#..#.#..#.#..#.#..#.#..#.#..#.#..#.#....\n" +
            "#..#.###..#..#.#..#.#..#.#..#.#....###..\n" +
            "###..#..#.###..####.###..####.#.##.#....\n" +
            "#.#..#..#.#....#..#.#.#..#..#.#..#.#....\n" +
            "#..#.###..#....#..#.#..#.#..#..###.#....\n";

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day10::new, "2022/D10_small.txt");
        assertEquals("13140", results.a);
        assertEquals(PART1_IMG, results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day10::new, "2022/D10_full.txt");
        assertEquals("12740", results.a);
        assertEquals(PART2_IMG, results.b);
    }
}