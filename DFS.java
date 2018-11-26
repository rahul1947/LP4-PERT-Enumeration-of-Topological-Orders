package rsn170330.lp4;

/**
 * CS 5V81.001: Implementation of Data Structures and Algorithms
 * Graphs: DFS: Strongly Connected Components - Short Project 10.
 * 
 * Version++: 2018-11-24: Return type of topologicalOrderings() changed to 
 *    null in case of cyclic Graph g.
 *      
 * @author Rahul Nalawade (rsn170330)
 */

import rbk.Graph;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;

import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Team task: 
 * 1. Implement the algorithm to find strongly connected components of a directed graph. 
 * Add the method to your DFS class from SP8.  Make changes so that all methods share 
 * as much of the code as possible. 
 * 
 * public static DFS stronglyConnectedComponents(Graph g) { ... }
 */

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
	private int cno; // Component no of the DFS graph
	private int topNum; // topNum = No of Vertices = |V|
	
	private LinkedList<Vertex> finishList; // to store topological ordering
	private boolean isCyclic;
	
	public static class DFSVertex implements Factory {
		private int cno; // Component no of this node
		private boolean seen; // flag to check if the node is seen or not
		
		Color vColor; // Not used, for reference
		
		private int top; // the order number in which we visit this node
		private Vertex parent; // parent of this node
		private int inDegrees; // number of inDegrees of this node
		
		// default constructor
		public DFSVertex(Vertex u) {
			seen = false;
			vColor = Color.WHITE; // Not used, for reference
			setParent(null);
			
			top = 0;
			inDegrees = 0;
			cno = 0;
		}

		public DFSVertex make(Vertex u) {
			return new DFSVertex(u);
		}
		
		// parent getter
		public Vertex getParent() {
			return parent;
		}
		
		// parent setter
		public void setParent(Vertex parent) {
			this.parent = parent;
		}

		// Not used, just for reference
		public enum Color {
			WHITE, GRAY, BLACK;
		}
	} 
	
	// default constructor
	// NOTE: setting it private because we don't need to make a public handle 
	// for some methods (like connectedComponents()) which need to run DFS before 
	// they are called.
	private DFS(Graph g) {

		super(g, new DFSVertex(null));
		topNum = g.size();
		finishList = new LinkedList<>();
		cno = 0;
		setCyclic(false);
	}
	
	/**
	 * Find strongly connected components of a DIRECTED graph.
	 * @param g the input graph
	 * @return the DFS object containing with updated relevant attributes
	 */
	public static DFS stronglyConnectedComponents(Graph g) {
		DFS d = new DFS(g);
		d.dfs(g);
		
		List<Vertex> list = d.finishList;
		
		//System.out.println(Arrays.toString(list.toArray()));
		
		g.reverseGraph();
		
		d.dfs(list);
		g.reverseGraph();
		
		return d;
	}
	
	/**
	 * Run Depth-First-Search algorithm on Graph g using method 1
	 * @param g the graph as input
	 * @return DFS object
	 */
	public static DFS depthFirstSearch(Graph g) {
		DFS d = new DFS(g);
		d.dfs(g);
		
		return d;
	}
	
	/**
	 * Runs Depth-First-Search on g as well as finishList, in a Generic way. 
	 * Keeping all other valid usages from SP08.
	 * 
	 * @param iterable the Iterable collection of type Vertex
	 */
	private void dfs(Iterable<Vertex> iterable) {
		// No of Vertices in g
		topNum = g.size();
		
		for (Vertex u: g) {
			get(u).seen = false;
			get(u).vColor = DFSVertex.Color.WHITE; // Not used, for reference
			get(u).setParent(null);
		}
		
		finishList = new LinkedList<>();
		cno = 0; // NOTE: class variable
		
		for (Vertex u: iterable) {
			//if (get(u).vColor == DFSVertex.Color.WHITE) {
			if (!get(u).seen) {
				cno++;
				dfsVisit(u);
			}
		}
	}
	
	/**
	 * Helper method which recursively visits the nodes in DFS manner
	 * @param u giving a visit to node 'u'
	 */
	private void dfsVisit(Vertex u) {
		get(u).vColor = DFSVertex.Color.GRAY;
		get(u).seen = true;
		get(u).cno = cno; // updating using class variable
		
		//System.out.print(" -> "+u+"("+get(u).top+")"); // uncomment to trace()*
		
		for (Edge e: g.incident(u)) {
			Vertex v = e.otherEnd(u);
			
			//if (get(v).vColor == DFSVertex.Color.WHITE) {
			if (!get(v).seen) {
				// Visited the node which is 'UnVisited' (or WHITE, v.top = 0)
				get(v).setParent(u);
				dfsVisit(v);
			}
			// When get(u).vColor == DFSVertex.Color.GRAY
			else if (get(u).top > get(v).top) {
				// Visited the node which is in 'Visiting' (or GRAY) status 
				// throw new Exception("Graph is not a DAG, as Edge ("+u+", "+v+") is a back edge.");
				setCyclic(true); // SHOULD NEVER THROW AN EXCEPTION. WORKING SILENTLY.
			}
			
			// When get(u).vColor == DFSVertex.Color.BLACK
			else {
				// Visited the node which was 'Visited' (or BLACK, v.top > u.top)
				// System.out.println("\nVertex "+v+" is already visited."); // uncomment to trace()*
			}
		}
		// top: the number of VISITED node.
		// E.g. if u.top = |V| u is the first node which was done in DFS algorithm
		get(u).top = topNum--;
		
		finishList.addFirst(u); // same as finishList.addFirst(u) 
		// DEFINE finishList as LinkedList (not a List)
		
		get(u).vColor = DFSVertex.Color.BLACK;
		//System.out.print(" <- "+u+"("+get(u).top+")"); // uncomment to trace()*
	}
	

	/**
	 * Method to update finishList which stores the nodes visited 
	 * in increasing order of inDegrees (using method 2) FROM CLASS NOTES
	 * @param g the input graph
	 */
	private void topologicalOrdering2(Graph g) {
		Queue<Vertex> zeroQ = new LinkedList<>();
		
		for (Vertex u: g) {
			get(u).inDegrees = u.inDegree();
			
			if (get(u).inDegrees == 0) {
				zeroQ.add(u);
			}
		}
		// No of nodes that have been processed
		int count = 0;
		
		while (!zeroQ.isEmpty()) {
			Vertex u = zeroQ.remove();
			get(u).top = ++count;
			finishList.add(u);
			
			for (Edge e: g.incident(u)) {
				Vertex v = e.otherEnd(u);
				get(v).inDegrees--; // virtually deleting the edge e
				
				if (get(v).inDegrees == 0) 
					zeroQ.add(v);
			}
		}
		
		if (count != g.size())
			setCyclic(true);
		
	}
	
	/**
	 * Find the number of connected components of a DAG by running DFS.
	 *  
	 * Enter the component number of each vertex u in u.cno. 
	 * Note that the graph g is available as a class field via GraphAlgorithm.
	 * @return total number of components
	 */
	public int connectedComponents() {
		return cno;
	}

	/**
	 * Precondition: Run strongly connected components, or 
	 * connected components algorithm. 
	 * 
	 * Returns Component no of vertex u.
	 * @param u the vertex
	 * @return component number of vertex u
	 */
	public int cno(Vertex u) {
		return get(u).cno;
	}
	
	/**
	 * Find topological oder of a DAG using DFS method 1.
	 * @param g the input graph
	 * @return the List of vertices in order of DFS, if not DAG return null	
	 */
	public static List<Vertex> topologicalOrder1(Graph g) {
		DFS d = depthFirstSearch(g);
		// Ran DFS and then giving the order
		return d.topologicalOrder1();
	}
	
	// Member function to find topological order using method 1
	public List<Vertex> topologicalOrder1() {
		// When graph is not a DAG
		if (isCyclic())
			return null;
		
		return this.finishList;
	}

	/**
	 * Find topological order of a DAG using the second algorithm.
	 * @param g the input graph
	 * @return the List of vertices in order of DFS, if not DAG return null
	 */
	public static List<Vertex> topologicalOrder2(Graph g) {
		DFS d = new DFS(g);
		// Calls method 2 which gives topological ordering
		d.topologicalOrdering2(g); 	
		
		return d.topologicalOrder2();
	}
	
	// Member function to find topological order using method 2
	public List<Vertex> topologicalOrder2() {
		// When graph is not a DAG
		if (isCyclic())
			return null;
		
		return this.finishList;
	}

	// Can be queried for a graph which was supposed to be DAG
	public boolean isCyclic() {
		return isCyclic;
	}
	
	// setter method
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;
	}

	public static void main(String[] args) throws Exception {
		//String string = "7 6   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   6 7 1 0";
		
		// Graph from Class Notes
		String string = "11 17   1 11 1   2 3 1   2 7 1   3 10 1   4 1 1   4 9 1   5 4 1   "+
		"5 7 1   5 8 1   6 3 1   7 8 1   8 2 1   9 11 1   10 6 1   11 3 1   11 4 1   11 6 1 0";
		
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

		// Read graph from input
		Graph g = Graph.readDirectedGraph(in);
		
		// ------------------- Strongly Connected Components (SP10: Task 1) -----------------
		DFS dSCC = DFS.stronglyConnectedComponents(g);
		int numSCC = dSCC.connectedComponents();
		
		System.out.println("# SP10 - STRONGLY CONNECTED COMPONENTS: ");
		g.printGraph(false);
		System.out.println("Number of components: " + numSCC + "\nu\tcno");
		for (Vertex u: g) {
			System.out.println(u + "\t" + dSCC.cno(u));
		}
		
		// --------------------------- NEW GRAPH (DAG) --------------------------------------
		string = "10 12   1 3 1   1 8 1   6 8 1   6 10 1   3 2 1   8 2 1   8 5 1   5 10 1   "+
		"2 4 1   5 4 1   4 7 1   10 9 1 0";
		
		in = new Scanner(string);

		// Read graph from input
		g = Graph.readDirectedGraph(in);
		
		System.out.println("\n# SP08 - CONNECTED COMPONENTS AND TOPOLOGICAL ORDERINGS: ");
		
		// ----------------------- Connected Components (SP08: Task 3) ----------------------
		DFS d = DFS.depthFirstSearch(g);
		int numcc = d.connectedComponents();
		
		
		g.printGraph(false);

		System.out.println("Number of components: " + numcc + "\nu\tcno");
		for (Vertex u: g) {
			System.out.println(u + "\t" + d.cno(u));
		}
		
		// ----------------------- Topological Order 1 (SP08: Task 1) -----------------------
		List<Vertex> tOrder = DFS.topologicalOrder1(g);
		
		System.out.println("\nTopological Ordering 1: ");
		
		for (Vertex u: tOrder) {
			System.out.print(u + " ");
		}
		System.out.println();
		
		
		// ----------------------- Topological Order 2 (SP08: Task 2) -----------------------
		tOrder = DFS.topologicalOrder2(g);
		
		System.out.println("\nTopological Ordering 2: ");
		
		for (Vertex u: tOrder) {
			System.out.print(u + " ");
		}
		System.out.println();
		
	}
}
