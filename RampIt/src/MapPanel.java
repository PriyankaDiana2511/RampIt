import gis.Point;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MapPanel extends JPanel{
	private int mapHeight;
	private int mapWidth;
	public static int MAX_ZOOM = 100;
	public static int MIN_ZOOM = 1;
	private int zoom = 1;
	private int horizontalOffset = 10;
	private int verticalOffset = 10; 
	private ArrayList<Segment> segments;

	public MapPanel() {
		segments = new ArrayList<Segment>();
	}

	public void addSegment(Segment s) {
		segments.add(s);
		ArrayList<Point> points = s.getPoints();
		for(Point p: points){
			if(p.getX() > mapWidth){
				mapWidth = (int) p.getX();
			}
			
			if(p.getY() > mapHeight){
				mapHeight = (int)p.getY();
			}
		}
		repaint();
	}

	public void zoomIn() {
		if (zoom < 100) {
			zoom++;
		}
		repaint();
	}
	public void zoomOut() {
		if (zoom > 1) {
			zoom--;
		}
		repaint();
	}
	public void shift(int x,int y){
		if(x+horizontalOffset <= 10 && x+horizontalOffset+(mapWidth*zoom) > this.getWidth()-10){
			horizontalOffset += x;
		}
		if(y+verticalOffset <= 10 && y+verticalOffset+(mapHeight*zoom) > this.getHeight()-10){
			verticalOffset += y;
		}
		repaint();
	}
	public void paintRoadSegment(Graphics g, Segment s){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.GRAY);
		ArrayList<Point> points = s.getPoints();
		int n = s.getPoints().size();
		int[] x = new int[n];
		int[] y = new int[n];
		for (int i = 0; i < points.size(); i++) {
			Point p1 = points.get(i);
			int x1 = (int) (p1.getX() * zoom)+horizontalOffset;
			int y1 = (int) p1.getY() * zoom+verticalOffset;
			int stroke = Math.max(zoom/2, 1);
			x[i] = x1;
			y[i]=y1;
		}
		g2.drawPolygon(x, y, n);
	}
	public void paintWalkSegment(Graphics g, Segment s){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.decode("16753510"));
		ArrayList<Point> points = s.getPoints();
		int n = s.getPoints().size();
		int[] x = new int[n];
		int[] y = new int[n];
		for (int i = 0; i < points.size(); i++) {
			Point p1 = points.get(i);
			int x1 = (int) (p1.getX() * zoom)+horizontalOffset;
			int y1 = (int) p1.getY() * zoom+verticalOffset;
			int stroke = Math.max(zoom/2, 1);
			x[i] = x1;
			y[i]=y1;
		}
		g2.drawPolygon(x, y, n);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.decode("16315890"));
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		for (Segment s : segments) {
			if(s.getType() == SegmentType.Road){
				this.paintRoadSegment(g2, s);
			}else if(s.getType() == SegmentType.Sidewalk){
				this.paintWalkSegment(g2, s);
				
			}else if(s.getType() == SegmentType.Trace){
				
			}
		}
	}
}
