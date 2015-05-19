package gis;

public class Rectangle {
	private double x;
	private double y;
	private double width;
	private double height;

	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException();
		}
		this.width = width;
		this.height = height;
	}

	public double area() {
		return width * height;
	}

	public double parameter() {
		return (2 * width) + (2 * height);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public static double areaEnlargement(Rectangle a, Rectangle b) {
		double x1 = a.getX();
		double x2 = x1 + a.getWidth();
		double y1 = a.getY();
		double y2 = a.getY() + a.getHeight();
		double area = a.area();

		double x3 = b.getX();
		double x4 = b.getX() + b.getWidth();
		double y3 = b.getY();
		double y4 = b.getY() + b.getHeight();

		double minx = Math.min(x1, x3);
		double maxx = Math.max(x2, x4);
		double nwidth = maxx - minx;

		double miny = Math.min(y1, y3);
		double maxy = Math.max(y2, y4);
		double nheight = maxy - miny;
		return Math.abs((nwidth * nheight) - area);
	}

	public static Rectangle merge(Rectangle r1, Rectangle r2) {
		if (r1 == null && r2 == null) {
			return null;
		} else if (r1 == null) {
			return r2;
		} else if (r2 == null) {
			return r1;
		} else {
			double x1 = r1.getX();
			double x2 = r1.getX() + r1.getWidth();
			double y1 = r1.getY();
			double y2 = r1.getY() + r1.getHeight();

			double x3 = r2.getX();
			double x4 = r2.getX() + r2.getWidth();
			double y3 = r2.getY();
			double y4 = r2.getY() + r2.getHeight();

			double minx = Math.min(x1, x3);
			double maxx = Math.max(x2, x4);
			double miny = Math.min(y1, y3);
			double maxy = Math.max(y2, y4);

			double width = maxx - minx;
			double height = maxy - miny;
			Rectangle r = new Rectangle(minx, miny, width, height);
			return r;
		}
	}
	@Override
	public String toString(){
		Point p = new Point(x,y);
		Point p2 = new Point (width,height);
		return p.toString()+" "+p2.toString();
	}
}
