import java.util.ArrayList;
import java.util.List;

import gis.graph.Edge;
import gis.graph.Graph;
import gis.graph.Vertex;
import gis.Entry;
import gis.Line;
import gis.Point;
import gis.RTree;
import gis.Rectangle;

public class MapMananger {
	private Graph<Point> graph;
	private RTree mapTree;

	public MapMananger(double th, ArrayList<Segment> m) {
		mapTree = new RTree(10, 4);
		for (Segment s : m) {
			ArrayList<Line> lines = s.getLines();
			for (Line l : lines) {
				mapTree.insert(l.boundingBox(), l);
			}
		}
	}

	public static Segment toSegment(List<Vertex<?>> arg0) {
		Segment s = new Segment(SegmentType.Route);
		for (Vertex<?> v : arg0) {
			Object val = v.value;
			if (val instanceof Point) {
				Point p = (Point) val;
				s.addPoint(p);
			}
		}
		return s;
	}
	public boolean checkVertex(Point p) {
		boolean valid = false;
		if (!graph.containsVetex(p)) {
			ArrayList<Entry<Rectangle, Object>> res = mapTree.search(p.boundingBox());
			for (Entry<Rectangle, Object> e : res) {
				Object o = e.getValue();
				if (o instanceof Line) {
					Line l = (Line) o;
					if (l.pointOnLine(p)) {
						Vertex<Point> v = new Vertex<Point>(p);
						Point p1 = l.p1;
						Point p2 = l.p2;
						Vertex<Point> v1 = graph.vertex(p1);
						Vertex<Point> v2 = graph.vertex(p2);
						insertVertex(v, v1, v2);
						graph.insterVertex(v);
					}
				}
			}
		}
		return valid;
	}

	public void insertVertex(Vertex<Point> v, Vertex<Point> v1, Vertex<Point> v2) {
		for (int i = 0; i < v1.adjacencies.size(); i++) {
			Edge e = v1.adjacencies.get(i);
			Vertex<?> ve = e.target;
			Object k = ve.value;
			if (k.equals(v2.value)) {
				Point p = v.value;
				Point p1 = v1.value;
				Point p2 = v2.value;
				v1.adjacencies.remove(i);
				v1.adjacencies.add(new Edge(v, p1.distance(p)));
				v.adjacencies.add(new Edge(v2, p.distance(p2)));
				break;
			}
		}
		for (int i = 0; i < v2.adjacencies.size(); i++) {
			Edge e = v2.adjacencies.get(i);
			Vertex<?> ve = e.target;
			Object k = ve.value;
			if (k.equals(v1.value)) {
				Point p = v.value;
				Point p1 = v2.value;
				Point p2 = v1.value;
				v2.adjacencies.remove(i);
				v2.adjacencies.add(new Edge(v, p1.distance(p)));
				v.adjacencies.add(new Edge(v1, p.distance(p2)));
				break;
			}
		}
	}
	public static ArrayList<Segment> Bypass(Segment trace, double th){
		//Create a list of all corner points
		ArrayList<Line> lines = trace.getLines();
		ArrayList<Point> points = new ArrayList<Point>();
		int k = 0;
		for(Line l :lines){
			points.add(l.p1);
			if(k == lines.size()-1){
				points.add(l.p2);
			}
			k++;
		}
		ArrayList<Segment> s = new ArrayList<Segment>();
		for(int i = 3; i < points.size();i++){
			Point p1 = points.get(i-3);
			Point p2 = points.get(i-2);
			Point p3 = points.get(i-1);
			Point p4 = points.get(i);
			Line l2 = new Line(p2,p3);
			Line l3 = new Line(p3,p4);
			Line l4 = new Line(p1,p4);
			Point itr2 = GeoComp.Inter(l4,l2);
			Point itr3 = GeoComp.Inter(l4,l3);
			if(itr3!= null && (itr3.equals(l3.p1) || itr3.equals(l3.p2)) && !itr3.equals(l2.p1) && !itr3.equals(l2.p2)){
				itr3 = null;
			}
			if(itr2 == null && itr3 == null){
				Segment sg = new Segment(SegmentType.Warning);
				sg.addPoint(p1);
				sg.addPoint(p2);
				sg.addPoint(p3);
				sg.addPoint(p4);
				double shortest = p1.distance(p2);
				double d = sg.distance();
				if(d*th > shortest){
					s.add(sg);	
				}
			}
		}
		return s;
	}
}
