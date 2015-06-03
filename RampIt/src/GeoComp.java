import gis.Line;
import gis.Point;

public class GeoComp {
	public static Point Inter(Line l1, Line l2) {
		double x1 = l1.p1.getX();
		double x2 = l1.p2.getX();
		double x3 = l2.p1.getX();
		double x4 = l2.p2.getX();

		double y1 = l1.p1.getY();
		double y2 = l1.p2.getY();
		double y3 = l2.p1.getY();
		double y4 = l2.p2.getY();

		double rise1 = y2 - y1;
		double run1 = x2 - x1;
		double rise2 = y4 - y3;
		double run2 = x4 - x3;

		if (run1 == 0 && run2 == 0) {
			double min = Math.min(y1, y2);
			double max = Math.max(y1, y2);
			if (x1==x3 && min <= y3 && max >= y3) {
				return new Point(x3, y3);
			} else if (x1==x3 && min <= y4 && max >= y4) {
				return new Point(x1, y1);
			}
			return null;
		} else if (run1 == 0) {
			double m = rise2 / run2;
			double b = y3 - (m * x3);
			double y = (m * x1) + b;
			double min = Math.min(y1, y2);
			double max = Math.max(y2, y2);
			if (min <= y && max >= y) {
				return new Point(x1, y);
			}
			return null;
		} else if (run2 == 0) {
			double m = rise1 / run1;
			double b = y1 - (m * x1);
			double y = (m * x3) + b;
			double min = Math.min(y3, y4);
			double max = Math.max(y3, y4);
			if (min <= y && max >= y) {
				return new Point(x3, y);
			}
			return null;
		}

		double m1 = rise1 / run1;
		double m2 = rise2 / run2;
		double b1 = y1 - (m1 * x1);
		double b2 = y3 - (m2 * x3);
		if (m1 == m2) {
			if(b1 != b2){
				return null;
			}
			double min = Math.min(y1, y2);
			double max = Math.max(y1, y2);
			if (min <= y3 && max >= y3) {
				return new Point(x3, y3);
			} else if (min <= y4 && max >= y4) {
				return new Point(x1, y1);
			}
			return null;
		}
		double x = (b2-b1)/(m1-m2);
		double ya= Math.round(m1*x+b1);
		double yb = Math.round(m2*x+b2);
		if(ya != yb){
			System.out.print("Err:"+ya+" "+yb);
			return null;
		}
		return new Point(x,y1);
		
	}
}