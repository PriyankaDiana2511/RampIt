public class LinePoint extends Point {
	public Line line;
	public Line line2;
	public LinePoint(Line l, double x, double y) {
		super(x, y);
		line = l;
	}
}