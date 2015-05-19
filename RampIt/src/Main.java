import gis.RTree;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Main {
	public static void main(String[] args) {
			RTree t = new RTree(5, 2);
			try {
				ArrayList<Segment> map = RoadParser.parseMap("streets.txt");
				int tLines = 0;
				for(Segment s : map){
					ArrayList<Line> lines = s.getLines();
					tLines += lines.size();
					for(Line l : lines){
						t.insert(l.boundingBox(), l);
					}
				}
				//MapFrame gui = new MapFrame(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		/*
		try {
			ArrayList<Segment> trace = RoadParser.parseMap("streets.txt");
			//ArrayList<Segment> m = segmentMerge(trace);
			//System.out.println(trace.size());
			MapFrame gui = new MapFrame(trace);

			/*
			 * ArrayList<Segment> map = new ArrayList<Segment>();
			 * ArrayList<Segment> streets = RoadParser.parseMap("streets.txt");
			 * map.addAll(streets); ArrayList<Segment> streets =
			 * RoadParser.parseMap("streets.txt"); map.addAll(streets);
			 * ArrayList<Segment> streets = RoadParser.parseMap("streets.txt");
			 * map.addAll(streets);
			 * 
			 * MapFrame gui = new MapFrame(map);
			 *
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	private static ArrayList<Segment> segmentMerge(ArrayList<Segment> segments){
		PriorityQueue<Segment> s = new PriorityQueue<Segment>();
		ArrayList<Segment> st = new ArrayList<Segment>();
		s.addAll(segments);
		while(!s.isEmpty()){
			Segment s1 = s.poll();
			Segment s2 = s.peek();
			if(s1.merge(s2)){
				s.poll();
				s.add(s1);
			}else{
				st.add(s1);
			}
		}
		//st.add(s.poll());
		return st;
	}
}
