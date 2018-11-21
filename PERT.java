package rsn170330.lp4;

/**
 * CS 5V81.001: Implementation of Data Structures and Algorithms 
 * Long Project LP4: PERT, Enumeration of topological orders
 * Team: LP101
 * @author Rahul Nalawade (rsn170330)
 * @author Prateek Sarna (pxs180012)
 * @author Bhavish Khanna Narayanan (bxn170002)
 * 
 */

import rbk.Graph;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Input: 
 *    1. G = (V,E), G must be a DAG
 *    2. duration(u) = {0, 1, 2, ...}, where u is a node (task) in V.
 *    
 * Output: 
 *    1. Critical Path Length
 *    2. slack(u)
 *    3. EC(u): Earliest Completion Time for each node (task)
 *    4. LC(u): Latest Completion Time for each node (task)
 */
public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
	
	// PERTVertex: Represents a task in PERT
	public static class PERTVertex implements Factory {
		
		int duration; // duration of this task 
		int slack; // slack available for this task
		int earliestCT; // Earliest Completion Time for this task
		int latestCT; // Latest Completion Time for this Task
		
		public PERTVertex(Vertex u) {
			duration = 0;
			slack = 0;
			earliestCT = 0;
			latestCT = 0;
		}
		
		public PERTVertex make(Vertex u) {
			return new PERTVertex(u);
		}
	}
	
	// Constructor
	public PERT(Graph g) {
		super(g, new PERTVertex(null));
		initialize(g); // with source and sink
	}
	
	/**
	 * To add edges in the graph 
	 *  a. from the source to all other vertices
	 *  b. from other vertices to the sink 
	 * 
	 * @param g the graph
	 */
	private void initialize(Graph g) {
		Vertex s = g.getVertex(1);
		Vertex t = g.getVertex(g.size());
		
		int m = g.edgeSize();
		
		for (int i = 2; i < g.size(); i++) {
			g.addEdge(s, g.getVertex(i), 1, ++m);
			g.addEdge(g.getVertex(i), t, 1, ++m);
		}
	}
	
	// setter for duration(u)
	public void setDuration(Vertex u, int d) {
		get(u).duration = d;
	}
	
	// setter for slack(u)
	private void setSlack(Vertex u, int s) {
		get(u).slack = s;
	}
	
	// setter for earliestCT(u)
	private void setEC(Vertex u, int e) {
		get(u).earliestCT = e;
	}
	
	// setter for latestCT(u)
	private void setLC(Vertex u, int l) {
		get(u).latestCT = l;
	}
	
	/**
	 * Implements PERT Algorithm by computing all the necessary Outputs.
	 * @return true when graph g is not a DAG
	 */
	public boolean pert() { 
		// Runs DFS and get the topological order
		DFS d = DFS.depthFirstSearch(g); 
		LinkedList<Vertex> tOrder = (LinkedList<Vertex>) d.topologicalOrder2();
		
		// When the graph is not a DAG
		if (d.isCyclic()) 
			return true; 
		
		// Initializing earliest completion time with 0
		for (Vertex u: g) { setEC(u, 0); }
		
		// Computing earliest time for each vertex
		for (Vertex u: tOrder) {
			
			for (Edge e: g.incident(u)) {
				Vertex v = e.otherEnd(u); // NOTE: v is predecessor of u
				
				// Base Case: EC(source) = 0, 
				// Recursive Case: EC(u) = max {EC(v)}
				int dr = get(u).earliestCT + get(v).duration;
				if (dr > get(v).earliestCT) {
					setEC(v, dr);
				}
			}
		}
		
		// maxTime = t.earliestCT
		int maxTime = get(g.getVertex(g.size())).earliestCT;
		
		// Initializing latest completion time with maxTime
		for (Vertex u: g) { get(u).latestCT = maxTime; }
		
		// To iterate reverse in topological order
		Iterator<Vertex> revIt = tOrder.descendingIterator();
		
		// Computing latest completion time for each vertex
		while (revIt.hasNext()) {
			Vertex u = revIt.next();
			
			for (Edge e: g.incident(u)) {
				Vertex v = e.otherEnd(u); // NOTE: v is successor of u
				
				// Base Case: LC(sink) = EC(sink), 
				// Recursive Case: LC(u) = min {(EC(v) - duration(v))}
				int dr = get(v).latestCT - get(v).duration;
				if (dr < get(u).latestCT) {
					setLC(u, dr);
				}
			}
			// slack(u) = LC(u) - EC(u)
			setSlack(u, (get(u).latestCT - get(u).earliestCT));
		}
		return false;
	}
	
	// getter for duration
	public int getDuration(Vertex u) {
		return get(u).duration;
	}
	
	// getter for earliest completion time
	public int ec(Vertex u) {
		return get(u).earliestCT;
	}
	
	// getter for latest start time
	public int lc(Vertex u) {
		return get(u).latestCT;
	}
	
	// getter for slack
	public int slack(Vertex u) {
		return get(u).slack;
	}
	
	/**
	 * Prints Critical Path
	 * @return number of critical vertices (tasks)
	 */
	public int criticalPath() {
		
		DFS d = DFS.depthFirstSearch(g);
		LinkedList<Vertex> tOrder = (LinkedList<Vertex>) d.topologicalOrder2();
		
		// When the graph is not a DAG
		if (d.isCyclic()) 
			return 0; 
		
		// For each vertex in the topological order
		for (Vertex u: tOrder) {
			
			// When there is no slack time
			if (slack(u) == 0) {
				System.out.print(" -> ");
				System.out.print(u.getName());
			}
		}
		return numCritical();
	}
	
	// returns true if the Vertex u is critical
	public boolean critical(Vertex u) {
		if (get(u).slack > 0)
			return false;
					
		return true;
	}
	
	// return number of critical vertices (tasks)
	public int numCritical() {
		int len = 0;
		
		for (Vertex u: g) {
			if (get(u).slack == 0)
				len++;
		}
		return len;
	}

	// setDuration(u, duration[u.getIndex()]);
	public static PERT pert(Graph g, int[] duration) {
		return null;
	}

	public static void main(String[] args) throws Exception {
		String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1 "+
				"5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1 "+ 
				"0 3 2 3 2 1 3 2 4 1 0";
		Scanner in;

		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
		Graph g = Graph.readDirectedGraph(in);
		g.printGraph(false);

		PERT p = new PERT(g);
		for (Vertex u : g) {
			p.setDuration(u, in.nextInt());
		}
		// Run PERT algorithm. Returns null if g is not a DAG
		if (p.pert()) {
			System.out.println("Invalid graph: not a DAG");
		} else {
			System.out.println("Number of critical vertices: " + p.numCritical());
			System.out.println("u\td\tEC\tLC\tSlack\tCritical");
			for (Vertex u : g) {
				System.out.println(u + "\t" + p.getDuration(u) + "\t" + p.ec(u) + 
					"\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
			}
			System.out.println("Critical Path: ");
			p.criticalPath();
			
		}
	}
}
