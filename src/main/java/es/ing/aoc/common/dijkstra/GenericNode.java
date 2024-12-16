package es.ing.aoc.common.dijkstra;

import java.util.Comparator;

// Node class
public record GenericNode<T>(T id, int cost) implements Comparator<GenericNode<T>> {

    @Override
    public int compare(GenericNode<T> node1, GenericNode<T> node2) {
        return Integer.compare(node1.cost, node2.cost);
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null || getClass()!=o.getClass()) return false;

        GenericNode node1 = (GenericNode) o;

        return id==node1.id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
