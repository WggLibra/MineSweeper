package view;

import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import JMineSweeper.JMineSweeper;

public class Images {
	
	public static final Image CLICKED = createImage("images/CLICKED.gif");
	public static final Image UNCLICKED = createImage("images/UNCLICKED.gif");
	public static final Image MINE = createImage("images/MINE.gif");
	public static final Image MARKED = createImage("images/MARKED.gif");
	public static final Image MARKED_MINE = createImage("images/MARKED_MINE.gif");
	public static final Image[] number = { createImage("images/1.gif"),
			createImage("images/2.gif"), createImage("images/3.gif"),
			createImage("images/4.gif"), createImage("images/5.gif"),
			createImage("images/6.gif"), createImage("images/7.gif"),
			createImage("images/8.gif") };

	protected static ImageIcon createImageIcon(String path) {
		ImageIcon icon = null;
		URL imgURL = JMineSweeper.class.getResource(path);
		if (imgURL != null) {
			icon = new ImageIcon(imgURL);
		} 
		else {
			System.err.println("Couldn't find file: " + path);
		}
		return icon;
	}

	@SuppressWarnings("finally")
	protected static Image createImage(String path) {
		Image image = null;
		try {
			URL imgURL = JMineSweeper.class.getResource(path);
			image = ImageIO.read(imgURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return image;
		}
	}
}
