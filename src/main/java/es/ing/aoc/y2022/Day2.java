package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.List;

public class Day2 extends Day {

    private static final String CTE_ROCK = "A";
    private static final String CTE_PAPER = "B";
    private static final String CTE_SCISSORS = "C";

    private static final String CTE_LOSE = "X";
    private static final String CTE_DRAW = "Y";
    private static final String CTE_WIN = "Z";

    private static final Integer CTE_WINNING_POINTS = 6;
    private static final Integer CTE_LOSING_POINTS = 3;
    private static final Integer CTE_DRAW_POINTS = 0;


    enum Play {
        ROCK(1, List.of(CTE_ROCK, CTE_LOSE),
                () -> Play.of(CTE_SCISSORS), () -> Play.of(CTE_PAPER)),
        PAPER(2, List.of(CTE_PAPER, CTE_DRAW),
                () -> Play.of(CTE_ROCK), () -> Play.of(CTE_SCISSORS)),
        SCISSORS(3, List.of(CTE_SCISSORS, CTE_WIN),
                () -> Play.of(CTE_PAPER), () -> Play.of(CTE_ROCK));

        private final int basePoints;
        private final List<String> options;
        private final OpponentHolder winWith;
        private final OpponentHolder loseWith;

        Play(int basePoints, List<String> options, OpponentHolder winWith, OpponentHolder loseWith) {
            this.basePoints = basePoints;
            this.options = options;
            this.winWith = winWith;
            this.loseWith = loseWith;
        }

        public int getBasePoints() {
            return basePoints;
        }

        public Play getWinWith() {
            return this.winWith.getOpponent();
        }

        public Play getLoseWith() {
            return this.loseWith.getOpponent();
        }

        public Integer matchResultAgainst(Play other) {
            if (this.getWinWith().equals(other)) {
                return CTE_WINNING_POINTS;
            } else if (this.getLoseWith().equals(other)) {
                return CTE_DRAW_POINTS;
            } else {
                return CTE_LOSING_POINTS;
            }
        }

        public Play getForResult(String resultExpected) {
            if (CTE_DRAW.equals(resultExpected)) {
                return this;
            } else if (CTE_WIN.equals(resultExpected)) {
                return this.getLoseWith();
            } else {
                return this.getWinWith();
            }
        }

        private interface OpponentHolder {
            Play getOpponent();
        }

        public static Play of(String letter) {
            for (Play p : Play.values()) {
                if (p.options.contains(letter)) {
                    return p;
                }
            }
            throw new RuntimeException("Letter not found!!!");
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        String[] match = fileContents.split(System.lineSeparator()); // when input file is multiline

        int total = 0;
        for (String line : match) {
            String[] plays = line.split(" ");

            Play first = Play.of(plays[0]);
            Play second = Play.of(plays[1]);

            total += second.matchResultAgainst(first);
            total += second.getBasePoints();
        }
        return String.valueOf(total);
    }

    @Override
    protected String part2(String fileContents) throws Exception {

        String[] match = fileContents.split(System.lineSeparator()); // when input file is multiline

        int total = 0;
        for (String line : match) {
            String[] plays = line.split(" ");

            Play first = Play.of(plays[0]);
            Play second = first.getForResult(plays[1]);

            total += second.matchResultAgainst(first);
            total += second.getBasePoints();
        }
        return String.valueOf(total);
    }

    public static void main(String[] args) {
        Day.run(Day2::new, "2022/D2_small.txt", "2022/D2_full.txt");
    }
}
