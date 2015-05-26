package gis.graph;

public class Vertex<T> implements Comparable<Vertex<T>>
{
    public final T value;
    public Edge[] adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex<?> previous;
    public Vertex(T value) { this.value = value; }
    public String toString() { return this.value.toString(); }
    public int compareTo(Vertex<T> other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}