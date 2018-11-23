package rsn170330.lp4;


import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import rbk.Graph;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Timer;
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

// Starter code for enumerating topological orders of a DAG 
public class EnumerateTopological extends 
	GraphAlgorithm<EnumerateTopological.EnumVertex> {
	
	private Vertex[] A; 
	private HashSet<Vertex> seen;
	
	private boolean print; // Set to true to print array in visit
	private long tOrders; // Number of permutations or combinations visited
	
	private boolean isCyclic;
	
	private Selector sel; // will approve putting element at A[d] 
	
	// ------------------------- Constructors --------------------------------
	
	public EnumerateTopological(Graph g) {
		super(g, new EnumVertex());
		print = false;
		tOrders = 0;
		A = new Vertex[g.size()];
		initialize();
		sel = new Selector();
		seen = new HashSet<>();
		isCyclic = false;
	}
	
	private void initialize() {
		DFS d = DFS.depthFirstSearch(g);
		isCyclic = d.isCyclic();
		List<Vertex> tOrder;
		int i = 0;
		
		if (isCyclic) {
			for (Vertex u : g) { A[i++] = u; }
		}
		else {
			tOrder = d.topologicalOrder1();
			for (Vertex u : tOrder) { A[i++] = u; }
		}
		
	}
	
	// ------------------------- EnumVertex ----------------------------------
	static class EnumVertex implements Factory {
		
		EnumVertex() {
			
		}

		public EnumVertex make(Vertex u) {
			return new EnumVertex();
		}
		
	}
	
	// -------------------------- Selector -----------------------------------
	class Selector extends Enumerate.Approver<Vertex> {
		
		@Override
		public boolean select(Vertex u) {
			
			return true;
		}

		@Override
		public void unselect(Vertex u) {
		
		}

		public void visit(Vertex[] arr) {
			tOrders++;
			if (print) {
				for (Vertex u : arr) {
					System.out.print(u + " ");
				}
				System.out.println();
			}
		}
	}
	
	private void permuteTopological(int c) {
		
		if (c == 0) {
			sel.visit(A);
		}
		
		else {
			int d = A.length - c;
			//System.out.println("Adding: "+A[d]);
			// seen.add(A[d]);
			// permuteTopological(c - 1);
			// seen.remove(A[d]); // good
			
			int i = d;
			while (i < A.length) {
				boolean successorContains = false;
				
				for (Edge e : g.outEdges(A[i])) {
					Vertex s = e.toVertex();
					
					if (seen.contains(s)) {
						successorContains = true;
						break;
					}
				}
				
				if (!successorContains) {
					Vertex temp = A[d];
					A[d] = A[i];
					A[i] = temp;
					
					seen.add(A[d]);
					permuteTopological(c - 1);
					seen.remove(A[d]);
					
					A[i] = A[d];
					A[d] = temp;
				}
				i++;
			}
			
			
		}
	}

	// To do: LP4; return the number of topological orders of g
	public long enumerateTopological(boolean flag) {
		print = flag;
		//sel.visit(A);
		
		// When Graph g is not a DAG
		if (isCyclic)
			return 0;
		
		permuteTopological(g.size());
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
		
		//if (args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
		String graph = "10 12   1 3 1   1 8 1   6 8 1   6 10 1   3 2 1   8 2 1   8 5 1   5 10 1   "+"2 4 1   5 4 1   4 7 1   10 9 1 0";
		// graph = "7 6   1 2 1   1 3 1   2 4 1   3 4 1   3 5 1   6 7 1 0"; //  permute-dag-07.txt
		// graph = "8 11   1 2 1   2 3 1   3 4 1   1 3 1   1 4 1   2 4 1   5 6 1   6 7 1   5 7 1   2 8 1   6 8 1 0"; // permute-dag-08a.txt
		// graph = "8 9   1 2 1   2 3 1   3 4 1   1 3 1   1 4 1   2 4 1   5 6 1   6 7 1   5 7 1 0"; // permute-dag-08b.txt
				
		Scanner in;

		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
		Graph g = Graph.readDirectedGraph(in);

		//Graph g = Graph.readDirectedGraph(new java.util.Scanner(System.in));
		
		
		//Graph g = Graph.readDirectedGraph(new Scanner(graph));
		g.printGraph(false);
		VERBOSE = 1;
		
		long result;
		
		Graph.Timer t = new Graph.Timer();
		
		if (VERBOSE > 0) {
			result = enumerateTopologicalOrders(g);
		} else {
			result = countTopologicalOrders(g);
		}
		
		System.out.println("\n" + result + "\n" + t.end());
	}
	
	// --------------------- Getters and Setters -----------------------------

}
