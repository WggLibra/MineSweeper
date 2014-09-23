package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.MineBoard;
import view.UI;

public class MenuAction implements ActionListener{

	private MineBoard board;
	private UI ui;
	
	public MenuAction(MineBoard board, UI ui) {
		this.board = board;
		this.ui = ui;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Restart")){
			board.restart(board.getRows(), board.getColons(), board.getMineNumber());
		}
		else if(e.getActionCommand().equals("Easy")){
			board.restart(9,9,10);
		}
		else if(e.getActionCommand().equals("Hard")){
			board.restart(16,16,40);
		}
		else if(e.getActionCommand().equals("Hell")){
			board.restart(16,30,99);
		}
		else if(e.getActionCommand().equals("Custom")){
			ui.custom();
		}
		else if(e.getActionCommand().equals("Quit")){
			System.exit(0);
		}
		else if(e.getActionCommand().equals("About")){
			ui.about();
		}
	}

}
