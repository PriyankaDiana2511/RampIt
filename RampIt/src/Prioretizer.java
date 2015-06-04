import gis.Entry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


public class Prioretizer {
	private class EntryComparator implements Comparator<Entry<Segment,Integer>>{
		@Override
		public int compare(Entry<Segment, Integer> o1,Entry<Segment, Integer> o2) {
			return o2.getValue()-o1.getValue();
		}
		
	}
	ArrayList<Entry<Segment,Integer>> entries;
	public Prioretizer(){
		entries = new ArrayList<Entry<Segment,Integer>>();
	}
	public void insertSegment(Segment s){
		Entry<Segment,Integer>  ke = new Entry<Segment,Integer>(s,1);
		if(entries.contains(ke)){
			int i =entries.indexOf(ke);	
			Entry<Segment,Integer>  e = entries.get(i);
			int c = e.getValue()+1;
			entries.remove(i);
			Entry<Segment,Integer>  e1 = new Entry<Segment,Integer>(s,c);
			entries.add(e1);
		}else{
			entries.add(ke);
		}
		
	}
	public void remove(Segment s){
		Entry<Segment,Integer>  ke = new Entry<Segment,Integer>(s,1);
		entries.remove(ke);
	}
	public PriorityQueue<Entry<Segment,Integer>> prioretyQueue(){
		EntryComparator c = new EntryComparator();
		PriorityQueue<Entry<Segment,Integer>> queue = new PriorityQueue<Entry<Segment,Integer>>(c);
		queue.addAll(this.entries);
		return queue;
	}
}
