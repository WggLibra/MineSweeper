package JMineSweeper;

import javax.swing.SwingUtilities;

import model.MineBoard;
import view.UI;

public class JMineSweeper {
	
	public JMineSweeper(){
		try{
	            SwingUtilities.invokeLater
	            (
	                new Runnable() 
	                {
	                    public void run() 
	                    {
	                    	MineBoard board = new MineBoard(20, 35, 100);
	                    	new UI(board);
	                    	
	                    }
	               }
	           );

	        }
	        catch (Exception e){}
	}
	
	public static void main(String[] args){
		new JMineSweeper();
	}
	
}
