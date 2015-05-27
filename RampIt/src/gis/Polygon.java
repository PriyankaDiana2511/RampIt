package gis;

import java.util.ArrayList;

public class Polygon {
	ArrayList<Line> edges;

	public Polygon(ArrayList<Line> edges) {
		if(!isValidPolygon(edges)){
			throw new IllegalArgumentException();
		}
		this.edges = edges;
	}
	private static boolean isValidPolygon(ArrayList<Line> lines){
		if(lines.size() < 3){
			return false;
		}
		for(int i = 0; i < lines.size();i++ ){
			Line next;
			Line curr;
			if(i == lines.size()-1){
				next = lines.get(0);
			}else{
				next = lines.get(i+1);
			}
			curr = lines.get(i);
			if(!curr.connects(next)){
				return false;
			}
		}
		return true;
	}
}
