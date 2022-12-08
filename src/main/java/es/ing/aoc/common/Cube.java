package es.ing.aoc.common;

import java.util.Optional;

public class Cube {

    public String id;
    public boolean state;
    public int xMin;
    public int xMax;
    public int yMin;
    public int yMax;
    public int zMin;
    public int zMax;

    public static Cube of(String id, boolean state, int[] xCoords, int[] yCoords, int[] zCoords) {

        Cube c = new Cube();

        c.id = id;
        c.state = state;

        c.xMin = xCoords[0];
        c.xMax = xCoords[1];

        c.yMin = yCoords[0];
        c.yMax = yCoords[1];

        c.zMin = zCoords[0];
        c.zMax = zCoords[1];

        return c;
    }

    public Long eval() {
        return (state ? +1 : -1) * (((xMax - xMin) + 1L) * ((yMax - yMin) + 1L) * ((zMax - zMin) + 1L));
    }

    public static Optional<Cube> intersect(Cube one, Cube two, boolean state) {
        Cube overlap = Cube.of(String.format("(%s-%s)", one, two), state,
                new int[]{Math.max(one.xMin, two.xMin), Math.min(one.xMax, two.xMax)},
                new int[]{Math.max(one.yMin, two.yMin), Math.min(one.yMax, two.yMax)},
                new int[]{Math.max(one.zMin, two.zMin), Math.min(one.zMax, two.zMax)});

        return overlap.valid() ? Optional.of(overlap) : Optional.empty();
    }

    public boolean isWithin(Cube other) {
        return other.xMin <= xMin && other.xMax >= xMax &&
                other.yMin <= yMin && other.yMax >= yMax &&
                other.zMin <= zMin && other.zMax >= zMax;
    }

    public boolean valid() {
        return xMin <= xMax && yMin <= yMax && zMin <= zMax;
    }

    public String toString() {
        return id;
    }
}
