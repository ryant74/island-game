import java.awt.Color; 
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.swing.*;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

public class Game extends JFrame {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
	public static int objects;
	public static int FRAME;
        public static float deltaTime;
	public static long lastFrameMillis;
	public static ResourceHandler resGrab;
	public static List<Paintable> paintableObjects = new ArrayList<Paintable>();
		public static float GRAPHICS_SCALE_FACTOR = 1f;
		public static int TILE_SIZE = (int) (50 * GRAPHICS_SCALE_FACTOR);
		/////
	public static ArrayList<Object> allComponents = new ArrayList<>();
		public static List<Item> items = new ArrayList<Item>();
		public static List<Tile> tiles = new ArrayList<Tile>();
	///
	public static Player player;
	public static Map map;
		public static File mapLocation = new File("./res/defaultmap.csv");
	
	public Game() {
		Game.objects++;
		setSize(1200, 700); 
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		map = new Map();
		
		
		resGrab = new ResourceHandler();
		
		/*
		 * 
		 * 
		 * 
		 */
		createComponents();
		
	}
	
	private void updateVars() {
		TILE_SIZE = (int) (50 * GRAPHICS_SCALE_FACTOR);
		FRAME++;
	}
	private void createComponents() {
		//fill in whole list
		allComponents.add(items);
		allComponents.add(tiles);
		allComponents.add(player);
		/*
		 * Create all components
		 */
			addNewInstance(new Item("Crystal", 5, 5));
			player = new Player("Crystal", 65, 110);
			addNewInstance(player);
			String[] s = {"water1", "water2", "water3"};
			addNewInstance(new Tile(s, 100, 100, 2));
	}
	public void paint(Graphics G) {
//		G.clearRect(0, 0, 1200, 700);
		G.setColor(Color.BLUE);
		G.drawString(objects + "", 50, 50);
		
		map.draw(G);
		
		for(Paintable obj : paintableObjects) {
			obj.draw(G);
		}
		
                
                updateVars();
                deltaTime = (System.currentTimeMillis() - lastFrameMillis) / 1000f;
                lastFrameMillis = System.currentTimeMillis();
                try{
                        TimeUnit.MILLISECONDS.sleep(50);
                } 
                catch (Exception e){
                    System.out.println("sucks");
                }
                
                toolkit.sync();
                repaint();
	}

	public static boolean detectItemPlayerCollision(Rectangle2D hitbox) {
		if(player.hitbox.intersects(hitbox))
			return true;
		return false;
	}
	public static boolean detectItemCollision(Rectangle2D hitbox) {
		for(Item i : items) {
			if(i.hitbox.intersects(hitbox))
				return true;
		}
		return false;
	}
	
	public static void addNewInstance(Object o) {
		if(o instanceof Paintable) {
			paintableObjects.add((Paintable) o);
		}
		if(o instanceof Item) {
			items.add((Item) o);
		}
		if(o instanceof Tile) {
			tiles.add((Tile) o);
		}
	}
}
