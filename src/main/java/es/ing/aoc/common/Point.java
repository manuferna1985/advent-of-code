package es.ing.aoc.common;

import java.util.Objects;

public class Point {
  public int x;
  public int y;
  public int z;

  public Point(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Point(String x, String y, String z) {
    this(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
  }

  public Point(int x, int y) {
    this(x, y, 0);
  }

  public Point(String x, String y) {
    this(Integer.parseInt(x), Integer.parseInt(y));
  }

  public Point(Point other) {
    this(other.x, other.y, other.z);
  }

  public Point(String[] strPoint) {
    this.x = Integer.parseInt(strPoint[0].trim());
    this.y = Integer.parseInt(strPoint[1].trim());
    if (strPoint.length > 2) {
      this.z = Integer.parseInt(strPoint[2].trim());
    }
  }

  public static Point of(int x, int y, int z) {
    return new Point(x, y, z);
  }

  public static Point of(int x, int y) {
    return new Point(x, y);
  }

  public static Point of(String[] coords) {
    return new Point(coords);
  }

  public static Point of(Point other){
    return new Point(other.x, other.y, other.z);
  }

  public String toString() {
    return String.format("[%d,%d,%d]", x, y, z);
  }

  @Override
  public boolean equals(Object o) {
    if (this==o) return true;
    if (o==null || getClass()!=o.getClass()) return false;
    Point point = (Point) o;
    return x==point.x && y==point.y && z==point.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  public static Point max(Point p1, Point p2) {
    return Point.of(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
  }

  public static Point min(Point p1, Point p2) {
    return Point.of(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
  }
}
