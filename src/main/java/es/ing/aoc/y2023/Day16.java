package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntUnaryOperator;

import static es.ing.aoc.y2023.Day16.Direction.DOWN;
import static es.ing.aoc.y2023.Day16.Direction.LEFT;
import static es.ing.aoc.y2023.Day16.Direction.RIGHT;
import static es.ing.aoc.y2023.Day16.Direction.UP;

public class Day16 extends Day {

  private static final List<Direction> HORIZONTAL = List.of(RIGHT, LEFT);
  private static final List<Direction> VERTICAL = List.of(UP, DOWN);

  public enum Direction {
    UP(x -> x - 1, y -> y),
    DOWN(x -> x + 1, y -> y),
    RIGHT(x -> x, y -> y + 1),
    LEFT(x -> x, y -> y - 1);

    private final IntUnaryOperator xFn;
    private final IntUnaryOperator yFn;

    Direction(IntUnaryOperator xFn, IntUnaryOperator yFn) {
      this.xFn = xFn;
      this.yFn = yFn;
    }
  }

  record Beam(Point pos, Direction dir) {
    Point getNextPosition() {
      return Point.of(dir.xFn.applyAsInt(pos.x), dir.yFn.applyAsInt(pos.y));
    }

    Beam changeDir(Direction other) {
      return new Beam(pos, other);
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    Character[][] charsMatrix = MatrixUtils.readMatrixFromFile(fileContents, Character.class, s -> s.charAt(0));
    return String.valueOf(getEnergyFrom(charsMatrix, new Beam(Point.of(0, 0), RIGHT)));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    Character[][] charsMatrix = MatrixUtils.readMatrixFromFile(fileContents, Character.class, s -> s.charAt(0));

    List<Integer> energies = new ArrayList<>();
    for (int x=0; x<charsMatrix.length; x++){
      for (int y=0; y<charsMatrix[x].length; y++){
        if (x == 0){
          energies.add(getEnergyFrom(charsMatrix, new Beam(Point.of(x, y), DOWN)));
        } else if (y == 0){
          energies.add(getEnergyFrom(charsMatrix, new Beam(Point.of(x, y), RIGHT)));
        } else if (x == charsMatrix.length - 1){
          energies.add(getEnergyFrom(charsMatrix, new Beam(Point.of(x, y), UP)));
        } else if (y == charsMatrix[x].length - 1){
          energies.add(getEnergyFrom(charsMatrix, new Beam(Point.of(x, y), LEFT)));
        }
      }
    }

    return String.valueOf(energies.stream().mapToInt(Integer::valueOf).max().orElse(0));
  }

  private int getEnergyFrom(Character[][] charsMatrix, Beam initialBeam) {
    Set<Point> energized = new HashSet<>();
    List<Beam> alreadyVisited = new ArrayList<>();
    List<Beam> beams = new ArrayList<>();
    beams.add(initialBeam);

    System.out.printf("[%-3d,%-3d] - %s\n", initialBeam.pos.x, initialBeam.pos.y, initialBeam.dir);

    while (!beams.isEmpty()) {

      Beam beam = beams.remove(0);

      boolean stop = false;

      while (!stop) {

        if (this.pointIsWithinLimits(charsMatrix, beam.pos) && !alreadyVisited.contains(beam)) {
          energized.add(beam.pos);
          alreadyVisited.add(beam);

          switch (charsMatrix[beam.pos.x][beam.pos.y]) {
            case '|':
              if (HORIZONTAL.contains(beam.dir)) {
                beams.add(new Beam(beam.pos, UP));
                beams.add(new Beam(beam.pos, DOWN));
                stop = true;
              }
              break;
            case '-':
              if (VERTICAL.contains(beam.dir)) {
                beams.add(beam.changeDir(RIGHT));
                beams.add(beam.changeDir(LEFT));
                stop = true;
              }
              break;
            case '/':
              beam = beam.changeDir(
                  switch (beam.dir) {
                    case LEFT -> DOWN;
                    case RIGHT -> UP;
                    case UP -> RIGHT;
                    case DOWN -> LEFT;
                  });
              break;
            case '\\':
              beam = beam.changeDir(
                  switch (beam.dir) {
                    case LEFT -> UP;
                    case RIGHT -> DOWN;
                    case UP -> LEFT;
                    case DOWN -> RIGHT;
                  });
              break;
          }

        } else {
          stop = true;
        }

        beam = new Beam(beam.getNextPosition(), beam.dir);
      }
    }
    return energized.size();
  }

  private boolean pointIsWithinLimits(Character[][] matrix, Point newPoint) {
    return newPoint.x >= 0 && newPoint.x < matrix.length && newPoint.y >= 0 && newPoint.y < matrix[0].length;
  }


  public static void main(String[] args) {
    Day.run(Day16::new, "2023/D16_small.txt", "2023/D16_full.txt");
  }
}
