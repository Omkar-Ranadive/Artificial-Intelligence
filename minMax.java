import java.util.ArrayList;
import java.util.Scanner; 


class board
{
	private String gameBoard[] = new String[9]; // 9 because, Tic Tac Toe has a 3x3 = 9 spaces 
	
	board()
	{
		// Initialize the board to empty spaces 
		for(int i = 0; i < 9; i++)
		{
			gameBoard[i] = " "; 
			
		}
	}
	
	public void printBoard()
	{
		System.out.println(gameBoard[0] + "  |  " + gameBoard[1] + "  |  " + gameBoard[2]);
		System.out.println(gameBoard[3] + "  |  " + gameBoard[4] + "  |  " + gameBoard[5]);
		System.out.println(gameBoard[6] + "  |  " + gameBoard[7] + "  |  " + gameBoard[8]);
	}
	
	public void updateBoard(int i, String turn)
	{
		if(!gameBoard[i].equals(" "))
		   System.out.println("Invalid move!");
		
		else
			gameBoard[i] = (turn.equals("MAX")?"0":"X"); 
		
	}
	public String[] getBoard()
	{
		return gameBoard; 
	}

}

class Result
{
	private String grid[]; 
	private int score; 
	private int depth; 
	
	Result(String grid[], int score, int depth)
	{
		this.grid = grid; 
		this.score = score; 
		this.depth = depth; 
	}
	
	// Function to update the current board state, once the AI agent has decide on its move 
	public void updateGrid(String grid[])
	{
		this.grid = grid; 
	}
	
	public int getScore()
	{
		return score; 
	}
	
	public int getDepth()
	{
		return depth; 
	}
	
	public String[] getBoard()
	{
		return grid; 
	}

}



// The AI agent class using minMax algorithm 
class player
{
	 //static int sDepth; // sDepth variable will control the normal depth 

	// Function to switch between the two players 
	public String toggle(String turn)
	{
		return (turn.equals("MIN")?"MAX":"MIN"); 
	}
	
	
	// Function to check if the winning state is reached 
	public int checkWin(String[] mat)
	{
		/*
		 Board dimensions based on indices: 
		 0 1 2
		 3 4 5 
		 6 7 8 
		 */
		
		if((mat[0].equals("0") && mat[1].equals("0") && mat[2].equals("0")) || (mat[3].equals("0") && mat[4].equals("0") && mat[5].equals("0")) ||
			(mat[6].equals("0") && mat[7].equals("0") && mat[8].equals("0")) || (mat[0].equals("0") && mat[3].equals("0") && mat[6].equals("0"))||
			(mat[1].equals("0") && mat[4].equals("0") && mat[7].equals("0")) || (mat[2].equals("0") && mat[5].equals("0") && mat[8].equals("0")) ||
			(mat[0].equals("0") && mat[4].equals("0") && mat[8].equals("0")) || (mat[2].equals("0") && mat[4].equals("0") && mat[6].equals("0"))		
			)
			return 1; 
		
		else if((mat[0].equals("X") && mat[1].equals("X") && mat[2].equals("X")) || (mat[3].equals("X") && mat[4].equals("X") && mat[5].equals("X")) ||
				(mat[6].equals("X") && mat[7].equals("X") && mat[8].equals("X")) || (mat[0].equals("X") && mat[3].equals("X") && mat[6].equals("X"))||
				(mat[1].equals("X") && mat[4].equals("X") && mat[7].equals("X")) || (mat[2].equals("X") && mat[5].equals("X") && mat[8].equals("X")) ||
				(mat[0].equals("X") && mat[4].equals("X") && mat[8].equals("X")) || (mat[2].equals("X") && mat[4].equals("X") && mat[6].equals("X"))		
				)
		return -1; 
			
		
		// Return 0 if winning state is not reached 
		return 0; 
	}
	
	// Function to check draw 
	public boolean checkDraw(String mat[])
	{
		// If any of the square is still empty, game goes on, else it's a draw 
		for(int i = 0; i < 9; i++)
		{
			if(mat[i].equals(" "))
				return false; 
		}
		return true; 
	}
	
	
	// Check if the game is over 
	public boolean checkOver(String mat[])
	{
		return (checkWin(mat) != 0)?true:false; 
	}
	
	
	// Generate the state space, depending on the current play 
	public ArrayList<String[]> treeGen(String mat[], String turn)
	{
		// Based on the current state, all possible moves will be added into nextNode list 
		ArrayList<String[]> nextNode = new ArrayList<String[]>(); 
		
		for(int i = 0; i < mat.length; i++)
		{
			// If an empty tile is present, perform a move 
			if(mat[i].equals(" "))
			{
				// Copy the current state into an temporary board state 
				String copyCurrent[] = mat.clone(); 
				
				// Check the turn (if MAX, play 0 or if MIN play X) 
				
				if(turn.equals("MAX"))
					copyCurrent[i] = "0"; 
				else
					copyCurrent[i] = "X";
				
				// Add to the list of children 
				nextNode.add(copyCurrent); 
			}
		}
		
		return (nextNode.size()==0)?null:nextNode; 
	}
	
