import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Solution {
	
	  public static void main(String[] args) throws FileNotFoundException {
		 // File file = new File("/msit2018-20/src/input000.txt");
		  Scanner sc=new Scanner(System.in);
	     WeightedQuickUnionPathCompressionUF unionFind;
	  int h=sc.nextInt();
	  System.out.println("h"+h);
	     Percolation p = new Percolation(h);
		/*System.out.println("Bottom virtual site position "
				+ p.bottomVS);
		System.out.println("Does it percolates? " + p.percolates());
		System.out.println("Grid lenght " + p.grid.length);
	//	int m=sc.nextInt();*/
		while(sc.hasNext())
		{
			int p1=sc.nextInt();
			int q=sc.nextInt();
			// System.out.println("p1"+p1+"---q"+q);
			p.open(p1, q);
			
			
		}
		
        System.out.println(p.percolates());

	         
	        }
	        
	    }

