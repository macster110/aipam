package com.jamdev.maven.aipam.clustering.snapToGrid.shortestPath;

import com.jamdev.maven.aipam.clustering.snapToGrid.AssignmentListener;

import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/******************************************************************************
 *  Compilation:  javac AssignmentProblem.java
 *  Execution:    java AssignmentProblem n
 *  Dependencies: DijkstraSP.java DirectedEdge.java
 *
 *  Solve an n-by-n assignment problem in n^3 log n time using the
 *  successive shortest path algorithm.
 *
 ******************************************************************************/

/**
 *  The {@code AssignmentProblem} class represents a data type for computing
 *  an optimal solution to an <em>n</em>-by-<em>n</em> <em>assignment problem</em>.
 *  The assignment problem is to find a minimum weight matching in an
 *  edge-weighted complete bipartite graph.
 *  <p>
 *  The data type supplies methods for determining the optimal solution
 *  and the corresponding dual solution.
 *  <p>
 *  This implementation uses the <em>successive shortest paths algorithm</em>.
 *  The order of growth of the running time in the worst case is
 *  O(<em>n</em>^3 log <em>n</em>) to solve an <em>n</em>-by-<em>n</em>
 *  instance.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/65reductions">Section 6.5</a>
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Jamie Macaulay <-modified slightly to add settings listener. 
 */
public class AssignmentProblem {
    private static final double FLOATING_POINT_EPSILON = 1E-14;
    private static final int UNMATCHED = -1;

    private int n;              // number of rows and columns
    private double[][] weight;  // the n-by-n cost matrix
    private double minWeight;   // minimum value of any weight
    private double[] px;        // px[i] = dual variable for row i
    private double[] py;        // py[j] = dual variable for col j
    private int[] xy;           // xy[i] = j means i-j is a match
    private int[] yx;           // yx[j] = i means i-j is a match
    
    /**
     * Listener for updates. 
     */
    private AssignmentListener assignMentListener;
    
    

	/**
     * Determines an optimal solution to the assignment problem.
     *
     * @param  weight the <em>n</em>-by-<em>n</em> matrix of weights
     * @throws IllegalArgumentException unless all weights are nonnegative
     * @throws IllegalArgumentException if {@code weight} is {@code null}
     */ 
    public AssignmentProblem(double[][] weight) {
        if (weight == null) throw new IllegalArgumentException("constructor argument is null");

        n = weight.length;
        this.weight = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (Double.isNaN(weight[i][j]))
                    throw new IllegalArgumentException("weight " + i + "-" + j  + " is NaN");
                if (weight[i][j] < minWeight) minWeight = weight[i][j];
                this.weight[i][j] = weight[i][j];
            }
        }
    }
    
    /**
     * Run the assignment solver. 
     * @return true if everything has run OK. 
     */
    public boolean process() {
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

        // add n edges to matching
        for (int k = 0; k < n; k++) {
        	if (assignMentListener!=null) {
        		assignMentListener.augmentingRowReduction(n, k,1);
        		if (assignMentListener.isCancelled()) {
        			return false;
        		}
        	}
            assert isDualFeasible();
            assert isComplementarySlack();
            augment();
        }
        return certifySolution();
    }

    // find shortest augmenting path and upate
    private void augment() {

        // build residual graph
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(2*n+2);
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
        DijkstraSP spt = new DijkstraSP(G, s);

        // augment along alternating path
        for (DirectedEdge e : spt.pathTo(t)) {
            int i = e.from(), j = e.to() - n;
            if (i < n) {
                xy[i] = j;
                yx[j] = i;
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
        double reducedCost = (weight[i][j] - minWeight) + px[i] - py[j];

        // to avoid issues with floating-point precision
        double magnitude = Math.abs(weight[i][j]) + Math.abs(px[i]) + Math.abs(py[j]);
        if (Math.abs(reducedCost) <= FLOATING_POINT_EPSILON * magnitude) return 0.0;

        assert reducedCost >= 0.0;
        return reducedCost;
    }

    /**
     * Returns the dual optimal value for the specified row.
     *
     * @param  i the row index
     * @return the dual optimal value for row {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < n}
     *
     */
    // dual variable for row i
    public double dualRow(int i) {
        validate(i);
        return px[i];
    }

    /**
     * Returns the dual optimal value for the specified column.
     *
     * @param  j the column index
     * @return the dual optimal value for column {@code j}
     * @throws IllegalArgumentException unless {@code 0 <= j < n}
     *
     */
    public double dualCol(int j) {
        validate(j);
        return py[j];
    }

    /**
     * Returns the column associated with the specified row in the optimal solution.
     *
     * @param  i the row index
     * @return the column matched to row {@code i} in the optimal solution
     * @throws IllegalArgumentException unless {@code 0 <= i < n}
     *
     */
    public int sol(int i) {
        validate(i);
        return xy[i];
    }

    /**
     * Returns the total weight of the optimal solution
     *
     * @return the total weight of the optimal solution
     *
     */
    public double weight() {
        double total = 0.0;
        for (int i = 0; i < n; i++) {
            if (xy[i] != UNMATCHED)
                total += weight[i][xy[i]];
        }
        return total;
    }

    private void validate(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException("index is not between 0 and " + (n-1) + ": " + i);
    }


    /**************************************************************************
     *
     *  The code below is solely for testing correctness of the data type.
     *
     **************************************************************************/

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
                StdOut.println("Not a perfect matching");
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
    private boolean certifySolution() {
        return isPerfectMatching() && isDualFeasible() && isComplementarySlack();
    }

    /**
     * Unit tests the {@code AssignmentProblem} data type.
     * Takes a command-line argument n; creates a random n-by-n matrix;
     * solves the n-by-n assignment problem; and prints the optimal
     * solution.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // create random n-by-n matrix
        int n = Integer.parseInt(args[0]);
        double[][] weight = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                weight[i][j] = StdRandom.uniform(900) + 100;  // 3 digits
            }
        }

        // solve assignment problem
        AssignmentProblem assignment = new AssignmentProblem(weight);
        StdOut.printf("weight = %.0f\n", assignment.weight());
        StdOut.println();

        // print n-by-n matrix and optimal solution
        if (n >= 20) return;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j == assignment.sol(i))
                    StdOut.printf("*%.0f ", weight[i][j]);
                else
                    StdOut.printf(" %.0f ", weight[i][j]);
            }
            StdOut.println();
        }
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

}

