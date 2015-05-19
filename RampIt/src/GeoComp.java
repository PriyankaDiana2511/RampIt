import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class GeoComp {
	public static void main(String args[]) {
		Line l1 = new Line(1, 1, 1, 4);
		Line l2 = new Line(1, 3, 1, 7);
		Line l3 = new Line(2, 9, 7, 8);
		Line l4 = new Line(4, 4, 7, 7);
		Line l5 = new Line(5, 2, 5, 7);
		Line l6 = new Line(7, 2, 11, 4);
		Line l7 = new Line(8, 5, 10, 1);
		ArrayList<Line> lines = new ArrayList<Line>();
		 lines.add(l1);
		 lines.add(l2);
		lines.add(l3);
		lines.add(l4);
		 lines.add(l5);
		lines.add(l6);
		lines.add(l7);
		ArrayList<LinePoint> intrs = SegmentIntersection(lines);
		for (LinePoint i : intrs) {
		}

	}

	public static ArrayList<LinePoint> SegmentIntersection(ArrayList<Line> S) {
		PriorityQueue<LinePoint> e = EventList(S);
		ArrayList<LinePoint> intersections = new ArrayList<LinePoint>();
		TreeMap<Double, Line> L = new TreeMap<Double, Line>();
		while (!e.isEmpty()) {
			LinePoint p = e.poll();
			Line s = p.line;
			if (isLeft(p)) {
				L.put(Math.min(s.p1.getY(), s.p2.getY()), s);
				Line s1 = Above(L, s);
				Line s2 = Below(L, s);
				LinePoint intersection = Inter(s, s1);
				if (intersection != null) {
					e.add(intersection);
					intersections.add(intersection);
				}
				LinePoint intr2 = Inter(s, s2);
				if (intr2 != null) {
					e.add(intr2);
					intersections.add(intr2);
				}
			} if (isRight(p)) {
				L.remove(Math.min(s.p1.getY(),s.p2.getY()));
				Line s1 = Above(L, s);
				Line s2 = Below(L, s);
				if (s1 != null && s2 != null) {
					LinePoint intersection = Inter(s1, s2);
					if (intersection != null && intersection.getX() > p.getX()) {
						e.add(intersection);
						intersections.add(intersection);
					}
				}
			} 
			
			
			if (p.line != null && p.line2 != null) {
				Line s1 = p.line;
				Line s2 = p.line2;
				Line s3 = Above(L, Min(s1, s2));
				Line s4 = Below(L, Max(s1, s2));
				if (s3 != null) {
					LinePoint intersection1 = Inter(s3, Min(s1, s2));
					if (intersection1 != null) {
						e.add(intersection1);
					}
					LinePoint intersection2 = Inter(s2, s3);
					if (intersection2 != null) {
						e.add(intersection2);
					}
				}
				if (s4 != null) {
					LinePoint intersection1 = Inter(s4, Max(s1, s2));
					if (intersection1 != null) {
						e.add(intersection1);
					}
					LinePoint intersection2 = Inter(s1, s4);
					if (intersection2 != null) {
						e.add(intersection2);
					}
				}
				Swap(L, s1, s2);
			}
			
			
		}
		return intersections;
	}

	public static void Swap(TreeMap<Double, Line> L, Line s1, Line s2) {
		double index_a = Math.min(s1.p1.getY(), s1.p2.getY());
		double index_b = Math.min(s2.p1.getY(), s2.p2.getY());
		L.remove(index_a);
		L.remove(index_b);
		L.put(index_b, s1);
		L.put(index_a, s2);
	}

	public static Line Max(Line s1, Line s2) {
		double x1 = Math.min(s1.p1.getX(), s1.p2.getX());
		double x2 = Math.min(s2.p1.getX(), s2.p2.getX());
		if (x1 > x2) {
			return s1;
		} else {
			return s2;
		}
	}

	public static Line Min(Line s1, Line s2) {
		double x1 = Math.min(s1.p1.getX(), s1.p2.getX());
		double x2 = Math.min(s2.p1.getX(), s2.p2.getX());
		if (x1 < x2) {
			return s1;
		} else {
			return s2;
		}
	}

	public static LinePoint Inter(Line l1, Line l2) {
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
				LinePoint p = new LinePoint(l1, x1, y3);
				p.line2 = l2;
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
			LinePoint p = new LinePoint(l1, x1, y);
			p.line2 = l2;
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
			LinePoint p = new LinePoint(l1, x3, y);
			p.line2 = l2;
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
			LinePoint p = new LinePoint(l1, x, y);
			p.line2 = l2;
			return p;

		}
	}

	public static Line Above(TreeMap<Double, Line> L, Line l) {
		Double i = Math.min(l.p1.getY(), l.p2.getY());
		Entry<Double, Line> e = L.higherEntry(i);
		if (e != null) {
			return e.getValue();
		}
		return null;
	}

	public static Line Below(TreeMap<Double, Line> L, Line l) {
		Double i = Math.min(l.p1.getY(), l.p2.getY());
		Entry<Double, Line> e = L.lowerEntry(i);
		if (e != null) {
			return e.getValue();
		}
		return null;
	}

	public static boolean isLeft(LinePoint p) {
		Line l = p.line;
		double x1 = l.p1.getX();
		double x2 = l.p2.getX();
		if (p.getX() == Math.min(x1, x2)) {
			return true;
		}
		return false;
	}

	public static boolean isRight(LinePoint p) {
		Line l = p.line;
		double x1 = l.p1.getX();
		double x2 = l.p2.getX();
		if (p.getX() == Math.max(x1, x2)) {
			return true;
		}
		return false;
	}

	public static PriorityQueue<LinePoint> EventList(ArrayList<Line> S) {
		ArrayList<LinePoint> points = new ArrayList<LinePoint>();
		for (Line s : S) {
			points.add(s.p1);
			points.add(s.p2);
		}
		PriorityQueue<LinePoint> eList = new PriorityQueue<LinePoint>();
		eList.addAll(points);
		return eList;
	}
}