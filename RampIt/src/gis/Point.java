package gis;
import java.util.Comparator;


public class Point implements Comparator<Point>,Comparable<Point>{
	private double myX;
	private double myY;
	
	public Point(double lat, double lng){
		myX = lat;
		myY = lng;
	}
	public double getX(){
		return myX;
	}
	public double getY(){
		return myY;
	}
	public double distance(Point p){
		double term_1 = p.getX()-myX;
		double term_2 = Math.pow(term_1, 2);
		double term_3 = p.getY() - myY;
		double term_4 = Math.pow(term_3,2);
		double term_5 = term_2+term_4;
		double result = Math.sqrt(term_5);
		return result;
	}
	@Override
	public String toString(){
		return "("+myX+", "+myY+")";
	}
	@Override
	public boolean equals(Object p){
		if(p instanceof Point){
			Point other = (Point)p;
			if(other.getX() == myX){
				if(other.getY() == myY){
					return true;
				}
			}
		}
		return false;
	}
	@Override
	public int compare(Point arg0, Point arg1) {
		double xd = arg1.getX()-arg0.getX();
		if(xd == 0){
			double yd = arg1.getY()-arg0.getY();
			return (int)yd;
		}
		return (int)xd;
	}
	@Override
	public int compareTo(Point o) {
		double xd = o.getX()-getX();
		if(xd == 0){
			double yd = o.getY()-getY();
			return (int)yd;
		}
		return (int)xd;
	}
}
