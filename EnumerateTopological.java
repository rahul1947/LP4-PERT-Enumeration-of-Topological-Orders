package rsn170330.lp4;

/**
 * CS 5V81.001: Implementation of Data Structures and Algorithms 
 * Long Project LP4: PERT, Enumeration of topological orders
 * Team: LP101
 * @author Rahul Nalawade (rsn170330)
 * @author Bhavish Khanna Narayanan (bxn170002)
 * 
 */

// Starter code for enumerating topological orders of a DAG 
import rbk.Graph;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Timer;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.Factory;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
	boolean print;  // Set to true to print array in visit
	long count;      // Number of permutations or combinations visited
	Selector sel;
	public EnumerateTopological(Graph g) {
		super(g, new EnumVertex());
		print = false;
		count = 0;
		sel = new Selector();
	}

	static class EnumVertex implements Factory {
		EnumVertex() { }
		public EnumVertex make(Vertex u) { return new EnumVertex();	}
	}

	class Selector extends Enumerate.Approver<Vertex> {
		@Override
		public boolean select(Vertex u) {
			return true;
		}

		@Override
		public void unselect(Vertex u) {
		}

		@Override
		public void visit(Vertex[] arr, int k) {
			count++;
			if(print) {
				for(Vertex u: arr) {
					System.out.print(u + " ");
				}
				System.out.println();
			}
		}
	}


	// To do: LP4; return the number of topological orders of g
	public long enumerateTopological(boolean flag) {
		print = flag;
		return count;
	}

	//-------------------static methods----------------------

	public static long countTopologicalOrders(Graph g) {
		EnumerateTopological et = new EnumerateTopological(g);
		return et.enumerateTopological(false);
	}

	public static long enumerateTopologicalOrders(Graph g) {
		EnumerateTopological et = new EnumerateTopological(g);
		return et.enumerateTopological(true);
	}

	public static void main(String[] args) {
		int VERBOSE = 0;
		if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
		Graph g = Graph.readDirectedGraph(new java.util.Scanner(System.in));
		Graph.Timer t = new Graph.Timer();
		long result;
		if(VERBOSE > 0) {
			result = enumerateTopologicalOrders(g);
		} else {
			result = countTopologicalOrders(g);
		}
		System.out.println("\n" + result + "\n" + t.end());
	}

}


