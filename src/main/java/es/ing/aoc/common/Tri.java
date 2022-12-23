package es.ing.aoc.common;

import java.util.Objects;

public class Tri<A, B, C> {
    public A a;
    public B b;
    public C c;

    public Tri(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public static <A, B, C> Tri<A, B, C> of(A a, B b, C c) {
        return new Tri<>(a, b, c);
    }

    public boolean contains(Object o) {
        return a.equals(o) || b.equals(o) || c.equals(o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tri<?, ?, ?> tri = (Tri<?, ?, ?>) o;
        return Objects.equals(a, tri.a) && Objects.equals(b, tri.b) && Objects.equals(c, tri.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    public String toString() {
        return "<" + a + ", " + b + ", " + c + ">";
    }
}
