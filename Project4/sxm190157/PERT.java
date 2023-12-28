/* Starter code for PERT algorithm (Project 4)
 * @author rbk
 */

// change to your netid
package sxm190157;

// replace sxa173731 with your netid below
import sxm190157.Graph;
import sxm190157.Graph.Vertex;
import sxm190157.Graph.Edge;
import sxm190157.Graph.GraphAlgorithm;
import sxm190157.Graph.Factory;

import java.io.File;
import java.util.*;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
    LinkedList<Vertex> finishList;
	enum Status{
		ACTIVE,
		NEW,
		FINISHED
	}
    public static class PERTVertex implements Factory {
		// Add fields to represent attributes of vertices here
		Vertex parent; //initializes the parent of a node
		double distance; //distance of vertex from source
		public int ec; //earliest completion time
		public int es; //earliest start time
		public int lc; //latest completion time
		public int ls; //latest start time
		public int slack; //slack
		Status status; //status (new, active, finished) to check if DAG
    	boolean mark;  //variable to check if vertex has been met or not. if vertex is met, we mark it. if not met, its unmarked
		public PERTVertex(Vertex u) {
			//initializing all PERTVertex field variables at beginning such that nothing has happened to them yet
			parent = null;
			distance = Double.POSITIVE_INFINITY; 
			ec = 0;
			lc = 0;
			es = 0;
			slack = 0;
			mark = false;
    	}
    	public PERTVertex make(Vertex u) { return new PERTVertex(u); }
    }

    // Constructor for PERT is private. Create PERT instances with static method pert().
    private PERT(Graph g) {
	super(g, new PERTVertex(null));
		finishList = new LinkedList<>(); //complete list for finishList initialized here
    }
    /**
     * Set the duration for a task represented by vertex u.
     * @param u The vertex representing the task.
     * @param d The duration of the task.
     */

    public void setDuration(Vertex u, int d) {
		u.duration = d; //duration is initialized here
    }

     /**
     * Implement the PERT algorithm. Returns false if the graph g is not a DAG.
     * @return True if the algorithm runs successfully on a DAG, false otherwise.
     */

    public boolean pert() {
	    //run topological to store topological ordering of nodes
	    topologicalOrder();
		LinkedList<Vertex> topological = topologicalOrder(); 
		//if topological == null (if graph is not a DAG), cycle is present, therefore return false
        if (topological == null) { 
            return false;
        } 

	    for(Vertex u: topological){
	        get(u).es = 0;
	    }
	    //computation for earliest completion time
	    for (Vertex u:topological){
	        get(u).ec = get(u).es + u.duration;
	        for(Edge e:g.incident(u)){
	            Vertex v = e.otherEnd(u);
	            if(get(v).es < get(u).ec){
	                get(v).es = get(u).ec;
	            }
	        }
	        
	    }
	    
	    // max of all the nodes early finish is CPL (project finish time)
	    int CPL = 0;
	    for (Vertex u:topological){
	        if(get(u).ec > CPL){
	            CPL = get(u).ec;
	        }
	    }
	    
	    //setting late finish values to CPL
	    for (Vertex u : topological){
	        get(u).lc = CPL;
	    }
	    
	    //calculating latest completion time 
		LinkedList<Vertex> reverseFinishList = new LinkedList<>(topological); //list to reverse order of topologicalOrder
	    Collections.reverse(reverseFinishList);

		for(Vertex u: reverseFinishList){ //for each vertex in reverse order of topological
	        get(u).ls = get(u).lc - u.duration;
	        get(u).slack = get(u).lc - get(u).ec;
	        for(Edge e:g.inEdges(u)){
	            Vertex v = e.fromVertex();
	            if(get(v).lc > get(u).ls){
	                get(v).lc = get(u).ls;
	            }
	        }
	    }
	    return true;
    }

    /**
     * Find a topological order of g using DFS.
     * @return A topological order of the graph.
     */

	LinkedList<Vertex> topologicalOrder() {
		if(!isDagAll(g)){
			return null;
		}
		else{
			for (Vertex u: g){
				get(u).mark = false; //unmarked
			}
			for(Vertex u: g){
				if(get(u).mark == false){
					topologicalOrder(u); //s is source node
				}
			}
		}
		return finishList;
    }

    /**
     * Recursive helper method to perform DFS and obtain a topological order.
     * @param s The source vertex for DFS.
     * @return A topological order of the graph.
     */

	LinkedList<Vertex> topologicalOrder(Vertex s){
		get(s).mark = true;
		for(Edge e: g.incident(s)){
			if (get(e.otherEnd(s)).mark == false){
				topologicalOrder(e.otherEnd(s));
			}
		}
		finishList.addFirst(s);
		return finishList;
	}
    /**
     * Helper method that checks if the graph is a Directed Acyclic Graph (DAG).
     * @param g The input graph.
     * @return True if the graph is a DAG, false otherwise.
     */

	boolean isDagAll(Graph g){
		for(Vertex u: g){
			get(u).status = Status.NEW; //initialize all vertex as new (no vertex has been met yet)
		}
		for (Vertex u: g){
			if (get(u).status == Status.NEW){
				if(!isDag(u)){return false;} //if not a DAG, return false, else return true
			}
		}
		return true;
	}
	
    /**
     * Helper method that checks if a given vertex and its incident edges form a DAG.
     * @param u The vertex to be checked.
     * @return True if the vertex and its incident edges form a DAG, false otherwise.
     */		
	boolean isDag(Vertex u){
		get(u).status = Status.ACTIVE;
		for(Edge e: g.incident(u)){
			if(get(e.otherEnd(u)).status == Status.ACTIVE){ //if the status is active (aka if the vertex has been met), graph is cyclic thus not a DAG  
				return false;
			}
			else if(get(e.otherEnd(u)).status == Status.NEW){
				if(!isDag(e.otherEnd(u))){
					return false;
				}
			}
		}
		get(u).status = Status.FINISHED; //completed checking if the graph is a DAG
		return true; //return true since it passed all cases
	}


    // The following methods are called after calling pert().

    /**
     * Returns the earliest completion time of a task represented by vertex u.
     * @param u The vertex representing the task.
     * @return The earliest completion time of the task.
     */

    public int ec(Vertex u) {
	    return get(u).ec;
    }

    /**
     * Returns the latest completion time of a task represented by vertex u.
     * @param u The vertex representing the task.
     * @return The latest completion time of the task.
     */

    public int lc(Vertex u) {
	    return get(u).lc;
    }

    /**
     * Returns the slack of a task represented by vertex u.
     * @param u The vertex representing the task.
     * @return The slack of the task.
     */

    public int slack(Vertex u) {
	    return get(u).slack;
    }

    /**
     * Checks if a task represented by vertex u is a critical vertex.
     * @param u The vertex representing the task.
     * @return True if the task is a critical vertex, false otherwise.
     */
	
    public int criticalPath() {
	    int criticalPathLength = 0;
	    
	    //check if slack is 0. If slack is zero, it is critical path, therefore find critical path length
	    for(Vertex u:g){
	        if(get(u).slack == 0)
				if(get(u).lc > criticalPathLength){
	            	criticalPathLength = get(u).lc;
	        }
	    }
	    return criticalPathLength;
    }

    // Is u a critical vertex?
    public boolean critical(Vertex u) {
	    return get(u).slack == 0; //critical vertex is slack is 0
    }

    // Number of critical vertices of g
    public int numCritical() {
    	int count = 0;
    	for(Vertex u:g){
    	    if(get(u).slack == 0){ //if slack is 0, it is critical vertex, therefore add to count
    	        count++;
    	    }
    	}
    	return count;
    }

    /* Create a PERT instance on g, runs the algorithm.
     * Returns PERT instance if successful. Returns null if G is not a DAG.
     */
    public static PERT pert(Graph g, int[] duration) {
	PERT p = new PERT(g);
	for(Vertex u: g) {
	    p.setDuration(u, duration[u.getIndex()]);
	}
	// Run PERT algorithm.  Returns false if g is not a DAG
	if(p.pert()) {
	    return p;
	} else {
	    return null;
	}
    }
    
    public static void main(String[] args) throws Exception {
	String graph = "10 13   1 2 1   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1";
	Scanner in;
	// If there is a command line argument, use it as file from which
	// input is read, otherwise use input from string.
	in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
	Graph g = Graph.readDirectedGraph(in);
	g.printGraph(false);

	int[] duration = new int[g.size()];
	for(int i=0; i<g.size(); i++) {
	    duration[i] = in.nextInt();
	}
	PERT p = pert(g, duration);
	if(p == null) {
	    System.out.println("Invalid graph: not a DAG");
	} else {
	    System.out.println("Number of critical vertices: " + p.numCritical());
	    System.out.println("u\tEC\tLC\tSlack\tCritical");
	    for(Vertex u: g) {
		System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
	    }
	}
    }
}
