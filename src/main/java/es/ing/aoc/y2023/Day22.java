package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day22 extends Day {

  static class Stick {

    private final String name;
    private final Point left;
    private final Point right;
    private final List<Point> allPoints;

    public Stick(String name, Point left, Point right) {
      this.name = name;
      this.left = Point.of(left);
      this.right = Point.of(right);
      this.allPoints = new ArrayList<>();

      Range<Integer> rx = Range.between(left.x, right.x);
      Range<Integer> ry = Range.between(left.y, right.y);
      Range<Integer> rz = Range.between(left.z, right.z);

      for (int x = rx.getMinimum(); x <= rx.getMaximum(); x++) {
        for (int y = ry.getMinimum(); y <= ry.getMaximum(); y++) {
          for (int z = rz.getMinimum(); z <= rz.getMaximum(); z++) {
            this.allPoints.add(Point.of(x, y, z));
          }
        }
      }
    }

    public Stick(Stick other) {
      this(other.name, other.left, other.right);
    }

    public Point getLeft() {
      return left;
    }

    public Point getRight() {
      return right;
    }

    public List<Point> getAllPoints() {
      return allPoints;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return String.format("%s: [(%s), (%s)]", name, left, right);
    }

    public void fall() {
      if (!isOnGround()) {
        this.left.z--;
        this.right.z--;
        this.allPoints.forEach(p -> p.z--);
      }
    }

    public boolean isOnGround() {
      return left.z==1 || right.z==1;
    }

    public List<Stick> findSticksImSupporting(List<Stick> allSticks) {
      return allSticks.stream().filter(s -> !s.equals(this)).filter(that -> isStickSupportedByMe(this, that)).toList();
    }

    public List<Stick> findSticksImSupportedOn(List<Stick> allSticks) {
      return allSticks.stream().filter(s -> !s.equals(this)).filter(that -> isStickSupportedByMe(that, this)).toList();
    }

    public List<Stick> findSticksImOnlySupporting(List<Stick> allSticks) {
      return findSticksImSupporting(allSticks).stream().filter(s -> s.findSticksImSupportedOn(allSticks).size()==1).toList();
    }

    public boolean isStickSupportedByMe(Stick me, Stick other) {
      Stick futureOther = new Stick(other);
      futureOther.fall();
      return me.getAllPoints().stream().anyMatch(futureOther.allPoints::contains);
    }

    public int chainReaction(List<Stick> allSticks, Set<Stick> fallen) {
      fallen.add(this);
      List<Stick> nextToFall = this.findSticksImSupporting(allSticks);

      List<Stick> nextToFallFiltered = new ArrayList<>();
      for (Stick candidate : nextToFall) {

        if (fallen.containsAll(candidate.findSticksImSupportedOn(allSticks))) {
          nextToFallFiltered.add(candidate);
        }
      }

      nextToFallFiltered.forEach(s -> s.chainReaction(allSticks, fallen));
      return fallen.size();
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {

    List<Stick> sticks = buildSticks(fileContents);
    System.out.println("[begin]: algorithm");
    fallingAlgorithm(sticks);
    System.out.println("[end]: algorithm");

    System.out.println("[begin]: disintegration");
    List<Stick> sticksToDisintegrate = sticks.stream()
        .filter(s -> s.findSticksImOnlySupporting(sticks).isEmpty()).toList();
    System.out.println("[end]: disintegration");

    return String.valueOf(sticksToDisintegrate.size());
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    List<Stick> sticks = buildSticks(fileContents);
    System.out.println("[begin]: algorithm");
    fallingAlgorithm(sticks);
    System.out.println("[end]: algorithm");

    System.out.println("[begin]: chainReaction");
    Map<Stick, Integer> fallenCount = new HashMap<>();
    for (Stick s : sticks) {
      int c = s.chainReaction(sticks, new HashSet<>()) - 1;
      fallenCount.put(s, c);
    }
    System.out.println("[end]: chainReaction");

    return String.valueOf(fallenCount.values().stream().mapToInt(Integer::valueOf).sum());
  }

  private List<Stick> buildSticks(String fileContents) {
    String[] lines = fileContents.split(System.lineSeparator());
    List<Stick> sticks = new ArrayList<>();
    int index = 1;
    for (String line : lines) {
      String[] parts = line.split("~");
      sticks.add(new Stick(String.valueOf(index++), Point.of(parts[0].split(",")), Point.of(parts[1].split(","))));
    }

    return sticks;
  }

  private void fallingAlgorithm(List<Stick> sticks) {

    boolean anyMoves = true;

    while (anyMoves) {
      anyMoves = false;
      for (Stick s : sticks) {

        if (!s.isOnGround()) {
          Stick futureStick = new Stick(s);
          futureStick.fall();

          if (isNotOverlapping(s, futureStick, sticks)) {
            s.fall();
            anyMoves = true;
          }
        }
      }
    }
  }

  private boolean isNotOverlapping(Stick s1, Stick s2, List<Stick> allSticks) {
    return allSticks.stream().filter(other -> !s1.equals(other)).flatMap(s -> s.getAllPoints().stream()).noneMatch(s2.allPoints::contains);
  }

  public static void main(String[] args) {
    Day.run(Day22::new, "2023/D22_small.txt", "2023/D22_full.txt");
  }
}
