package gis.graph;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.TreeSet;
public class Graph<T>
{
	private ArrayList<Vertex<T>> verticies;
	public Graph(){
		verticies = new ArrayList<Vertex<T>>();
	}
	public ArrayList<Vertex<T>> verticies(){
		return verticies;
	}
	public void addVertex(Vertex<T> v){
		this.verticies.add(v);
	}
    public static void computePaths(Vertex<?> source)
    {
        source.minDistance = 0.;
        PriorityQueue<Vertex<?>> vertexQueue = new PriorityQueue<Vertex<?>>();
      	vertexQueue.add(source);

	while (!vertexQueue.isEmpty()) {
	    Vertex<?> u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge e : u.adjacencies)
            {
                Vertex<?> v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
		if (distanceThroughU < v.minDistance) {
		    vertexQueue.remove(v);
		    v.minDistance = distanceThroughU ;
		    v.previous = u;
		    vertexQueue.add(v);
		}
            }
        }
    }
    public static List<Vertex<?>> getShortestPathTo(Vertex<?> target)
    {
        List<Vertex<?>> path = new ArrayList<Vertex<?>>();
        for (Vertex<?> vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
    public void computePaths(Object source){
    	for(Vertex<T> v: this.verticies){
    		Object o = v.value;
    		if(o.equals(source)){
    			computePaths(v);
    			break;
    		}
    	}
    	throw new IllegalArgumentException();
    }
    public List<Vertex<?>> getShortestDistance(Object target){
    	for(Vertex<T> v: this.verticies){
    		Object o = v.value;
    		if(o.equals(target)){
    			return getShortestDistance(v);
    		}
    	}
    	return null;
    }
}