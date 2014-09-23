package view;

import java.util.Observable;
import java.util.Observer;

import model.NotifyDoubted;
import model.NotifyFlagged;
import model.NotifyGG;
import model.NotifyRestart;
import model.NotifyReveal;
import model.NotifyTimerChange;
import model.NotifyUnclicked;
import model.NotifyWin;

public class TestObserve implements Observer{

	@Override
	public void update(Observable board, Object o) {
		if (o instanceof NotifyReveal){
			System.out.println("********");
		}
		else if(o instanceof NotifyFlagged){
			System.out.println("********");
		}
		else if(o instanceof NotifyDoubted){
			System.out.println("********");
		}
		else if(o instanceof NotifyUnclicked){
			System.out.println("********");
		}
		else if(o instanceof NotifyGG) {
			System.out.println("********");
		}
		else if(o instanceof NotifyRestart){
			System.out.println("********");
		}
		else if(o instanceof NotifyWin){
			System.out.println("********");
		}
		else if(o instanceof NotifyTimerChange){
			System.out.println("********");
		}
	}

}
