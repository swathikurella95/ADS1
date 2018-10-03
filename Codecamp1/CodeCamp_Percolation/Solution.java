import java.util.*;

class WeightedQuickUnionPathCompressionUF {
    private int[] parent;  // parent[i] = parent of i
    private int[] size;    // size[i] = number of sites in tree rooted at i
                           // Note: not necessarily correct if i is not a root node
    private int count;     // number of components

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own 
     * component.
     *
     * @param  n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public WeightedQuickUnionPathCompressionUF(int n) {
        count = n;
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int count() {
        return count;
    }
  

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param  p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    public int find(int p) {
        validate(p);
        int root = p;
        while (root != parent[root])
            root = parent[root];
        while (p != root) {
            int newp = parent[p];
            parent[p] = root;
            p = newp;
        }
        return root;
    }

   /**
     * Returns true if the the two sites are in the same component.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless
     *         both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));  
        }
    }  

    /**
     * Merges the component containing site {@code p} with the 
     * the component containing site {@code q}.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @throws IllegalArgumentException unless
     *         both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;

        // make smaller root point to larger one
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
    }

    /**
     * Reads in a sequence of pairs of integers (between 0 and n-1) from standard input, 
     * where each integer represents some site;
     * if the sites are in different components, merge the two components
     * and print the pair to standard output.
     *
     * @param args the command-line arguments
     */
  
} 
class Percolation {
	 private int rowSize;
		private int gridSize;
		boolean[] grid;
		WeightedQuickUnionPathCompressionUF unionFind;
		int bottomVS;
		private int topVS;

		/**
		 *
		 * Creates N-by-N grid, with all sites blocked.
		 * @param number = size
		 *
		 */
		public Percolation(int number) {
			if (number <= 0) {
	            throw new java.lang.IllegalArgumentException(
	            		"N must be larger than 0");
			}
			rowSize    = number;
			gridSize   = number * number;
			grid 	   = new boolean[gridSize];
			bottomVS   = gridSize + 1;
			topVS      = gridSize;
			unionFind  = new WeightedQuickUnionPathCompressionUF(gridSize + 2);

		}

		/**
		 *
		 * Checks that the indexes are not out of bounds.
		 * @param i = row
		 * @param j = column
		 */
		private void checkIndex(int i, int j) {

			if (i < 1 || i > rowSize) {
	            throw new java.lang.IndexOutOfBoundsException(
	            		"i must be between 1 and " + rowSize);
	        }
	        if (j < 1 || j > rowSize) {
	            throw new java.lang.IndexOutOfBoundsException(
	            		"j must be between 1 and " + rowSize);
	        }
		}

		/**
		 *
		 * Private method to change from 2D to 1D.
		 * @param i = row
		 * @param j = column
		 * @return newIndex
		 */
		private int changeDimension(int i, int  j) {
			// 2D coordinates to right index on 1D array.
			// ( row -1 x column ) + row -1
			int newIndex = (i - 1) * rowSize + (j - 1);
			return newIndex;
		}

		/**
		 *
		 * open site (row i, column j) if it is not already.
		 * @param i = row
		 * @param j = column
		 *
		 */
		public void open(int i, int j) {
			checkIndex(i, j);
			int index = changeDimension(i, j);
			grid[index] = true;

			if (i == 1) {
				unionFind.union(index, topVS);
			}

			boolean hasN = false;
			for (int k = 0; k < 4; k++) {
	            int adjacent = getAdjacentIndex(i, j, k);
	            if (adjacent != -1) {
	            	unionFind.union(adjacent, index);
	                hasN = true;
	            }
			}

			if (hasN) {
	            // check if this made any of the bottom nodes connected
	            // to the top
	            for (int index2 = gridSize - 1; index2 >= gridSize - rowSize;
	            		index2--) {
	                if (grid[index2] && unionFind.connected(topVS, index2)) {
	                    unionFind.union(index2, bottomVS);
	                    break;
	                   }
	            }
			}

		}

		/**
		 *
		 * Find all valid adjacent open sites.
		 * @param i = row
		 * @param j = column
		 * @param k = direction (up, down, left, right)
		 * @return adjacent
		 */
		private int getAdjacentIndex(int i, int j, int k) {

			int row = 0, column = 0, adjacent = -1;

			if (k == 0) {  //up
				row = i - 1;
				column = j;
			} else if (k == 1) { //down
				row = i + 1;
				column = j;
			} else if (k == 2) { //left
				row = i;
				column = j - 1;
			} else if (k == 3) {
				row = i;
				column = j + 1;
			} else {
				throw new java.lang.IllegalArgumentException(
						"Direction must be between 0 and 3");
			}

			if (row > 0 && row <= rowSize && column > 0
					&& column <= rowSize) {
				if (isOpen(row, column)) {
					adjacent = changeDimension(row, column);
				}
			}

			return adjacent;
		}

		/**
		 *
		 *is site (row i, column j) open?
		 * @param i = row
		 * @param j = column
		 * @return isItOpen
		 *
		 */
		public boolean isOpen(int i, int j) {
			checkIndex(i, j);
			boolean isItOpen = grid[changeDimension(i, j)];
			return isItOpen;

		}

		/**
		 * Is the site (row i, column j) full?
		 * @param i = row
		 * @param j = column
		 * @return isItFull
		 */
		public boolean isFull(int i, int j) {
			checkIndex(i, j);
			boolean isItFull = unionFind.connected(
					topVS, changeDimension(i, j));
			return isItFull;

		}

		/**
		 * Does the system percolate?
		 * @return doesIt
		 */
		public boolean percolates() {
			boolean doesIt = unionFind.connected(topVS, bottomVS);
			return doesIt;
		}


		

 }

public class Solution {
	
	public static void main(final String[] args) {
		
		Scanner sc=new Scanner(System.in);
		int size=sc.nextInt();
		Percolation p = new Percolation(size);
		/*System.out.println("Bottom virtual site position "
				+ p.bottomVS);
		System.out.println("Does it percolates? " + p.percolates());
		System.out.println("Grid lenght " + p.grid.length);
		 p.open(4, 1);
		 p.open(3, 1);
		 p.open(2, 1);
		 p.open(1, 1);
		 p.open(1, 4);
		 p.open(2, 4);
		 System.out.println(p.unionFind.connected(16, 8));
		 p.open(4, 4);
		 System.out.println(p.unionFind.find(3));*/
        System.out.println("Does it percolates? " + p.percolates());

	}

}

// You can implement the above API to solve the problem