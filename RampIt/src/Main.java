import java.util.ArrayList;


public class Main {
	public static void main(String[] args) {
		try {
			ArrayList<Segment> map = RoadParser.parseMap("streets.txt");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
