import java.util.ArrayList;

import gis.Line;
import gis.RTree;
import gis.Point;
import gis.Rectangle;
import gis.Entry;
public class MapMananger {
	private RTree traces;
	private RTree sidewalks;
	private RTree streets;
	private RTree obsticles;
	
	public ArrayList<Entry<Point,Integer>> ramps(Rectangle region){
		int[] counts = {1,2,56,4,3};
		ArrayList<Entry<Point,Integer>> results= new ArrayList<Entry<Point,Integer>>();
		ArrayList<Point> traceList = new ArrayList<Point>();
		traceList.add(new Point(5,5));
		traceList.add(new Point(5,10));
		traceList.add(new Point(5,15));
		traceList.add(new Point(10,5));
		traceList.add(new Point(10,10));
		traceList.add(new Point(10,15));
		traceList.add(new Point(15,5));
		traceList.add(new Point(15,10));
		traceList.add(new Point(15,15));
		for(int i = 0; i < traceList.size();i++){
			int count = 0;
			if(i < counts.length){
				count = counts[i];
			}
			Entry<Point,Integer> e = new Entry<Point,Integer>(traceList.get(i),count);
			results.add(e);
		}
		return results;
	}
	
	public static boolean isShortestPath(Segment s){
		Point p1 = s.getPoints().get(0);
		Point p2 = s.getPoints().get(s.getPoints().size()-1);
		double sd = p1.distance(p2);
		double d = s.distance();
		if(d == sd){
			return true;
		}
		return false;
	}
	public Segment shortestPath(Segment s){
		return shortestPath(s,0,s.getPoints().size());
	}
	
	public Segment shortestPath(Segment s){
		ArrayList<Point> points = s.getPoints();
		if(points.size() <= 2){
			Segment cpy = new Segment(s.getType());
			for(Point p :points){
				Point k = new Point(p.getX(),p.getY());
				cpy.addPoint(k);
			}
			return cpy;
		}else{
			int n = points.size()/2;
			
		}
	}
	
	public static void main(String [] args){
		Line l1 = new Line(0,0,0,100);
		Line l2 = new Line(0,100,100,100);
		Line l3 = new Line(100,100,100,0);
		Line l4 = new Line(100,0,0,0);
		Segment s1 = new Segment(SegmentType.Sidewalk);
		s1.addPoint(l1.p1);
		s1.addPoint(l1.p2);
		Segment s2 = new Segment(SegmentType.Sidewalk);
		s2.addPoint(l2.p1);
		s2.addPoint(l2.p2);
		Segment s3 = new Segment(SegmentType.Sidewalk);
		s3.addPoint(l3.p1);
		s3.addPoint(l3.p2);
		Segment s4 = new Segment(SegmentType.Sidewalk);
		s4.addPoint(l4.p1);
		s4.addPoint(l4.p2);
		
		Segment s5 = new Segment(SegmentType.Road);
		s5.addPoint(new Point(50,0));
		s5.addPoint(new Point(50,100));
		Segment s6 = new Segment(SegmentType.Road);
		s6.addPoint(new Point(0,50));
		s6.addPoint(new Point(100,50));
		
		Segment s7 = new Segment(SegmentType.Trace);
		s7.addPoint(new Point(0,0));
		s7.addPoint(new Point(0,100));
		s7.addPoint(new Point(100,100));
		//s7.addPoint(new Point(100,0));
		ArrayList<Segment> map = new ArrayList<Segment>();
		
		map.add(s1);
		map.add(s2);
		map.add(s3);
		map.add(s4);
		
		map.add(s5);
		map.add(s6);
		
		map.add(s7);
		System.out.print(isShortestPath(s7));
		MapFrame f = new MapFrame(map);
	}
}
