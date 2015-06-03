package gis.graph;

public class Edge
{
    public final Vertex<?> target;
    public final double weight;
    public Edge(Vertex<?> argTarget, double argWeight)
    { target = argTarget; weight = argWeight; }
    @Override
    public String toString(){
    	return "---"+weight+"--->"+target;
    }
}