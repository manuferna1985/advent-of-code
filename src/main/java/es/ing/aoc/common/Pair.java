package es.ing.aoc.common;

import java.util.Objects;

public class Pair<A, B> {
    public A a;
    public B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    public static Pair<Integer, Integer> of (String pair, String sep){
        String[] parts = pair.split(sep);
        return new Pair<>(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(a, pair.a) && Objects.equals(b, pair.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "<" + a + ", " + b + ">";
    }
 }
