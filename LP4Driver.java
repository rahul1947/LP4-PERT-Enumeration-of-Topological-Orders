package rsn170330.lp4;

import java.util.Scanner;

// Driver code for PERT: *recommend to use main() of PERT instead*
public class LP4Driver {
	public static void main(String[] args) throws Exception {
		boolean details = false;
		if (args.length > 0) {
			details = Boolean.parseBoolean(args[0]);
		}
		
		String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1 "
			+ " 6 8 1   6 9 1   7 10 1   8 10 1   9 10 1 "
			+ " 0 3 2 3 2 1 3 2 4 1 0 ";
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = (args.length > 1) ? new Scanner(new java.io.File(args[1])) : new Scanner(graph);
		
		rbk.Graph g = rbk.Graph.readDirectedGraph(in);
		//g.printGraph(false);

		int[] duration = new int[g.size()];
		for (int i = 0; i < g.size(); i++) {
			duration[i] = in.nextInt();
		}
		
		PERT p = PERT.pert(g, duration);
		if (p == null) {
			System.out.println("Invalid graph: not a DAG");
		} 
		else { 
			if (g.size() <= 20 || details) {
				g.printGraph(false);
				
				System.out.println("u\tDur\tEC\tLC\tSlack\tCritical");
				
				for (rbk.Graph.Vertex u : g) {
					System.out.println(u + "\t" + duration[u.getIndex()] + "\t" + p.ec(u) 
					+ "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
				}
			}
			System.out.println("Length of Critical Path: " + p.criticalPath() 
			+ "\nNumber of Critical Nodes: " + p.numCritical());
		}
	}
}
