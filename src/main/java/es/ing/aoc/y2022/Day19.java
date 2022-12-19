package es.ing.aoc.y2022;

import static es.ing.aoc.y2022.Day19.Material.CLAY;
import static es.ing.aoc.y2022.Day19.Material.GEODE;
import static es.ing.aoc.y2022.Day19.Material.OBSIDIAN;
import static es.ing.aoc.y2022.Day19.Material.ORE;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 extends Day {

    private static final Pattern BLUEPRINT_PATTERN = Pattern.compile("\\d+");

    enum Material {
        GEODE,
        OBSIDIAN,
        CLAY,
        ORE;
    }

    static class Blueprint {
        Integer id;
        Map<Material, List<Pair<Material, Integer>>> robotCosts;

        Blueprint(Integer id) {
            this.id = id;
            robotCosts = new HashMap<>();
            for (Material mat : Material.values()) {
                robotCosts.put(mat, new ArrayList<>());
            }
        }
    }

    static class FactoryOption {
        Blueprint blueprint;
        Map<Material, Integer> storage;
        Map<Material, Integer> robots;

        public FactoryOption(Blueprint blueprint, Map<Material, Integer> storage, Map<Material, Integer> robots) {
            this.blueprint = blueprint;
            this.storage = storage;
            this.robots = robots;
        }

        public Integer getPoints() {
            return robots.get(GEODE) * 200 + robots.get(OBSIDIAN) * 100 +
                    storage.get(GEODE) * 5 + storage.get(OBSIDIAN) * 2 + storage.get(CLAY);
        }

        public static FactoryOption copy(FactoryOption other) {
            return new FactoryOption(other.blueprint, new HashMap<>(other.storage), new HashMap<>(other.robots));
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        List<Blueprint> blueprints = readBlueprintsFrom(fileContents);
        return String.valueOf(workingRobots(blueprints, 24, true, 15000, true));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        List<Blueprint> blueprints = readBlueprintsFrom(fileContents);
        return String.valueOf(workingRobots(blueprints, 32, false, 2500, false));
    }

    private List<Blueprint> readBlueprintsFrom(String fileContents) {
        String[] lines = fileContents.split(System.lineSeparator()); // when input file is multiline
        List<Blueprint> blueprints = new ArrayList<>();

        for (String line : lines) {
            List<Integer> numbers = getLineNumbers(line);
            Blueprint bp = new Blueprint(numbers.get(0));

            // ORE
            bp.robotCosts.get(ORE).add(new Pair<>(ORE, numbers.get(1)));

            // CLAY
            bp.robotCosts.get(CLAY).add(new Pair<>(ORE, numbers.get(2)));

            // OBSIDIAN
            bp.robotCosts.get(OBSIDIAN).addAll(List.of(
                    new Pair<>(ORE, numbers.get(3)),
                    new Pair<>(CLAY, numbers.get(4))));

            // GEODE
            bp.robotCosts.get(GEODE).addAll(List.of(
                    new Pair<>(ORE, numbers.get(5)),
                    new Pair<>(OBSIDIAN, numbers.get(6))));

            blueprints.add(bp);
        }

        return blueprints;
    }

    private List<Integer> getLineNumbers(String line) {
        List<Integer> numbers = new ArrayList<>();
        Matcher m = BLUEPRINT_PATTERN.matcher(line);
        while (m.find()) {
            numbers.add(Integer.parseInt(m.group()));
        }
        return numbers;
    }

    private Integer workingRobots(List<Blueprint> blueprints, int maxTime, boolean earlyPrunning, int iterations, boolean qualityResultWithAll) {
        int result = qualityResultWithAll ? 0 : 1;

        for (Blueprint bp : blueprints) {
            // Initial Storage
            Map<Material, Integer> initialStorage = initialMaterialMap();

            // Initial factory
            Map<Material, Integer> initialRobots = initialMaterialMap();
            initialRobots.put(ORE, 1);

            List<FactoryOption> currentOptions = new ArrayList<>(List.of(new FactoryOption(bp, initialStorage, initialRobots)));

            // Time loop
            List<FactoryOption> aliveOptions = new ArrayList<>();
            for (int i = 1; i <= maxTime; i++) {
                aliveOptions.clear();

                for (FactoryOption opt : currentOptions) {
                    List<Material> newRobots = getRobotBuildOptions(bp, opt.storage, earlyPrunning);

                    for (Material mat : newRobots) {
                        FactoryOption newOption = FactoryOption.copy(opt);
                        robotStartBuild(bp, newOption.storage, mat);
                        robotMine(newOption.storage, newOption.robots);
                        robotEndBuild(newOption.robots, mat);
                        aliveOptions.add(newOption);
                    }

                    if (newRobots.size() < Material.values().length) {
                        robotMine(opt.storage, opt.robots);
                        aliveOptions.add(opt);
                    }
                }

                currentOptions.clear();
                currentOptions.addAll(aliveOptions.stream()
                        .sorted((o1, o2) -> Integer.compare(o2.getPoints(), o1.getPoints()))
                        .limit(iterations)
                        .collect(Collectors.toList()));
            }

            int maxGeodes = currentOptions.stream().mapToInt(opt -> opt.storage.get(GEODE)).max().orElse(0);

            if (qualityResultWithAll) {
                result += (maxGeodes * bp.id);
            } else {
                result *= maxGeodes;
                if (bp.id == 3) {
                    break;
                }
            }
        }
        return result;
    }

    private Map<Material, Integer> initialMaterialMap() {
        return Arrays.stream(Material.values()).collect(Collectors.toMap(mat -> mat, mat -> 0));
    }

    private void robotMine(Map<Material, Integer> storage, Map<Material, Integer> robots) {
        for (Material mat : Material.values()) {
            if (robots.get(mat) > 0) {
                storage.put(mat, storage.get(mat) + robots.get(mat));
            }
        }
    }

    private List<Material> getRobotBuildOptions(Blueprint bp, Map<Material, Integer> storage, boolean earlyPrunning) {
        List<Material> newRobots = new ArrayList<>();

        for (Material mat : Material.values()) {
            List<Pair<Material, Integer>> costs = bp.robotCosts.get(mat);

            if (costs.stream().allMatch(c -> storage.get(c.a) >= c.b)) {
                // Robot can be built
                newRobots.add(mat);

                // Cutting some tree branches
                if (earlyPrunning && (mat.equals(GEODE) || mat.equals(OBSIDIAN))) {
                    break;
                }
            }
        }

        return newRobots;
    }

    private void robotStartBuild(Blueprint bp, Map<Material, Integer> storage, Material robotToBuild) {
        bp.robotCosts.get(robotToBuild).forEach(c -> storage.put(c.a, storage.get(c.a) - c.b));
    }

    private void robotEndBuild(Map<Material, Integer> robots, Material newRobot) {
        robots.put(newRobot, robots.get(newRobot) + 1);
    }

    public static void main(String[] args) {
        Day.run(Day19::new, "2022/D19_small.txt", "2022/D19_full.txt");
    }
}
