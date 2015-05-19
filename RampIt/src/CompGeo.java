import java.util.ArrayList;
import gis.Point;
public class CompGeo {
	public static void main(String []args){
		Line l = new Line(0,1,5,5);
		Line l2 = new Line(2,5,5,6);
		ArrayList<Line> lines = new ArrayList<Line>();
		lines.add(l);
		lines.add(l2);
		SegmentIntersectionTest(lines);
	}

	public static boolean SegmentIntersectionTest(ArrayList<Line> S) {
		ArrayList<LinePoint> e = EventList(S);
		ArrayList<Line> L = new ArrayList<Line>();
		while (e.size() > 0) {
			LinePoint p = e.get(0);
			Line l = p.line;
			Line s1 = Above(L, l);
			Line s2 = Below(L, l);
			Line s3 = null;
			Line s4 = null;
			e.remove(0);
			if (p.equals(new Point(l.p1.getX(), l.p1.getY()))) {
				Insert(L, l);
				if (s1 != null) {
					LinePoint intr = inter(l, s1);
					if (intr != null) {
						Insert(e, intr);
					}
				}
				if (s2 != null) {
					LinePoint intr = inter(l, s2);
					if (intr != null) {
						Insert(e, intr);
					}
				}
			}
			
			if(p.equals(new Point(l.p2.getX(),l.p2.getY()))){
				L.remove(l);
				if(s1 != null && s2 != null){
					LinePoint intr = inter(s1,s2);
					if(l.p1.getX() < intr.getX()){
						Insert(e,intr);
					}
				}
			}
			LinePoint intr = inter(s1,s2);
			if(p.equals(intr)){
				s2 = Above();
			}
			
		}
		return false;
	}

	private static LinePoint inter(Line s1, Line s2) {
		double x = 0;
		double y = 0;
		double x1 = s1.getX1();
		double x2 = s1.getX2();
		double y1 = s1.getY1();
		double y2 = s1.getY2();
		double x3 = s2.getX1();
		double x4 = s2.getX2();
		double y3 = s2.getY1();
		double y4 = s2.getY2();
		if (x1 == x2 && x3 == x4 && x1 == x3) {
			if (y3 <= y2) {
				return new LinePoint(x1, y3, s1);
			}
		} else if (x1 == x2) {
			double rise = y4 - y3;
			double run = x4 - x3;
			double m = rise / run;
			double b = y3 - (x3 * m);
			y = m * x1 + b;
			if (y < y1 || y > y2) {
				return null;
			}
			return new LinePoint(x1, y, s1);
		}
		double rise1 = y2 - y1;
		double run1 = x2 - x1;
		double m1 = rise1 / run1;
		double b1 = y1 - (x1 * m1);
		double rise2 = y4 - y3;
		double run2 = x4 - x3;
		double m2 = rise2 / run2;
		double b2 = y3 - (x3 * m2);
		if (m1 == m2) {
			return null;
		}
		x = b2;
		x = x - b1;
		x = x / (m1 - m2);
		y = (m1 * x) + b1;

		double max1 = Math.max(x1, x2);
		double max2 = Math.max(x3, x4);
		double min = Math.min(max1, max2);

		double min1 = Math.min(x1, x2);
		double min2 = Math.min(x3, x4);
		double max = Math.max(min1, min2);
		if (x < max || x > min) {
			return null;
		}

		return new LinePoint(x, y, s1);

	}

	private static ArrayList<LinePoint> EventList(ArrayList<Line> S) {
		int n = S.size();
		LinePoint[] endpoints = new LinePoint[2 * n];
		// Add data to list
		for (int i = 0; i < n; i++) {
			int j = i * 2;
			Line l = S.get(i);
			LinePoint p = new LinePoint(l.getX1(), l.getY1(), l);
			endpoints[j] = p;
			LinePoint p2 = new LinePoint(l.getX2(), l.getY2(), l);
			endpoints[j + 1] = p2;
		}
		LinePoint[] ep = mergeSort(endpoints);
		ArrayList<LinePoint> endpointList = new ArrayList<LinePoint>();
		for (int i = 0; i < ep.length; i++) {
			endpointList.add(ep[i]);
		}
		return endpointList;
	}

