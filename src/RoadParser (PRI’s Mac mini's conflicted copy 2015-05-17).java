import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RoadParser {

	public static ArrayList<Segment> parseMap(String mapFile, SegmentType roadType) throws Exception {
		ArrayList<Segment> map = new ArrayList<Segment>();
		String mapString = contentsOfFile(mapFile);
		char[][] map2D = map2D(mapString);
		map = generateSegments(map2D,roadType);
		return map;
	}
	public static ArrayList<Segment> generateSegments(char[][] map, SegmentType type){
		int width = 0;
		int height = map.length;
		//Calculate Boundries
		for(int i = 0; i < map.length;i++){
			char[] d = map[i];
			if(d.length > width){
				width = d.length;
			}
			
		}
		
		
		ArrayList<Segment> array = new ArrayList<Segment>();
		//EW Scan
		for(int i = 0; i < height;i++){
			Segment s = new Segment(type);
			for(int j = 0 ; j < width;j++){
				char k = map[i][j];	
				if(k == '-'|| k == '+'){
					Point p = new Point(j,i);
					s.addPoint(p);
				}else{
					if(s.getPoints().size() > 1){
						array.add(s);
					}
					s = new Segment(type);
				}
			}
			if(s.getPoints().size() > 1){
				array.add(s);
			}
		}
	
		//NS Scan
		for(int i = 0; i < width;i++){
			Segment s = new Segment(type);
			for(int j = 0 ; j < height;j++){
				char k = map[j][i];	
				if(k == '|'|| k == '+'||k=='['||k==']'){
					Point p = new Point(i,j);
					s.addPoint(p);
				}else if(k == '.'){
					Point p = new Point(i,j);
				}else if(k=='['||k==']'){
					Point p = new Point(i,j);
					//w.addPoint(p);
					s.addPoint(p);
				}else{
					if(s.getPoints().size() > 1){
						array.add(s);
					}
					s = new Segment(type);
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
			Segment s = new Segment(type);
			while(y >= 0){
				if( x < width && y < height){
					char k = map[y][x];
					if(k == '/' || k == '+'){
						s.addPoint(new Point(x,y));
					}else{
						if(s.getPoints().size() > 1){
							array.add(s);
						}
						s = new Segment(type);
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
			Segment s = new Segment(type);
			while(x >= 0){
				if(y >= 0 && y < height){
					char k = map[y][x];
					if(k == '\\' || k == '+'){
						s.addPoint(new Point(x,y));
					}else{
						if(s.getPoints().size() > 1){
							array.add(s);
						}
						s = new Segment(type);
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
