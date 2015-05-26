import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import gis.graph.Edge;
import gis.graph.Graph;
import gis.graph.Vertex;
import gis.Entry;
import gis.Point;
import gis.SortedArrayList;
import gis.Tuple;
class PointComparator implements Comparator<Tuple<Point,Point>>{
	@Override
	public int compare(Tuple<Point, Point> o1, Tuple<Point, Point> o2) {
		Point arg0 = o1.getFirstElement();
		Point arg1 = o2.getFirstElement();
		if(arg0.getX() != arg1.getX()){
			return (int) (arg0.getX()-arg1.getX());
		}
		if(arg0.getY() != arg1.getY()){
			return (int) (arg0.getY()-arg1.getY());
		}		
		arg0 = o1.getSecondElement();
		arg1 = o2.getSecondElement();
		if(arg0.getX() != arg1.getX()){
			return (int) (arg0.getX()-arg1.getX());
		}
		if(arg0.getY() != arg1.getY()){
			return (int) (arg0.getY()-arg1.getY());
		}
		return 0;
	}
	
}

class VertexComparator implements Comparator<Entry<Point,Vertex<Point>>>{

	@Override
	public int compare(Entry<Point, Vertex<Point>> o1, Entry<Point, Vertex<Point>> o2) {
		Point arg0 = o1.getKey();
		Point arg1 = o2.getKey();
		if(arg0.getX() == arg1.getX()){
			return (int) (arg0.getY()-arg1.getY());
		}
		return (int) (arg0.getX()-arg1.getX());
	}
	
}
public class SegmentAnalyzer {
	private double threashhold;
	private ArrayList<Segment> map;
	private Point prv;
	private Graph<Point> graph;
	
	
	public SegmentAnalyzer(double th, ArrayList<Segment> m){
		this.threashhold = th;
		this.map = m;
	}
	public static Segment toSegment(List<Vertex<?>> arg0){
		Segment s = new Segment(SegmentType.Route);
		for(Vertex<?>v: arg0){
			Object val = v.value;
			if(val instanceof Point){
				Point p = (Point)val;
				s.addPoint(p);
			}
		}
		return s;
	}
	public static Graph<Point> generateGraph(ArrayList<Segment> segments){
		Graph<Point> g = new Graph<Point>();
		SortedArrayList<Tuple<Point,Point>> pts = new SortedArrayList<Tuple<Point,Point>>(new PointComparator());
		SortedArrayList<Entry<Point,Vertex<Point>>> vmap = new SortedArrayList<Entry<Point,Vertex<Point>>>(new VertexComparator());
		for(Segment s :segments){
			ArrayList<Line> lines = s.getLines();
			for(Line l :lines ){
				Tuple<Point,Point> p1= new Tuple<Point,Point>(l.p1,l.p2);
				Tuple<Point,Point> p2= new Tuple<Point,Point>(l.p2,l.p1);
				pts.add(p1);
				pts.add(p2);
			}
		}
		int j = 0;
		while(j < pts.size()){
			int count = 0;
			Tuple<Point,Point> t = pts.get(j);
			Point c = t.getFirstElement();
			int i = j;
			
			while(i < pts.size()){
				Point d = pts.get(i).getFirstElement();
				if(d.equals(c)){
					count++;	
					i++;
				}else{
					break;
				}
			}
			int k = 0;
			Vertex<Point> v1 = new Vertex<Point>(c);
			Entry<Point,Vertex<Point>> e1 = new Entry<Point,Vertex<Point>>(c,v1);
			int ix1 = vmap.indexOf(e1);
			if(ix1  == -1){
				vmap.add(e1);
				g.addVertex(v1);
			}else{
				e1 = vmap.get(ix1);
				v1 = e1.getValue();
			}
			v1.adjacencies = new Edge[count];
			while(k < count){
				Tuple<Point,Point> t1 = pts.get(j);
				Point c2 = t1.getSecondElement();
				Vertex<Point> v2 = new Vertex<Point>(c2);
				Entry<Point,Vertex<Point>> e2 = new Entry<Point,Vertex<Point>>(c2,v2);
				int ix2 = vmap.indexOf(e2);
				if(ix2  == -1){
					vmap.add(e2);
					g.addVertex(v2);
				}else{
					e2 = vmap.get(ix2);
					v2 = e2.getValue();
				}
				v1.adjacencies[k] = new Edge(v2,c.distance(c2));
				j++;
				k++;
			}
		}
		return g;
		
	}
	public boolean needsRamp(Segment trace){
		double d1 = trace.distance();
		graph = generateGraph(map);
		prv = trace.getPoints().get(0);
		graph.computePaths(prv);
		ArrayList<Point> points  = trace.getPoints();
		int n = points.size();
		List<Vertex<?>> res = graph.getShortestDistance(trace.getPoints().get(n-1));
		Segment s = toSegment(res);
		if(s.distance()*this.threashhold >= d1){
			return true;
		}else{
			return false;
		}
	}
}
