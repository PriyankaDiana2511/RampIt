package gis;

import java.util.ArrayList;

public class RTree {
	private Node root;
	private int M;
	private int m;

	public RTree(int M, int m) {
		this.M = M;
		this.m = m;
	}

	public ArrayList<Entry<Rectangle, Object>> search(Rectangle S) {
		return search(S, root);
	}

	public ArrayList<Entry<Rectangle, Object>> search(Rectangle S, Node T) {
		if (T.isLeaf()) {
			ArrayList<Entry<Rectangle, Object>> entries = T.entries;
			ArrayList<Entry<Rectangle,Object>> results = new ArrayList<Entry<Rectangle,Object>>();
			for (Entry<Rectangle, Object> e : entries) {
				if(Overlap(e.getBounds(),S)){
					results.add(e);
				}
			}
			return results;
		} else {
			ArrayList<Entry<Rectangle, Object>> entries = T.entries;
			ArrayList<Entry<Rectangle,Object>> results = new ArrayList<Entry<Rectangle,Object>>();
			for (Entry<Rectangle, Object> e : entries) {
				Node n = (Node)e.getValue();
				if(Overlap(n.bounds,S)){
					ArrayList<Entry<Rectangle,Object>> ents = search(S,n);
					results.addAll(ents);
				}
			}
			return results;
		}
	}

	private boolean Overlap(Rectangle r1, Rectangle r2) {
		double x1 = r1.getX();
		double x2 = r1.getX() + r1.getHeight();
		double x3 = r2.getX();
		double x4 = r2.getX() + r2.getHeight();

		double y1 = r1.getY();
		double y2 = r1.getY() + r1.getWidth();
		double y3 = r2.getY();
		double y4 = r2.getY() + r2.getHeight();
		if (x1 > x4 || x2 < x3) {
			return false;
		}
		if (y1 > y4 || y2 < y3) {
			return false;
		}

		return true;
	}

	public Tuple<Node, Node> AdjustTree(Node N, Node NN) {
		adjustBounds(N);
		if (N == root) {
			return new Tuple<Node, Node>(N, NN);
		}
		Node P = N.parent;
		if (NN != null) {
			adjustBounds(NN);
			if (P.entries.size() < M) {
				Entry<Rectangle, Object> E1 = new Entry<Rectangle, Object>(
						NN.bounds, NN);
				P.addEntry(E1);
			} else {
				Entry<Rectangle, Object> E1 = new Entry<Rectangle, Object>(
						NN.bounds, NN);
				P.addEntry(E1);
				Tuple<Node, Node> n = SplitTree(P);// QuadSplit(P.entries);
				Node PP = n.getSecondElement();
				return AdjustTree(P, PP);
			}
		}
		return new Tuple<Node, Node>(P, null);
	}

	private void adjustBounds(Node N) {
		boolean init = false;
		double minx = -1;
		double miny = -1;
		double maxx = -1;
		double maxy = -2;
		Node nnode = (Node) N;
		ArrayList<Entry<Rectangle, Object>> entries = nnode.entries;
		for (Entry<Rectangle, Object> e : entries) {
			Rectangle b = e.getBounds();
			if (!init) {
				minx = b.getX();
				miny = b.getY();
				maxx = b.getX() + b.getWidth();
				maxy = b.getY() + b.getHeight();
				init = true;
			}
			double x1 = b.getX();
			double y1 = b.getY();
			double x2 = b.getX() + b.getWidth();
			double y2 = b.getY() + b.getHeight();
			minx = Math.min(x1, minx);
			miny = Math.min(y1, miny);
			maxx = Math.max(x2, maxx);
			maxy = Math.max(y2, maxy);
		}
		if (init) {
			double width = Math.abs(maxx - minx);
			double height = Math.abs(maxy - miny);
			N.bounds = new Rectangle(minx, miny, width, height);
		}
	}

	public void insert(Rectangle bounds, Object data) {
		if (root == null) {
			Entry<Rectangle, Object> E1 = new Entry<Rectangle, Object>(bounds,
					data);
			root = new Node();
			root.addEntry(E1);
			AdjustTree(root, null);
		} else {
			Node L = ChooseLeaf(bounds);
			ArrayList<Entry<Rectangle, Object>> entries = L.entries;
			if (entries.size() < M) {
				L.addEntry(new Entry<Rectangle, Object>(bounds, data));
			} else {
				Tuple<Node, Node> t = SplitTree(L);
				Node LL = t.getSecondElement();
				Tuple<Node, Node> k = AdjustTree(L, LL);
				Node P = k.getFirstElement();
				Node PP = k.getSecondElement();
				if (PP != null) {
					Node n = new Node();
					Entry<Rectangle, Object> E1 = new Entry<Rectangle, Object>(P.bounds, P);
					n.addEntry(E1);
					
					Entry<Rectangle, Object> E2 = new Entry<Rectangle, Object>(PP.bounds, PP);
					n.addEntry(E2);
					
					root = n;
					adjustBounds(n);
				}
				insert(bounds,data);
			}
		}
	}

	private Node ChooseLeaf(Rectangle i) {
		Node N = root;
		if (N.isLeaf()) {
			return N;
		} else {
			while (!N.isLeaf()) {
				N = chooseChild(N, i);
			}
			return N;
		}
	}

