package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        private Integer id;
        private List<BigInteger> items;
        private UnaryOperator<BigInteger> operation;
        private ModPredicate test;
        private Integer monkeyWhenTrue;
        private Integer monkeyWhenFalse;
        private BigInteger numberOfInspections;

        public Monkey() {
            this.numberOfInspections = BigInteger.ZERO;
        }

        public static Monkey of(Integer id, String items, UnaryOperator<BigInteger> operation, ModPredicate test, Integer monkeyWhenTrue, Integer monkeyWhenFalse) {
            return new Monkey()
                    .withId(id)
                    .withItems(items)
                    .withOperation(operation)
                    .withTest(test)
                    .withMonkeyWhenTrue(monkeyWhenTrue)
                    .withMonkeyWhenFalse(monkeyWhenFalse);
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

        public Monkey withId(Integer id) {
            this.id = id;
            return this;
        }

        public Monkey withItems(String items) {
            this.items = Arrays.stream(items.split(","))
                    .map(String::trim)
                    .map(BigInteger::new)
                    .collect(Collectors.toList());
            return this;
        }

        public Monkey withOperation(UnaryOperator<BigInteger> operation) {
            this.operation = operation;
            return this;
        }

        public Monkey withTest(ModPredicate test) {
            this.test = test;
            return this;
        }

        public Monkey withMonkeyWhenTrue(Integer monkeyWhenTrue) {
            this.monkeyWhenTrue = monkeyWhenTrue;
            return this;
        }

        public Monkey withMonkeyWhenFalse(Integer monkeyWhenFalse) {
            this.monkeyWhenFalse = monkeyWhenFalse;
            return this;
        }

        public Monkey withNumberOfInspections(BigInteger numberOfInspections) {
            this.numberOfInspections = numberOfInspections;
            return this;
        }

        public boolean isComplete() {
            return Stream.of(id, items, operation, test, monkeyWhenTrue, monkeyWhenFalse).allMatch(Objects::nonNull);
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

        List<Pair<Pattern, BiConsumer<Monkey, Matcher>>> expectedPatterns = Stream.of(
                        new Pair<String, BiConsumer<Monkey, Matcher>>("^Monkey ([0-9]+).+$",
                                (monkey, matcher) -> monkey.withId(Integer.parseInt(matcher.group((1))))),
                        new Pair<String, BiConsumer<Monkey, Matcher>>("Starting items: ([0-9, ]+)$",
                                (monkey, matcher) -> monkey.withItems(matcher.group(1))),
                        new Pair<String, BiConsumer<Monkey, Matcher>>("Operation: new = old ([+*]{1}) (.+)$",
                                (monkey, matcher) -> monkey.withOperation(parseOperation(matcher))),
                        new Pair<String, BiConsumer<Monkey, Matcher>>("Test: divisible by ([0-9]+)$",
                                (monkey, matcher) -> monkey.withTest(ModPredicate.of(Integer.parseInt(matcher.group((1)))))),
                        new Pair<String, BiConsumer<Monkey, Matcher>>("If true: throw to monkey ([0-9]+)$",
                                (monkey, matcher) -> monkey.withMonkeyWhenTrue(Integer.parseInt(matcher.group((1))))),
                        new Pair<String, BiConsumer<Monkey, Matcher>>("If false: throw to monkey ([0-9]+)$",
                                (monkey, matcher) -> monkey.withMonkeyWhenFalse(Integer.parseInt(matcher.group((1))))))
                .map(pair -> new Pair<>(Pattern.compile(pair.a), pair.b))
                .collect(Collectors.toList());

        Monkey monkey = new Monkey();
        for (String line : fileContents.split(System.lineSeparator())) {
            for (Pair<Pattern, BiConsumer<Monkey, Matcher>> patternAndConsumer : expectedPatterns) {
                Matcher matcher = patternAndConsumer.a.matcher(line);
                if (matcher.find()) {
                    patternAndConsumer.b.accept(monkey, matcher);
                    if (monkey.isComplete()) {
                        monkeys.put(monkey.id, monkey);
                        monkey = new Monkey();
                    }
                    break;
                }
            }
        }

        return monkeys;
    }

    private UnaryOperator<BigInteger> parseOperation(final Matcher matcher) {
        UnaryOperator<BigInteger> operation;

        if ("+".equals(matcher.group(1))) {
            if ("old".equals(matcher.group(2))) {
                operation = old -> old.add(old);
            } else {
                operation = old -> old.add(new BigInteger(matcher.group(2)));
            }
        } else if ("*".equals(matcher.group(1))) {
            if ("old".equals(matcher.group(2))) {
                operation = old -> old.multiply(old);
            } else {
                operation = old -> old.multiply(new BigInteger(matcher.group(2)));
            }
        } else {
            throw new RuntimeException("Operation not implemented!");
        }
        return operation;
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
