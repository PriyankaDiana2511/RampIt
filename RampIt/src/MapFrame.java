import gis.Point;
import gis.graph.Edge;
import gis.graph.Graph;
import gis.graph.Vertex;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MapFrame extends JFrame implements ActionListener, MouseListener,
		MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private MapPanel mapView;
	private GraphPanel graphPanel;
	private JButton zoomIn;
	private JButton zoomOut;
	private JButton trace;
	private JLabel pointA;
	private JLabel pointB;
	private JTextField traces;
	private JButton route;
	private Point p1;
	private Point p2;
	private Point sDrag;
	private Graph<Point> graph;

	public static void main(String[] args) {

		Graph<Point> g;
		try {
			g = RoadParser.generateGraph("sidewalks.txt");
			MapFrame f = new MapFrame();
			f.display(g);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public MapFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graphPanel = new GraphPanel();
		this.add(graphPanel, BorderLayout.CENTER);
		
		JPanel tools = new JPanel();
		zoomIn = new JButton("+");
		zoomIn.addActionListener(this);
		zoomOut = new JButton("-");
		zoomOut.addActionListener(this);
		this.traces = new JTextField(4);
		trace = new JButton("Simulate Trace");
		trace.addActionListener(this);
		tools.setLayout(new FlowLayout());
		tools.add(zoomIn);
		tools.add(zoomOut);
		tools.add(traces);
		tools.add(trace);
		add(tools, BorderLayout.NORTH);
		
		JPanel sideBar = new JPanel();
		this.pointA = new JLabel();
		this.pointB = new JLabel();
		route = new JButton("Route");
		route.addActionListener(this);
		route.setEnabled(false);
		sideBar.setLayout(new FlowLayout());
		sideBar.add(route);
		sideBar.add(this.pointA);
		sideBar.add(this.pointB);
		add(sideBar, BorderLayout.EAST);
		
		graphPanel.addMouseMotionListener(this);
		graphPanel.addMouseListener(this);
		this.setMinimumSize(new Dimension(600, 600));
		graphPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		sDrag = new Point(0, 0);
		pack();
		setVisible(true);
	}

	public static ArrayList<Segment> segments(Graph<Point> g) {
		ArrayList<Segment> sgs = new ArrayList<Segment>();

		ArrayList<Vertex<Point>> vertecies = g.verticies();
		for (Vertex<Point> v : vertecies) {
			Point p = v.value;
			ArrayList<Edge> adjs = v.adjacencies;
			for (Edge e : adjs) {
				Vertex<?> v1 = e.target;
				Point p2 = (Point) v1.value;
				Segment s = new Segment(SegmentType.Sidewalk);
				s.addPoint(p);
				s.addPoint(p2);
				sgs.add(s);
			}
		}
		return sgs;
	}

	public void display(Graph<Point> g) {
		this.graph = g;
		this.graphPanel.setGgraph(g);
	}

	public void actionPerformed(ActionEvent evt) {
		Object src = evt.getSource();
		if (src == zoomIn) {
			// mapView.zoomIn();
			graphPanel.zoomIn();
		} else if (src == zoomOut) {
			// mapView.zoomOut();
			graphPanel.zoomOut();
		} else if (src == trace) {
			String countString = this.traces.getText();
			int count = Integer.parseInt(countString);
			while(count > 0){
				this.simulateTrace(this.graphPanel.graph());
				count--;
			}
		}else if(src == route){
			this.route();
		}
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		int xOffset = (int) (arg0.getX() - sDrag.getX());
		int yOffset = (int) (arg0.getY() - sDrag.getY());
		sDrag = new Point(arg0.getX(), arg0.getY());
		// mapView.shift(xOffset,yOffset);
		graphPanel.shift(xOffset, yOffset);
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (graph != null) {
			int h = this.graphPanel.horizontalOffset;
			int v = this.graphPanel.verticalOffset;
			int z = this.graphPanel.zoom;
			double x = (e.getX() - h) / z;
			double y = (e.getY() - v) / z;
			Point p = new Point(x, y);
			if (this.p2 == null) {
				this.p2 = p;
				this.pointB.setText(p.toString());
				Vertex<Point> v1 = this.graph.vertex(this.p1);
				Vertex<Point> v2 = this.graph.vertex(this.p2);
				if(v1 != null && v2 != null){
					this.route.setEnabled(true);
				}
			} else {
				this.p1 = p;
				this.pointA.setText(p.toString());
				this.p2 = null;
				this.pointB.setText("");
				this.route.setEnabled(false);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		sDrag = new Point(e.getX(), e.getY());

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void route() {
		Vertex<Point> v1 = this.graph.vertex(this.p1);
		Vertex<Point> v2 = this.graph.vertex(this.p2);
		
		for (Vertex<Point> v : graph.verticies()) {
			v.minDistance = Double.POSITIVE_INFINITY;
			v.previous = null;
		}
		Graph.computePaths(v1);
		List<Vertex<?>> res = Graph.getShortestPathTo(v2);
		Segment s = new Segment(SegmentType.Trace);
		for (Vertex<?> v : res) {
			Point p = (Point) v.value;
			s.addPoint(p);
		}
		//this.graphPanel.addTrace(s);
		ArrayList<Segment> sgs = MapMananger.Bypass(s,1);
		for(Segment sg : sgs){
			this.graphPanel.addTrace(sg);
		}
	}
	public Segment simulateTrace(Graph<Point> g) {
		ArrayList<Vertex<Point>> vts = g.verticies();
		int n = vts.size();
		Random rnd = new Random();
		int i1 = rnd.nextInt(n);
		int i2 = rnd.nextInt(n);
		while (i2 == i1) {
			i2 = rnd.nextInt(n);
		}
		Vertex<Point> v1 = vts.get(i1);
		Vertex<Point> v2 = vts.get(i2);
		for (Vertex<Point> v : g.verticies()) {
			v.minDistance = Double.POSITIVE_INFINITY;
			v.previous = null;
		}
		Graph.computePaths(v1);
		List<Vertex<?>> res = Graph.getShortestPathTo(v2);
		Segment s = new Segment(SegmentType.Trace);
		for (Vertex<?> v : res) {
			Point p = (Point) v.value;
			s.addPoint(p);
		}
		//this.graphPanel.addTrace(s);
		ArrayList<Segment> sgs = MapMananger.Bypass(s,1);
		for(Segment sg : sgs){
			this.graphPanel.addTrace(sg);
		}
		return s;
	}
}
