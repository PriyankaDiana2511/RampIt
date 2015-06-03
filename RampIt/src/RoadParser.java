import gis.Line;
import gis.Point;
import gis.graph.Edge;
import gis.graph.Graph;
import gis.graph.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RoadParser {

	public static ArrayList<Segment> parseMap(String file) throws Exception {
		ArrayList<Segment> map = new ArrayList<Segment>();
		String mapString = contentsOfFile(file);
		char[][] map2D = map2D(mapString);
		map = generateSegments(map2D);
		return map;
	}

	public static Graph<Point> generateGraph(String file) throws Exception {
		Graph<Point> g = new Graph<Point>();
		ArrayList<Segment> segments = parseMap(file);
		int width = 0;
		int height = 0;
		//Caculate Size
		for (Segment s : segments) {
			ArrayList<Point> points = s.getPoints();
			for (Point p : points) {
				if (p.getX() > width) {
					width = (int) p.getX();
				}
				if (p.getY() > height) {
					height = (int) p.getY();
				}
			}
		}
		
		// Initiate Vertices
		Vertex<?> vertecies[][] = new Vertex<?>[height + 1][width + 1];
		for (Segment s : segments) {
			ArrayList<Line> lines = s.getLines();
			for (Line l : lines) {
				Point p1 = l.p1;
				Point p2 = l.p2;
				Vertex<?> h1 = vertecies[(int) p1.getY()][(int) p1.getX()];
				if (h1 == null) {
					Vertex<Point> v1 = new Vertex<Point>(p1);
					vertecies[(int) p1.getY()][(int) p1.getX()] = v1;
					g.addVertex(v1);
				}
				
				
				Vertex<?> h2 = vertecies[(int) p2.getY()][(int) p2.getX()];
				if (h2 == null) {
					Vertex<Point> v2 = new Vertex<Point>(p2);
					vertecies[(int) p2.getY()][(int) p2.getX()] = v2;
					g.addVertex(v2);
				}
			}
		}

		// Add Edges
		for (Segment s : segments) {
			ArrayList<Line> lines = s.getLines();
			for (Line l : lines) {
				Point p1 = l.p1;
				Point p2 = l.p2;
				Vertex<?> v1 = vertecies[(int) p1.getY()][(int) p1.getX()];
				Vertex<?> v2 = vertecies[(int) p2.getY()][(int) p2.getX()];
				Edge e1 = new Edge(v2, p1.distance(p2));
				Edge e2 = new Edge(v1, p1.distance(p2));
				v1.adjacencies.add(e1);
				v2.adjacencies.add(e2);
			}
		}
		return g;
	}
	private static ArrayList<Segment> generateSegments(char[][] map){
		int width = 0;
		int height = map.length;
		for (int i = 0; i < map.length; i++) {
			char[] d = map[i];
			if (d.length > width) {
				width = d.length;
			}

		}

		ArrayList<Segment> array = new ArrayList<Segment>();
		// EW Scan
		for (int i = 0; i < height; i++) {
			Segment s = new Segment(SegmentType.Road);
			for (int j = 0; j < width; j++) {
				char k = map[i][j];
				Point p = new Point(j, i);
				if (k == '-' || k == '+') {
					s.addPoint(p);
				}
				if(k !='-'){
					if (s.getPoints().size() > 1) {
						array.add(s.copy());
					}
					s = new Segment(SegmentType.Road);
				}
				if(k == '+'){
					s.addPoint(p);
				}
			}
			if (s.getPoints().size() > 1) {
				array.add(s.copy());
			}
		}

		// NS Scan
		for (int i = 0; i < width; i++) {
			Segment s = new Segment(SegmentType.Road);
			for (int j = 0; j < height; j++) {
				char k = map[j][i];
				Point p = new Point(i, j);
				if (k == '|'|| k == '+') {
					s.addPoint(p);
				}
				
				if(k !='|'){
					if (s.getPoints().size() > 1) {
						array.add(s.copy());
					}
					s = new Segment(SegmentType.Road);
				}
				if(k == '+'){
					s.addPoint(p);
				}
			}
			if (s.getPoints().size() > 1) {
				array.add(s.copy());
			}
		}
		// SE
		int max = width + height;
		for (int i = 0; i < max; i++) {
			int x = 0;
			int y = i;
			Segment s = new Segment(SegmentType.Road);
			while (y >= 0) {
				Point p = new Point(x,y);
				if (x < width && y < height) {
					char k = map[y][x];
					if (k == '/' || k == '+') {
						s.addPoint(p);
					}
					if(k !='/'){
						if (s.getPoints().size() > 1) {
							array.add(s);
						}
						s = new Segment(SegmentType.Road);
					}
					if(k == '+'){
						s.addPoint(p);
					}
				}
				x++;
				y--;
			}
			if (s.getPoints().size() > 1) {
				array.add(s);
			}
		}
		// SW
		for (int i = 0; i < max; i++) {
			int x = width - 1;
			int y = i;
			Segment s = new Segment(SegmentType.Road);
			while (x >= 0) {
				Point p = new Point(x,y);
				if (y >= 0 && y < height) {
					char k = map[y][x];
					if (k == '\\' || k == '+') {
						s.addPoint(p);
					}
					if(k !='\\'){
						if (s.getPoints().size() > 1) {
							array.add(s);
						}
						s = new Segment(SegmentType.Road);
					}
					if(k == '+'){
						s.addPoint(p);
					}
				}
				x--;
				y--;
			}
			if (s.getPoints().size() > 1) {
				array.add(s);
			}
		}
		return array;
		
	}
	private static char[][] map2D(String mapString) {
		String[] mapRows = mapString.split("\n");
		int width = 0;
		int height = mapRows.length;
		// Calculate Boundaries
		for (int y = 0; y < height; y++) {
			String row = mapRows[y];
			if (row.length() > width) {
				width = row.length();
			}
		}
		char[][] map2D = new char[height][width];
		for (int y = 0; y < height; y++) {
			String row = mapRows[y];
			for (int x = 0; x < width; x++) {
				char curr = ' ';
				if (x < row.length()) {
					curr = row.charAt(x);
				}
				map2D[y][x] = curr;
			}
		}
		return map2D;
	}

	private static String contentsOfFile(String file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();

		while (line != null) {
			sb.append(line);
			sb.append("\n");
			line = br.readLine();
		}
		br.close();
		return sb.toString();
	}
}
