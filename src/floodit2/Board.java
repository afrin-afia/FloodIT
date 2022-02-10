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

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import javax.swing.JOptionPane;


class block {
    public int color,taken,edger,edged,edgel,edgeu;
    
    public block(Scanner in){
        color= in.nextInt();
        taken=-1;       //which node group this block belongs to
        edger=-1;
        edged=-1;
        edgel=-1;
        edgeu=-1;
    }
    public block(){
        color=-1;
        taken=-1;
    }
    
}

public class Board 
{
	private int g;
	public block[][] board;
	public Board parent = null;
	public Board(block[][] colors, Board parent,int g) 
	{	
			
                this.board= new block[colors.length][colors.length];
		for(int i=0;i<colors.length;i++){
                    for(int j=0;j<colors.length;j++){
                        this.board[i][j]= new block();
                        this.board[i][j].color= colors[i][j].color;
                        this.board[i][j].taken= colors[i][j].taken;
                    }
                }
                this.parent=parent;						 													 					//row i, column j)
		this.g = g; 
	}
	
	public int f() 
	{
		return g+heuristic1();
	}
	// returns the estimated distance from current board to final state using heuristic1
	public int heuristic1() 
	{       //this is admissible, but slower
		int h=0,color;
                LinkedList<Integer> colorList= new LinkedList();
                for(int i=0;i<this.board.length;i++){
                    for(int j=0;j<this.board[i].length;j++){
                            color= this.board[i][j].color;
                            if(!colorList.contains(color)){
                                colorList.add(color);
                                h++;
                            }
                    }
                }
                return h;	
	}
	// returns the estimated distance from current board to final state using heuristic2
	public int heuristic2()  
	{
                //non-admissible but faster heuristic. count #unmerged blocks till now
                int h=0;
                
                int[] colors= new int[7];
                for(int i=0;i<this.board.length;i++){
                        for(int j=0;j<this.board[i].length;j++){
                                this.board[i][j].taken=-1;
                        }
                }
                LinkedList<Integer>[] graphInit= makeGraph(this.board,FloodIt2.N,colors);
                for(int i=0;i<this.board.length;i++){
                        for(int j=0;j<this.board[i].length;j++){
                                if(this.board[i][j].taken !=1 ){
                                    h++;
                                }
                        }
                }
                return h;
	}
	// is this board the goal board? i.e., all color same. 
	public boolean isGoal() 
	{
		
            
                int color1= board[0][0].color;
                int flag=1;
                for (int x = 0; x < FloodIt2.N; x++){
                    for (int j = 0; j < FloodIt2.N; j++){
                            
                            if(board[x][j].color!=color1){
                                flag=0;
                                break;
                            }
                        if(flag==0) break;
                    }
                                    
                }
                if(flag==1) return true;  
                return false;
	}
	
	public Board getCopy(Board boardob)
	{
		Board nxtBoard = new Board(boardob.board,boardob, boardob.g+1) ;
		for (int i=0; i<boardob.board.length;i++)
		{
			for(int j=0;j<boardob.board[i].length;j++)
			{
					nxtBoard.board[i][j].color  = boardob.board[i][j].color;
                                       
			}
		}
		return nxtBoard;
	}
        
        
        public static int isNbValid(int nb_x,int nb_y,int n){
            if(nb_x<0 ||nb_x>=n || nb_y<0 || nb_y>=n) return 0;
            else return 1;
        }
        
        
        
