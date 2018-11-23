package rsn170330.lp4;

import java.util.List;
import java.util.Scanner;

import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.Factory;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;

public class EnumeratePath extends GraphAlgorithm<EnumeratePath.EnumVertex> {
	
	boolean print; // Set to true to print array in visit
	long count; // Number of permutations or combinations visited
	Selector sel;
	
	private Vertex[] A; // stores the path
	private Vertex source; // source
	private Vertex sink; // sink
	
	public EnumeratePath(Graph g) {
		super(g, new EnumVertex());
		print = false;
		count = 0;
		sel = new Selector();
		
		A = new Vertex[g.size()];
		source = g.getVertex(1);
		sink = g.getVertex(g.size());
	}
	
	static class EnumVertex implements Factory {
		long count; // N(u): number of ways you can reach u from source 
		
		EnumVertex() {
			count = 0;
		}
		
		public EnumVertex make(Vertex u) {
			return new EnumVertex();
		}
	}
	
	public void setCount(Vertex u, long c) {
		get(u).count = c;
	}
	
	public long getCount(Vertex u) {
		return get(u).count;
	}
	
	class Selector extends Enumerate.Approver<Vertex> {
		
		public Selector() {
			
		}
		
		@Override
		public boolean select(Vertex u) {
			return true;
		}
		
		@Override
		public void unselect(Vertex u) {
			
		}
		
		@Override
		public void visit(Vertex[] arr, int k) {
			
			if (print) {
				for (int i = 0; i < k; i++) {
					System.out.print(arr[i] + " ");
				}
				System.out.println();
			}
		}
		
	}
	
	// O(|V|+|E|): linear time
	public long countPaths() {
		
		// Initialize count for each vertex
		for(Vertex u : g) { get(u).count = 0; }
		
		// setting count of source
		setCount(source, 1); // s.count = 1
		
		// topList: a topological order on Graph g
		List<Vertex> topList = DFS.topologicalOrder1(g);
		
		// LI: u.count = N(u)
		for (Vertex u : topList) {
			
			for (Edge e : g.incident(u)) {
				Vertex v = e.otherEnd(u);
				
				get(v).count += get(u).count;
			}
		}
		
		return get(sink).count;
		
	}
	
	/**
	 * 
	 * Precondition: 
	 *  1. call from enumerate(vertex p, i-1), where p is predecessor of u.
	 *  2. a[0..i-1] have been selected
	 * 
	 * @param u the visiting vertex
	 * @param i the index of vertex u
	 */
	public void enumeratePaths(Vertex u, int i) {
		
		A[i] = u;
		
		if (u.equals(g.getVertex(sink))) {
			sel.visit(A, i + 1);
		}
		else {
			for (Edge e : g.incident(u)) {
				Vertex v = e.otherEnd(u);
				
				enumeratePaths(v, i + 1);
			}
		}
	}
	
	public long enumeratePaths(boolean flag) {
		print = flag;
		
		long totalPaths = countPaths();
		
		// Runtime: O(t.count)
		if (print) {
			enumeratePaths(source, 0);
		}
		
		return totalPaths;
	}
	
	// -------------------------- STATIC METHODS -----------------------------

	public static long countPaths(Graph g) {
		EnumeratePath et = new EnumeratePath(g);
		return et.enumeratePaths(false);
	}

	public static long enumeratePaths(Graph g) {
		EnumeratePath et = new EnumeratePath(g);
		return et.enumeratePaths(true);
	}
	
	public static void main(String[] args) throws Exception {
		
		int VERBOSE = 0;
		
		if (args.length > 0) {
			VERBOSE = Integer.parseInt(args[0]);
		}
		
		// COMMENT TO TRACE
		Graph g = Graph.readDirectedGraph(new java.util.Scanner(System.in));
		
		// UNCOMMENT TO TRACE A SIMPLE GRAPH
		// String graph = "6 7   1 2 1   1 3 1   2 4 1   3 4 1   3 5 1   4 6 1   5 6 1 0";
		// Graph g = Graph.readDirectedGraph(new Scanner(graph));
		// g.printGraph(false);
		// VERBOSE = 1;
		
		long result;
		
		Graph.Timer t = new Graph.Timer();
		
		if (VERBOSE > 0) {
			result = enumeratePaths(g);
		} else {
			result = countPaths(g);
		}
		
		System.out.println("\nNumber of Paths: " + result + "\n" + t.end());

	}

}
