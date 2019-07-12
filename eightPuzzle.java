import java.util.ArrayList;
import java.util.HashMap; 
import java.util.Map; 
import java.util.Scanner; 


class points
{
	private int x; 
	private int y; 
	
	points(int x, int y)
	{
		this.x = x; 
		this.y = y; 
	}
	
	public int get_x()
	{
		return x; 
	}
	
	public int get_y()
	{
		return y; 
	}

}

class PuzzleSolver
{
	private int initial[][]; 
	private int goal[][]; 
	private int temp[][];
	private ArrayList<int[][]> frontier; 
	private int n = 3; 
	private boolean found_sol = false; 
	private long total_nodes = 0; 
	private HashMap<String, Integer> visited; 
	private String tag; 
	private points goalInfo[]; 
	
	PuzzleSolver(int initial[][], int goal[][])
	{
		this.initial = new int[n][n]; 
		this.goal = new int[n][n]; 
		temp = new int[n][n]; 
		visited = new HashMap<>();
		goalInfo = new points[n*n]; 
		
		this.initial = initial; 
		this.goal = goal; 
		
		frontier = new ArrayList<int[][]>(); 	
		
		
		// Build up the co-ordinates table to speed up manhattan distance calculation 
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				goalInfo[goal[i][j]] = new points(i, j); 
			}
		}
		
		
	}
	
	
	public void bfs()
	{
	    reset(); 
		while(!found_sol)
		{
			// Get the current state, as BFS is FIFO, grab the shallowest node 
			int cur_state[][] = frontier.get(0); 
			
			// Remove the state from frontier 
			frontier.remove(0); 
			
			total_nodes++; 
			
			// Print the current state 
			System.out.println("Current node:" + total_nodes);
			printBoard(cur_state); 
			
			// Check whether the current state is the final state 
			
			if(isGoal(cur_state))
			{
				found_sol = true; 
				System.out.println("Solution reached!");
				System.out.println("BFS: Nodes travesed: " + total_nodes);
			}
			
			else
			{
				// Explore the children, we pass -1 because depth info doesn't matter in case of BFS
				create_children(cur_state, -1); 
			}

		}
	}
	
	
	public void dfs()
	{
		reset();
		
		while(!found_sol)
		{
			// As DFS is LIFO, get the last state first 
			int cur_state[][] = frontier.get(frontier.size()-1); 
			total_nodes++; 
			
			// Remove the last state from frontier 
			frontier.remove(frontier.size()-1); 
			
			// Print the current state 
			System.out.println("Current node:" + total_nodes);
			printBoard(cur_state); 
			
			// Check whether the current state is the final state 
			
			if(isGoal(cur_state))
			{
				found_sol = true; 
				System.out.println("Solution reached!");
				System.out.println("DFS: Nodes travesed: " + total_nodes);
			}
			
			else
			{
				// Explore the children 
				create_children(cur_state, -1); 
			}
		}
		
	}
	
	
	public void dls(int limit)
	{
		int depth = 0; 
		reset(); 
		
		while(!found_sol && frontier.size() != 0)
		{
			
		// As DFS is LIFO, get the last state first 
		int cur_state[][] = frontier.get(frontier.size()-1); 
		total_nodes++; 
		
		// Remove the last state from frontier 
		frontier.remove(frontier.size()-1); 
		
		// Print the current state 
		System.out.println("Current node:" + total_nodes);
		printBoard(cur_state); 
		
		// Check whether the current state is the final state 
		
		if(isGoal(cur_state))
		{
			found_sol = true; 
			System.out.println("Solution reached for DLS with Limit: " + limit);
			System.out.println("DLS: Nodes travesed: " + total_nodes);
		}
		
		else
		{
		    tag = create_tag(cur_state); 
		    // Get the depth info about the current state 
		    depth = visited.get(tag); 
		    
		    
			// Explore the children, the children will have a depth of parent's depth + 1  
			if(depth < limit)
			{
				create_children(cur_state, depth+1); 
			}
		}
		

		}
		
		if(!found_sol)
		{
			System.out.println("Failed to find a solution with depth limit of " + limit);
			System.out.println("DLS: Nodes traversed: " + total_nodes);
		}

	}
	
	
	public void ids()
	{
		int limit = 0; 
		int depth = 0; 
		
		while(!found_sol)
		{
			limit++; 
			depth = 0; 
			reset(); 
			
			while(frontier.size() != 0 && !found_sol)
			{
				// As DFS is LIFO, get the last state first 
				int cur_state[][] = frontier.get(frontier.size()-1); 
				total_nodes++; 
				
				// Remove the last state from frontier 
				frontier.remove(frontier.size()-1); 
				
				// Print the current state 
				System.out.println("Current node:" + total_nodes);
				printBoard(cur_state); 
				
				// Check whether the current state is the final state 
				
				if(isGoal(cur_state))
				{
					found_sol = true; 
					System.out.println("Solution reached for IDS with limit: " + limit);
					System.out.println("IDS: Nodes travesed: " + total_nodes);
				}
				
				else
				{
					tag = create_tag(cur_state); 
				    // Get the depth info about the current state 
				    depth = visited.get(tag); 
				    
					// Explore the children, the children will have a depth of parent's depth + 1  
					if(depth < limit)
					{
						create_children(cur_state, depth+1); 
					}
				}
			}
			
			if(!found_sol)
			{
				System.out.println("For IDS with limit: " + limit + " no solution was found");
				System.out.println("IDS: Nodes traversed: " + total_nodes);
			}
			
		}

	}
	
	public void greedyBFS()
	{
		
		reset(); 
		
		int best_move = 0; // The best move in the beginning is the first state (initial state) 
		int dist = Integer.MAX_VALUE; 
		int temp_dist = 0; 
		
		while(!found_sol)
		{
			int cur_state[][] = frontier.get(best_move); 
			total_nodes++; 
			// Print the current state 
			System.out.println("Current node:" + total_nodes);
			printBoard(cur_state); 
			
			// Check whether the current state is the final state 
			if(isGoal(cur_state))
			{
				found_sol = true; 
				System.out.println("Solution found!");
				System.out.println("Greedy BFS: Nodes travesed: " + total_nodes);
			}
			
			else
			{
				// Explore it's neighbors 
				create_children(cur_state, -1);
				
				// Find the one in the frontier which has the minimum heuristic 
				dist = Integer.MAX_VALUE;
				
				for(int i = 0; i < frontier.size(); i++)
				{
					temp = frontier.get(i); 
					temp_dist = manhattan(temp); 
					
					if(temp_dist < dist)
					{
						dist = temp_dist; 
						best_move = i; 
					}
				
				}
				
			}
			
		
		}
	}
	
	
	public void astar()
	{
		reset(); 
		int depth = 0; // Our depth will be our cost function g(n) and h(n) will be manhattan 
		int best_move = 0; // The best move in the beginning is the first state (initial state) 
		int dist = Integer.MAX_VALUE; 
		int temp_dist = 0; 
		
		while(!found_sol)
		{
			int cur_state[][] = frontier.get(best_move); 
			total_nodes++; 
			
			frontier.remove(best_move);
					
			// Print the current state 
			System.out.println("Current node:" + total_nodes);
			printBoard(cur_state); 
			
			// Check whether the current state is the final state 
			if(isGoal(cur_state))
			{
				found_sol = true; 
				System.out.println("Solution found!");
				System.out.println("A*: Nodes travesed: " + total_nodes);
			}
			
			else
			{
				tag = create_tag(cur_state); 
				depth = visited.get(tag); 
				
				// Explore it's neighbors 
				
				create_children(cur_state, depth+1);
				
				// Find the one in the frontier which has the minimum cost h(n) + g(n)  
				dist = Integer.MAX_VALUE;
				
				for(int i = 0; i < frontier.size(); i++)
				{
					temp = frontier.get(i); 
					temp_dist = manhattan(temp); 
					tag = create_tag(temp); 
					depth = visited.get(tag); 
					
					if(temp_dist + depth < dist)
					{
						dist = temp_dist + depth; 
						best_move = i; 
					}
				
				}
				
			}
			
		
		}

	}
	
	
	public int manhattan(int cur_state[][])
	{
		int dist = 0; 
		int x1, y1; 
        // We will take advantage of the fact that the goal state is going to remain the same, so we are always aware of the goal co-ordinates

		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				// Don't calculate for the empty tile 
				if(cur_state[i][j] != 0)
				{
					x1 = goalInfo[cur_state[i][j]].get_x(); 
					y1 = goalInfo[cur_state[i][j]].get_y(); 
					
					dist += Math.abs(x1 - i) + Math.abs(y1 - j); 
				}
		
			}
		}
		
		return dist; 
	}
	
	public void reset()
	{
		// Clean out everything before running a different search algorithm 
		total_nodes = 0; 
		found_sol = false; 
		frontier.clear();
		visited.clear();
		
		// Add initial state to the frontier 
		frontier.add(initial); 
		
		// Add initial to visited, as the root (initial) is at depth 0, pass depth as 0 
		visited.put(create_tag(initial), 0);
	}
	
	public String create_tag(int cur_state[][])
	{
		StringBuilder tag = new StringBuilder();
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
				tag.append(Integer.toString(cur_state[i][j]));
		}
		
		
		return String.valueOf(tag); 

	}
	
	public void printBoard(int cur_state[][])
	{
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
				System.out.print(cur_state[i][j] + "    ");
			
		    System.out.println("");
		}
		
		System.out.println("");
		
	}
	
	public boolean isGoal(int cur_state[][])
	{

		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				if(cur_state[i][j] != goal[i][j])
				{
					return false;  
				}
		
			}
		}
		
		return true; 

	}
	
	public int[][] clone2d(int arr[][])
	{
		int new_arr[][] = new int[n][n]; 
		
		for(int i = 0; i < n; i++)
		{
		    new_arr[i] = arr[i].clone(); 
			
		}
		
		return new_arr; 
		
	}
	
	public void create_children(int cur_state[][], int depth)
	{
		// First find the empty tile (we denote it by 0) 
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				if(cur_state[i][j] == 0)
				{
					// Now generate new states by moving neighboring tiles 					
					
					// If it's the 0th row, don't perform the down operation 
					if(i == 0)
					{
						// If it's the 0th column, don't perform the right operation 
						if(j == 0)
						{
							// Perform left operation
							temp = clone2d(cur_state);  
							temp[i][j] = temp[i][j+1]; 
							temp[i][j+1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
						
							
							// Perform up operation 
							temp = clone2d(cur_state);
							temp[i][j] = temp[i+1][j]; 
							temp[i+1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
						}
						
						else if(j == n-1)
						{
							// Perform right operation 
							temp = clone2d(cur_state);
							temp[i][j] = temp[i][j-1]; 
							temp[i][j-1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform up operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i+1][j]; 
							temp[i+1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							} 
						}
						
						else
						{
							// Perform left operation
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i][j+1]; 
							temp[i][j+1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform right operation 
							temp = clone2d(cur_state);
							temp[i][j] = temp[i][j-1]; 
							temp[i][j-1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform up operation 
							temp = clone2d(cur_state);
							temp[i][j] = temp[i+1][j]; 
							temp[i+1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							} 
					
						}
						
					}
					
					// If it's the last row, don't perform the up operation 
					else if(i == n-1)
					{
						
						if(j == 0)
						{
							// Perform left operation
							temp = clone2d(cur_state);
							temp[i][j] = temp[i][j+1]; 
							temp[i][j+1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							} 
							
							// Perform down operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i-1][j]; 
							temp[i-1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
						}
						
						else if(j == n-1)
						{
							// Perform right operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i][j-1]; 
							temp[i][j-1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							} 
							
							// Perform down operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i-1][j]; 
							temp[i-1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
				
						}
						
						else
						{
							// Perform left operation
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i][j+1]; 
							temp[i][j+1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform right operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i][j-1]; 
							temp[i][j-1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform down operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i-1][j]; 
							temp[i-1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
			
						}
						
					}
					
					// All operations are permissible 
					else
					{
						if(j == 0)
						{
							// Perform left operation
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i][j+1]; 
							temp[i][j+1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform up operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i+1][j]; 
							temp[i+1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform down operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i-1][j]; 
							temp[i-1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
						}
						
						else if(j == n-1)
						{
							// Perform right operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i][j-1]; 
							temp[i][j-1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform up operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i+1][j]; 
							temp[i+1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform down operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i-1][j]; 
							temp[i-1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
						}
						
						else
						{
							// Perform left operation
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i][j+1]; 
							temp[i][j+1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform right operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i][j-1]; 
							temp[i][j-1] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform up operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i+1][j]; 
							temp[i+1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
							
							// Perform down operation 
							temp = clone2d(cur_state); 
							temp[i][j] = temp[i-1][j]; 
							temp[i-1][j] = 0; 
							tag = create_tag(temp); 
							if(!visited.containsKey(tag))
							{
								frontier.add(temp); 
								visited.put(tag, depth);
								
							}
				
						}
				
					}
					
				}
			}
		}
		
		
		
	}
	
}



class eightPuzzle
{
	public static void main(String args[])
	{
		
		/*
		 Examples: 
		 
		 
		 Initial state: 
		 1 2 3 
		 5 6 0
		 7 8 4 
		 
		 Goal state: 
		 1 2 3 
		 5 8 6
		 0 7 4  
		 
		Enter the initial state 
		1 2 3
		8 0 4 
		7 6 5 
		
		Enter the goal state: 
		8 1 3
		0 2 4
		7 6 5

		 */
		
		Scanner sc = new Scanner(System.in); 
		int n = 3; 
		int initial[][] = new int[n][n]; 
		int goal[][] = new int[n][n]; 
		
		
		System.out.println("Enter the initial state: ");
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				initial[i][j] = sc.nextInt(); 
			}
		}
		
		System.out.println("Enter the goal state: ");
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				goal[i][j] = sc.nextInt(); 
			}
		}
		

		PuzzleSolver puzObj = new PuzzleSolver(initial, goal); 
		
		
		//puzObj.bfs();
		//puzObj.dfs();
		//puzObj.dls(2);
		//puzObj.ids();
		
		//puzObj.greedyBFS();
		puzObj.astar();
		
		sc.close();
		
	}
	
	
}