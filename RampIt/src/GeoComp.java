import gis.Entry;
import gis.Point;
import gis.RTree;
import gis.Rectangle;
import gis.SortedArrayList;
import gis.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class GeoComp {
	private static class EntryComparator implements
			Comparator<Entry<Point, Tuple<Line, Line>>> {

		@Override
		public int compare(Entry<Point, Tuple<Line, Line>> o1,Entry<Point, Tuple<Line, Line>> o2) {
			Point arg0 = o1.getKey();
			Point arg1 = o2.getKey();
			if (arg0.getX() == arg1.getX()) {
				return (int) (arg0.getY() - arg1.getY());
			} else {
				return (int) (arg0.getX() - arg1.getX());
			}
		}
	}
	
	private static class LineComparator implements Comparator<Line>{
		public double x=0;
		@Override
		public int compare(Line o1, Line o2) {
			double x1 = o1.p1.getX();
			double x2 = o1.p2.getX();
			double x3 = o2.p1.getX();
			double x4 = o2.p2.getX();
			
			double y1 = o1.p1.getY();
			double y2 = o1.p2.getY();
			double y3 = o2.p1.getY();
			double y4 = o2.p2.getY();
			
			double rise1 = y2-y1;
			double run1 = x2-x1;
			double rise2 = y4-y3;
			double run2 = x4-x3;
			if(run1 == 0 && run2 == 0){
				return (int) (x1-x3);
			}else if(run1==0){
				return (int) (x1-x);
			}else if(run2 == 0){
				return (int) (x3-x);
			}
			double m1 = rise1/run1;
			double m2 = rise2/run2;
			double b1 = y1-(m1*x1);
			double b2 = y3-(m2*x3);
			
			double val1 = (m1*x)+b1;
			double val2 = (m2*x+b2);
			return (int) (val1-val2);
		}

		
	}
	
	public static void main(String args[]) {
		LineComparator lc = new LineComparator();
		lc.x = 4;
		SortedArrayList<Line> lines = new SortedArrayList<Line>(lc);
		lines.add(new Line(0,3,6,3));
		lines.add(new Line(0,0,6,6));
		lines.add(new Line(0,6,6,0));
		//System.out.println(lines);
	}

	private static PriorityQueue<Entry<Point, Tuple<Line, Line>>> eventList(ArrayList<Line> S) {
		PriorityQueue<Entry<Point, Tuple<Line, Line>>> points = new PriorityQueue<Entry<Point, Tuple<Line, Line>>>(new EntryComparator());
		for(Line s :S){
			Tuple<Line, Line> t = new Tuple<Line,Line>(s,null);
			Entry<Point,Tuple<Line,Line>> e1 = new Entry<Point,Tuple<Line,Line>>(s.p1,t);
			Entry<Point,Tuple<Line,Line>> e2 = new Entry<Point,Tuple<Line,Line>>(s.p2,t);
			points.add(e1);
			points.add(e2);
		}
		return points;
	}

	public static ArrayList<Entry<Point, Tuple<Line, Line>>> SegmentIntersection(ArrayList<Line> S) {
		PriorityQueue<Entry<Point, Tuple<Line, Line>>> e = eventList(S);
		ArrayList<Entry<Point, Tuple<Line, Line>>> intersections = new ArrayList<Entry<Point, Tuple<Line, Line>>>();
		LineComparator lc = new LineComparator();
		SortedArrayList<Line> L = new SortedArrayList<Line>(lc);
		while(!e.isEmpty()){
			Entry<Point,Tuple<Line,Line>> ce = e.poll();
			Point p = ce.getKey(); 
			if(left(ce)){
				Line s = ce.getValue().getFirstElement();
				L.add(s);
				Line s1 = Above(L,s);
				Line s2 = Below(L,s);
				Point intr1 = Inter(s,s1);
				Point intr2 = Inter(s,s2);
				lc.x = p.getX();
				if(intr1 != null){
					Tuple<Line,Line> t = new Tuple<Line,Line>(s,s1);
					Entry<Point, Tuple<Line,Line>> tp = new Entry<Point, Tuple<Line,Line>>(intr1,t);
					e.add(tp);
				}
				if(intr2 != null){
					Tuple<Line,Line> t = new Tuple<Line,Line>(s,s2);
					Entry<Point, Tuple<Line,Line>> tp = new Entry<Point, Tuple<Line,Line>>(intr2,t);
					e.add(tp);
				}
			}else if(right(ce)){
				Line s = ce.getValue().getFirstElement();
				Line s1 = Above(L,s);
				Line s2 = Below(L,s);
				Point intr = Inter(s1,s2);
				if(intr != null){
					Tuple<Line,Line> t = new Tuple<Line,Line>(s1,s2);
					Entry<Point, Tuple<Line,Line>> tp = new Entry<Point, Tuple<Line,Line>>(intr,t);
					e.add(tp);
					L.remove(s);
				}
			}else if(intersection(ce)){
				intersections.add(ce);
				double p1 = p.getX()+1;
				Line s1 = ce.getValue().getFirstElement();
				Line s2 = ce.getValue().getSecondElement();
				Line s3 = Above(L,Max(p1,s1,s2));
				Line s4 = Below(L,Min(p1,s1,s2));
				Point intr1 = Inter(s3,Min(p1,s1,s2));
				Point intr2 = Inter(s4,Max(p1,s1,s2));
				Point intr3 = Inter(s3,Max(p1,s1,s2));
				if(intr1 != null){
					Tuple<Line,Line> t = new Tuple<Line,Line>(s3,Min(p1,s1,s2));
					Entry<Point, Tuple<Line,Line>> tp = new Entry<Point, Tuple<Line,Line>>(intr1,t);
					e.add(tp);
				}
				if(intr2 != null){
					Tuple<Line,Line> t = new Tuple<Line,Line>(s1,Max(p1,s1,s2));
					Entry<Point, Tuple<Line,Line>> tp = new Entry<Point, Tuple<Line,Line>>(intr3,t);
					e.add(tp);
				}
				Swap(L,s1,s2);
			}
		}
		return intersections;
	}
	private static  Line Min(double x,Line s1, Line s2){
		LineComparator c1 = new LineComparator();
		c1.x = x;
		int cmpr = c1.compare(s1, s2);
		if(cmpr < 0){
			return s1;
		}else{
			return s2;
		}
	}
	private static Line Max(double x,Line s1, Line s2){
		LineComparator c1 = new LineComparator();
		c1.x = x;
		int cmpr = c1.compare(s1, s2);
		if(cmpr > 0){
			return s1;
		}else{
			return s2;
		}
	}
	private static void Swap(SortedArrayList<Line> L,Line s1,Line s2){
		boolean k1 =L.remove(s1);
		boolean k2 = L.remove(s2);
		//if(k1){
			L.add(s1);
		//}else if(k2){
			L.add(s2);
		//}
	}
	private static boolean left(Entry<Point, Tuple<Line, Line>> e){
		Line s = e.getValue().getFirstElement();
		Point p = e.getKey();
		if(s.p1.equals(p)){
			return true;
		}
		return false;
	}
	private static boolean right(Entry<Point, Tuple<Line, Line>> e){
		Line s = e.getValue().getFirstElement();
		Point p = e.getKey();
		if(s.p2.equals(p)){
			return true;
		}
		return false;
	}
	private static boolean intersection(Entry<Point, Tuple<Line, Line>> e){
			Line s1 = e.getValue().getFirstElement();
			Line s2 = e.getValue().getSecondElement();
			if(s2 != null){
				Point p = Inter(s1,s2);
				if(p.equals(e.getKey())){
					return true;
				}
			}
			return false;
	}
	public static Point Inter(Line l1, Line l2) {
		if (l1 == null || l2 == null) {
			return null;
		}
		double x1 = l1.p1.getX();
		double x2 = l1.p2.getX();
		double x3 = l2.p1.getX();
		double x4 = l2.p2.getX();
		double y1 = l1.p1.getY();
		double y2 = l1.p2.getY();
		double y3 = l2.p1.getY();
		double y4 = l2.p2.getY();
		if (x1 == x2 && x3 == x4 && x1 == x3) {
			if (y3 < y2) {
				Point p = new Point(x1, y3);
				return p;
			}
			return null;
		} else if (x1 == x2) {
			double rise = y4 - y3;
			double run = x4 - x3;
			double m = rise / run;
			double b = y3 - (m * x3);
			double y = m * x1 + b;
			if (y < y1 || y > y2) {
				return null;
			}
			Point p = new Point(x1, y);
			return p;
		} else if (x3 == x4) {
			double rise = y2 - y1;
			double run = x2 - x1;
			double m = rise / run;
			double b = y1 - (m * x1);
			double y = m * x3 + b;
			if (y < y3 || y > y4) {
				return null;
			}
			Point p = new Point(x3, y);
			return p;
		} else {
			double rise1 = y2 - y1;
			double run1 = x2 - x1;
			double m1 = rise1 / run1;
			double b1 = y1 - (m1 * x1);

			double rise2 = y4 - y3;
			double run2 = x4 - x3;
			double m2 = rise2 / run2;
			double b2 = y3 - (m2 * x3);
			if (m1 == m2) {
				return null;
			}
			double x = (b2 - b1) / (m1 - m2);
			double y = (m1 * x) + b1;
			double min1 = Math.min(x1, x2);
			double min2 = Math.min(x3, x4);
			double maxmin = Math.max(min1, min2);
			double max1 = Math.max(x1, x2);
			double max2 = Math.max(x3, x4);
			double minmax = Math.min(max1, max2);
			if (x < maxmin || x > minmax) {
				return null;
			}
			Point p = new Point(x, y);
			return p;

		}
	}
	public static Line Above(SortedArrayList<Line> L, Line l) {
		int index  = L.indexOf(l);
		if(index > 0 && index < L.size()-2){
			return L.get(index+1);
		}
		return null;
	}
	public static Line Below(SortedArrayList<Line> L, Line l) {
		int index  = L.indexOf(l);
		if(index > 0){
			return L.get(index-1);
		}
		return null;
	}
}