package es.ing.aoc.common;

import java.util.Objects;

public class FloatPoint {

  public float x;
  public float y;
  public float z;

  public FloatPoint(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public FloatPoint(String x, String y, String z) {
    this(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
  }

  public FloatPoint(float x, float y) {
    this(x, y, 0);
  }

  public FloatPoint(String x, String y) {
    this(Float.parseFloat(x), Float.parseFloat(y));
  }

  public FloatPoint(FloatPoint other) {
    this(other.x, other.y, other.z);
  }

  public FloatPoint(String[] strPoint) {
    this.x = Float.parseFloat(strPoint[0]);
    this.y = Float.parseFloat(strPoint[1]);
    if (strPoint.length > 2) {
      this.z = Float.parseFloat(strPoint[2]);
    }
  }

  public static FloatPoint of(float x, float y, float z) {
    return new FloatPoint(x, y, z);
  }

  public static FloatPoint of(float x, float y) {
    return new FloatPoint(x, y);
  }

  public String toString() {
    return String.format("[%.1f,%.1f,%.1f]", x, y, z);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FloatPoint point = (FloatPoint) o;
    return x == point.x && y == point.y && z == point.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getZ() {
    return z;
  }

  public boolean isIntegerPoint(){
    return ((int)x) == x && ((int)y) == y && ((int)z) == z;
  }

  public Point getIntegerPoint(){
    return Point.of((int)x, (int)y, (int)z);
  }
}
