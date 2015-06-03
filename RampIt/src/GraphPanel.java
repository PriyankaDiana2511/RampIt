import gis.Line;
import gis.Point;
import gis.graph.Edge;
import gis.graph.Graph;
import gis.graph.Vertex;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel{
	private Graph<Point> graph;
	private ArrayList<Segment> traces;
	int zoom;
	int horizontalOffset;
	int verticalOffset;
	double mapWidth;
	double mapHeight;
	public GraphPanel() {
		graph = new Graph<Point>();
		traces = new ArrayList<Segment>();
		zoom = 1;
		horizontalOffset= 0;
		verticalOffset = 0;
		ArrayList<Vertex<Point>> vts = graph.verticies();
		for(Vertex<Point> v : vts){
			Point p = v.value;
			if(p.getX() > mapWidth){
				mapWidth = p.getX();
			}
			if(p.getY() > mapHeight){
				mapHeight = p.getY();
			}
		}
	}
	public void addTrace(Segment s){
		this.traces.add(s);
		this.repaint();
	}
	public void setGgraph(Graph<Point> g){
		graph = g;
		zoom = 1;
		horizontalOffset= 0;
		verticalOffset = 0;
		ArrayList<Vertex<Point>> vts = graph.verticies();
		for(Vertex<Point> v : vts){
			Point p = v.value;
			if(p.getX() > mapWidth){
				mapWidth = p.getX();
			}
			if(p.getY() > mapHeight){
				mapHeight = p.getY();
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
	public int zoom(){
		return zoom;
	}
	public int horizontal(){
		return this.horizontalOffset;
	}
	public int verticalOffset(){
		return this.horizontalOffset;
	}
	public Graph<Point> graph(){
		return graph;
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.DARK_GRAY);
		ArrayList<Vertex<Point>> vts = graph.verticies();
		for(Vertex<Point> v : vts){
			Point p1 = v.value;
			int x1 = (int)p1.getX()*zoom + this.horizontalOffset;
			int y1 = (int)p1.getY()*zoom + this.verticalOffset;
 			ArrayList<Edge> adjs = v.adjacencies;
 			for(Edge e : adjs){
 				Vertex<?> v2 = e.target;
 				Point p2 = (Point) v2.value;
 				int x2 = (int)p2.getX()*zoom + this.horizontalOffset;
 				int y2 = (int)p2.getY()*zoom + this.verticalOffset;
 				g2.drawLine(x1, y1, x2, y2);
 			}
		}
		for(Segment s :this.traces){
			if(s.getType() == SegmentType.Trace){
				g2.setColor(Color.ORANGE);
			}else if(s.getType() == SegmentType.Warning){
				g2.setColor(Color.RED);
			}
			ArrayList<Line> lines = s.getLines();
			for(Line l :lines){
				Point p1 = l.p1;
				Point p2 = l.p2;
				int x1 = (int)p1.getX()*zoom + this.horizontalOffset;
				int y1 = (int)p1.getY()*zoom + this.verticalOffset;
				int x2 = (int)p2.getX()*zoom + this.horizontalOffset;
				int y2 = (int)p2.getY()*zoom + this.verticalOffset;
				g2.drawLine(x1, y1, x2, y2);
			}
		}
	}
}
