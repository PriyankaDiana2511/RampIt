import gis.Line;
import gis.Point;

import java.util.ArrayList;

public class Segment{
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
		return points.toString();
	}
	
	public Segment copy(){
		Segment c = new Segment(getType());
		ArrayList<Point> pts = getPoints();
		for(Point p :pts){
			Point pc = new Point(p.getX(),p.getY());
			c.addPoint(pc);
		}
		return c;
	}
	public ArrayList<Line> getLines(){
		ArrayList<Line>  lines=  new ArrayList<Line>();
		Line l = null;
		for(int i = 1; i < this.points.size();i++){
			Point p1 = points.get(i-1);
			Point p2 = points.get(i);
			if(l == null){
				l = new Line(p1,p2);
			}else{
				Line k = new Line(l.p1,p2);
				//Line k2 = new Line(l.p2,p2);
				if(k.pointOnLine(p1)){
					l = new Line(l.p1,p2);
				}else{ 
					Line c = new Line(l.p1,l.p2);
					lines.add(c);
					l = new Line(p1,p2);
				}
			}
			if(i == points.size()-1){
				lines.add(l);
			}
		}
		return lines;
	}
}
