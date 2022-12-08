package es.ing.aoc.y2021;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.GenericTransitionSystem;
import es.ing.aoc.common.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Day23 extends Day {
    private final static boolean PRINTPATH = false;
    private final static boolean COUNTALL = false;

    // #############
    // #01234567890#  0 to 11
    // ###A#B#C#D### 10 12 14 16
    //   #A#B#C#D#   11 13 15 17
    //   #########

    private static final class State {
        final int[] places;
        private static final long[] baseCost = {0, 1, 10, 100, 1000};

        public State(int[] places) {
            this.places = Arrays.copyOf(places, places.length);
        }

        public State(State other, int from, int to) {
            this.places = Arrays.copyOf(other.places, other.places.length);
            this.places[to] = this.places[from];
            this.places[from] = 0;
        }

        /**
         * Check if the way is clear
         */
        private boolean wayClear(int from, int to) {
            if (from == to) return true;

            switch (from) {
                case 12:
                    return places[2] == 0 && wayClear(2, to);
                case 13:
                    return places[12] == 0 && places[2] == 0 && wayClear(2, to);
                case 14:
                    return places[4] == 0 && wayClear(4, to);
                case 15:
                    return places[14] == 0 && places[4] == 0 && wayClear(4, to);
                case 16:
                    return places[6] == 0 && wayClear(6, to);
                case 17:
                    return places[16] == 0 && places[6] == 0 && wayClear(6, to);
                case 18:
                    return places[8] == 0 && wayClear(8, to);
                case 19:
                    return places[18] == 0 && places[8] == 0 && wayClear(8, to);
            }

            // from is 0-11
            // to = 0-11

            if (from < to) {
                for (int i = from + 1; i <= to; i++) {
                    if (places[i] != 0) return false;
                }
                return true;
            } else {
                for (int i = from - 1; i >= to; i--) {
                    if (places[i] != 0) return false;
                }
                return true;
            }
        }

        public long dist(int from, int to) {
            switch (from) {
                case 12:
                    return 1 + dist(2, to);
                case 13:
                    return 2 + dist(2, to);
                case 14:
                    return 1 + dist(4, to);
                case 15:
                    return 2 + dist(4, to);
                case 16:
                    return 1 + dist(6, to);
                case 17:
                    return 2 + dist(6, to);
                case 18:
                    return 1 + dist(8, to);
                case 19:
                    return 2 + dist(8, to);
                default:
                    switch (to) {
                        case 12:
                            return 1 + dist(from, 2);
                        case 13:
                            return 2 + dist(from, 2);
                        case 14:
                            return 1 + dist(from, 4);
                        case 15:
                            return 2 + dist(from, 4);
                        case 16:
                            return 1 + dist(from, 6);
                        case 17:
                            return 2 + dist(from, 6);
                        case 18:
                            return 1 + dist(from, 8);
                        case 19:
                            return 2 + dist(from, 8);
                        default:
                            return Math.abs(from - to);
                    }
            }
        }

        public long cost(int token, int from, int to) {
            return baseCost[token] * dist(from, to);
        }

        public List<Pair<State, Long>> successors() {
            var res = new ArrayList<Pair<State, Long>>();

            // never move to 2/4/6/8
            // only move from 0-11 to appropriate 12/13/14/15/16/17/18/19
            // only move from 12-19 to 0-11

            for (int i = 0; i < 11; i++) {
                if (places[i] == 1) {
                    // move A
                    if (places[12] == 0) {
                        if (places[13] == 1) {
                            if (wayClear(i, 2)) {
                                res.add(Pair.of(new State(this, i, 12), cost(1, i, 12)));
                            }
                        } else if (places[13] == 0 && wayClear(i, 2)) {
                            res.add(Pair.of(new State(this, i, 13), cost(1, i, 13)));
                        }
                    }
                } else if (places[i] == 2) {
                    // move B
                    if (places[14] == 0) {
                        if (places[15] == 2) {
                            if (wayClear(i, 4)) {
                                res.add(Pair.of(new State(this, i, 14), cost(2, i, 14)));
                            }
                        } else if (places[15] == 0 && wayClear(i, 4)) {
                            res.add(Pair.of(new State(this, i, 15), cost(2, i, 15)));
                        }
                    }
                } else if (places[i] == 3) {
                    // move B
                    if (places[16] == 0) {
                        if (places[17] == 3) {
                            if (wayClear(i, 6)) {
                                res.add(Pair.of(new State(this, i, 16), cost(3, i, 16)));
                            }
                        } else if (places[17] == 0 && wayClear(i, 6)) {
                            res.add(Pair.of(new State(this, i, 17), cost(3, i, 17)));
                        }
                    }
                } else if (places[i] == 4) {
                    // move B
                    if (places[18] == 0) {
                        if (places[19] == 4) {
                            if (wayClear(i, 8)) {
                                res.add(Pair.of(new State(this, i, 18), cost(4, i, 18)));
                            }
                        } else if (places[19] == 0 && wayClear(i, 8)) {
                            res.add(Pair.of(new State(this, i, 19), cost(4, i, 19)));
                        }
                    }
                }
            }

            for (int j = 0; j < 11; j++) {
                if (j == 2) continue;
                if (j == 4) continue;
                if (j == 6) continue;
                if (j == 8) continue;
                if (places[j] != 0) continue; // unoccupied?

                if (places[12] != 0 && places[2] == 0 && wayClear(2, j)) {
                    res.add(Pair.of(new State(this, 12, j), cost(places[12], 12, j)));
                }
                if (places[13] != 0 && places[12] == 0 && places[2] == 0 && wayClear(2, j)) {
                    res.add(Pair.of(new State(this, 13, j), cost(places[13], 13, j)));
                }
                if (places[14] != 0 && places[4] == 0 && wayClear(4, j)) {
                    res.add(Pair.of(new State(this, 14, j), cost(places[14], 14, j)));
                }
                if (places[15] != 0 && places[14] == 0 && places[4] == 0 && wayClear(4, j)) {
                    res.add(Pair.of(new State(this, 15, j), cost(places[15], 15, j)));
                }
                if (places[16] != 0 && places[6] == 0 && wayClear(6, j)) {
                    res.add(Pair.of(new State(this, 16, j), cost(places[16], 16, j)));
                }
                if (places[17] != 0 && places[16] == 0 && places[6] == 0 && wayClear(6, j)) {
                    res.add(Pair.of(new State(this, 17, j), cost(places[17], 17, j)));
                }
                if (places[18] != 0 && places[8] == 0 && wayClear(8, j)) {
                    res.add(Pair.of(new State(this, 18, j), cost(places[18], 18, j)));
                }
                if (places[19] != 0 && places[18] == 0 && places[8] == 0 && wayClear(8, j)) {
                    res.add(Pair.of(new State(this, 19, j), cost(places[19], 19, j)));
                }
            }
            return res;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return Arrays.equals(places, state.places);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(places);
        }

        @Override
        public String toString() {
            return Arrays.toString(places);
        }

        public boolean isFinal() {
            return places[12] == 1 && places[13] == 1 &&
                    places[14] == 2 && places[15] == 2 &&
                    places[16] == 3 && places[17] == 3 &&
                    places[18] == 4 && places[19] == 4;
        }

        public String draw() {
            // #############
            // #01234567890#  0 to 11
            // ###A#B#C#D### 10 12 14 16
            //   #A#B#C#D#   11 13 15 17
            //   #########

            var sb = new StringBuilder();
            sb.append("#############\n");
            sb.append("#");
            for (int i = 0; i < 11; i++) sb.append(" ABCD".charAt(places[i]));
            sb.append("#\n");
            sb.append("###");
            sb.append(" ABCD".charAt(places[12]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[14]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[16]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[18]));
            sb.append("###\n  #");
            sb.append(" ABCD".charAt(places[13]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[15]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[17]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[19]));
            sb.append("###\n  #########\n");
            return sb.toString();
        }
    } // class

    public int read(char ch) {
        if (ch == 'A') return 1;
        if (ch == 'B') return 2;
        if (ch == 'C') return 3;
        if (ch == 'D') return 4;
        return 0;
    }

    @Override
    protected void part1(String fileContents) {
        var lines = fileContents.split(System.lineSeparator());

        var pos = new int[20];
        pos[12] = read(lines[2].charAt(3));
        pos[13] = read(lines[3].charAt(3));
        pos[14] = read(lines[2].charAt(5));
        pos[15] = read(lines[3].charAt(5));
        pos[16] = read(lines[2].charAt(7));
        pos[17] = read(lines[3].charAt(7));
        pos[18] = read(lines[2].charAt(9));
        pos[19] = read(lines[3].charAt(9));

        var ts = new GenericTransitionSystem<State>() {
            @Override
            public Collection<State> initial() {
                return List.of(new State(pos));
            }

            @Override
            public Collection<Pair<State, Long>> successors(State state) {
                return state.successors();
            }

            @Override
            public boolean isFinal(State state) {
                return state.isFinal();
            }
        };

        var res = ts.reachFinal();
        System.out.println("Part 1: " + res.a.get(res.b.get(res.b.size() - 1)));

        if (PRINTPATH) {
            var distance = res.a;
            for (var state : res.b) {
                System.out.printf("At distance %d:%n", distance.get(state));
                System.out.println(state.draw());
            }
        }

//        test(distance, new State(new int[]{0,0,0,2,0,0,0,0,0,0,0,0,2,1,3,4,0,3,4,1}));
//        test(distance, new State(new int[]{0,0,0,2,0,0,0,0,0,0,0,0,2,1,0,4,3,3,4,1}));
//        test(distance, new State(new int[]{0,0,0,2,0,4,0,0,0,0,0,0,2,1,0,0,3,3,4,1}));
//        test(distance, new State(new int[]{0,0,0,0,0,4,0,0,0,0,0,0,2,1,0,2,3,3,4,1}));
//        test(distance, new State(new int[]{0,0,0,0,0,4,0,0,0,0,0,0,0,1,2,2,3,3,4,1}));
//        test(distance, new State(new int[]{0,0,0,0,0,4,0,4,0,1,0,0,0,1,2,2,3,3,0,0}));
//        test(distance, new State(new int[]{0,0,0,0,0,0,0,0,0,1,0,0,0,1,2,2,3,3,4,4}));
//        test(distance, new State(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,2,3,3,4,4}));
    }

    private void test(Map<State, Long> distance, State state) {
        var d = distance.get(state);
        if (d != null) {
            System.out.printf("%s distance %d%n", state, d);
        } else {
            System.out.printf("%s unreached%n", state);
        }
    }

    private static final class State2 {
        /*
         #############
         #01234567890# 0 .. 10
         ###A#B#C#D### 12 16 20 24
           #A#B#C#D#   13 17 21 25
           #A#B#C#D#   14 18 22 26
           #A#B#C#D#   15 19 23 27
           #########
        */

        final int[] places;
        final static long[] baseCost = {0, 1, 10, 100, 1000};

        public State2(int[] places) {
            this.places = Arrays.copyOf(places, places.length);
        }

        public State2(State2 other, int from, int to) {
            this.places = Arrays.copyOf(other.places, other.places.length);
            this.places[to] = this.places[from];
            this.places[from] = 0;
        }

        /**
         * Check if the way is clear
         */
        private boolean wayClear(int from, int to) {
            if (from == to) return true;

            switch (from) {
                case 12:
                    return places[2] == 0 && wayClear(2, to);

                case 13:
                    return places[12] == 0 && wayClear(12, to);

                case 14:
                    return places[13] == 0 && wayClear(13, to);

                case 15:
                    return places[14] == 0 && wayClear(14, to);

                case 16:
                    return places[4] == 0 && wayClear(4, to);

                case 17:
                    return places[16] == 0 && wayClear(16, to);

                case 18:
                    return places[17] == 0 && wayClear(17, to);

                case 19:
                    return places[18] == 0 && wayClear(18, to);

                case 20:
                    return places[6] == 0 && wayClear(6, to);

                case 21:
                    return places[20] == 0 && wayClear(20, to);

                case 22:
                    return places[21] == 0 && wayClear(21, to);

                case 23:
                    return places[22] == 0 && wayClear(22, to);

                case 24:
                    return places[8] == 0 && wayClear(8, to);

                case 25:
                    return places[24] == 0 && wayClear(24, to);

                case 26:
                    return places[25] == 0 && wayClear(25, to);

                case 27:
                    return places[26] == 0 && wayClear(26, to);

            }

            // from is 0-11
            // to = 0-11

            if (from < to) {
                for (int i = from + 1; i <= to; i++) {
                    if (places[i] != 0) return false;
                }
                return true;
            } else {
                for (int i = from - 1; i >= to; i--) {
                    if (places[i] != 0) return false;
                }
                return true;
            }
        }

        public long cost(int token, int from, int to) {
            return baseCost[token] * dist(from, to);
        }

        public long dist(int from, int to) {
            switch (from) {
                case 12:
                    return 1 + dist(2, to);
                case 13:
                    return 2 + dist(2, to);
                case 14:
                    return 3 + dist(2, to);
                case 15:
                    return 4 + dist(2, to);
                case 16:
                    return 1 + dist(4, to);
                case 17:
                    return 2 + dist(4, to);
                case 18:
                    return 3 + dist(4, to);
                case 19:
                    return 4 + dist(4, to);
                case 20:
                    return 1 + dist(6, to);
                case 21:
                    return 2 + dist(6, to);
                case 22:
                    return 3 + dist(6, to);
                case 23:
                    return 4 + dist(6, to);
                case 24:
                    return 1 + dist(8, to);
                case 25:
                    return 2 + dist(8, to);
                case 26:
                    return 3 + dist(8, to);
                case 27:
                    return 4 + dist(8, to);
                default:
                    switch (to) {
                        case 12:
                            return 1 + dist(from, 2);
                        case 13:
                            return 2 + dist(from, 2);
                        case 14:
                            return 3 + dist(from, 2);
                        case 15:
                            return 4 + dist(from, 2);
                        case 16:
                            return 1 + dist(from, 4);
                        case 17:
                            return 2 + dist(from, 4);
                        case 18:
                            return 3 + dist(from, 4);
                        case 19:
                            return 4 + dist(from, 4);
                        case 20:
                            return 1 + dist(from, 6);
                        case 21:
                            return 2 + dist(from, 6);
                        case 22:
                            return 3 + dist(from, 6);
                        case 23:
                            return 4 + dist(from, 6);
                        case 24:
                            return 1 + dist(from, 8);
                        case 25:
                            return 2 + dist(from, 8);
                        case 26:
                            return 3 + dist(from, 8);
                        case 27:
                            return 4 + dist(from, 8);
                        default:
                            return Math.abs(from - to);
                    }
            }
        }

        public List<Pair<State2, Long>> successors() {
            var res = new ArrayList<Pair<State2, Long>>();

            // never move to 2/4/6/8
            // only move from 0-11 to appropriate 12/13/14/15/16/17/18/19
            // only move from 12-19 to 0-11

            for (int i = 0; i < 11; i++) {
                if (places[i] == 0) {
                    // continue
                } else if (places[i] == 1) {
                    // move A
                    if (places[12] == 0 && places[13] == 1 && places[14] == 1 && places[15] == 1 && wayClear(i, 2)) {
                        res.add(Pair.of(new State2(this, i, 12), cost(1, i, 12)));
                    }
                    if (places[12] == 0 && places[13] == 0 && places[14] == 1 && places[15] == 1 && wayClear(i, 2)) {
                        res.add(Pair.of(new State2(this, i, 13), cost(1, i, 13)));
                    }
                    if (places[12] == 0 && places[13] == 0 && places[14] == 0 && places[15] == 1 && wayClear(i, 2)) {
                        res.add(Pair.of(new State2(this, i, 14), cost(1, i, 14)));
                    }
                    if (places[12] == 0 && places[13] == 0 && places[14] == 0 && places[15] == 0 && wayClear(i, 2)) {
                        res.add(Pair.of(new State2(this, i, 15), cost(1, i, 15)));
                    }
                } else if (places[i] == 2) {
                    // move B
                    if (places[16] == 0 && places[17] == 2 && places[18] == 2 && places[19] == 2 && wayClear(i, 4)) {
                        res.add(Pair.of(new State2(this, i, 16), cost(2, i, 16)));
                    }
                    if (places[16] == 0 && places[17] == 0 && places[18] == 2 && places[19] == 2 && wayClear(i, 4)) {
                        res.add(Pair.of(new State2(this, i, 17), cost(2, i, 17)));
                    }
                    if (places[16] == 0 && places[17] == 0 && places[18] == 0 && places[19] == 2 && wayClear(i, 4)) {
                        res.add(Pair.of(new State2(this, i, 18), cost(2, i, 18)));
                    }
                    if (places[16] == 0 && places[17] == 0 && places[18] == 0 && places[19] == 0 && wayClear(i, 4)) {
                        res.add(Pair.of(new State2(this, i, 19), cost(2, i, 19)));
                    }
                } else if (places[i] == 3) {
                    // move C
                    if (places[20] == 0 && places[21] == 3 && places[22] == 3 && places[23] == 3 && wayClear(i, 6)) {
                        res.add(Pair.of(new State2(this, i, 20), cost(3, i, 20)));
                    }
                    if (places[20] == 0 && places[21] == 0 && places[22] == 3 && places[23] == 3 && wayClear(i, 6)) {
                        res.add(Pair.of(new State2(this, i, 21), cost(3, i, 21)));
                    }
                    if (places[20] == 0 && places[21] == 0 && places[22] == 0 && places[23] == 3 && wayClear(i, 6)) {
                        res.add(Pair.of(new State2(this, i, 22), cost(3, i, 22)));
                    }
                    if (places[20] == 0 && places[21] == 0 && places[22] == 0 && places[23] == 0 && wayClear(i, 6)) {
                        res.add(Pair.of(new State2(this, i, 23), cost(3, i, 23)));
                    }
                } else if (places[i] == 4) {
                    // move D
                    if (places[24] == 0 && places[25] == 4 && places[26] == 4 && places[27] == 4 && wayClear(i, 8)) {
                        res.add(Pair.of(new State2(this, i, 24), cost(4, i, 24)));
                    }
                    if (places[24] == 0 && places[25] == 0 && places[26] == 4 && places[27] == 4 && wayClear(i, 8)) {
                        res.add(Pair.of(new State2(this, i, 25), cost(4, i, 25)));
                    }
                    if (places[24] == 0 && places[25] == 0 && places[26] == 0 && places[27] == 4 && wayClear(i, 8)) {
                        res.add(Pair.of(new State2(this, i, 26), cost(4, i, 26)));
                    }
                    if (places[24] == 0 && places[25] == 0 && places[26] == 0 && places[27] == 0 && wayClear(i, 8)) {
                        res.add(Pair.of(new State2(this, i, 27), cost(4, i, 27)));
                    }
                }
            }

            for (int j = 0; j < 11; j++) {
                if (j == 2) continue;
                if (j == 4) continue;
                if (j == 6) continue;
                if (j == 8) continue;
                if (places[j] != 0) continue; // unoccupied?

                if (places[12] != 0 && places[2] == 0 && wayClear(2, j)) {
                    res.add(Pair.of(new State2(this, 12, j), cost(places[12], 12, j)));
                }
                if (places[13] != 0 && places[12] == 0 && places[2] == 0 && wayClear(2, j)) {
                    res.add(Pair.of(new State2(this, 13, j), cost(places[13], 13, j)));
                }
                if (places[14] != 0 && places[13] == 0 && places[12] == 0 && places[2] == 0 && wayClear(2, j)) {
                    res.add(Pair.of(new State2(this, 14, j), cost(places[14], 14, j)));
                }
                if (places[15] != 0 && places[14] == 0 && places[13] == 0 && places[12] == 0 && places[2] == 0 && wayClear(2, j)) {
                    res.add(Pair.of(new State2(this, 15, j), cost(places[15], 15, j)));
                }

                if (places[16] != 0 && places[4] == 0 && wayClear(4, j)) {
                    res.add(Pair.of(new State2(this, 16, j), cost(places[16], 16, j)));
                }
                if (places[17] != 0 && places[16] == 0 && places[4] == 0 && wayClear(4, j)) {
                    res.add(Pair.of(new State2(this, 17, j), cost(places[17], 17, j)));
                }
                if (places[18] != 0 && places[17] == 0 && places[16] == 0 && places[4] == 0 && wayClear(4, j)) {
                    res.add(Pair.of(new State2(this, 18, j), cost(places[18], 18, j)));
                }
                if (places[19] != 0 && places[18] == 0 && places[17] == 0 && places[16] == 0 && places[4] == 0 && wayClear(4, j)) {
                    res.add(Pair.of(new State2(this, 19, j), cost(places[19], 19, j)));
                }

                if (places[20] != 0 && places[6] == 0 && wayClear(6, j)) {
                    res.add(Pair.of(new State2(this, 20, j), cost(places[20], 20, j)));
                }
                if (places[21] != 0 && places[20] == 0 && places[6] == 0 && wayClear(6, j)) {
                    res.add(Pair.of(new State2(this, 21, j), cost(places[21], 21, j)));
                }
                if (places[22] != 0 && places[21] == 0 && places[20] == 0 && places[6] == 0 && wayClear(6, j)) {
                    res.add(Pair.of(new State2(this, 22, j), cost(places[22], 22, j)));
                }
                if (places[23] != 0 && places[22] == 0 && places[21] == 0 && places[20] == 0 && places[6] == 0 && wayClear(6, j)) {
                    res.add(Pair.of(new State2(this, 23, j), cost(places[23], 23, j)));
                }

                if (places[24] != 0 && places[8] == 0 && wayClear(8, j)) {
                    res.add(Pair.of(new State2(this, 24, j), cost(places[24], 24, j)));
                }
                if (places[25] != 0 && places[24] == 0 && places[8] == 0 && wayClear(8, j)) {
                    res.add(Pair.of(new State2(this, 25, j), cost(places[25], 25, j)));
                }
                if (places[26] != 0 && places[25] == 0 && places[24] == 0 && places[8] == 0 && wayClear(8, j)) {
                    res.add(Pair.of(new State2(this, 26, j), cost(places[26], 26, j)));
                }
                if (places[27] != 0 && places[26] == 0 && places[25] == 0 && places[24] == 0 && places[8] == 0 && wayClear(8, j)) {
                    res.add(Pair.of(new State2(this, 27, j), cost(places[27], 27, j)));
                }
            }
            return res;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State2 state = (State2) o;
            return Arrays.equals(places, state.places);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(places);
        }

        @Override
        public String toString() {
            return Arrays.toString(places);
        }

        public boolean isFinal() {
            return places[12] == 1 && places[13] == 1 &&
                    places[14] == 1 && places[15] == 1 &&
                    places[16] == 2 && places[17] == 2 &&
                    places[18] == 2 && places[19] == 2 &&
                    places[20] == 3 && places[21] == 3 &&
                    places[22] == 3 && places[23] == 3 &&
                    places[24] == 4 && places[25] == 4 &&
                    places[26] == 4 && places[27] == 4;
        }

        public String draw() {
            // #############
            // #01234567890#  0 to 11
            // ###A#B#C#D### 10 12 14 16
            //   #A#B#C#D#   11 13 15 17
            //   #########

            var sb = new StringBuilder();
            sb.append("#############\n");
            sb.append("#");
            for (int i = 0; i < 11; i++) sb.append(" ABCD".charAt(places[i]));
            sb.append("#\n");
            sb.append("###");
            sb.append(" ABCD".charAt(places[12]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[16]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[20]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[24]));
            sb.append("###\n  #");
            sb.append(" ABCD".charAt(places[13]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[17]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[21]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[23]));
            sb.append("###\n  #");
            sb.append(" ABCD".charAt(places[14]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[18]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[22]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[26]));
            sb.append("###\n  #");
            sb.append(" ABCD".charAt(places[15]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[19]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[23]));
            sb.append("#");
            sb.append(" ABCD".charAt(places[27]));
            sb.append("###\n  #########\n");
            return sb.toString();
        }
    } // class

    @Override
    protected void part2(String fileContents) {
        var lines = fileContents.split(System.lineSeparator());

        var pos = new int[28];
        pos[12] = read(lines[2].charAt(3));
        pos[13] = 4;
        pos[14] = 4;
        pos[15] = read(lines[3].charAt(3));
        pos[16] = read(lines[2].charAt(5));
        pos[17] = 3;
        pos[18] = 2;
        pos[19] = read(lines[3].charAt(5));
        pos[20] = read(lines[2].charAt(7));
        pos[21] = 2;
        pos[22] = 1;
        pos[23] = read(lines[3].charAt(7));
        pos[24] = read(lines[2].charAt(9));
        pos[25] = 1;
        pos[26] = 3;
        pos[27] = read(lines[3].charAt(9));

        var ts = new GenericTransitionSystem<State2>() {
            @Override
            public Collection<State2> initial() {
                return List.of(new State2(pos));
            }

            @Override
            public Collection<Pair<State2, Long>> successors(State2 state) {
                return state.successors();
            }

            @Override
            public boolean isFinal(State2 state) {
                return state.isFinal();
            }
        };

        var res = ts.reachFinal();
        System.out.println("Part 2: " + res.a.get(res.b.get(res.b.size() - 1)));

        if (PRINTPATH) {
            var distance = res.a;
            for (var state : res.b) {
                System.out.printf("At distance %d:%n", distance.get(state));
                System.out.println(state.draw());
            }
        }

        if (COUNTALL) {
            System.out.println("Total number of states: " + ts.reachAll().size());
        }
    }

    public static void main(String[] args) {
        Day.run(Day23::new, "2021/D23_small.txt", "2021/D23_full.txt");
    }
}