	// Function to run min-max recursively to generate the tree nodes 
	public Result runMinMax(String mat[], String turn, int check, int depth)
	{
		// Note, the check variable is used, because we only want to update the board state using minmax if AI agent is playing. 
		// Therefore, if check =  1, update the grid 
		
		
		// Generate the nodes based on the current depth and state 
		ArrayList<String[]> nodes = treeGen(mat, turn); 
		
			
		if(nodes == null || checkOver(mat))
		{
			return new Result(mat, checkWin(mat), depth);
		}
		
		else
		{
			
			ArrayList<Result> listScore = new ArrayList<Result>(); 
			
			// Now, once the current children are generated, recursively go into every child state as the current state 
			for(int i = 0; i < nodes.size(); i++)
			{
				// Make sure to toggle the turn 
				listScore.add(runMinMax(nodes.get(i), toggle(turn), 1, depth+1)); 
			}
			
			// Essentially, get the best result at every depth 
			Result r = bestResult(listScore, turn); 
			
			if(check == 1)
				r.updateGrid(mat);
		    
			
			return r; 

		}

	}
	
	public Result bestResult(ArrayList<Result> listScore, String turn)
	{
		Result r = listScore.get(0); // Get the element at 0th position 
		
		if(turn.equals("MAX"))
		{
			for(int i = 1; i < listScore.size(); i++)
			{
				
				if((listScore.get(i).getScore() > r.getScore()) ||
				(listScore.get(i).getScore() == r.getScore() && listScore.get(i).getDepth() < r.getDepth()))
				{
					r = listScore.get(i);
				}
			}
		}
		else
		{
			for(int i = 1; i < listScore.size(); i++)
			{
				if((listScore.get(i).getScore() < r.getScore()) || 
				(listScore.get(i).getScore() == r.getScore() && listScore.get(i).getDepth() < r.getDepth()))
				{
					r = listScore.get(i);
				}
				
			}
			
		}
		
		return r; 
		
		
	}
	
	
}


class minMax
{
	// Note, MAX = AI Agent (0), MIN = User (X) 
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in); 
		
		// Create the board 
        board newBoard = new board();
        // Create AI agent 
        player agent = new player(); 
		System.out.println("The board values are as follows: ");
		System.out.println("0	1	2");
		System.out.println("3	4	5");
		System.out.println("6	7	8");
		System.out.println("Enter one of the above indices, to make a move at that position");
		while(true)
		{
			System.out.println("Cur board: ");
			newBoard.printBoard(); 
			
			System.out.println("Enter your move: (Value between 0-8)");
			int move = sc.nextInt(); 
			System.out.println("You played the following move: ");
			newBoard.updateBoard(move, "MIN"); 
			newBoard.printBoard(); 

			
			// Check if the game is won by somebody  
			if(agent.checkOver(newBoard.getBoard()))
			{
				if(agent.checkWin(newBoard.getBoard()) == 1)
					System.out.println("The AI agent won the game!");
				
				else if(agent.checkWin(newBoard.getBoard()) == -1)
					System.out.println("Congrats! You won the game!");
				
				break; 
			}
			else if(agent.checkDraw(newBoard.getBoard()))
			{
				System.out.println("It's a draw!");
				break; 
			}
			
			// If game is not over, ask the AI agent to play it's move 
			Result finalResult = agent.runMinMax(newBoard.getBoard(), "MAX", 0, 0);
			
			// Update the game board with the result of finalResult 
		    String curBoard[] = newBoard.getBoard(); 
		    String modBoard[] = finalResult.getBoard(); 
		    
		    for(int i = 0; i < 9; i++)
		    {
		    	if(curBoard[i].equals(" ") && modBoard[i].equals("0"))
		    	{
		    		newBoard.updateBoard(i, "MAX");
		    		break; 
		    	}

		    }
           
		    System.out.println("Agent played the following move: ");
			newBoard.printBoard(); 
			// Check if the game is won by somebody  
			if(agent.checkOver(newBoard.getBoard()))
			{
				if(agent.checkWin(newBoard.getBoard()) == 1)
					System.out.println("The AI agent won the game!");
				
				else if(agent.checkWin(newBoard.getBoard()) == -1)
					System.out.println("Congrats! You won the game!");
				
				break; 
			}
			else if(agent.checkDraw(newBoard.getBoard()))
			{
				System.out.println("It's a draw!");
				break; 
			} 
			
		}
		
		sc.close(); 
		
		
	}
	
	
}