package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day5 extends Day {

  record RangeMap(Range<Long> sourceRange, Long destRangeStart) {}

  @Override
  protected String part1(String fileContents) throws Exception {

    String[] lines = fileContents.split(System.lineSeparator());
    long[] seeds = getLongSeeds(lines);
    List<List<RangeMap>> seedMaps = buildSeedMaps(lines);

    for (List<RangeMap> terrain : seedMaps) {
      for (int i = 0; i < seeds.length; i++) {
        for (RangeMap tr : terrain) {
          if (tr.sourceRange.contains(seeds[i])) {
            seeds[i] = applyTransformation(tr, seeds[i]);
            break;
          }
        }
      }
    }

    return String.valueOf(Arrays.stream(seeds).min().orElse(0));
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    String[] lines = fileContents.split(System.lineSeparator());
    List<Range<Long>> seeds = getSeedInRanges(lines);
    List<List<RangeMap>> seedMaps = buildSeedMaps(lines);

    for (List<RangeMap> terrain : seedMaps) {
      List<Range<Long>> newSeeds = new ArrayList<>();

      for (Range<Long> seed : seeds) {
        List<Range<Long>> remaining = List.of(seed);
        List<Range<Long>> transformed = new ArrayList<>();

        for (RangeMap tr : terrain) {
          List<Range<Long>> newRemaining = new ArrayList<>();
          for (Range<Long> sr : remaining) {

            if (tr.sourceRange.isOverlappedBy(sr)) {
              Range<Long> intersect = tr.sourceRange.intersectionWith(sr);
              transformed.add(Range.between(applyTransformation(tr, intersect.getMinimum()), applyTransformation(tr, intersect.getMaximum())));

              if (sr.getMinimum() < tr.sourceRange.getMinimum()) {
                newRemaining.add(Range.between(sr.getMinimum(), tr.sourceRange.getMinimum() - 1));
              }

              if (sr.getMaximum() > tr.sourceRange.getMaximum()) {
                newRemaining.add(Range.between(tr.sourceRange.getMaximum() + 1, sr.getMaximum()));
              }
            } else {
              newRemaining.add(sr);
            }
          }
          remaining = newRemaining;
        }
        newSeeds.addAll(remaining);
        newSeeds.addAll(transformed);
      }

      seeds = newSeeds;
    }

    return String.valueOf(seeds.stream().map(Range::getMinimum).mapToLong(Long::longValue).min().orElse(0));
  }

  private List<Range<Long>> getSeedInRanges(String[] lines) {
    long[] rawSeeds = getLongSeeds(lines);
    List<Range<Long>> seeds = new ArrayList<>();

    for (int i = 0; i < rawSeeds.length; i += 2) {
      seeds.add(Range.between(rawSeeds[i], rawSeeds[i] + rawSeeds[i + 1] - 1));
    }
    return seeds;
  }

  private static long applyTransformation(RangeMap tr, Long seedValue) {
    return seedValue + (tr.destRangeStart - tr.sourceRange.getMinimum());
  }

  private List<List<RangeMap>> buildSeedMaps(String[] lines) {
    List<List<RangeMap>> seedMaps = new ArrayList<>();

    List<RangeMap> currentMap = new ArrayList<>();
    for (int i = 3; i < lines.length; i++) {
      if (lines[i].isEmpty()) {
        continue;
      }

      if (lines[i].endsWith("map:")) {
        seedMaps.add(currentMap);
        currentMap = new ArrayList<>();
      } else {
        long[] parts = Arrays.stream(lines[i].split(" ")).mapToLong(Long::parseLong).toArray();
        currentMap.add(new RangeMap(Range.between(parts[1], parts[1] + parts[2] - 1), parts[0]));
      }
    }
    seedMaps.add(currentMap);

    return seedMaps;
  }

  private long[] getLongSeeds(String[] lines) {
    return Arrays.stream(lines[0].substring(lines[0].indexOf(":") + 1).trim().split(" "))
        .mapToLong(Long::parseLong)
        .toArray();
  }

  public static void main(String[] args) {
    Day.run(Day5::new, "2023/D5_small.txt", "2023/D5_full.txt");
  }
}
