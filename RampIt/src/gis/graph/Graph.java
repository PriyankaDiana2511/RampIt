package gis.graph;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Graph<T> {
	private ArrayList<Vertex<T>> verticies;

	public Graph() {
		verticies = new ArrayList<Vertex<T>>();
	}

	public ArrayList<Vertex<T>> verticies() {
		return verticies;
	}

	public void addVertex(Vertex<T> v) {
		this.verticies.add(v);
	}

	public static void computePaths(Vertex<?> source) {
		source.minDistance = 0.;
		PriorityQueue<Vertex<?>> vertexQueue = new PriorityQueue<Vertex<?>>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex<?> u = vertexQueue.poll();

			// Visit each edge exiting u
			for (Edge e : u.adjacencies) {
				Vertex<?> v = e.target;
				double weight = e.weight;
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);
					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
	}

	public static List<Vertex<?>> getShortestPathTo(Vertex<?> target) {
		List<Vertex<?>> path = new ArrayList<Vertex<?>>();
		for (Vertex<?> vertex = target; vertex != null; vertex = vertex.previous) {
			path.add(vertex);
		}
		Collections.reverse(path);
		return path;
	}

	public void computePaths(Object source) {
		for (Vertex<T> v : this.verticies) {
			v.minDistance = Double.POSITIVE_INFINITY;
			v.previous = null;
		}
		boolean computed = false;
		for (Vertex<T> v : this.verticies) {
			Object o = v.value;
			if (o.equals(source)) {
				computePaths(v);
				computed = true;
				break;
			}
		}
		if (!computed) {
			throw new IllegalArgumentException();
		}
	}

	public boolean containsVetex(Object o) {
		for (Vertex<?> v : this.verticies) {
			Object ov = v.value;
			if (o.equals(ov)) {
				return true;
			}
		}
		return false;
	}

	public void insterVertex(Vertex<T> v) {
		this.verticies.add(v);
	}

	public List<Vertex<?>> getShortestDistance(Object target) {
		for (Vertex<T> v : this.verticies) {
			Object o = v.value;
			if (o.equals(target)) {
				List<Vertex<?>> lines = Graph.getShortestPathTo(v);
				return lines;
			}
		}
		return null;
	}

	public Vertex<T> vertex(Object o) {
		for (Vertex<T> c : this.verticies) {
			Object val = c.value;
			if (val.equals(o)) {
				return c;
			}
		}
		return null;
	}
}