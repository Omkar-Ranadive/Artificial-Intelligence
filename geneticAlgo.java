import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random; 

class populator
{
	private int n; 
	private int value[]; 
	private int weight[]; 
	private int tWeight; 
	private int popProduced; 
	private Random rand; 
	private int gen_number; 
	private StringBuilder bestEntity; 
	private int bestFitness = -1; 
	populator(int n, int value[], int weight[], int tWeight)
	{
		this.n = n; 
		this.value = value; 
		this.weight = weight; 
		this.tWeight = tWeight; 
		rand = new Random(); 
		gen_number = 1; 
		
		// Create an initial population of 6 
		popProduced = 10; 
		
	}
	
	public StringBuilder get_best_entity()
	{
		return bestEntity; 
		
	}
    
	public int get_best_fitness()
	{
		return bestFitness; 
	}
	
	public StringBuilder[] popInitializer()
	{
		StringBuilder curGen[] = new StringBuilder[popProduced]; 
		
		for(int i = 0; i < popProduced; i++ )
		{
			curGen[i] = new StringBuilder(); 
			for(int j = 0; j < n; j++)
			{
				// Append either a 0 or 1, 1 specifies that the item is selected, 0 specifies it isn't 
				curGen[i].append(Integer.valueOf(rand.nextInt(2))); 
			}
	
		}
		return curGen; 
	}
		
	public int[] fitness(StringBuilder curGen[])
	{
		
		int fitnessPop[] = new int[popProduced]; 
		
		int pop_cost = 0; 
		int pop_weight = 0; 
		
		for(int i = 0; i < popProduced; i++)
		{
			// If total weight of population exceeds knapsack capacity then fitness = 0 
			// Else, take the total cost as fitness 
			
			pop_weight = 0; 
	    	pop_cost = 0; 
			
		    for(int j = 0; j < n; j++)
		    {
		    	
		    	if(curGen[i].charAt(j) == '1')
		    	{
		    		pop_weight += weight[j]; 
		    		pop_cost += value[j]; 
		    	}
		    }
		    
		    if(pop_weight > tWeight)
		    {
		    	fitnessPop[i] = 0; 
		    }
		    else
		    {
		    	fitnessPop[i] = pop_cost; 
		    }
			
		}
		
		return fitnessPop;
	}
	
	public void print_details(StringBuilder curGen[], int fitnessPop[])
	{
		System.out.println("Generation: " + gen_number);
		
		for(int i = 0; i < popProduced; i++)
		{
			System.out.println(curGen[i] + " :  Fitness " + fitnessPop[i]);
		}
	}
	
	public StringBuilder[] selection(StringBuilder curGen[], int fitnessPop[])
	{
		// Select the best half out of the total population 
		StringBuilder newGen[] = new StringBuilder[popProduced/2];
		
		
		// Arrange population as per fitness function 
		Integer index_of_pop[] = new Integer[fitnessPop.length]; 
		for(int i = 0; i < fitnessPop.length; i++)
			index_of_pop[i] = i; 
		
		
		// Now, actually arrange them in ascending order; i.e lowest fitness comes first
		// Therefore, the last index is the best breed 
		 Arrays.sort(index_of_pop, new Comparator<Integer>()
		 {
	       public int compare(final Integer i1, final Integer i2)
	       {
	    	   return -Integer.compare(fitnessPop[i1], fitnessPop[i2]);
	       }
		 }
		 );
		 
	
		 
		 
		
		 /*The top value has a (popProduced-1)/popProduced chance of getting chosen 
	       Then the next (popProduced-2)/popProduced and so on 
       	*/
		
//		 double uniformDist  = rand.nextDouble(); // Uniform distribution between 0 to 1 
//		 int index_selected = 0;  
//		 if(uniformDist < 0.6)
//			 index_selected = 1; 
//		 if(uniformDist < )
//		 
		 for(int i = 0; i < popProduced/2; i++)
		 {
			 newGen[i] = curGen[index_of_pop[i]]; 
		 }
		 
		 System.out.println("Selected population: ");
		 
		 for(int i = 0; i < popProduced/2; i++)
		 {
			 System.out.println(newGen[i] + "  Fitness: " + fitnessPop[index_of_pop[i]]);
			 
		 }
		 
		 // Add the top entity as the best solution if it's fitness is higher than previous top entity
		 if(fitnessPop[index_of_pop[0]] >= bestFitness)
		 {
			 bestEntity = newGen[0]; 
			 bestFitness = fitnessPop[index_of_pop[0]]; 
		 }
			 
		 
	
		return newGen; 
		
		
	}
	
