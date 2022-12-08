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

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Point(String[] strPoint) {
        this.x = Integer.parseInt(strPoint[0]);
        this.y = Integer.parseInt(strPoint[1]);
        if (strPoint.length > 2) {
            this.z = Integer.parseInt(strPoint[2]);
        }
    }

    public String toString() {
        return String.format("[%d,%d,%d]", x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y && z == point.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
