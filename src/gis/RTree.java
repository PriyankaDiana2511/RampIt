package gis;

import java.util.ArrayList;

public class RTree<E> {
	private Node root;
	private int M;
	private int m;

	public RTree(Node n, int M, int m) {
		this.M = M;
		this.m = m;
	}

	public void AdjustTree(Node N,Node NN){
		if(N.parent != null){
			Node P = N.parent;
			Rectangle I = N.bounds;
		}
	}
	private void adjustBounds(Node N){
		boolean first = true;
		int minx = -1;
		int miny = -1;
		int maxx = -1;
		int maxy = -2;
		if(N instanceof NNode){
			NNode nnode = (NNode)N;
			ArrayList<Node> entries = nnode.entries;
			for(Node n : entries){
				Rectangle b = n.bounds;
				
			}
		}else{
			
		}
	}
	public void insert(Rectangle bounds, E data) {
		Leaf<E> L = this.ChooseLeaf(bounds);
		ArrayList<Entry<Rectangle, E>> entries = L.entries;
		if (entries.size() < M) {
			L.entries.add(new Entry<Rectangle, E>(bounds, data));
		} else {
			Tuple<Leaf<E>, Leaf<E>> t = QuadSplit(entries);
			Leaf<E> LL = t.getSecondElement();
			L.entries.removeAll(LL.entries);
			LL.parent = L.parent;
		}
	}

	@SuppressWarnings("unchecked")
	private Leaf<E> ChooseLeaf(Rectangle i) {
		Node N = root;
		if (N instanceof RTree.Leaf) {
			Leaf<E> L = (Leaf<E>) N;
			return L;
		} else {
			while (N instanceof NNode) {
				NNode K = (NNode)N;
				N = chooseChild(K, i);
			}
			return (Leaf<E>) N;
		}
	}

	@SuppressWarnings("unchecked")
	private Node chooseChild(NNode n, Rectangle bounds) {
		Node F = null;
		double minAE = -1;
		double minArea = -1;
		ArrayList<Node> children = n.entries;
		for (Node q : children) {
			double ae = Rectangle.areaEnlargement(q.bounds, bounds);
			double area = q.bounds.area();
			if (F == null || minAE < ae) {
				F = q;
				minAE = ae;
				minArea = area;
			} else if (minAE == ae && area < minArea) {
				F = q;
				minArea = area;
			}
		}
		return F;
	}
	private Tuple<Entry<Rectangle, E>, Entry<Rectangle, E>> PickSeeds(
			ArrayList<Entry<Rectangle, E>> entries) {
		double max_d = 0;
		Entry<Rectangle, E> e1 = null;
		Entry<Rectangle, E> e2 = null;
		for (Entry<Rectangle, E> E1 : entries) {
			for (Entry<Rectangle, E> E2 : entries) {
				Rectangle r1 = E1.getBounds();
				Rectangle r2 = E2.getBounds();
				Rectangle j = Rectangle.merge(r1, r2);
				double a1 = r1.area();
				double a2 = r2.area();
				double a3 = j.area();
				double d = a3 - a1 - a2;
				if (d > max_d) {
					max_d = d;
					e1 = E1;
					e2 = E2;
				}
			}
		}
		return new Tuple<Entry<Rectangle, E>, Entry<Rectangle, E>>(e1, e2);
	}

	private Tuple<Leaf<E>, Leaf<E>> QuadSplit(ArrayList<Entry<Rectangle, E>> entries) {
		Tuple<Entry<Rectangle, E>, Entry<Rectangle, E>> seeds = PickSeeds(entries);
		ArrayList<Entry<Rectangle, E>> group1 = new ArrayList<Entry<Rectangle, E>>();
		ArrayList<Entry<Rectangle, E>> group2 = new ArrayList<Entry<Rectangle, E>>();
		Rectangle r1 = null;
		Rectangle r2 = null;
		Entry<Rectangle, E> e1 = seeds.getFirstElement();
		Entry<Rectangle, E> e2 = seeds.getSecondElement();
		group1.add(e1);
		group2.add(e2);
		r1 = e1.getBounds();
		r2 = e2.getBounds();
		ArrayList<Entry<Rectangle, E>> cpy = new ArrayList<Entry<Rectangle, E>>();
		cpy.addAll(entries);
		while (!cpy.isEmpty()) {
			if (group1.size() + entries.size() == m) {
				group1.addAll(entries);
				break;
			} else if (group2.size() + entries.size() == m) {
				group2.addAll(entries);
				break;
			} else {
				int next = PickNext(entries, r1, r2);
				Entry<Rectangle, E> e = entries.remove(next);
				double ae1 = 0;
				double a1 = 0;
				double ae2 = 0;
				double a2 = 0;
				if (r1 == null) {
					ae1 = e.getBounds().area();
				} else {
					ae1 = Rectangle.areaEnlargement(r1, e.getBounds());
					a1 = r1.area();
				}
				if (r2 == null) {
					ae2 = e.getBounds().area();
				} else {
					ae2 = Rectangle.areaEnlargement(r2, e.getBounds());
					a2 = r2.area();
				}

				if (ae1 < ae2) {
					group1.add(e);
					r1 = Rectangle.merge(r1, e.getBounds());
				} else if (ae2 < ae1) {
					group2.add(e);
					r2 = Rectangle.merge(e.getBounds(), r2);
				} else if (a1 < a2) {
					group1.add(e);
					r1 = Rectangle.merge(r1, e.getBounds());
				} else if (a2 < a1) {
					group2.add(e);
					r2 = Rectangle.merge(r2, e.getBounds());
				} else {
					group1.add(e);
					r1 = Rectangle.merge(r1, e.getBounds());
				}
			}
		}
		Leaf<E> L = new Leaf<E>(r1);
		L.entries.addAll(group1);
		Leaf<E> LL = new Leaf<E>(r2);
		LL.entries.addAll(group2);
		return new Tuple<Leaf<E>, Leaf<E>>(L, LL);
	}

	private int PickNext(ArrayList<Entry<Rectangle, E>> entries, Rectangle g1,
			Rectangle g2) {
		double max_d = 0;
		int max_element = -1;
		int i = 0;
		for (Entry<Rectangle, E> e : entries) {
			double d1 = 0;
			if (g1 == null) {
				d1 = e.getBounds().area();
			} else {
				d1 = Rectangle.areaEnlargement(g1, e.getBounds());
			}
			double d2 = 0;
			if (g2 == null) {
				d2 = e.getBounds().area();
			} else {
				d2 = Rectangle.areaEnlargement(g2, e.getBounds());
			}
			double d = Math.abs(d2 - d1);
			if (d > max_d) {
				max_d = d;
				max_element = i;
			}
			i++;
		}
		return max_element;
	}

	private static class Node {
		public Rectangle bounds;
		public Node parent;
	}

	private static class NNode extends Node {
		public ArrayList<Node> entries;
		public NNode parent;
		public NNode() {
			entries = new ArrayList<Node>();
		}
	}

	private static class Leaf<S> extends Node {
		public ArrayList<Entry<Rectangle, S>> entries;
		public NNode parent;
		public Leaf(Rectangle r) {
			bounds = r;
			entries = new ArrayList<Entry<Rectangle, S>>();
		}
	}
}
