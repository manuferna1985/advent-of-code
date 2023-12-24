package es.ing.aoc.common;

import java.util.Objects;

public class LongPoint {
  public long x;
  public long y;
  public long z;

  public LongPoint(long x, long y, long z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public LongPoint(String x, String y, String z) {
    this(Long.parseLong(x), Long.parseLong(y), Long.parseLong(z));
  }

  public LongPoint(long x, long y) {
    this(x, y, 0L);
  }

  public LongPoint(String x, String y) {
    this(Long.parseLong(x), Long.parseLong(y));
  }

  public LongPoint(LongPoint other) {
    this(other.x, other.y, other.z);
  }

  public LongPoint(String[] strPoint) {
    this.x = Long.parseLong(strPoint[0].trim());
    this.y = Long.parseLong(strPoint[1].trim());
    if (strPoint.length > 2) {
      this.z = Long.parseLong(strPoint[2].trim());
    }
  }

  public static LongPoint of(long x, long y, long z) {
    return new LongPoint(x, y, z);
  }

  public static LongPoint of(long x, long y) {
    return new LongPoint(x, y);
  }

  public static LongPoint of(String[] coords) {
    return new LongPoint(coords);
  }

  public static LongPoint of(LongPoint other){
    return new LongPoint(other.x, other.y, other.z);
  }

  public String toString() {
    return String.format("[%d,%d,%d]", x, y, z);
  }

  @Override
  public boolean equals(Object o) {
    if (this==o) return true;
    if (o==null || getClass()!=o.getClass()) return false;
    LongPoint point = (LongPoint) o;
    return x==point.x && y==point.y && z==point.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  public long getX() {
    return x;
  }

  public long getY() {
    return y;
  }

  public long getZ() {
    return z;
  }

  public static LongPoint max(LongPoint p1, LongPoint p2) {
    return LongPoint.of(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
  }

  public static LongPoint min(LongPoint p1, LongPoint p2) {
    return LongPoint.of(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
  }
}
