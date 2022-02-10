package floodit2;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame; 
import javax.swing.JButton; 
import java.awt.GridLayout; 
 
public class Grid {
 
        public JFrame frame=new JFrame(); 
        static JButton[][] grid; 
 
        public Grid(int width, int length,block[][] board){ 
                frame.setLayout(new GridLayout(width,length)); 
                grid=new JButton[width][length]; 
                for(int y=0; y<length; y++){
                        for(int x=0; x<width; x++){
                                grid[x][y]=new JButton(""); 
                                if(board[x][y].color==1) grid[x][y].setBackground(Color.RED);
                                else if(board[x][y].color==2) grid[x][y].setBackground(Color.BLUE);
                                else if(board[x][y].color==3) grid[x][y].setBackground(Color.GREEN);
                                else if(board[x][y].color==4) grid[x][y].setBackground(Color.YELLOW);
                                else if(board[x][y].color==5) grid[x][y].setBackground(Color.PINK);
                                else if(board[x][y].color==6) grid[x][y].setBackground(Color.CYAN);
                                
                                
                                grid[x][y].setOpaque(true);
                                grid[x][y].setPreferredSize(new Dimension(40, 40));
                                frame.add(grid[x][y]); 
                        }
                }
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack(); 
                frame.setVisible(true); 
        }
        Grid(){
            
        }
        
        public static void main(int n, block[][] board) {
               
                
                new Grid(n,n,board);
        }
}