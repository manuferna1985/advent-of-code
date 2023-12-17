package es.ing.aoc.common.dijkstra;

import es.ing.aoc.common.Point;

import java.util.Comparator;

// CustomNode class
public class CustomNode implements Comparator<CustomNode> {

  public enum Direction {
    N(0), S(1), E(2), W(3);
    private final int value;

    Direction(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  private final Point point;
  private final Direction dir;
  private final int dirStraightMoves;
  private final int cost;

  public CustomNode(Point point, Direction dir, int dirStraightMoves, int cost) {
    this.point = point;
    this.dir = dir;
    this.dirStraightMoves = dirStraightMoves;
    this.cost = cost;
  }

  public Point getPoint() {
    return point;
  }

  public Direction getDir() {
    return dir;
  }

  public int getDirStraightMoves() {
    return dirStraightMoves;
  }

  public int getCost() {
    return cost;
  }

  @Override
  public int compare(CustomNode o1, CustomNode o2) {
    return Integer.compare(o1.cost, o2.cost);
  }

  @Override
  public boolean equals(Object o) {
    if (this==o) return true;
    if (o==null || getClass()!=o.getClass()) return false;

    CustomNode that = (CustomNode) o;

    if (getDirStraightMoves()!=that.getDirStraightMoves()) return false;
    if (!getPoint().equals(that.getPoint())) return false;
    return getDir()==that.getDir();
  }

  @Override
  public int hashCode() {
    int result = getPoint().hashCode();
    result = 31 * result + getDir().hashCode();
    result = 31 * result + getDirStraightMoves();
    return result;
  }

  public CustomNode cloneWithDistance(int newDistance){
    return new CustomNode(this.point, this.dir, this.dirStraightMoves, newDistance);
  }
}
