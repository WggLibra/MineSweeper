package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import control.CoreAction;
import control.MenuAction;
import model.*;


public class UI extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private MineBoard board;
	private GamePanel gamePanel;
	private JButton restart;
	private Graphics g2;
	private CoreAction coreListener;
	private MenuAction menuListener;
	private int timeUsed;
	private int moved;
	private int rows;
	private int colons;
	private final int size = 16;
	private final int margin = 12;
	private final int headHeight = 80;
	private final int startButtonWidth = 60;
	private final int startButtonHeight = 40;
	private final int menuBarHeight = 21;
	
	public UI(MineBoard board){
		this.board = board;
		this.rows = board.getRows();
		this.colons = board.getColons();
		launchFrame();
	}
	
	public void launchFrame(){
		//setTitle("Mine Sweeper");
		setLayout(null);
		setResizable(false);
		coreListener = new CoreAction(board,this);
		menuListener = new MenuAction(board, this);
		//Build menus
		JMenu game = new JMenu("Game");
		JMenu save = new JMenu("Save/Load");
		JMenu help = new JMenu("Help");
		//Build menu items
		JMenuItem item;
		item = new JMenuItem("Restart");
		game.add(item);
		item.addActionListener(menuListener);
		game.addSeparator();
		
		JCheckBoxMenuItem easy = new JCheckBoxMenuItem("Easy");
		JCheckBoxMenuItem hard = new JCheckBoxMenuItem("Hard");
		JCheckBoxMenuItem hell = new JCheckBoxMenuItem("Hell");
		JCheckBoxMenuItem custom = new JCheckBoxMenuItem("Custom");
		ButtonGroup group = new ButtonGroup();
		group.add(easy);
		group.add(hard);
		group.add(hell);
		group.add(custom);
		game.add(easy);
		easy.addActionListener(menuListener);
		game.add(hard);
		hard.addActionListener(menuListener);
		game.add(hell);
		hell.addActionListener(menuListener);
		game.add(custom);
		custom.addActionListener(menuListener);
		custom.setSelected(true);
		
		game.addSeparator();
		item = new JMenuItem("Mark");
		game.add(item);
		item.addActionListener(menuListener);
		item = new JMenuItem("Quit");
		game.add(item);
		item.addActionListener(menuListener);
		item = new JMenuItem("Save");
		save.add(item);
		item.addActionListener(menuListener);
		item = new JMenuItem("Load");
		save.add(item);
		item.addActionListener(menuListener);
		item = new JMenuItem("About");
		help.add(item);
		//Add menus to a menu bar
		JMenuBar menu= new JMenuBar();
		menu.add(game);
		menu.add(save);
		menu.add(help);
		setJMenuBar(menu);
		restart = new JButton("Restart");
		restart.addActionListener(coreListener);
		
		gamePanel = new GamePanel(this);
		gamePanel.setLayout(null);
		gamePanel.setBackground(Color.WHITE);
		this.setContentPane(gamePanel);
		gamePanel.add(restart);
		gamePanel.addMouseListener(coreListener);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		restart(rows,colons);
	}
	
	public void restart(int rows, int colons){
		this.rows = rows;
		this.colons = colons;
		this.timeUsed = board.getTime();
		this.moved = board.getMoved();
		setTitle("Time " + timeUsed + "        Moved " + moved);
		setSize(margin * 2 + colons * size + 3, headHeight + margin + menuBarHeight + rows * size + 29);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width -getWidth()) / 2, (screenSize.height - getHeight()) / 2);
		restart.setBounds((getWidth() - startButtonWidth) / 2, (headHeight - startButtonHeight) / 2, startButtonWidth, startButtonHeight);
		setVisible(true);
		Image imageBoard = gamePanel.createImage(size * colons, size * rows);
		g2 = imageBoard.getGraphics();
		gamePanel.setImageBoard(imageBoard);
		gamePanel.setX(margin);
		gamePanel.setY(headHeight);
		gamePanel.setEdge(2);
		for (int i = 0; i < colons; i++){
			for (int j = 0; j < rows; j++){
				drawImage(Images.UNCLICKED, i, j);
			}
		}
		updateImage();
	}
	
	public void update(Observable board, Object o){
		Location temp = null;
		if (o instanceof NotifyReveal){
			int type = ((NotifyReveal) o).getType();
			temp = ((NotifyReveal) o).getLocation();
			if(type == 0){
				this.drawBlank(temp);
			}
			else{
				drawNumbers(type, temp);
			}
		}
		else if(o instanceof NotifyFlagged){
			temp = ((NotifyFlagged) o).getLocation();
			drawImage(Images.MARKED_MINE, temp);
		}
		else if(o instanceof NotifyDoubted){
			temp = ((NotifyDoubted) o).getLocation();
			drawImage(Images.MARKED, temp);
		}
		else if(o instanceof NotifyUnclicked){
			temp = ((NotifyUnclicked) o).getLocation();
			drawImage(Images.UNCLICKED, temp);
		}
		else if(o instanceof NotifyGG) {
			temp = ((NotifyGG) o).getLocation();
			drawImage(Images.MINE, temp);
		}
		else if(o instanceof NotifyRestart){
			int temp_row = ((NotifyRestart) o).getRows();
			int temp_columns = ((NotifyRestart) o).getColumns();
			this.restart(temp_row, temp_columns);
		}
		else if(o instanceof NotifyWin){
			win(((NotifyWin) o).getScore());
			System.out.println("U win it");
		}
		else if(o instanceof NotifyTimerChange){
			timeUsed = ((NotifyTimerChange) o).getTime();
			moved = ((NotifyTimerChange) o).getMoved();
			this.setTitle("Time " + timeUsed + "        Moved " + moved);
			//System.out.print(timeUsed);
		}
	}
	
	public void custom(){
		try {
			String input = JOptionPane.showInputDialog("Please specify three integer numbers(format in rows,cols,mines):");
			if(input == null)
				return;
			String[] ss = input.split(",");
			int[] is = new int[3];
			for(int i=0;i<ss.length;i++)
				is[i] = Integer.parseInt(ss[i]);
			board.restart(is[0], is[1], is[2]);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Invalid input,Please try again!");
		}
		
	}
	
	public Location getLocation(MouseEvent e){
		Location mouseLocation = null;
		int relative_X = e.getX() - margin;
		int relative_Y = e.getY() - headHeight;
		int temp_X = 0;
		int temp_Y = 0;
		if(relative_X >=0 && relative_X <= colons*size){
			if(relative_Y >=0 && relative_Y <= rows*size){
				for(int i=1;i<colons+1;i++){
					if(i*size < relative_X){
						temp_X = i;
					}
				}
				for(int j=1;j<rows+1;j++){
					if(j*size < relative_Y){
						temp_Y = j;
					}
				}
				mouseLocation = new Location(temp_X,temp_Y);
			}
		}
		if(mouseLocation != null){
			return mouseLocation;
		}
		else{
			return null;
		}
	}
	
	private void win(int score){
		JOptionPane.showMessageDialog(null, "You win  it!  Scored : "+ score);
	}
	
	private void drawBlank(Location location){
		drawImage(Images.CLICKED, location);
	}
	
	private void drawNumbers(int index, Location location){
		drawImage(Images.number[index-1], location);
	}
	
	private void drawImage(Image image, int x, int y){
		g2.drawImage(image, x * size, y * size, null);
	}

	private void drawImage(Image image, Location location){
		g2.drawImage(image, location.x * size, location.y * size, null);
	}
	
	public void about(){
		JOptionPane.showMessageDialog(null, "Created by Xinwei Ye 15/12/2011");
	}
	
	public void updateImage(){
		gamePanel.repaint();
	}
	
}
