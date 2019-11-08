import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Map {
	
	public static Tile[][] baseArray = new Tile[56][52];
	public static Tile[][] overlayArray = new Tile[56][52];
	File baseLocation, overlayLocation;
	public static List<Tile> collisionTiles = new ArrayList<Tile>();
	
	public Map() {
		baseLocation = Game.mapLocation;
		overlayLocation = Game.mapOverlayLocation;
		readMapsFromCSV();
	}

	public void update() {
		for(int i = 0; i < baseArray.length; i++) {
			for(int j = 0; j < baseArray[0].length; j++) {
				if(baseArray[i][j] != null)
					baseArray[i][j].update();
				if(overlayArray[i][j] != null)
					overlayArray[i][j].update();
			}
		}
	}
	
	private void readMapsFromCSV() {
		try {
			String line;
			BufferedReader csvReader = new BufferedReader(new FileReader(baseLocation));
			
			int j = 0;
			
			while ((line = csvReader.readLine()) != null) {
			    String[] data = line.split(",");
			    //creates new key from name in first column and path in second
			    for(int i = 0; i < data.length; i++) {
			    	Tile t;
			    	if(data[i].equals("water")) {
			    		String[] s = {"water1","water2","water3","water4","water5","water6","water7","water8","water9"};
			    		Animation a = new Animation(s, 2.0f);
			    		t = new Tile(a, i, j, 1);
			    	} else {
			    		t = new Tile(data[i] + "", i, j, 0);
			    	}
			    	if(data[i].equals("tree-grass"))
			    		collisionTiles.add(t);
//			    	Game.addNewInstance(t);
			    	baseArray[i][j] = t;
			    	Game.activeObjects.add(t);
			    }
			    j++;
			}
			csvReader.close();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//now handle overlay tiles
		try {
			String line;
			BufferedReader csvReader = new BufferedReader(new FileReader(overlayLocation));
			
			int j = 0;
			
			while ((line = csvReader.readLine()) != null) {
			    String[] data = line.split(",");
			    //creates new key from name in first column and path in second
			    for(int i = 0; i < data.length; i++) {
			    	if(data[i] != "") {
				    	Tile t;
				    	//if overlay tile is going over a water tile, don't put it in layer 2
				    	t = new Tile(data[i] + "", i, j, data[i].contains("sand") ? 1 : 2);
				    	overlayArray[i][j] = t;
				    	Game.activeObjects.add(t);
			    	}
			    }
			    j++;
			}
			csvReader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		findShore();
		
	}
	
	void findShore(){
		for(Tile[] array : baseArray) {
			for(Tile t : array) {
				if(t.tileSprite.renderLayer == 1) {
					if(tileIsShoreline(t)) {
						collisionTiles.add(t);
					}
				}
			}
		}
	}
	
	boolean tileIsShoreline(Tile t) {
		boolean isShore = false;
		for(int x = t.getX() - 1; x < t.getX() + 2; x++) {
			for(int y = t.getY() - 1; y < t.getY() + 2; y++) {
				if(x >= 0 && x < baseArray.length && y >= 0 && y < baseArray[x].length) {
					if(baseArray[x][y].tileSprite.renderLayer != 1 && (x != t.getX() || y != t.getY())) {
						isShore = true;
						break;
					}
				}
			}
			if(isShore) {
				break;
			}
		}
		return isShore;
	}

}
