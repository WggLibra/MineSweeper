package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Random;

import javax.swing.Timer;

public class MineBoard extends Observable{
	private boolean freezed;
	private int time;
	private int mineNumber;
	private int rows;
	private int columns;
	private int revealedMineCount;
	private int moved;
	private NotifyRestart restart;
	private NotifyReveal reveal;
	private NotifyFlagged flagged;
	private NotifyDoubted doubted;
	private NotifyUnclicked unclicked;
	private NotifyWin win;
	private NotifyGG gg;
	private NotifyTimerChange notifyTimerChange;
	private Timer timer;
	private FieldCell[][] cells;
	private boolean isNewGame;
	
	public MineBoard(){
		
	}
	
	public MineBoard(int rows,int columns,int mineNumber){
		restart = new NotifyRestart();
		reveal = new NotifyReveal();
		flagged = new NotifyFlagged();
		doubted = new NotifyDoubted();
		unclicked = new NotifyUnclicked();
		notifyTimerChange = new NotifyTimerChange();
		win = new NotifyWin();
		gg = new NotifyGG();
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!freezed || time < 1000){
					time++;
					setChanged();
					notifyTimerChange.setTime(time);
					notifyObservers(notifyTimerChange);
				}				
			}
		});
		freezed = true;
		restart(rows,columns, mineNumber);
	}
	
	public void restart(int rows, int columns, int mineNumber){
		if(this.rows == rows && this.columns == columns && this.mineNumber == mineNumber){
			for(int i=0;i<columns;i++){
				for(int j=0;j<rows;j++){
					cells[i][j].reset();
				}
			}
		}
		else{
			this.rows = rows;
			this.columns = columns;
			this.mineNumber = mineNumber;
			//this.type = type;
			cells = new FieldCell[columns][rows];
			for(int i=0;i<columns;i++){
				for(int j=0;j<rows;j++){
					cells[i][j] = new FieldCell();
				}
			}
		}
		mine();
		setMineNumbers();
		timer.stop();
		time = 0;
		moved = 0;
		revealedMineCount = 0;
		freezed = false;
		isNewGame = false;
		notifyRestart();
	}
	
	

	private void mine(){
		int x = 0;
		int y = 0;
		Random xRandom = new Random();
		Random yRandom = new Random();
		for(int i=0;i<mineNumber;i++){
			x = xRandom.nextInt(columns);
			y = yRandom.nextInt(rows);
			if(cells[x][y].isMine()){
				i--;
				continue;
			}
			else{
				cells[x][y].setMine();
			}
		}
	}
	
	private void setMineNumbers(){
		for(int i=0;i<columns;i++){
			for(int j=0;j<rows;j++){
				if(cells[i][j].isMine()){
					Location aMineLocation = new Location(i,j);
					setValues(aMineLocation);
				}
			}
		}
	}
	
	//Add the value "1" around a mine
	private void setValues(Location location){
		int x = location.getX();
		int y = location.getY();
		//System.out.println(x + "_" + y);
		for(int i=x-1;i<=x+1;i++){
			for(int j=y-1;j<=y+1;j++){
				if(isValidX(i) && isValidY(j)){
					if(!cells[i][j].isMine()){
						cells[i][j].setNumber();
						cells[i][j].addValue();
					}
				}
			}
		}
	}
	
	//Release the blank area automatically while clicked
	private void release(Location blankLocation){
		int temp_X = blankLocation.getX();
		int temp_Y = blankLocation.getY();
		Location temp = null;
		for(int i=temp_X-1;i<=temp_X+1;i++){
			for(int j=temp_Y-1;j<=temp_Y+1;j++){
				if(isValidX(i) && isValidY(j)){
					temp = new Location(i,j);
					if(isUnclickedState(temp)){
						setRevealedState(temp);
						notifyReveal(temp);
						if(isBlankCell(temp)){
						release(temp);
						}
					}
				}
			}
		}
	}
	
	private int countAroundFlags(Location location){
		int aroundFlags = 0;
		int x = location.getX();
		int y = location.getY();
		Location temp = null;
		for(int i=x-1;i<=x+1;i++){
			for(int j=y-1;j<=y+1;j++){
				if(isValidX(i) && isValidY(j)){
					temp = new Location(i,j);
					if(isFlaggedState(temp)){
						aroundFlags++;
					}
				}
			}
		}
		return aroundFlags;
	}
	
	private void checkAround(Location numberLocation){
		int x = numberLocation.x;
		int y = numberLocation.y;
		if(countAroundFlags(numberLocation) == cells[x][y].getValue()){
			for(int i=x-1;i<=x+1;i++){
				for(int j=y-1;j<=y+1;j++){
					if(isValidX(i) && isValidY(j)){
						Location temp = new Location(i,j);
						if(isUnclickedState(temp)){
							setRevealedState(temp);
							notifyReveal(temp);
						}
					}
				}
			}
		}
	}
	
	//Check if the parameter x out of bounds
	private boolean isValidX(int coordinate) {
		boolean isValid = true;
		if (coordinate < 0 || coordinate >= columns){
			isValid = false;
		}
		return isValid;
	}

	////Check if the parameter y out of bounds
	private boolean isValidY(int coordinate) {
		boolean isValid = true;
		if (coordinate < 0 || coordinate >= rows){
			isValid = false;
		}
		return isValid;
	}
	
	private void win(){
		freezed = true;
		notifyWin();
	}
	
	private void gameover(Location location){
		System.out.println("GG");
		timer.stop();
		freezed = true;
		Location temp = null;
		for(int i=0;i<columns;i++){
			for(int j=0;j<rows;j++){
				temp = new Location(i,j);
				if(isMineCell(temp)){
					//setRevealedState(temp);
					notifyGG(temp);//!!!
				}
			}
		}
	}
	
	private void notifyRestart(){
		this.setChanged();
		restart.setRows(rows);
		restart.setColumns(columns);
		notifyObservers(restart);
	}
	
	private void notifyReveal(Location location){
		if(!isNewGame){
			timer.restart();
			isNewGame = true;
		}
		setChanged();
		if(isNumberCell(location)){
			reveal.setType(getValue(location));
		}
		else if(isBlankCell(location)){
			reveal.setType(0);
		}
		reveal.setLocation(location);
		notifyObservers(reveal);
	}
	
	private void notifyFlagged(Location location){
		if(!isNewGame){
			timer.restart();
			isNewGame = true;
		}
		setChanged();
		flagged.setLocation(location);
		notifyObservers(flagged);
	}
	
	private void notifyDoubted(Location location){
		setChanged();
		doubted.setLocation(location);
		notifyObservers(doubted);
	}
	
	private void notifyUnclicked(Location location){
		setChanged();
		unclicked.setLocation(location);
		notifyObservers(unclicked);
	}
	
	private void notifyWin(){
		setChanged();
		win.setScore(getScore());
		notifyObservers(win);
	}
	
	private void notifyGG(Location location){
		this.setChanged();
		gg.setLocation(location);
		notifyObservers(gg);
	}
	
	public void leftClicked(Location location){
		if(!freezed){
			if(isUnclickedState(location) || isDoubtedState(location)){
				setRevealedState(location);
				moved++;
				setChanged();
				notifyTimerChange.setMoved(moved);
				notifyObservers(notifyTimerChange);
				if(!freezed){
					notifyReveal(location);
					if(isBlankCell(location)){
						release(location);
					}
				}
			}
		}
		if(revealedMineCount == rows*columns - mineNumber){
			win();
		}
	}
	
	public void rightClicked(Location location){
		if(!freezed){
			if(isUnclickedState(location)){
				setFlaggedState(location);
				notifyFlagged(location);
			}
			else if(isFlaggedState(location)){
				setDoubtedState(location);
				notifyDoubted(location);
			}
			else if(isDoubtedState(location)){
				setUnclickedState(location);
				notifyUnclicked(location);
			}
		}
	}
	
	public void bothClick(Location location){
		if(!freezed){
			if(isRevealedState(location) && isNumberCell(location)){
				checkAround(location);
			}
		}
	}
	
	private int getScore(){
		int score = 100*((rows*columns - mineNumber - moved)) - time*100;
		return score;
	}
	
	private int getValue(Location location){
		int value = 0;
		if(cells[location.getX()][location.getY()].isNumber()){
			value = cells[location.getX()][location.getY()].getValue();
		}
		else{
			value = -1;
		}
		return value;
	}
	
	private boolean isBlankCell(Location location){
		boolean isBlank = false;
		if(cells[location.getX()][location.getY()].isBlank()){
			isBlank = true;
		}
		return isBlank;
	}
	
	private boolean isMineCell(Location location){
		boolean isMined = false;
		if(cells[location.getX()][location.getY()].isMine()){
			isMined = true;
		}
		return isMined;
	}
	
	private boolean isNumberCell(Location location){
		boolean isNumber = false;
		if(cells[location.getX()][location.getY()].isNumber()){
			isNumber = true;
		}
		return isNumber;
	}
	
	private boolean isUnclickedState(Location location){
		boolean isUnclicked = false;
		if(cells[location.getX()][location.getY()].isUNCLICKED()){
			isUnclicked = true;
		}
		return isUnclicked;
	}
	
	private boolean isRevealedState(Location location){
		boolean isRevealed = false;
		if(cells[location.getX()][location.getY()].isREVEALED()){
			isRevealed = true;
		}
		return isRevealed;
	}
	
	private boolean isFlaggedState(Location location){
		boolean isFlagged = false;
		if(cells[location.getX()][location.getY()].isFLAGGED()){
			isFlagged = true;
		}
		return isFlagged;
	}
	
	private boolean isDoubtedState(Location location){
		boolean isDoubted = false;
		if(cells[location.getX()][location.getY()].isDOUBTED()){
			isDoubted = true;
		}
		return isDoubted;
	}

	private void setRevealedState(Location location){
		if(isUnclickedState(location) || isDoubtedState(location)){
			cells[location.getX()][location.getY()].setREVEALED();
		}
		if(isMineCell(location) && !freezed){
			gameover(location);
		}
		revealedMineCount++;
	}
	
	private void setFlaggedState(Location location){
		if(isUnclickedState(location)){
			cells[location.getX()][location.getY()].setFLAGGED();
		}
	}

	private void setDoubtedState(Location location){
		if(isFlaggedState(location)){
			cells[location.getX()][location.getY()].setDOUBTED();
		}
	}
	
	private void setUnclickedState(Location location){
		cells[location.getX()][location.getY()].setUNCLICKED();
	}
	
	public int getMineNumber() {
		return mineNumber;
	}

	public int getRows() {
		return rows;
	}

	public int getColons() {
		return columns;
	}

	public int getTime() {
		return time;
	}

	public int getMoved(){
		return moved;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	public void setMineNumber(int mineNumber) {
		this.mineNumber = mineNumber;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setColons(int colons) {
		this.columns = colons;
	}
	
}
