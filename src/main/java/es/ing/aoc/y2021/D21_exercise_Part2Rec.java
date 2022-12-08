package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class D21_exercise_Part2Rec {

    private static final Integer WINNING_THRESHOLD = 21;
    private static final List<Integer> DICE_ROLLS;
    private static final int[] DIRAC_POSITIONS_PRECALC;

    static {
        DICE_ROLLS = new ArrayList<>();

        for (int i=1; i<=3; i++){
            for (int j=1; j<=3; j++){
                for (int k=1; k<=3; k++){
                    DICE_ROLLS.add(i +j + k);
                }
            }
        }

        DIRAC_POSITIONS_PRECALC = new int[20];
        for (int i=0; i < DIRAC_POSITIONS_PRECALC.length; i++){
            DIRAC_POSITIONS_PRECALC[i] = modDirac10(i);
        }
    }

    public static class Pair{
        Player one;
        Player two;

        public static Pair of(Player one, Player two) {
            Pair p = new Pair();
            p.one = one;
            p.two = two;
            return p;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return one.equals(pair.one) && two.equals(pair.two);
        }

        @Override
        public int hashCode() {
            return Objects.hash(one, two);
        }

        public String toString(){
            return String.format("{%s | %s}", one, two);
        }
    }

    public static class Player{
        Integer position;
        Integer points;

        public static Player of(Integer position, Integer points){
            Player p = new Player();
            p.position = position;
            p.points = points;
            return p;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Player player = (Player) o;
            return position.equals(player.position) && points.equals(player.points);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, points);
        }

        public String toString(){
            return String.format("[%d]=%d", position, points);
        }
    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D19_exercise.class.getResource("2021/D21_full.txt").toURI()), Charset.defaultCharset());

        long before = System.nanoTime();

        // Start coding here. ;-)
        Integer[] players = allLines.stream()
                .map(s -> Integer.parseInt(s.substring(s.indexOf(":") + 2).trim()))
                .toArray(Integer[]::new);

        long[] totalWins = calculateWinsFor(
                Pair.of(Player.of(players[0], 0), Player.of(players[1], 0)),
                new HashMap<>());

        System.out.println("P1 wins in: " + totalWins[0]);
        System.out.println("P2 wins in: " + totalWins[1]);

        long after = System.nanoTime();

        System.out.printf("Elapsed time: %.0f ms%n%n", ((double)(after-before))/1000000);
    }

    private static long[] calculateWinsFor(Pair universe, Map<Pair, long[]> coveredPaths){

        long p1Wins = 0;
        long p2Wins = 0;

        for (Player p1Option : calculatePlayerNextOptions(universe.one)){
            if (p1Option.points >= WINNING_THRESHOLD){
                p1Wins++;
            } else {
                for (Player p2Option : calculatePlayerNextOptions(universe.two)){
                    if (p2Option.points >= WINNING_THRESHOLD) {
                        p2Wins++;
                    } else {
                        Pair newSubPath = Pair.of(p1Option, p2Option);
                        long[] subPathWins;

                        if (coveredPaths.containsKey(newSubPath)){
                            subPathWins = coveredPaths.get(newSubPath);
                        } else {
                            subPathWins = calculateWinsFor(newSubPath, coveredPaths);
                            coveredPaths.put(newSubPath, subPathWins);
                        }
                        p1Wins += subPathWins[0];
                        p2Wins += subPathWins[1];
                    }
                }
            }
        }

        return new long[]{p1Wins, p2Wins};
    }

    private static List<Player> calculatePlayerNextOptions(Player player){

        List<Player> options = new ArrayList<>();
        List<Integer> diceRolls = DICE_ROLLS;

        Integer newPosition;
        for (Integer currentDice : diceRolls){
            newPosition = DIRAC_POSITIONS_PRECALC[player.position + currentDice];
            options.add(Player.of(newPosition, player.points + newPosition));
        }

        return options;
    }

    private static Integer modDirac10(Integer position) {
        return (position > 10) ? modDirac10(position - 10) : position;
    }
}
