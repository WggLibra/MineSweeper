package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import model.Location;
import model.MineBoard;
import view.UI;

	

public class CoreAction implements ActionListener, MouseListener{
	
	private MineBoard board;
	private UI ui;
	private Location location;
	
	public CoreAction(MineBoard board, UI ui){
		this.board = board;
		this.ui = ui;
		board.addObserver(ui);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		board.restart(board.getRows(), board.getColons(), board.getMineNumber());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			leftClick(e);
		}
		else if(e.getButton() == MouseEvent.BUTTON3){
			rightClick(e);
		}
		else if(e.getButton() == MouseEvent.BUTTON2){
			bothClick(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	private void rightClick(MouseEvent e){
		this.location = ui.getLocation(e);
		if(location != null){
			board.rightClicked(location);
			ui.updateImage();
		}
	}
	
	private void leftClick(MouseEvent e){
		this.location = ui.getLocation(e);
		if(location != null){
			board.leftClicked(location);
			ui.updateImage();
		}
	}
	
	private void bothClick(MouseEvent e){
		this.location = ui.getLocation(e);
		if(location != null){
			board.bothClick(location);
			ui.updateImage();
		}
	}
	
}
