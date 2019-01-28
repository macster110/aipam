package com.jamdev.maven.aipam.clustering.snapToGrid.shortestPath;

import com.jamdev.maven.aipam.clustering.snapToGrid.AssignmentListener;

import edu.princeton.cs.algs4.AdjMatrixEdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/******************************************************************************
 *  Compilation:  javac AssignmentProblemDense.java
 *  Execution:    java AssignmentProblemDense n
 *  Dependencies: DenseDijkstraSP.java AdjMatrixEdgeWeightedDigraph.java DirectedEdge.java
 *
 *  Solve an n-by-n assignment problem using successive shortest path
 *  algorithm in n^3 time.
 *
 *  Assumes n-by-n cost matrix is nonnegative. If not, can add positive 
 *  constant to all entries.
 *
 ******************************************************************************/

public class AssignmentProblemDense {
    private static final double FLOATING_POINT_EPSILON = 1E-14;
    private static final int UNMATCHED = -1;

    private int n;              // number of rows and columns
    private double[][] weight;  // the n-by-n cost matrix
    private double[] px;        // px[i] = dual variable for row i
    private double[] py;        // py[j] = dual variable for col j
    private int[] xy;           // xy[i] = j means i-j is a match
    private int[] yx;           // yx[j] = i means i-j is a match
    
    /**
     * Listener for updates. 
     */
    private AssignmentListener assignMentListener;


	public AssignmentProblemDense(double[][] weight) {
        this.weight = weight.clone();
        n = weight.length;

        // dual variables
        px = new double[n];
        py = new double[n];

        // initial matching is empty
        xy = new int[n];
        yx = new int[n];
        for (int i = 0; i < n; i++)
            xy[i] = UNMATCHED;
        for (int j = 0; j < n; j++)
            yx[j] = UNMATCHED;


    }
	
	/**
	 * Process the algorithm. 
	 * @return true if processing finishes successfully. 
	 */
	boolean process() {
		// add N edges to matching
		for (int k = 0; k < n; k++) {
			if (assignMentListener!=null) {
				assignMentListener.augmentingRowReduction(n, k,1);
				System.out.println("Snap " + k + " of " + n + "   " + assignMentListener.isCancelled());
				if (assignMentListener.isCancelled()) {
					
					return false;
				}
			}
			assert isDualFeasible();
			assert isComplementarySlack();
			augment();
		}
		assert check();

		return true; 
	}

    // find shortest augmenting path and upate
    private void augment() {

        // build residual graph
        AdjMatrixEdgeWeightedDigraph G = new AdjMatrixEdgeWeightedDigraph(2*n + 2);
        int s = 2*n, t = 2*n+1;
        for (int i = 0; i < n; i++) {
            if (xy[i] == UNMATCHED)
                G.addEdge(new DirectedEdge(s, i, 0.0));
        }
        for (int j = 0; j < n; j++) {
            if (yx[j] == UNMATCHED)
                G.addEdge(new DirectedEdge(n+j, t, py[j]));
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (xy[i] == j) G.addEdge(new DirectedEdge(n+j, i, 0.0));
                else            G.addEdge(new DirectedEdge(i, n+j, reducedCost(i, j)));
            }
        }

        // compute shortest path from s to every other vertex
        DenseDijkstraSP spt = new DenseDijkstraSP(G, s);

        // augment along alternating path
        for (DirectedEdge e : spt.pathTo(t)) {
            int v = e.from(), w = e.to() - n;
            if (v < n) {
                xy[v] = w;
                yx[w] = v;
            }
        }

        // update dual variables
        for (int i = 0; i < n; i++)
            px[i] += spt.distTo(i);
        for (int j = 0; j < n; j++)
            py[j] += spt.distTo(n+j);
    }

    // reduced cost of i-j
    // (subtracting off minWeight reweights all weights to be non-negative)
    private double reducedCost(int i, int j) {
        double reducedCost = weight[i][j] + px[i] - py[j];
            
        // to avoid issues with floating-point precision
        double magnitude = Math.abs(weight[i][j]) + Math.abs(px[i]) + Math.abs(py[j]);
        if (Math.abs(reducedCost) <= FLOATING_POINT_EPSILON * magnitude) return 0.0;

        assert reducedCost >= 0.0;
        return reducedCost;
    }

    // total weight of min weight perfect matching
    public double weight() {
        double total = 0.0;
        for (int i = 0; i < n; i++) {
            if (xy[i] != UNMATCHED)
                total += weight[i][xy[i]];
        }
        return total;
    }

    public int sol(int i) {
        return xy[i];
    }

    // check that dual variables are feasible
    private boolean isDualFeasible() {
        // check that all edges have >= 0 reduced cost
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (reducedCost(i, j) < 0) {
                	StdOut.println("Dual variables are not feasible");
                    return false;
                }
            }
        }
        return true;
    }

    // check that primal and dual variables are complementary slack
    private boolean isComplementarySlack() {

        // check that all matched edges have 0-reduced cost
        for (int i = 0; i < n; i++) {
            if ((xy[i] != UNMATCHED) && (reducedCost(i, xy[i]) != 0)) {
            	StdOut.println("Primal and dual variables are not complementary slack");
                return false;
            }
        }
        return true;
    }

    // check that primal variables are a perfect matching
    private boolean isPerfectMatching() {

        // check that xy[] is a perfect matching
        boolean[] perm = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (perm[xy[i]]) {
            	 System.out.println("Not a perfect matching");
                return false;
            }
            perm[xy[i]] = true;
        }

        // check that xy[] and yx[] are inverses
        for (int j = 0; j < n; j++) {
            if (xy[yx[j]] != j) {
            	StdOut.println("xy[] and yx[] are not inverses");
                return false;
            }
        }
        for (int i = 0; i < n; i++) {
            if (yx[xy[i]] != i) {
                StdOut.println("xy[] and yx[] are not inverses");
                return false;
            }
        }

        return true;
    }


    // check optimality conditions
    private boolean check() {
        return isPerfectMatching() && isDualFeasible() && isComplementarySlack();
    }
    
    /**
     * Get the assignment listener for the algorithm
     * @return the assignment listener
     */
    public AssignmentListener getAssignMentListener() {
		return assignMentListener;
	}

    /**
     * Set the assignment listener for the algorithm
     * @return the assignment listener to set. 
     */
	public void setAssignMentListener(AssignmentListener assignMentListener) {
		this.assignMentListener = assignMentListener;
	}
	
	
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        double[][] weight = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                weight[i][j] = StdRandom.uniform();

        AssignmentProblemDense assignment = new AssignmentProblemDense(weight);
        StdOut.println("weight = " + assignment.weight());
        for (int i = 0; i < n; i++)
        	System.out.println(i + "-" + assignment.sol(i));
    }

}