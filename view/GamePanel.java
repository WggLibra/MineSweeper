package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;


public class GamePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	UI ui = null;
	private int edge;
	private int x;
	private int y;
	private int imageHeight;
	private int imageWidth;
	private Image imageBoard;
	//private MineArea mineArea;
	
	
	public GamePanel(UI ui){
		this.ui = ui;
		//mineArea = new MineArea(ui.rows,ui.colons,ui.size,ui.margin,ui.headHeight);
		//add(mineArea);
		edge = 3;
	}
	
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (imageBoard == null)
			return;
		//this.drawMapFrame(0, 0, getWidth(), getHeight(), edge, true, g);
		this.drawMapFrame(x - edge, x - edge, imageWidth + edge * 2, y - (x - edge) * 2, edge, false, g);//upper boarder
		this.drawMapFrame(x - edge, y - edge, imageWidth + edge * 2, imageHeight + edge * 2, edge, false, g);//lower boarder
		g.drawImage(imageBoard, x, y, null);//Mine area
		//URL imageURL = GamePanel.class.getResource("images/UNCLICKED.gif");
		//try {
			//Image image = ImageIO.read(imageURL);
			//g.drawImage(image, 0, 0, null);
		//} catch (IOException e) {
			//System.out.println("Fuck");
		//}
		/*
		URL imageURL = UI.class.getResource("images/UNCLICKED.gif");
		try {
			Image image = ImageIO.read(imageURL);
			for(int i=0;i<ui.rows;i++){
				for(int j=0;j<ui.colons;j++){
					g.drawImage(image, i*ui.size, j*ui.size, null);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	//draw the map side frame
	private void drawMapFrame(int x, int y, int width, int height, int side,
			boolean raised, Graphics g) {

		//the sets of coordinate of side Polygons
		int[][] setX = { { x, x + width, x + width - side, x + side },//up
				{ x + width - side, x + width, x + width, x + width - side },//right
				{ x + side, x + width - side, x + width, x },//down
				{ x, x + side, x + side, x } };//left

		int[][] setY = {
				{ y, y, y + side, y + side },//up
				{ y + side, y, y + height, y + height - side },//right
				{ y + height - side, y + height - side, y + height, y + height },//down
				{ y, y + side, y + height - side, y + height } };//left
		
		//draw the side polygons
		for (int i = 0; i <= 3; i++) {
			if (raised) {//raised
				switch (i) {//set color
				case 0:
					g.setColor(new Color(244, 242, 232));					
					break;
				case 1:
					g.setColor(new Color(152, 150, 139));
					break;
				case 2:
					g.setColor(new Color(102, 101, 93));
					break;
				case 3:
					g.setColor(new Color(240, 238, 224));
					break;
				}
			} else {//lowered
				switch (i) {//set color
				case 0:
					g.setColor(new Color(100, 100, 100));
					break;
				case 1:
					g.setColor(new Color(240, 240, 240));
					break;
				case 2:
					g.setColor(new Color(250, 250, 250));
					break;
				case 3:
					g.setColor(new Color(120, 120, 120));
					break;
				}
			}
			g.fillPolygon(setX[i], setY[i], 4);//draw
		}
	}
	
	public void setEdge(int edge) {
		this.edge = edge;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setImageBoard(Image imageBoard) {
		this.imageBoard = imageBoard;
		this.imageWidth = imageBoard.getWidth(null);
		this.imageHeight = imageBoard.getHeight(null);
	}
	
	/*
	private class MineArea extends JPanel{

		private int rows;
		private int colons;
		private int size;
		private int x;
		private int y;
		
		public MineArea(int rows,int colons, int size, int x, int y) {
			this.rows = rows;
			this.colons = colons;
			this.size = size;
			this.x = x;
			this.y = y;
			
			setLocation(x, y);
			setSize(colons*size, rows*size);
			//setBackground(Color.GREEN);
			setVisible(true);
			
		}
		
		public void draw(){
			for (int i = 0; i < rows*size; i++) {
				for (int j = 0; j < colons*size; j++) {
					//ui.drawImage(Images.UNCLICKED, i, j);
				}
			}
		}
		
	}
	*/
}
