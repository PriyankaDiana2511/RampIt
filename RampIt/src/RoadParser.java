import gis.Point;

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
	private static Segment generateTrace(char[][] traceArray) throws Exception {
		Segment s = new Segment(SegmentType.Trace);
		char[] row1 = traceArray[0];
		int width = row1.length;
		int height = traceArray.length;
		int x = 0;
		int y = 0;
		//Find an end point
		while(y< height){
			char c = traceArray[y][x];
			if(c == '*'){
				break;
			}
			x++;
			if(x == width){
				x = 0;
				y++;
			}
		}
		if(y == height){
			throw new Exception();
		}
		 
		
		return s;
	}
	private static ArrayList<Segment> generateSegments(char[][] map){
		int width = 0;
		int height = map.length;
		for(int i = 0; i < map.length;i++){
			char[] d = map[i];
			if(d.length > width){
				width = d.length;
			}
			
		}
		
		
		ArrayList<Segment> array = new ArrayList<Segment>();
		//EW Scan
		for(int i = 0; i < height;i++){
			Segment s = new Segment(SegmentType.Road);
			for(int j = 0 ; j < width;j++){
				char k = map[i][j];	
				if(k == '-'|| k == '+'){
					Point p = new Point(j,i);
					s.addPoint(p);
				}else{
					if(s.getPoints().size() > 1){
						array.add(s);
					}
					s = new Segment(SegmentType.Road);
				}
			}
			if(s.getPoints().size() > 1){
				array.add(s);
			}
		}
	
		//NS Scan
		for(int i = 0; i < width;i++){
			Segment s = new Segment(SegmentType.Road);
			for(int j = 0 ; j < height;j++){
				char k = map[j][i];	
				if(k == '|'|| k == '+'){
					Point p = new Point(i,j);
					s.addPoint(p);
				}else{
					if(s.getPoints().size() > 1){
						array.add(s);
					}
					s = new Segment(SegmentType.Road);
				}
			}
			if(s.getPoints().size() > 1){
				array.add(s);
			}
		}
		//SE
		int max = width+height;
		for(int i = 0; i < max; i++){
			int x = 0;
			int y = i;
			Segment s = new Segment(SegmentType.Road);
			while(y >= 0){
				if( x < width && y < height){
					char k = map[y][x];
					if(k == '/' || k == '+'){
						s.addPoint(new Point(x,y));
					}else{
						if(s.getPoints().size() > 1){
							array.add(s);
						}
						s = new Segment(SegmentType.Road);
					}
				}
				x++;
				y--;
			}
			if(s.getPoints().size() > 1){
				array.add(s);
			}
		}
		//SW
		for(int i = 0; i < max; i++){
			int x = width-1;
			int y = i;
			Segment s = new Segment(SegmentType.Road);
			while(x >= 0){
				if(y >= 0 && y < height){
					char k = map[y][x];
					if(k == '\\' || k == '+'){
						s.addPoint(new Point(x,y));
					}else{
						if(s.getPoints().size() > 1){
							array.add(s);
						}
						s = new Segment(SegmentType.Road);
					}
				}
				x--;
				y--;
			}
			if(s.getPoints().size() > 1){
				array.add(s);
			}
		}
		return array;
	}
	private static char[][] map2D(String mapString){
		String[] mapRows = mapString.split("\n");
		int width = 0;
		int height=mapRows.length;
		// Calculate Boundaries
		for(int y = 0; y < height;y++){
			String row = mapRows[y];
			if(row.length() > width){
				width = row.length();
			}
		}
		char[][] map2D = new char[height][width];
		for(int y = 0; y < height;y++){
			String row = mapRows[y];
			for(int x = 0; x < width;x++){
				char curr = ' ';
				if(x < row.length()){
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
