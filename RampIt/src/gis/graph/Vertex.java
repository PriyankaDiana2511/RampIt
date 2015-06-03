package gis.graph;

import java.util.ArrayList;

public class Vertex<T> implements Comparable<Vertex<T>> {
	public final T value;
	// public Edge[] adjacencies;
	public ArrayList<Edge> adjacencies;
	public double minDistance = Double.POSITIVE_INFINITY;
	public Vertex<?> previous;

	public Vertex(T value) {
		this.value = value;
		this.adjacencies = new ArrayList<Edge>();
	}

	public String toString() {
		return this.value.toString();
	}

	public int compareTo(Vertex<T> other) {
		return Double.compare(minDistance, other.minDistance);
	}
	
	@Override
	public boolean equals(Object o){
		return this.value.equals(o);
	}
}