	public StringBuilder[] crossOver(StringBuilder newGen[])
	{
		int partner1;
		int partner2; 
		int genes_of_1;
		int max = popProduced/2; 
		int min = 0; 
		int mutate_index; 
		StringBuilder newPop[] = new StringBuilder[popProduced]; 
		double uniformDist; 
		
		// Mate randomly between the selected population 
		for(int i = 0; i < popProduced; i++)
		{
			newPop[i] = new StringBuilder();
			partner1 = rand.nextInt(max - min) + min; // rand(Max - Min) + min will generate number in that range
			partner2 = rand.nextInt(max - min) + min; 
			
			// Choose how much of first will be selected 
			genes_of_1 = rand.nextInt(n); 
			
			for(int g1 = 0; g1 < genes_of_1; g1++)
			{
				newPop[i].append(newGen[partner1].charAt(g1)); 
			}
			
			for(int g2 = genes_of_1; g2 < n; g2++)
			{
				newPop[i].append(newGen[partner2].charAt(g2)); 
			}
			
			// Random mutation with a small probability 
			uniformDist = rand.nextDouble(); 
			
			if(uniformDist < 0.3)
			{
				mutate_index = rand.nextInt(n); 
				
				if(newPop[i].charAt(mutate_index) == '1')
					newPop[i].replace(mutate_index, mutate_index+1, String.valueOf('0')); 
				else
					newPop[i].replace(mutate_index, mutate_index+1, String.valueOf('1')); 
			}
			
		}
		
		// Almost always add the best entity to the list of selected population to ensure quicker convergence 
		uniformDist = rand.nextDouble(); 
		
		if(uniformDist < 0.7)
		{
			newPop[popProduced-1] = bestEntity; 
		}
		
		gen_number++; 
		
	
      return newPop; 		
	}
	
	
}


class geneticAlgo
{
	public static void main(String args[])
	{
		
		/*
		 Example input: 
		 Val: {60, 100, 120} 
		 Weight: {10, 20, 30} 
		 Max weight: 50 
		 Solution profit: 220 
		 Selected items {Item 3 and item 2} 
		 Input: 
		10 60
		20 100
		30 120

		 
		 
		 Example 2: 
		 Val {1, 4, 5, 7} 
		 Weight: 1, 3, 4, 5
		 Total: 7 
		 Best: 9 
		 Selected items: {Item 2 and 3} 
		 input: 
		 1 1 
		 3 4
		 4 5 
		 5 7 
		 
		 */
	
		
		Scanner sc = new Scanner(System.in); 
		System.out.println("Enter the total number of items:");
		int n = sc.nextInt(); 
		int val[] = new int[n]; 
		int weight[] = new int[n];
		System.out.println("Enter the weight and value of each item:");
		
		for(int i = 0; i < n; i++)
		{
			weight[i] = sc.nextInt(); 
			val[i] = sc.nextInt();
		}
		System.out.println("Enter the total weight:");
		int tWeight = sc.nextInt();
		
		
		
		
		populator worldObj = new populator(n, val, weight, tWeight); 
		
		// Create the initial population 
		StringBuilder curGen[] = worldObj.popInitializer(); 
		int cur_fitness[]; 
		StringBuilder new_gen[]; 
		// Now run the genetic process for some iterations 
		
		for(int i = 0; i < 10; i++)
		{
			// Get fitness of current population 
			cur_fitness = worldObj.fitness(curGen); 
			worldObj.print_details(curGen, cur_fitness);
			
			// Perform selection 
			new_gen = worldObj.selection(curGen, cur_fitness); 
			
			// Perform crossOver and mutation 
			curGen = worldObj.crossOver(new_gen); 
		
		}
		
		System.out.println();
		System.out.println("-------------------------------");
		System.out.println();
		System.out.println("The best solution is: ");
		System.out.println(worldObj.get_best_entity() + " with fitness: " + worldObj.get_best_fitness());
		
		System.out.println("The following items are selected: ");
		StringBuilder best = worldObj.get_best_entity(); 
		
		for(int i = 0; i < n; i++)
		{
			if(best.charAt(i) == '1')
			{
				System.out.println("Item: " + (i+1) + " Weight: " + weight[i] + " Cost:" + val[i]);
			}
		}
		
		System.out.println("Total gains: " + worldObj.get_best_fitness());
		
		
		
		sc.close();
		
		
	}
	
	
}