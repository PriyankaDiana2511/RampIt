package gis;

public class Entry<I,O> {
	private I bounds;
	private O value;
	
	public Entry(I bounds, O value){
		this.bounds = bounds;
		this.value = value;
	}
	public O getValue(){
		return this.value;
	}
	public I getKey(){
		return this.bounds;
	}
}
