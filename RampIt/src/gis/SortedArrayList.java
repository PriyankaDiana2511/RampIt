package gis;

import java.util.ArrayList;
import java.util.Comparator;

@SuppressWarnings("serial")
public class SortedArrayList<T> extends ArrayList<T> {
	private Comparator<T> comparator;

	public SortedArrayList(Comparator<T> c) {
		super();
		comparator = c;
	}

	@Override
	public boolean add(T arg0) {
		int index;
		if (size() == 0) {
			index = 0;
		} else {
			index = findIndex(arg0, 0, size());
		}
		add(index, arg0);
		T obj = get(index);
		return arg0.equals(obj);
	}

	private int findIndex(T obj, int min, int max) {
		if (min + 1 >= max) {
			T o2 = get(min);
			int cmpr = comparator.compare(obj, o2);
			if (cmpr < 0) {
				return min;
			} else {
				return max;
			}
		}
		int m = (min + max) / 2;
		T o2 = get(m);
		int cmpr = comparator.compare(obj, o2);
		if (cmpr < 0) {
			return findIndex(obj, min, m);
		} else if (cmpr == 0) {
			return m;
		} else {
			return findIndex(obj, m, max);
		}
	}

	@Override
	public int indexOf(Object arg0) {
		if (size() == 0) {
			return -1;
		} else {
			T f = get(0);
			@SuppressWarnings("unchecked")
			Class<T> c = (Class<T>) f.getClass();
			T k = c.cast(arg0);
			int index = indexOf(k, 0, size());
			if(index < 0||index >= size()){
				return -1;
			}
			T e = get(index);
			if (e.equals(k)) {
				return index;
			}
		}
		return -1;
	}
	private int indexOf(T obj, int min,int max){
		if (min + 1 >= max) {
			T o2 = get(min);
			int cmpr = comparator.compare(obj, o2);
			if (cmpr <= 0) {
				return min;
			} else {
				return max;
			}
		}
		int m = (min + max) / 2;
		T o2 = get(m);
		int cmpr = comparator.compare(obj, o2);
		if (cmpr < 0) {
			return indexOf(obj, min, m);
		} else if (cmpr == 0) {
			return m;
		} else {
			return indexOf(obj, m, max);
		}
	}
	@Override
	public boolean remove(Object arg0) {
		int index = indexOf(arg0);
		if (index >= 0) {
			remove(index);
			return true;
		}
		return false;
	}
}
