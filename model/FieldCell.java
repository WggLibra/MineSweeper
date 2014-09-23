package model;

public class FieldCell {
	private int type;
	private int status;
	private int value;
	
	public FieldCell(){
		reset();
	}
	
	public void reset(){
		setBlank();
		setUNCLICKED();
		value = 0;
	}
	
	public boolean isBlank(){
		if(type == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isNumber(){
		if(type == 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isMine(){
		if(type == 2){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void setBlank(){
		type = 0;
	}
	
	public void setNumber(){
		type = 1;
	}
	
	public void setMine(){
		type = 2;
	}
	
	public boolean isUNCLICKED(){
		if(status == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isREVEALED(){
		if(status == 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isFLAGGED(){
		if(status == 2){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isDOUBTED(){
		if(status == 3){
			return true;
		}
		else{
			return false;
		}
	}
	
	/*public boolean isRightFlagged(){
		if(status == 4){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isWrongFlagged(){
		if(status == 5){
			return true;
		}
		else{
			return false;
		}
	}*/
	
	public void setUNCLICKED(){
		status = 0;
	}
	
	public void setREVEALED(){
		status = 1;
	}
	
	public void setFLAGGED(){
		status = 2;
	}
	
	public void setDOUBTED(){
		status = 3;
	}
	
	/*public void setRightFlagged(){
		status = 4;
	}
	
	public void setWrongFlagged(){
		status = 5;
	}*/
	
	public int getValue(){
		return value;
	}
	
	public void addValue(){
		value++;
	}
	
}
