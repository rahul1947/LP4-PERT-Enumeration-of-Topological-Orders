package rsn170330.lp4;

import java.io.File;

/**
 * CS 5V81: Implementation of Data Structures and Algorithms
 * Extension of Long Project LP4: PERT, Enumeration of topological orders
 * 
 * @author Rahul Nalawade (rsn170330)
 *
 * @param <T> the Generic Type
 */

import java.util.List;
import java.util.Scanner;

import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.Factory;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;

/**
 * Code for enumerating paths from source (first vertex) 
 * to sink (last vertex) of a DAG
 */
public class EnumeratePath extends GraphAlgorithm<EnumeratePath.EnumVertex> {
	
	// no of path enumerations (required for both Enumeration Algorithms)
	private long paths; 
	
	private boolean print; // set true to print array in visit
	private Selector selector; // Approver of EnumeratePath itself
	
	//  to use Selection Algorithm?
	private static boolean withSelector = false; 
	
	private Vertex[] A; // array of all vertices
	private Vertex source; // source
	private Vertex sink; // sink
	
	// ------------------------- Constructor ---------------------------------
	public EnumeratePath(Graph g) {
		super(g, new EnumVertex());
		paths = 0; // # paths visited
		print = false; // no printing
		
		// will approve selection of an element
		selector = new Selector(g.size()); // passing number of vertices
		
		// passed as an array to Enumerate Object
		A = g.getVertexArray(); 
		
		source = g.getVertex(1); // first vertex
		sink = g.getVertex(g.size()); // last vertex
	}
	
	//setter for N(u): count of paths from source to reach u 
	public void setCount(Vertex u, long c) {
		get(u).count = c;
	}
	
	// getter of N(u)
	public long getCount(Vertex u) {
		return get(u).count;
	}
	
	// ------------------------- EnumVertex ----------------------------------
	static class EnumVertex implements Factory {
		
		// N(u): number of ways you can reach u from source
		long count;  
		
		EnumVertex() { count = 0; }
		
		public EnumVertex make(Vertex u) {
			return new EnumVertex();
		}
	}
	
	// -------------------------- Selector -----------------------------------
	class Selector extends Enumerate.Approver<Vertex> {
		
		// stack: to keep track of traced vertices from source to stack.top
		VertexStack<Vertex> stack; // required for Algorithm 1 only

		// parameterized to initialize stack implemented as an array
		public Selector(int size) {
			stack = new VertexStack<>(size);
		}
		
		// Selects vertex u only if it extends path from top of the stack. 
		@Override
		public boolean select(Vertex u) {
			
			// When stack is empty, only accept source
			if (stack.empty()) {
				if (source.equals(u)) {
					stack.push(source);
					return true;
				}
				
				return false;
			}
			// accept u only if there is an edge (stack.top, u)
			else {
				
				Vertex w = stack.peek(); // top of the stack
				for (Edge e : g.incident(w)) {
					
					// edge exits :) 
					if (u.equals(e.otherEnd(w))) {
						
						stack.push(u);
						// When we could reach sink early* and u is sink*** 
						if (stack.size() < g.size() && u.equals(sink)) {
							
							// Preparing for visit(T[], int k)
							Vertex[] vArray = new Vertex[stack.size()];
							stack.toArray(vArray);
							
							this.visit(vArray, stack.size()); // stack.size() < |V|
						}
						
						return true;
					}
				}
				return false;	
			}
		}
		
		// Un-selects selected vertex by incrementing count of inDegrees.
		@Override
		public void unselect(Vertex u) {
			stack.pop();
		}
		
		// Visits array with first k elements. Prints them if needed. 
		@Override
		public void visit(Vertex[] arr, int k) {
			paths++; // (for both Enumeration Algorithms)
			
			if (print) {
				for (int i = 0; i < k; i++) {
					System.out.print(arr[i] + " ");
				}
				System.out.println();
			}
		}
	}
	
	/**
	 * Gives count of number of paths from source to sink
	 * Complexity: O(|V|+|E|): linear time
	 * 
	 * @return number of paths
	 */
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
				// sum of all N(u), such that u is predecessor of v
				get(v).count += get(u).count;
			}
		}
		return get(sink).count;
	}
	
	/**
	 * Gives enumeration of all the paths from source to vertex u.
	 * 
	 * Precondition: 
	 *  1. call from enumerate(vertex p, i-1), where p is predecessor of u.
	 *  2. a[0..i-1] have been selected
	 *  
	 *  Runtime: O(t.count)
	 * 
	 * @param u the visiting vertex
	 * @param i the index of vertex u
	 */
	public void enumeratePaths(Vertex u, int i) {
		
		A[i] = u;
		
		if (u.equals(g.getVertex(sink))) {
			selector.visit(A, i + 1);
		}
		else {
			for (Edge e : g.incident(u)) {
				Vertex v = e.otherEnd(u);
				
				enumeratePaths(v, i + 1);
			}
		}
	}
	
	/**
	 * Enumerates all paths from source to sink.
	 * 
	 * Precondition (only for Algorithm 2): 
	 *   updated N(u) for each vertex in the Graph
	 *   
	 * @param flag do we need to print all enumerations?
	 * @return number of all paths from source to sink
	 */
	public long enumeratePaths(boolean flag) {
		print = flag; 
		
		if (print) {
			
			// When we want to use Selection Algorithm
			if (withSelector) {
				Enumerate<Vertex> en = new Enumerate<>(A, selector);
				en.permute(A.length); // k = no of vertices
				// NOTE: we are not reaching the case c == 0 in this permute, 
				// So, cannot do return en.count :(
			}
			// Better Enumeration Algorithm
			else {
				enumeratePaths(source, 0);
				// NOTE: this algorithm uses only visit() of Selector class* 
			}
			return paths;
		}
		// When need to count paths only
		return countPaths();
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
		if (args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
		
		Scanner in;
		String graph = "6 7   1 2 1   1 3 1   2 4 1   3 4 1   3 5 1   4 6 1   5 6 1 0"; // 3 paths
		// graph = "4 3   1 2 1   2 3 1   3 4 1 0"; // 1 path only: 1 2 3 4
		// graph = "7 6   1 2 1   1 4 1   2 3 1   4 3 1   4 5 1   6 7 1 0"; // no paths
		
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 1 ? new Scanner(new File(args[1])) : new Scanner(graph);
		Graph g = Graph.readDirectedGraph(in);
		 
		// Graph g = Graph.readDirectedGraph(new Scanner(graph));
		g.printGraph(false);
		
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
