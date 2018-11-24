package rsn170330.lp4;

import java.io.File;
import java.util.Scanner;

import rbk.Graph;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.Factory;

/**
 * CS 5V81.001: Implementation of Data Structures and Algorithms 
 * Long Project LP4: PERT, Enumeration of topological orders
 * Team: LP101
 * @author Rahul Nalawade (rsn170330)
 * @author Prateek Sarna (pxs180012)
 * @author Bhavish Khanna Narayanan (bxn170002)
 */

// Code for enumerating topological orders of a DAG 
public class EnumerateTopological extends 
	GraphAlgorithm<EnumerateTopological.EnumVertex> {
	
	private long tOrders; // no of topological enumerations
	private boolean print; // set true to print array in visit
	private Selector sel; // Approver of EnumerateTopological itself
	
	private Vertex[] A; // array of all vertices
	private boolean isCyclic; // to determine if Graph is DAG or not
	
	// ------------------------- Constructor ---------------------------------
	public EnumerateTopological(Graph g) {
		super(g, new EnumVertex());
		
		tOrders = 0; // # topological orders visited
		print = false; // no printing
		sel = new Selector(); // will approve selection of an element
		
		DFS d = DFS.depthFirstSearch(g);
		isCyclic = d.isCyclic();
		
		// passed as an array to Enumerate object
		A = g.getVertexArray();
		
		initialize(); // initializes inDegrees of each vertex in Graph g
	}
	
	// ------------------------- EnumVertex ----------------------------------
	static class EnumVertex implements Factory {
		
		int inDegrees; // number of incoming edges on a vertex
		
		EnumVertex() { inDegrees = 0; }

		public EnumVertex make(Vertex u) {
			return new EnumVertex(); 
		}
	}
	
	// -------------------------- Selector -----------------------------------
	class Selector extends Enumerate.Approver<Vertex> {
		
		/* Selects vertex u only if it has no incoming edge onto it. */
		@Override
		public boolean select(Vertex u) {
			
			// When u has no incoming edge
			if (get(u).inDegrees == 0) {
				
				// As u is selected, decrement inDegrees of it's children
				for (Edge e : g.incident(u)) {
					Vertex v = e.otherEnd(u);
					get(v).inDegrees--;
				}
				return true;
			}
			return false;
		}
		
		/* Un-selects selected vertex by incrementing count of inDegrees. */
		@Override
		public void unselect(Vertex u) {
			
			// Increment count v as u is un-selected for each edge(u,v)
			for (Edge e : g.incident(u)) {
				Vertex v = e.otherEnd(u);
				
				get(v).inDegrees++;
			}
		}
		
		/* Visits array with first k elements. Prints them if needed. */ 
		@Override 
		public void visit(Vertex[] arr, int k) {
			tOrders++; // number of valid permutations visited
			
			if (print) {
				for (Vertex u : arr) {
					System.out.print(u + " ");
				}
				System.out.println();
			}
		}
	}
	
	/* Initializes the inDegrees of each vertex in the Graph. */
	private void initialize() {
		
		for (Vertex u : g) {
			get(u).inDegrees = u.inDegree();
		}
	}
	
	/**
	 * Gives count of all Enumerations of Topological ordering of Graph g.
	 * 
	 * Precondition: updated inDegrees of all vertices in the Graph.
	 * 
	 * @param flag do we need to print all enumerations?
	 * @return number of all topological enumerations
	 */
	public long enumerateTopological(boolean flag) {
		print = flag;
		
		// When Graph g is not a DAG
		if (isCyclic) return 0;
		
		Enumerate<Vertex> en = new Enumerate<>(A, sel);
		en.permute(A.length); // k = no of vertices
		
		return tOrders;
	}

	// -------------------------- STATIC METHODS -----------------------------
	public static long countTopologicalOrders(Graph g) {
		EnumerateTopological et = new EnumerateTopological(g);
		return et.enumerateTopological(false);
	}

	public static long enumerateTopologicalOrders(Graph g) {
		EnumerateTopological et = new EnumerateTopological(g);
		return et.enumerateTopological(true);
	}

	public static void main(String[] args) throws Exception {
		
		int VERBOSE = 0;
		if (args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
		VERBOSE = 1;
		
		Scanner in;
		String graph = "10 12   1 3 1   1 8 1   6 8 1   6 10 1   3 2 1   8 2 1   8 5 1   5 10 1   2 4 1   5 4 1   4 7 1   10 9 1 0"; // class notes - 110
		// graph = "7 6   1 2 1   1 3 1   2 4 1   3 4 1   3 5 1   6 7 1 0"; //  permute-dag-07.txt - 105
		// graph = "8 11   1 2 1   2 3 1   3 4 1   1 3 1   1 4 1   2 4 1   5 6 1   6 7 1   5 7 1   2 8 1   6 8 1 0"; // permute-dag-08a.txt - 118
		// graph = "8 9   1 2 1   2 3 1   3 4 1   1 3 1   1 4 1   2 4 1   5 6 1   6 7 1   5 7 1 0"; // permute-dag-08b.txt - 280
		
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[1])) : new Scanner(graph);
		Graph g = Graph.readDirectedGraph(in);
		
		g.printGraph(false);
		
		long result;		
		Graph.Timer t = new Graph.Timer();
		
		if (VERBOSE > 0) {
			result = enumerateTopologicalOrders(g);
		} else {
			result = countTopologicalOrders(g);
		}
		
		System.out.println("\n" + result + "\n" + t.end());
	}
}