        public static  LinkedList<Integer>[] makeGraph(block[][] board, int N, int[] colors){
                        //now we've found the 2d array, need to make a graph from it. 
                nodeNum=0;
                nodeColor.removeAll(nodeColor);

                int nb_x,nb_y,nb_valid;
                
                for (int i = 0; i < N; i++){
                        for (int j = 0; j < N; j++){
                           
                            if(board[i][j].taken ==-1){  //this block not in a group, check if it can go in an existing group

                                 //check right nbr, if it can take me
                                nb_x= i;       
                                nb_y=j+1;
                                nb_valid= isNbValid(nb_x,nb_y,N);
                                if(nb_valid==1 && board[nb_x][nb_y].taken!=-1 ){
                                        if(board[nb_x][nb_y].color== board[i][j].color){
                                            board[i][j].taken =board[nb_x][nb_y].taken; //attach me with right nbr
                                            
                                            continue;
                                        }
                                }
                                
                                //check lower nbr, if it can take me
                                nb_x= i+1;       
                                nb_y=j;
                                nb_valid= isNbValid(nb_x,nb_y,N);
                                if(nb_valid==1 && board[nb_x][nb_y].taken!=-1 ){
                                        if(board[nb_x][nb_y].color== board[i][j].color){
                                            board[i][j].taken =board[nb_x][nb_y].taken; //attach me with lower nbr
                                            
                                            continue;
                                        }
                                }
                                
                            }
                            
                            //createEdge=0;
                            if(board[i][j].taken ==-1){//none has taken me, so creating new group
                                nodeNum++;
                                board[i][j].taken=nodeNum;
                                
                                //save my color into a linkedList(nodeColor)
                                nodeColor.add(board[i][j].color);
                                if(colors[board[i][j].color]==0){
                                    colors[board[i][j].color]++;
                                    numColors++;
                                }
                                else colors[board[i][j].color]++;
                            }
                            nb_x= i;       //check right nbr to take it in my group
                            nb_y=j+1;
                            
                            nb_valid= isNbValid(nb_x,nb_y,N);
                            
                            if(nb_valid==1 ){
                                
                                if(board[i][j].color != board[nb_x][nb_y].color){
                                    //make an edge 
                                        board[i][j].edger=1;
                                    
                                }
                                else if( board[i][j].color== board[nb_x][nb_y].color && board[nb_x][nb_y].taken==-1){
                                    //nbr is valid and color matched, so take this nbr in my group!
                                    board[nb_x][nb_y].taken=board[i][j].taken;
                                    
                                }
                                
                            }
                            
                            //check left nbr
                            nb_x= i;
                            nb_y= j-1;
                            nb_valid= isNbValid(nb_x,nb_y,N);
                            
                            if(nb_valid==1 ){
                                if( board[i][j].color != board[nb_x][nb_y].color){
                                    //nbr is valid and color matched, so take this nbr in my group!
                                    board[i][j].edgel =1;
                                    
                                }
                                else if( board[i][j].color== board[nb_x][nb_y].color && board[nb_x][nb_y].taken==-1){
                                     board[nb_x][nb_y].taken=board[i][j].taken;
                                    
                                }
                            }
                            //check upper nbr
                            nb_x= i-1;
                            nb_y= j;
                            nb_valid= isNbValid(nb_x,nb_y,N);
                            
                             if(nb_valid==1 ){
                                if( board[i][j].color != board[nb_x][nb_y].color){
                                    //nbr is valid and color dont matched, so dont take this nbr in my group!
                                    board[i][j].edgeu =1;
                                    
                                }
                                else if( board[i][j].color== board[nb_x][nb_y].color && board[nb_x][nb_y].taken==-1){
                                     board[nb_x][nb_y].taken=board[i][j].taken;
                                     
                                }
                            }
                           
                            //check lower nbr
                            nb_x= i+1;
                            nb_y= j;
                            nb_valid= isNbValid(nb_x,nb_y,N);
                            
                            if(nb_valid==1 ){
                                if( board[i][j].color != board[nb_x][nb_y].color){
                                    //nbr is valid and color matched, so take this nbr in my group!
                                    board[i][j].edged =1;
                                    
                                }
                                else if( board[i][j].color== board[nb_x][nb_y].color && board[nb_x][nb_y].taken==-1){
                                     board[nb_x][nb_y].taken=board[i][j].taken;
                                     
                                }
                            }
                            
                        }
                }
                
                
                //make graph by putting edges
                LinkedList<Integer> graph[] = new LinkedList[nodeNum+1];// System.out.println("nodenum= "+nodeNum);
                for (int i=1;i<=nodeNum;i++){
                        graph[i] = new LinkedList<Integer>();
                }
                 
                for (int i = 0; i < N; i++){
                        for (int j = 0; j < N; j++){
                            
                            nb_x= i;
                            nb_y=j+1;
                            nb_valid= isNbValid(nb_x,nb_y,N);
                            if(board[i][j].edger ==1 && nb_valid==1 ){
                                
                                if(!graph[board[i][j].taken].contains(board[nb_x][nb_y].taken )){
                                    
                                    if(board[i][j].taken!=board[nb_x][nb_y].taken)    
                                            graph[board[i][j].taken].add(board[nb_x][nb_y].taken);
                                } 
                                
                                if(!graph[board[nb_x][nb_y].taken].contains(board[i][j].taken)){
                                    if(board[i][j].taken!=board[nb_x][nb_y].taken)  
                                        graph[board[nb_x][nb_y].taken].add(board[i][j].taken);
                                }
                                
                            }
                            
                            nb_x= i+1;
                            nb_y=j;
                            nb_valid= isNbValid(nb_x,nb_y,N);
                            if(board[i][j].edged ==1 && nb_valid==1 ){
                                
                                if(!graph[board[i][j].taken].contains(board[nb_x][nb_y].taken)){
                                   if(board[i][j].taken!=board[nb_x][nb_y].taken) 
                                        graph[board[i][j].taken].add(board[nb_x][nb_y].taken);
                                }
                                
                                if(!graph[board[nb_x][nb_y].taken].contains(board[i][j].taken)){
                                    if(board[i][j].taken!=board[nb_x][nb_y].taken)  
                                        graph[board[nb_x][nb_y].taken].add(board[i][j].taken);
                                }
                                
                            }
                            
                            //left nbr
                            nb_x=i;
                            nb_y=j-1;
                            nb_valid= isNbValid(nb_x,nb_y,N);
                            if(board[i][j].edgel ==1 && nb_valid==1 ){
                                if(!graph[board[i][j].taken].contains(board[nb_x][nb_y].taken)){
                                    
                                    if(board[i][j].taken!=board[nb_x][nb_y].taken) 
                                        graph[board[i][j].taken].add(board[nb_x][nb_y].taken);
                                }
                                if(!graph[board[nb_x][nb_y].taken].contains(board[i][j].taken)){
                                     
                                    if(board[i][j].taken!=board[nb_x][nb_y].taken) 
                                        graph[board[nb_x][nb_y].taken].add(board[i][j].taken);
                                }
                            }
                            
                            //upper
                            nb_x=i-1;
                            nb_y=j;
                            nb_valid= isNbValid(nb_x,nb_y,N);
                            if(board[i][j].edgeu ==1 && nb_valid==1 ){
                                if(!graph[board[i][j].taken].contains(board[nb_x][nb_y].taken)){
                                    
                                    if(board[i][j].taken!=board[nb_x][nb_y].taken) 
                                        graph[board[i][j].taken].add(board[nb_x][nb_y].taken);
                                }
                                if(!graph[board[nb_x][nb_y].taken].contains(board[i][j].taken)){
                                    if(board[i][j].taken!=board[nb_x][nb_y].taken)     
                                        graph[board[nb_x][nb_y].taken].add(board[i][j].taken);
                                }
                            }
                            
                            
                        }
                }
                
//                //PRINT 
//                for( int i=1; i<=nodeNum;i++ ){
//                        System.out.print(i+"-->");
//                        for(int j:graph[i]){
//                           System.out.print(j +"  ");
//                     }
//                     System.out.println("");
//                     
//                     
//                 }
                return graph;

                 //omuk node er color ki- ta janar jonno set an array
                  //int nodeColor[] = new int [nodeNum+1];
                
    }
        

