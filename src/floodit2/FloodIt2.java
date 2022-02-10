/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floodit2;

/**
 *
 * @author Meghla
 */
import static floodit2.Board.makeGraph;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
Main function takes, the input file, constructs a  Solver object. 
In the constructor solve the initial board with A* search.
Save the number of minimum moves and  sequence of solution in member variables.
The functions solution() and moves() return these member functions.
 */

/**/
public class FloodIt2 
{

	ArrayList<Board> solution; //stores the sequence of boards upto solution
	int minMove; //the number of moves
	
	
	class MyComparator implements Comparator<Board>
	{

		@Override
		public int compare(Board o1, Board o2) {
			// TODO Auto-generated method stub
			return o1.f()-o2.f();       //board.f= heuristic distance
		}
		
	}
	public int nodesExpanded=0;
	public FloodIt2(Board initial) // find a solution to the initial
	{							 // board (using the A* algorithm)

		PriorityQueue<Board> PQ = new PriorityQueue<>(10, new MyComparator());
		
		//insert initial node into a priority queue PQ
		PQ.add(initial);
		//System.out.println("Root: "+initial);
		//PQ always pops the node with lowest f(s)
		while (!PQ.isEmpty())
		{
			nodesExpanded++;
			Board node = PQ.poll();

                        if(node.isGoal())
			{
				minMove = node.get_g();
				System.out.println("Goal: "+node.toString());
				solution = new ArrayList<Board>();
                                //g.main(N,node.board);
                                solution.add(node);
                                while(true){
                                    Board p= node.parent;
                                    if(p==null) break;
                                    else{
                                        //g.main(N,p.board);
                                        solution.add(p);
                                        node=p;
                                    }
                                }
				//populate solution arraylist with the board sequence from initial to goal
				return;
			}
			
			ArrayList<Board> neighbors = node.neighbors();
			
			for(int i=0;i<neighbors.size();i++)
			{
				PQ.add(neighbors.get(i));

			}

		}

	}

	// Returns the minimum number of moves to solve
	public int moves() 			 
	{
		return minMove;
	}
	// sequence of boards in the
	// shortest solution
	public ArrayList<Board> solution() 
	{		
		return solution;
	}
        public static int N;
	public static void main(String args[]) 
	{
		
                
                Scanner in=null;
		try 
		{
			in = new Scanner(new File("input.txt"));
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true)
		{

			N = in.nextInt();
			if(N==0) return;

			block[][] colors = new block[N][N];
			for (int i = 0; i < N; i++)
				for (int j = 0; j < N; j++)
					colors[i][j] = new block(in);

			Board initial = new Board(colors,null,0);
                        
                        g= new Grid();

			FloodIt2 solver = new FloodIt2(initial);
			System.out.println("Nodes Expanded: "+ solver.nodesExpanded);
			System.out.println("Minimum number of moves = " + solver.moves());
			ArrayList<Board> solution = solver.solution();
                        System.out.println("solution=");
			for (int i=0;i<solution.size();i++)
				System.out.println(solution.get(i));
                        
                        int s= solution.size()-1;
                        for (int i=s;i>=0;i--){
                            g.main(N,solution.get(i).board);
                             try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(FloodIt2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        }

			//for bonus - show_result_in_gui(solution);

		}
	}
        //public static LinkedList<Integer>[] graph;
        static Grid g;
	
}


