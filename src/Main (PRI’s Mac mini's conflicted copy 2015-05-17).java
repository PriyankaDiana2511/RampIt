import java.io.IOException;
import java.util.ArrayList;
public class Main{
	public static void main(String[]args){
			ArrayList<Segment> streets;
			ArrayList<Segment> walks;
			ArrayList<Segment> walks2;
			ArrayList<Segment> map;
			ArrayList<Segment> trace;
			try {
				streets = RoadParser.parseMap("streets.txt",SegmentType.Road);
				walks = RoadParser.parseMap("sidewalks.txt", SegmentType.Sidewalk);
				walks2 = RoadParser.parseMap("sidewalks 2.txt", SegmentType.Sidewalk);
				trace = RoadParser.parseMap("trace.txt", SegmentType.Trace);
				map = new ArrayList<Segment>();
				map.addAll(streets);
				map.addAll(walks);
				map.addAll(walks2);
				map.addAll(trace);
				createGui(map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void createGui(ArrayList<Segment> map){
		MapFrame frame = new MapFrame(map);
	}
}
