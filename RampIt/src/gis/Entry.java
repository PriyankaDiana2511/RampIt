package gis;

public class Entry<I,O> {
	private I key;
	private O value;
	
	public Entry(I bounds, O value){
		this.key = bounds;
		this.value = value;
	}
	public O getValue(){
		return this.value;
	}
	public I getKey(){
		return this.key;
	}
	
	@Override 
	public boolean equals(Object o){
		if(o instanceof Entry<?,?>){
			Object c = ((Entry<?,?>) o).getKey();
			return c.equals(key);
		}
		return false;
	}
}