	// all neighboring boards
	public ArrayList<Board> neighbors() 
	{
            //this=zar nbr ber krte chai
            //System.out.println("NOW THIS=");
            Board tp= new Board(this.board,this.parent,this.get_g());
            for(int i=0; i<FloodIt2.N;i++){
                    for(int j=0;j<FloodIt2.N;j++){
                            
                            tp.board[i][j].taken=-1;
                    }
                }
            ///System.out.println(tp.toString());
            int[] colors= new int[7];
            LinkedList<Integer> nbrz= new LinkedList();
            
            LinkedList<Integer>[] graphInit= makeGraph(tp.board,FloodIt2.N,colors);
            
            ListIterator<Integer> iter1 = graphInit[1].listIterator();
            int toExplore=9999;
            //System.out.println("nbrz=");
            while(iter1.hasNext()){
                int n= iter1.next();    
                nbrz.add(n);//System.out.print(n+" ");
            } 


            ///////////////////////
            ArrayList<Board> nbrs= new ArrayList();
            //Board test=new Board(this.board,this,this.get_g()+1);;
            ListIterator<Integer> iterz = nbrz.listIterator();
            //int c=0;
            while(iterz.hasNext()){
                
                Board test= new Board(tp.board,tp,tp.get_g()+1); 

                toExplore= iterz.next();

                for(int i=0; i<FloodIt2.N;i++){
                    for(int j=0;j<FloodIt2.N;j++){
                            if(test.board[i][j].taken==1){
                                //System.out.println("changing color frm to..."+test.board[i][j].color+"  "+nodeColor.get(toExplore-1));
                            
                                test.board[i][j].color= nodeColor.get(toExplore-1);
                            }
                            test.board[i][j].taken=-1;
                    }
                }
                nbrs.add(test);

            }
           
            return nbrs;
                
	}
	// does this board equal y?
	public boolean equals(Object y) 
	{
		return false;
	}
	
	public int get_g()
	{
		return g;
	}
	
	public Board getParent() {
		return parent;
	}

	// string representation of the
	//board (in the output format specified below)
	public String toString() 
	{
		String str="\ng: "+g+" h:"+heuristic1()+"\n";
		for (int i=0; i<board.length;i++)
		{
			for(int j=0;j<board[i].length;j++)
			{
				str += (board[i][j].color+" ");
			}
			str +="\n";
		}
		return str;


	}
        public String toStringTaken() 
	{
		String str="\ng: "+g+" h:"+heuristic1()+"\n";
		for (int i=0; i<board.length;i++)
		{
			for(int j=0;j<board[i].length;j++)
			{
				str += (board[i][j].taken+" ");
			}
			str +="\n";
		}
		return str;

	}

	// for testing purpose
	//public static void main(String[] args) 
	//{
			
	//}
         static LinkedList<Integer> nodeColor= new LinkedList() ;
         //static int colors[];
         static int numColors,nodeNum;
}