	private static LinePoint[] mergeSort(LinePoint[] points) {
		if (points.length == 1) {
			return copy(points, 0, points.length);
		} else if (points.length == 2) {
			LinePoint p1 = points[0];
			LinePoint p2 = points[1];
			LinePoint a1[] = new LinePoint[2];
			if (p1.getX() <= p2.getX()) {
				a1[0] = new LinePoint(p1.getX(), p1.getY(), p1.line);
				a1[1] = new LinePoint(p2.getX(), p2.getY(), p2.line);
			} else {
				a1[0] = new LinePoint(p2.getX(), p2.getY(), p2.line);
				a1[1] = new LinePoint(p1.getX(), p1.getY(), p1.line);
			}
			return a1;
		} else {
			int n = points.length;
			int m = n / 2;
			LinePoint[] c1 = copy(points, 0, m);
			LinePoint[] c2 = copy(points, m, n);
			LinePoint[] a1 = mergeSort(c1);
			LinePoint[] a2 = mergeSort(c2);
			return merge(a1, a2);
		}
	}

	private static LinePoint[] merge(LinePoint[] p1, LinePoint[] p2) {
		int i = 0;
		int k = 0;
		int m = p1.length;
		int n = p2.length;
		LinePoint mrg[] = new LinePoint[m + n];
		int c = 0;
		while (i < m && k < n) {
			LinePoint q1 = p1[i];
			LinePoint q2 = p2[k];
			if (q1.getX() <= q2.getX()) {
				mrg[c] = q1;
				i++;
			} else {
				mrg[c] = q2;
				k++;
			}
			c++;
		}
		while (i < m) {
			LinePoint q1 = p1[i];
			mrg[c] = q1;
			c++;
			i++;
		}
		while (k < n) {
			LinePoint q1 = p2[k];
			mrg[c] = q1;
			c++;
			k++;
		}
		return mrg;
	}

	private static LinePoint[] copy(LinePoint[] points, int i, int j) {
		int n = Math.abs(j - i);
		LinePoint copy[] = new LinePoint[n];
		int cnt = 0;
		for (int k = i; k < j; k++) {
			LinePoint p = points[k];
			LinePoint c = new LinePoint(p.getX(), p.getY(), p.line);
			copy[cnt] = c;
			cnt++;
		}
		return copy;
	}

	private static void Insert(ArrayList<Line> L, Line l) {
		if (L.size() < 1) {
			L.add(l);
		} else {
			Line o = L.get(0);
			int i = 0;
			while (i < L.size() && o.getY1() < l.getY1()) {
				o = L.get(i);
				i++;
			}
			L.add(i, l);
		}
	}

	private static void Insert(ArrayList<LinePoint> L, LinePoint l) {
		if (L.size() < 1) {
			L.add(l);
		} else {
			LinePoint o = L.get(0);
			int i = 0;
			while (i < L.size() && o.getY() < l.getY()) {
				o = L.get(i);
				i++;
			}
			L.add(i, l);
		}
	}

	private static Line Above(ArrayList<Line> L, Line l) {
		int i = L.indexOf(l);
		if (i == 0) {
			return null;
		} else {
			return L.get(i - 1);
		}
	}

	private static Line Below(ArrayList<Line> L, Line l) {
		int i = L.indexOf(l);
		if (i == 0) {
			return L.get(1);
		} else if (i == L.size() - 1) {
			return null;
		} else {
			return L.get(i - 1);
		}
	}
}

class LinePoint extends Point {
	public Line line;

	public LinePoint(double x, double y, Line l) {
		super(x, y);
		this.line = l;
	}
}