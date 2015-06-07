package com.brackeen.javagamebook.tilegame;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

import com.brackeen.javagamebook.graphics.Sprite;

/**
    The TileMap class contains the data for a tile-based
    map, including Sprites. Each tile is a reference to an
    Image. Of course, Images are used multiple times in the tile
    map.
*/
public class TileMap {

    private Image[][] tiles;
    private LinkedList sprites;
    private Sprite player;
    private Sprite bullet;
    private Sprite bulletenemy;
   // public static Point explodeposition;
    public static ArrayList explodeblocks;
    public static ArrayList gasblocks;


    /**
        Creates a new TileMap with the specified width and
        height (in number of tiles) of the map.
    */
    public TileMap(int width, int height) {
        tiles = new Image[width][height];
        sprites = new LinkedList();
        explodeblocks = new ArrayList();
        gasblocks = new ArrayList();
    }


    /**
        Gets the width of this TileMap (number of tiles across).
    */
    public int getWidth() {
        return tiles.length;
    }


    /**
        Gets the height of this TileMap (number of tiles down).
    */
    public int getHeight() {
        return tiles[0].length;
    }


    /**
        Gets the tile at the specified location. Returns null if
        no tile is at the location or if the location is out of
        bounds.
    */
    public Image getTile(int x, int y) {
        if (x < 0 || x >= getWidth() ||
            y < 0 || y >= getHeight())
        {
            return null;
        }
        else {
            return tiles[x][y];
        }
    }


    /**
        Sets the tile at the specified location.
    */
    public void setTile(int x, int y, Image tile) {
        tiles[x][y] = tile;
    }


    /**
        Gets the player Sprite.
    */
    public Sprite getPlayer() {
        return player;
    }


    /**
        Sets the player Sprite.
    */
    public void setPlayer(Sprite player) {
        this.player = player;
    }
    
	    /**
	    Gets the bullet Sprite.
	*/
	public Sprite getBullet() {
	    return bullet;
	}
	public Sprite getBulletEnemy() {
	    return bulletenemy;
	}
	
	/**
	    Sets the player Sprite.
	*/
	public void setBullet(Sprite bullet) {
	    this.bullet = bullet;
	}
	public void setBulletEnemy(Sprite bulletenemy) {
	    this.bulletenemy = bulletenemy;
	}

    /**
        Adds a Sprite object to this map.
    */
    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }


    /**
        Removes a Sprite object from this map.
    */
    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }


    /**
        Gets an Iterator of all the Sprites in this map,
        excluding the player Sprite.
    */
    public Iterator getSprites() {
        return sprites.iterator();
    }
    
   /* public void locationExplode(int x, int y){
    	
    	explodeposition = new Point(x,y);
    }*/
    
    public void locationExplode(int x, int y){
    	Point locExplode = new Point();
    	locExplode.setLocation(x, y);
    	explodeblocks.add(locExplode);
    }
    
    public void locationGas(int x, int y){
    	Point locExplode = new Point();
    	locExplode.setLocation(x, y);
    	gasblocks.add(locExplode);
    }

    
}