	private Node chooseChild(Node n, Rectangle bounds) {
		Node F = null;
		double minAE = -1;
		double minArea = -1;
		ArrayList<Entry<Rectangle, Object>> children = n.entries;
		for (Entry<Rectangle, Object> c : children) {
			if (c.getValue() instanceof Node) {
				Node q = (Node) c.getValue();
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
		}
		return F;
	}

	private Tuple<Entry<Rectangle, Object>, Entry<Rectangle, Object>> PickSeeds(ArrayList<Entry<Rectangle, Object>> entries) {
		double max_d = 0;
		Entry<Rectangle, Object> e1 = null;
		Entry<Rectangle, Object> e2 = null;
		for (Entry<Rectangle, Object> E1 : entries) {
			for (Entry<Rectangle, Object> E2 : entries) {
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
		return new Tuple<Entry<Rectangle, Object>, Entry<Rectangle, Object>>(
				e1, e2);
	}

	private Tuple<ArrayList<Entry<Rectangle, Object>>, ArrayList<Entry<Rectangle, Object>>> QuadSplit(ArrayList<Entry<Rectangle, Object>> entries) {
		Tuple<Entry<Rectangle, Object>, Entry<Rectangle, Object>> seeds = PickSeeds(entries);
		ArrayList<Entry<Rectangle, Object>> group1 = new ArrayList<Entry<Rectangle, Object>>();
		ArrayList<Entry<Rectangle, Object>> group2 = new ArrayList<Entry<Rectangle, Object>>();
		Rectangle r1 = null;
		Rectangle r2 = null;
		Entry<Rectangle, Object> e1 = seeds.getFirstElement();
		Entry<Rectangle, Object> e2 = seeds.getSecondElement();
		group1.add(e1);
		group2.add(e2);
		r1 = e1.getBounds();
		r2 = e2.getBounds();
		ArrayList<Entry<Rectangle, Object>> cpy = new ArrayList<Entry<Rectangle, Object>>();
		cpy.addAll(entries);
		cpy.remove(e1);
		cpy.remove(e2);
		while (!cpy.isEmpty()) {
			int next = PickNext(cpy, r1, r2);
			Entry<Rectangle, Object> e = cpy.get(next);
			cpy.remove(next);
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
			
			if (group1.size() + cpy.size() <= m) {
				group1.addAll(cpy);
				cpy.clear();
			} else if (group2.size() + cpy.size() <= m) {
				group2.addAll(cpy);
				cpy.clear();
			} 

		}
		return new Tuple<ArrayList<Entry<Rectangle, Object>>, ArrayList<Entry<Rectangle, Object>>>(
				group1, group2);
	}

	private Tuple<Node, Node> SplitTree(Node N) {
		Tuple<ArrayList<Entry<Rectangle, Object>>, ArrayList<Entry<Rectangle, Object>>> groups = QuadSplit(N.entries);
		N.entries = new ArrayList<Entry<Rectangle, Object>>();
		N.addAllEntries(groups.getFirstElement());
		Node NN = new Node();
		NN.addAllEntries(groups.getSecondElement());
		return new Tuple<Node, Node>(N, NN);
	}

	private int PickNext(ArrayList<Entry<Rectangle, Object>> entries,
			Rectangle g1, Rectangle g2) {
		double max_d = 0;
		int max_element = -1;
		int i = 0;
		for (Entry<Rectangle, Object> e : entries) {
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
			if (d >= max_d) {
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
		ArrayList<Entry<Rectangle, Object>> entries;

		public Node() {
			entries = new ArrayList<Entry<Rectangle, Object>>();
			bounds = new Rectangle(0, 0, 0, 0);
		}

		public void addAllEntries(ArrayList<Entry<Rectangle, Object>> entries) {
			for (Entry<Rectangle, Object> e : entries) {
				addEntry(e);
			}
		}

		public void addEntry(Entry<Rectangle, Object> e) {
			Object o = e.getValue();
			if (o instanceof Node) {
				Node n = (Node) o;
				n.parent = this;
				n.bounds = e.getBounds();
			}
			this.entries.add(e);
		}

		public boolean isLeaf() {
			if (this.entries.size() == 0) {
				return true;
			}
			Entry<Rectangle, Object> e = entries.get(0);
			if (e.getValue() instanceof Node) {
				return false;
			}
			return true;
		}
		public String toString(){
			return "["+entries.size()+"]";
		}
	}

	public int size() {
		return size(root);
	}

	public int size(Node n) {
		if (n.isLeaf()) {
			return n.entries.size();
		} else {
			int count = 0;
			for (Entry<Rectangle, Object> e : n.entries) {
				Node q = (Node) e.getValue();
				count += size(q);
			}
			return count;
		}
	}

	@Override
	public String toString() {
		return toString(root);
	}

	public String toString(Node N) {
		ArrayList<Entry<Rectangle, Object>> entries = N.entries;
		StringBuilder s = new StringBuilder();
		if (N.isLeaf()) {
			for (Entry<Rectangle, Object> e : entries) {
				Object o = e.getValue();
				s.append(o.toString());
				s.append("\t");
			}
			s.append("\n");
		} else {
			for (Entry<Rectangle, Object> e : entries) {
				Node n = (Node) e.getValue();
				s.append(toString(n));
			}
		}
		return s.toString();
	}
}
