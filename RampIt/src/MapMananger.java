import java.util.ArrayList;

import gis.RTree;
import gis.Point;
import gis.Rectangle;
import gis.Entry;
public class MapMananger {
	private RTree traces;
	private RTree sidewalks;
	private RTree streets;
	private RTree obsticles;
	
	public ArrayList<Entry<Point,Integer>> ramps(Rectangle region){
		int[] counts = {1,2,56,4,3};
		ArrayList<Entry<Point,Integer>> results= new ArrayList<Entry<Point,Integer>>();
		ArrayList<Point> traceList = new ArrayList<Point>();
		traceList.add(new Point(5,5));
		traceList.add(new Point(5,10));
		traceList.add(new Point(5,15));
		traceList.add(new Point(10,5));
		traceList.add(new Point(10,10));
		traceList.add(new Point(10,15));
		traceList.add(new Point(15,5));
		traceList.add(new Point(15,10));
		traceList.add(new Point(15,15));
		for(int i = 0; i < traceList.size();i++){
			int count = 0;
			if(i < counts.length){
				count = counts[i];
			}
			Entry<Point,Integer> e = new Entry<Point,Integer>(traceList.get(i),count);
			results.add(e);
		}
		return results;
	}
	
}
