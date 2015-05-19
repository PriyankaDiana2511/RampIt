import gis.Point;

import java.util.ArrayList;

public class Segment implements Comparable<Segment> {
	private ArrayList<Point> points;
	private SegmentType type;

	public Segment(SegmentType t) {
		points = new ArrayList<Point>();
		this.type = t;
	}

	public void addPoint(Point p) {
		points.add(p);
	}

	public ArrayList<Point> getPoints() {
		return this.points;
	}

	public SegmentType getType() {
		return this.type;
	}

	public void setSegmentType(SegmentType t) {
		this.type = t;
	}

	public double distance() {
		double totalDistance = 0;
		for (int i = 1; i < points.size(); i++) {
			Point p1 = points.get(i - 1);
			Point p2 = points.get(i);
			double currDistance = p1.distance(p2);
			totalDistance += currDistance;
		}
		return totalDistance;
	}

	@Override
	public String toString() {
		return points.get(0).toString();
	}

	public boolean merge(Segment o) {
		if(o == null){
			return false;
		}
		ArrayList<Point> o_points = o.getPoints();
		if (!points.isEmpty() && !o_points.isEmpty()) {
			Point p1 = this.points.get(points.size() - 1);
			Point p2 = o_points.get(0);
			if (p1.equals(p2)) {
				points.addAll(o_points);
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(Segment arg0) {
		if (points.isEmpty() && arg0.getPoints().isEmpty()) {
			return 0;
		} else if (points.isEmpty()) {
			return -1;
		} else if (arg0.getPoints().isEmpty()) {
			return 1;
		} else {
			Point p1 = points.get(0);
			Point p2 = arg0.getPoints().get(0);
			double x1 = p1.getX();
			double x2 =p2.getX();
			if (x1 == x2) {
				double y1 = p1.getY();
				double y2 = p2.getY();
				return (int) (y1 - y2);
			}
			return (int) (x1-x2);
		}
	}
	public double slope(Point p1, Point p2){
		if(p1.equals(p2)){
			return 0;
		}else{
			double x1 = p1.getX();
			double x2 = p2.getX();
			double y1 = p1.getY();
			double y2 = p2.getY();
			double rise = y2-y1;
			double run = x2-x1;
			return rise/run;
		}
	}
	
	public ArrayList<Line> getLines(){
		ArrayList<Line>  lines=  new ArrayList<Line>();
		Point p=null;
		Point e= null;	
		boolean inf = false;
		double slope = 0; //0=still, 1=right, 2= up, 3=down,4=left
		for(int i = 0;i<points.size();i++){
			Point c = points.get(i);
			if(p == null){
				p = points.get(i);
			}else if(e == null){
				e = points.get(i);
				slope = slope(p,e);
				if(p.getX() == e.getX()){
					inf = true;
				}
			}else if(slope(p,c)==slope){
				e = p;
			}else if(c.getX() == p.getY() && inf){
				e = c;
			}else{
				Line l = new Line(p,e);
				lines.add(l);
				p =e;
				e = c;
				inf = false;
			}
			if( i == points.size()-1){
				Line l = new Line(p,e);
				lines.add(l);
			}
		}
		
		return lines;
	}
}
