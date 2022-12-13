package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Day11 extends Day {

    private static final BigDecimal WORRY_DIV_FACTOR = BigDecimal.valueOf(3.0);
    private static final int MONKEYS_TO_COUNT = 2;

    static final class ModPredicate implements Predicate<BigInteger> {

        private final BigInteger mod;

        private ModPredicate(BigInteger mod) {
            this.mod = mod;
        }

        public static ModPredicate of(int mod) {
            return new ModPredicate(BigInteger.valueOf(mod));
        }

        @Override
        public boolean test(BigInteger n) {
            return n.mod(mod).equals(BigInteger.ZERO);
        }
    }

    static final class Monkey {
        private final List<BigInteger> items;
        private final UnaryOperator<BigInteger> operation;
        private final ModPredicate test;
        private final Integer monkeyWhenTrue;
        private final Integer monkeyWhenFalse;
        private BigInteger numberOfInspections;

        private Monkey(List<BigInteger> items, UnaryOperator<BigInteger> operation, ModPredicate test, Integer monkeyWhenTrue, Integer monkeyWhenFalse) {
            this.items = new ArrayList<>(items);
            this.operation = operation;
            this.test = test;
            this.monkeyWhenTrue = monkeyWhenTrue;
            this.monkeyWhenFalse = monkeyWhenFalse;
            this.numberOfInspections = BigInteger.ZERO;
        }

        public static Monkey of(String items, UnaryOperator<BigInteger> operation, ModPredicate test, Integer monkeyWhenTrue, Integer monkeyWhenFalse) {
            return new Monkey(
                    Arrays.stream(items.split(","))
                            .map(String::trim)
                            .map(BigInteger::new)
                            .collect(Collectors.toList()),
                    operation, test, monkeyWhenTrue, monkeyWhenFalse);
        }

        public BigInteger inspectNextItem(BigInteger customFactor) {
            this.numberOfInspections = this.numberOfInspections.add(BigInteger.ONE);
            return applyFactor(this.operation.apply(this.items.remove(0)), customFactor);
        }

        public Integer testAndNextMonkey(BigInteger item) {
            return this.test.test(item) ? this.monkeyWhenTrue : this.monkeyWhenFalse;
        }

        public void addItemToMonkeyQueue(BigInteger newItem) {
            this.items.add(newItem);
        }

        private BigInteger applyFactor(BigInteger item, BigInteger factor) {
            return Optional.ofNullable(factor)
                    .map(item::mod)
                    .orElse(new BigDecimal(item).divide(WORRY_DIV_FACTOR, RoundingMode.FLOOR).toBigInteger());
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        return String.valueOf(executeMonkeysGame(getMonkeysDataFor(fileContents), 20, true));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        return String.valueOf(executeMonkeysGame(getMonkeysDataFor(fileContents), 10000, false));
    }

    private Map<Integer, Monkey> getMonkeysDataFor(String fileContents) {
        final Map<Integer, Monkey> monkeys = new HashMap<>();
        if (!fileContents.contains("Monkey 7")) {
            monkeys.put(0, Monkey.of("79, 98",
                    old -> old.multiply(BigInteger.valueOf(19)), ModPredicate.of(23), 2, 3));
            monkeys.put(1, Monkey.of("54, 65, 75, 74",
                    old -> old.add(BigInteger.valueOf(6)), ModPredicate.of(19), 2, 0));
            monkeys.put(2, Monkey.of("79, 60, 97",
                    old -> old.multiply(old), ModPredicate.of(13), 1, 3));
            monkeys.put(3, Monkey.of("74",
                    old -> old.add(BigInteger.valueOf(3)), ModPredicate.of(17), 0, 1));
        } else {
            monkeys.put(0, Monkey.of("57",
                    old -> old.multiply(BigInteger.valueOf(13)), ModPredicate.of(11), 3, 2));
            monkeys.put(1, Monkey.of("58, 93, 88, 81, 72, 73, 65",
                    old -> old.add(BigInteger.valueOf(2)), ModPredicate.of(7), 6, 7));
            monkeys.put(2, Monkey.of("65, 95",
                    old -> old.add(BigInteger.valueOf(6)), ModPredicate.of(13), 3, 5));
            monkeys.put(3, Monkey.of("58, 80, 81, 83",
                    old -> old.multiply(old), ModPredicate.of(5), 4, 5));
            monkeys.put(4, Monkey.of("58, 89, 90, 96, 55",
                    old -> old.add(BigInteger.valueOf(3)), ModPredicate.of(3), 1, 7));
            monkeys.put(5, Monkey.of("66, 73, 87, 58, 62, 67",
                    old -> old.multiply(BigInteger.valueOf(7)), ModPredicate.of(17), 4, 1));
            monkeys.put(6, Monkey.of("85, 55, 89",
                    old -> old.add(BigInteger.valueOf(4)), ModPredicate.of(2), 2, 0));
            monkeys.put(7, Monkey.of("73, 80, 54, 94, 90, 52, 69, 58",
                    old -> old.add(BigInteger.valueOf(7)), ModPredicate.of(19), 6, 0));
        }
        return monkeys;
    }

    private BigInteger executeMonkeysGame(final Map<Integer, Monkey> monkeys, final int numberOfRounds, final boolean applyWorryFactor) {
        BigInteger item;

        // Factor to apply when we don´t have a specific one to avoid numbers growing too much.
        final BigInteger factor = monkeys.values().stream()
                .map(m -> m.test.mod)
                .reduce(BigInteger::multiply)
                .orElseThrow(() -> new RuntimeException("Not enough data to calculate factor"));

        for (int r = 0; r < numberOfRounds; r++) {
            for (int m = 0; m < monkeys.size(); m++) {
                Monkey mk = monkeys.get(m);
                while (!mk.items.isEmpty()) {
                    item = mk.inspectNextItem(!applyWorryFactor ? factor : null);
                    monkeys.get(mk.testAndNextMonkey(item)).addItemToMonkeyQueue(item);
                }
            }
        }

        // Return the multiplication of the inspections for the 2 monkeys with more inspections
        return monkeys.values().stream()
                .map(m -> m.numberOfInspections)
                .sorted(Comparator.reverseOrder())
                .limit(MONKEYS_TO_COUNT)
                .reduce(BigInteger::multiply)
                .orElseThrow(() -> new RuntimeException("Not enough data to calculate monkeys inspections"));
    }

    public static void main(String[] args) {
        Day.run(Day11::new, "2022/D11_small.txt", "2022/D11_full.txt");
    }
}
