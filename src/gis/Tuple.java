package gis;

public class Tuple<A, B> {
	private A element1;
	private B element2;

	public Tuple(A e1, B e2) {
		element1 = e1;
		element2 = e2;
	}
	public A getFirstElement(){
		return element1;
	}
	public B getSecondElement(){
		return element2;
	}
}
