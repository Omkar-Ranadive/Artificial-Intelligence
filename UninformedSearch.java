import java.util.ArrayList;
import java.util.Scanner;



class BFS
{
	/*
	Note that for N-queen as the solution is achieved at the nth depth, 
	BFS will be much slower than DFS and will expand more nodes
	*/
	
	private int pos[]; 
	private ArrayList<int[]> frontier; 
	private ArrayList<Integer> queens; // A list to keep track of number of queens at each level 
	private int n; 
	private long expanded_nodes = 0; 
	private boolean found_sol = false; 
	
	
	BFS(int n)
	{
		this.n = n; 
		frontier = new ArrayList<int[]>(); 
		queens = new ArrayList<Integer>();
		pos = new int[n+1]; 
	}
	
	public void print_board(int k, int i)
	{
		int temp[] = pos.clone(); 
		temp[k] = i; 
		for(int j = 1; j<= n; j++)
		{
		    	for(int l = 1; l <= n; l++)
		    	{
		    		if(l == temp[j])
		    		  System.out.print(" Q ");
		    		else
		    			System.out.print(" * ");
		    	}
				System.out.println("");

		}
		System.out.println("");
		
	}
	
	
	public Boolean isSafe(int k, int i)
	{
		for(int j = 1; j < k; j++)
		{
			if(pos[j]==i || Math.abs(pos[j]-i) == Math.abs(j-k))
			{
				return false; 
			}
			
		}
		return true; 
	}
	
	
	public void queenSolver(int k, int n)
	{
		// Add the initial state of empty board to the frontier 
		frontier.add(pos); 
		queens.add(k); // Initially k = 1 
		while(!found_sol)
		{
		// Get the first element out of the frontier (As BFS is FIFO) 
		pos = frontier.get(0).clone(); 
		
		k = queens.get(0); 
		
		// Delete the node from frontier 
		frontier.remove(0); 
		queens.remove(0); 
		
		for(int i = 1; i <= n; i++)
		{
			expanded_nodes++; 
	
			
			if(!found_sol)
			{
				System.out.println("Total nodes expanded: " + expanded_nodes);
				print_board(k, i); 
				pos[k] = i;
			} 
			
			// Check if the final state is reached 
			if(isSafe(k, i))
			{
				frontier.add(pos.clone()); 
				queens.add(k+1); 
				//pos[k] = i; 
				if(k == n)
				{
				found_sol = true; 
				System.out.println("\n\n-------------------------");	
				System.out.println("Solution state is: ");
				print_board(k, i);
				break; 
				}
				
			
			}
		
			
	
		}
		
	}
	}
	
}



class DFS
{
	
	private int pos[]; 
	private int n; 
	private boolean found_sol = false; 
	private long expanded_nodes = 0; 

	
	DFS(int n)
	{
		this.n = n; 
		pos = new int[n+1];
	}
	
	
	public void print_board(int k, int i)
	{
		int temp[] = pos.clone(); 
		temp[k] = i; 
		for(int j = 1; j<= n; j++)
		{
		    	for(int l = 1; l <= n; l++)
		    	{
		    		if(l == temp[j])
		    		  System.out.print(" Q ");
		    		else
		    			System.out.print(" * ");
		    	}
				System.out.println("");

		}
		System.out.println("");
		
	}
	
	public void queenSolver(int k,int n)
	{
		for(int i = 1; i <= n; i++)
		{
			expanded_nodes++; 

			if(!found_sol)
			{
				System.out.println("Total nodes expanded: " + expanded_nodes);

				print_board(k, i); 

				
			}

			if(isSafe(k,i))
			{
				
				pos[k] = i; 
				if(k == n)
				{
				found_sol = true; 
				System.out.println("\n\n-------------------------");	
				System.out.println("Solution state is: ");
				print_board(k, i);
				}
				
				
				if(!found_sol)
				{
				queenSolver(k+1,n);

				}
				
				

			}
			
		}
		
	}
	
	public Boolean isSafe(int k, int i)
	{
		for(int j = 1; j < k; j++)
		{
			if(pos[j]==i || Math.abs(pos[j]-i) == Math.abs(j-k))
			{
				return false; 
			}
			
		}
		return true; 
	}
	
}


class Deepening
{
	
	private int pos[]; 
	private int n; 
	private boolean found_sol = false; 
	private long expanded_nodes = 0; 
    private int depth; 
    private boolean failure = false; 
	
	Deepening(int n, int depth)
	{
		this.depth = depth; 
		this.n = n; 
		pos = new int[n+1];
	}
	
	public boolean getStatus()
	{
		return failure; 
	}
	
	public void print_board(int k, int i)
	{
		int temp[] = pos.clone(); 
		temp[k] = i; 
		for(int j = 1; j<= n; j++)
		{
		    	for(int l = 1; l <= n; l++)
		    	{
		    		if(l == temp[j])
		    		  System.out.print(" Q ");
		    		else
		    			System.out.print(" * ");
		    	}
				System.out.println("");

		}
		System.out.println("");
		
	}
	
	public void queenSolver(int k, int n)
	{
		if(k > depth)
		{
			failure = true; 
			return; 
			
		}
		
		
		
		for(int i = 1; i <= n; i++)
		{
			expanded_nodes++; 
			
			
			

			if(!found_sol)
			{
				System.out.println("Total nodes expanded: " + expanded_nodes);

				print_board(k, i); 

				
			}

			if(isSafe(k,i))
			{
				
				pos[k] = i; 
				if(k == n)
				{
				found_sol = true; 
				System.out.println("\n\n-------------------------");	
				System.out.println("Solution state is: ");
				print_board(k, i);
				}
				
				
				if(!found_sol)
				{
				queenSolver(k+1,n);

				}
				
				

			}
			
		}
		
	}
	
	public Boolean isSafe(int k, int i)
	{
		for(int j = 1; j < k; j++)
		{
			if(pos[j]==i || Math.abs(pos[j]-i) == Math.abs(j-k))
			{
				return false; 
			}
			
		}
		return true; 
	}
	
	
	
}



class UninformedSearch
{
public static void main(String args[])
{
	Scanner sc = new Scanner(System.in);
	System.out.println("Enter the size of the board:");
	int n = sc.nextInt();
	//DFS s = new DFS(n);
	//s.queenSolver(1, n);
	
	BFS b = new BFS(n); 
	b.queenSolver(1, n);
	
	// Depth limited search 
//	int depth = 3; 
//	Deepening dl = new Deepening(n, depth); 
//	dl.queenSolver(1,  n);
//    if(dl.getStatus())
//    {
//    	System.out.println("Failed to find a solution at depth : " + depth);
//    }
	
    //IDS 
//    for(int d = 1; d <= n; d++)
//    {
//    	Deepening id = new Deepening(n, d); 
//    	id.queenSolver(1, n);
//    	
//    	if(id.getStatus())
//    	{
//    		System.out.println("Failed to find a solution at depth: " + d);
//    	}
//    	else
//    	{
//    		System.out.println("Solution found at depth : " + d);
//    		break; 
//    	}
//    	
//    	
//    }
//    
    
	
	}
	
	
	
}
	
	
