
public class Line implements Comparable<Line> {
	public LinePoint p1;
	public LinePoint p2;
	public Line(Point p1, Point p2){
		this.p1 = new LinePoint(this,p1.getX(),p1.getY());
		this.p2 = new LinePoint(this, p2.getX(),p2.getY());
	}
	public Line(int x1, int y1, int x2, int y2){
		this.p1 = new LinePoint(this,x1,y1);
		this.p2 = new LinePoint(this,x2,y2);
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof Line){
			Line oLine = (Line)o;
			if(oLine.p1.equals(p1) && oLine.p2.equals(p2)){
				return true;
			}else if(oLine.p1.equals(p2) && oLine.p2.equals(p1)){
				return true;
			}
		}
		return false;
	}
	@Override
	public int compareTo(Line o) {
		double x1 = Math.min(p1.getX(), p2.getX());
		double x2 = Math.min(o.p1.getX(), o.p2.getX());
		if(x1 == x2){
			double y1 = Math.min(p1.getY(), p2.getY());
			double y2 = Math.min(o.p1.getY(), o.p2.getY());
			return (int)(y1-y2);
		}
		return (int)(x1-x2);
	}
}
