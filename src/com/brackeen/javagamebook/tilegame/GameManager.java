package com.brackeen.javagamebook.tilegame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Iterator;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;
import javax.swing.ImageIcon;

import com.brackeen.javagamebook.graphics.*;
import com.brackeen.javagamebook.sound.*;
import com.brackeen.javagamebook.input.*;
import com.brackeen.javagamebook.test.GameCore;
import com.brackeen.javagamebook.tilegame.sprites.*;


/**
    GameManager manages all parts of the game.
*/
public class GameManager extends GameCore {
	//int directionB;
	public int explodeflag = 1;
	public int gasshootflag = 0;
	public long gastimeinitial;
	public long bintangtimeinitial;
	public long gastimefinal;
	public long bintangtimefinal;
	public int bisanembak=0;
	int i = 0;
	int truee = 1;
	int bintangflag = 0;
    public static void main(String[] args) {
        new GameManager().run();
    }

    // uncompressed, 44100Hz, 16-bit, mono, signed, little-endian
    private static final AudioFormat PLAYBACK_FORMAT =
        new AudioFormat(44100, 16, 1, true, false);

    private static final int DRUM_TRACK = 1;

    public static final float GRAVITY = 0.002f;
    
    private static final int MAXHEALTH = 400;

    private Point pointCache = new Point();
    private TileMap map;
    private MidiPlayer midiPlayer;
    private SoundManager soundManager;
    private ResourceManager resourceManager;
    private Sound prizeSound;
    private Sound boopSound;
    private InputManager inputManager;
    private TileMapRenderer renderer;

    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction jump;
    private GameAction exit;
    private GameAction shoot;
    private GameAction down;
    
    private int bulletdirection = 1;

    private int healthnumber = 20;

    public void init() {
        super.init();

        // set up input manager
        initInput();

        // start resource manager
        resourceManager = new ResourceManager(
        screen.getFullScreenWindow().getGraphicsConfiguration());

        // load resources
        renderer = new TileMapRenderer();
        renderer.setBackground(
            resourceManager.loadImage("background.png"));

        // load first map
        map = resourceManager.loadNextMap();

        // load sounds
        soundManager = new SoundManager(PLAYBACK_FORMAT);
        prizeSound = soundManager.getSound("sounds/prize.wav");
        boopSound = soundManager.getSound("sounds/boop2.wav");

        // start music
        midiPlayer = new MidiPlayer();
        Sequence sequence =
            midiPlayer.getSequence("sounds/music.midi");
        midiPlayer.play(sequence, true);
        toggleDrumPlayback();
        
    }


    /**
        Closes any resurces used by the GameManager.
    */
    public void stop() {
        super.stop();
        midiPlayer.close();
        soundManager.close();
    }


    private void initInput() {
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        jump = new GameAction("jump",
            GameAction.DETECT_INITAL_PRESS_ONLY);
        down = new GameAction("down");
        exit = new GameAction("exit",
            GameAction.DETECT_INITAL_PRESS_ONLY);
        shoot = new GameAction("shoot");//, GameAction.DETECT_INITAL_PRESS_ONLY);
        
        inputManager = new InputManager(
            screen.getFullScreenWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(jump, KeyEvent.VK_UP);
        inputManager.mapToKey(down, KeyEvent.VK_DOWN);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(shoot, KeyEvent.VK_S);
    }
    
    private final long PERIOD = 1000L; // Adjust to suit timing
    private long lastTime = System.currentTimeMillis() - PERIOD;
    private long prev = System.currentTimeMillis();
    private long time = System.currentTimeMillis();
    private long now = System.currentTimeMillis();
    private long later = System.currentTimeMillis();
    private long lastTimes = System.currentTimeMillis();
    private long thisTimes = System.currentTimeMillis();
    
    private void checkInput(long elapsedTime) {

        if (exit.isPressed()) {
            stop();
        }
        Player player = (Player)map.getPlayer();
        Bullet bullet = (Bullet)map.getBullet();
      //  Bullet bullet2 = (Bullet)map.getBullet();
       // Bullet bullet3 = (Bullet)map.getBullet();
       /* for (int j = 0;j<10;j++){
        	bullet[j] = (Bullet)map.getBullet();
        }*/
        //Bullet bullet1 = (Bullet)map.getBullet();//---------------------------------------------------
        float dx_ = player.getVelocityX();
        float oldX_ = player.getX();
        float newX_ = oldX_ + dx_ * elapsedTime;
        if (player.isAlive()) {
            float velocityX = 0;
            if (moveLeft.isPressed()) {
            	//directionB = -1;
            	bullet.setDirection(0);
            	bulletdirection = 0;
                velocityX-=player.getMaxSpeed();
                if(healthnumber+1 <= MAXHEALTH)
                {
                	if(TileMapRenderer.pixelsToTiles(oldX_)!=TileMapRenderer.pixelsToTiles(newX_))
                	healthnumber+=1;
                }
                else healthnumber=MAXHEALTH;

            }
            if (moveRight.isPressed()) {
            	//directionB = 1;
            	bullet.setDirection(1);
            	bulletdirection = 1;
                velocityX+=player.getMaxSpeed();
                if(healthnumber+1 <= MAXHEALTH)
                {
                	if(TileMapRenderer.pixelsToTiles(oldX_)!=TileMapRenderer.pixelsToTiles(newX_))
                	healthnumber+=1;
                }
                else healthnumber=MAXHEALTH;


            }
            if (jump.isPressed()) {
                player.jump(false);
            }
            if(down.isPressed()){
            	player.down();
            }
            if (shoot.isPressed()){
            	Image img = new ImageIcon("images/fly1.png").getImage();
            	Animation bulletanim = new Animation();
            	bulletanim.addFrame(img,  1000);
                //new EchoFilter(2000, .7f), false);
            	//now = System.currentTimeMillis();
            	//if(now - later > 300L){
            	//bisanembak=0;
            	if(gasshootflag==1){
            		gastimefinal = System.currentTimeMillis();
            		if(gastimefinal-gastimeinitial > 1000L){
            			gasshootflag = 0;
            		}
            	}
            	if(i == 10){
            		i = 0;
            		time = System.currentTimeMillis();
            		truee = 0;
            		}
            		//player.setState(Creature.STATE_DEAD);
            	if(truee == 0){
            		prev = System.currentTimeMillis();
       
            	if(prev-time >1000L){
            		truee = 1;
            	}}
            	/*switch(i){
            	case 0:*/
            	
            	
            	
            	if(truee == 1 && gasshootflag==0){
            		
                	//map.addSprite(bullet1);
                	long thisTimes = System.currentTimeMillis();

                    if ((thisTimes - lastTimes) >= 200L) 
                    {
                    	soundManager.play(prizeSound);
                    	
                        lastTimes = thisTimes;
                        Bullet bullet1 = new Bullet(bulletanim,bulletanim,bulletanim,bulletanim);
                        if(bulletdirection == 1)
                        bullet1.dx = 1f;
                        else if(bulletdirection == 0)
                        bullet1.dx = -1f;
                        bullet1.setX(player.getX());
                        bullet1.setY(player.getY());
                        map.addSprite(bullet1);
                        /*
                        bullet.setState(Creature.STATE_NORMAL);
                    	
                    	bullet.setX((player.getX()));//+(30*bullet1.direction)));
                    	bullet.setY((player.getY()*1.05f));
                        map.addSprite(bullet);*/
                        i++;
                       // if(healthnumber+5 <= MAXHEALTH) healthnumber+=5;
                       // else healthnumber=MAXHEALTH;
                    }
            	}
            	//i++;//}
            	//later = now;
            	/*
            	bullet[i].setState(Creature.STATE_NORMAL);
                bullet[i].setDirection(directionB);
                bullet[i].setX((player.getX()+(30*bullet[i].direction)));
                bullet[i].setY((player.getY()+10));
                map.addSprite(bullet[i]);
                i++;
            	*/
            	//addSprite();
            	//resourceManager.addSprite(map, (Sprite)bullet,(int)(player.getX()+(50*bullet.direction+20)), (int)(player.getY()+50) );
            	/*
            	bullet.setState(Creature.STATE_NORMAL);
            	bullet.setDirection(directionB);
            	bullet.setX((player.getX()+(30*bullet.direction)));
            	bullet.setY((player.getY()+10));
            	map.addSprite(bullet);*/
            	//bullet.setDirection(directionB);
            }
            if (!jump.isPressed())
            {
            	long thisTime = System.currentTimeMillis();

                if ((thisTime - lastTime) >= PERIOD) 
                {
                    lastTime = thisTime;
                    if(healthnumber+5 <= MAXHEALTH) healthnumber+=5;
                    else healthnumber=MAXHEALTH;
                }
            }
            player.setVelocityX(velocityX);
        }
        //bintangtimefinal = System.currentTimeMillis();
        //if(bintangflag ==1 && bintangtimefinal - bintangtimeinitial > 5000L){
        //	bintangflag = 0;
        //}

    }


    public void draw(Graphics2D g) {
        renderer.draw(g, map,
            screen.getWidth(), screen.getHeight());
        
        drawInterface( g );
    }


    /**
        Gets the current map.
    */
    public TileMap getMap() {
        return map;
    }


    /**
        Turns on/off drum playback in the midi music (track 1).
    */
    public void toggleDrumPlayback() {
        Sequencer sequencer = midiPlayer.getSequencer();
        if (sequencer != null) {
            sequencer.setTrackMute(DRUM_TRACK,
                !sequencer.getTrackMute(DRUM_TRACK));
        }
    }


    /**
        Gets the tile that a Sprites collides with. Only the
        Sprite's X or Y should be changed, not both. Returns null
        if no collision is detected.
    */
    public Point getTileCollision(Sprite sprite,
        float newX, float newY)
    {
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);

        // get the tile locations
        int fromTileX = TileMapRenderer.pixelsToTiles(fromX);
        int fromTileY = TileMapRenderer.pixelsToTiles(fromY);
        int toTileX = TileMapRenderer.pixelsToTiles(
            toX + sprite.getWidth() - 1);
        int toTileY = TileMapRenderer.pixelsToTiles(
            toY + sprite.getHeight() - 1);

        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() ||
                    map.getTile(x, y) != null)
                {
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }

        // no collision found
        return null;
    }

   
    
    /**
        Checks if two Sprites collide with one another. Returns
        false if the two Sprites are the same. Returns false if
        one of the Sprites is a Creature that is not alive.
    */
    public boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }

        // if one of the Sprites is a dead Creature, return false
        if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
            return false;
        }
        if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
            return false;
        }

        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());

        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
            s2x < s1x + s1.getWidth() &&
            s1y < s2y + s2.getHeight() &&
            s2y < s1y + s1.getHeight());
    }


    /**
        Gets the Sprite that collides with the specified Sprite,
        or null if no Sprite collides with the specified Sprite.
    */
    public Sprite getSpriteCollision(Sprite sprite) {

        // run through the list of Sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if (isCollision(sprite, otherSprite)) {
                // collision found, return the Sprite
            	
                return otherSprite;
            }
        }

        // no collision found
        return null;
    }


    /**
        Updates Animation, position, and velocity of all Sprites
        in the current map.
    */
    public void update(long elapsedTime) {
        Creature player = (Creature)map.getPlayer();


        // player is dead! start map over
        if (player.getState() == Creature.STATE_DEAD) {
            map = resourceManager.reloadMap();
            return;
        }

        // get keyboard/mouse input
        checkInput(elapsedTime);

        // update player
        updateCreature(player, elapsedTime);
        player.update(elapsedTime);

        // update other sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            if (sprite instanceof Creature) {
                Creature creature = (Creature)sprite;
                if (creature.getState() == Creature.STATE_DEAD) {
                    i.remove();
                }
                else {
                    updateCreature(creature, elapsedTime);
                }
            }
            // normal update
            sprite.update(elapsedTime);
        }
        
    }


    /**
        Updates the creature, applying gravity for creatures that
        aren't flying, and checks collisions.
    */
    private void updateCreature(Creature creature,
        long elapsedTime)
    {

        // apply gravity
        if (!creature.isFlying()) {
            creature.setVelocityY(creature.getVelocityY() +
                GRAVITY * elapsedTime);
        }

        // change x
        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile =
            getTileCollision(creature, newX, creature.getY());
        if (tile == null) {
            creature.setX(newX);
        }
        else {
            // line up with the tile boundary
            if (dx > 0) {
                creature.setX(
                    TileMapRenderer.tilesToPixels(tile.x) -
                    creature.getWidth());
                for(int iter=0;iter<TileMap.explodeblocks.size();iter++){
                	if((tile.x == ((Point)TileMap.explodeblocks.get(iter)).x) && (tile.y == ((Point)TileMap.explodeblocks.get(iter)).y) && explodeflag==1){
                		healthnumber-=10;
                		TileMap.explodeblocks.remove(iter);
                		//explodeflag = 0;
                	}
                }
                
                for(int iter=0;iter<TileMap.gasblocks.size();iter++){
                	if((tile.x == ((Point)TileMap.gasblocks.get(iter)).x) && (tile.y == ((Point)TileMap.gasblocks.get(iter)).y)){	
                		gasshootflag = 1;
                		gastimeinitial = System.currentTimeMillis();
                	}
                }
            }
            else if (dx < 0) {
                creature.setX(
                    TileMapRenderer.tilesToPixels(tile.x + 1));
                for(int iter=0;iter<TileMap.explodeblocks.size();iter++){
                	if((tile.x+1 == ((Point)TileMap.explodeblocks.get(iter)).x+1) && (tile.y == ((Point)TileMap.explodeblocks.get(iter)).y) && explodeflag==1){
                		healthnumber-=10;
                		TileMap.explodeblocks.remove(iter);
                		//explodeflag = 0;
                	}
                }
                
                for(int iter=0;iter<TileMap.gasblocks.size();iter++){
                	if((tile.x+1 == ((Point)TileMap.gasblocks.get(iter)).x+1) && (tile.y == ((Point)TileMap.gasblocks.get(iter)).y)){	
                		gasshootflag = 1;
                		gastimeinitial = System.currentTimeMillis();

                	}
                }
            }
            creature.collideHorizontal();
            if(creature instanceof Bullet){
            	creature.setState(Creature.STATE_DEAD);
            }
            if(creature instanceof BulletEnemy){
            	creature.setState(Creature.STATE_DEAD);
            }
        }
        if (creature instanceof Player) {
            checkPlayerCollision((Player)creature, false);
        }
        if (creature instanceof Bullet) {
            checkBulletCollision((Bullet)creature);
        }
        // change y
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(creature, creature.getX(), newY);
        if (tile == null) {
            creature.setY(newY);
        }
        else {
            // line up with the tile boundary
            if (dy > 0) {
                creature.setY(
                    TileMapRenderer.tilesToPixels(tile.y) -
                    creature.getHeight());
                for(int iter=0;iter<TileMap.explodeblocks.size();iter++){
                	if((tile.x == ((Point)TileMap.explodeblocks.get(iter)).x) && (tile.y == ((Point)TileMap.explodeblocks.get(iter)).y) && explodeflag==1){
                		healthnumber-=10;
                		//explodeflag = 0;
                		TileMap.explodeblocks.remove(iter);
                	}
                }
             
                for(int iter=0;iter<TileMap.gasblocks.size();iter++){
                	if((tile.x == ((Point)TileMap.gasblocks.get(iter)).x) && (tile.y == ((Point)TileMap.gasblocks.get(iter)).y)){	
                		gasshootflag = 1;
                		gastimeinitial = System.currentTimeMillis();
                	}
                }
            }
            else if (dy < 0) {
                creature.setY(
                    TileMapRenderer.tilesToPixels(tile.y + 1));
                
                for(int iter=0;iter<TileMap.explodeblocks.size();iter++){
                	if((tile.x == ((Point)TileMap.explodeblocks.get(iter)).x) && (tile.y+1 == ((Point)TileMap.explodeblocks.get(iter)).y+1) && explodeflag==1){
                		healthnumber-=10;
                		//explodeflag = 0;
                		TileMap.explodeblocks.remove(iter);
                	}
                }
                for(int iter=0;iter<TileMap.gasblocks.size();iter++){
                	if((tile.x == ((Point)TileMap.gasblocks.get(iter)).x) && (tile.y+1 == ((Point)TileMap.gasblocks.get(iter)).y+1)){	
                		gasshootflag = 1;
                		gastimeinitial = System.currentTimeMillis();
                	}
                }
            }
            creature.collideVertical();
        }
        if (creature instanceof Player) {
            boolean canKill = (oldY < creature.getY());
            checkPlayerCollision((Player)creature, canKill);
        }
        /*
        if (creature instanceof Bullet) {
            boolean canKill = (oldY < creature.getY());
            checkBulletCollision((Bullet));
        }*/
        
      
        
    }
    
//-----------------------------------------------------------------------------------------------
  
    public void checkBulletCollision(Bullet bullet){
    	Sprite collisionSprite = getSpriteCollision(bullet);
        if (collisionSprite instanceof Creature) {
        	Creature badguy = (Creature)collisionSprite;
            soundManager.play(boopSound);
        	badguy.setState(Creature.STATE_DYING);
        	if(healthnumber+10 <= MAXHEALTH) healthnumber += 10;
            else healthnumber=MAXHEALTH;
        	bullet.setState(Creature.STATE_DEAD);
        	//map.remove(bullet);
        }
             
    }
    

    /**
        Checks for Player collision with other Sprites. If
        canKill is true, collisions with Creatures will kill
        them.
    */
    public void checkPlayerCollision(Player player,
        boolean canKill)
    {
        if (!player.isAlive()) {
            return;
        }

        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
        	//if(bintangflag == 1){
        		bintangtimeinitial = System.currentTimeMillis();
        	//}
        	acquirePowerUp((PowerUp)collisionSprite);
            
        }
        else if(collisionSprite instanceof Bullet){}
        else if(collisionSprite instanceof BulletEnemy){
        	BulletEnemy bulletenemy = (BulletEnemy)collisionSprite;
        	if(bintangflag==1 ){
        		if(System.currentTimeMillis()-bintangtimeinitial > 1000){
        			
        			healthnumber+=0;
        			bintangtimeinitial = System.currentTimeMillis();
        			bintangflag=0;
        		}
        
        	}
        	else{
        		soundManager.play(boopSound);
        	bulletenemy.setState(Creature.STATE_DEAD);
        	healthnumber -= 5;
        	
        	}
        	
        }
        else if (collisionSprite instanceof Creature) {
            Creature badguy = (Creature)collisionSprite;
            if (canKill) {
                // kill the badguy and make player bounce
                soundManager.play(boopSound);
                badguy.setState(Creature.STATE_DYING);
                player.setY(badguy.getY() - player.getHeight());
                if(healthnumber+10 <= MAXHEALTH) healthnumber += 10;
                else healthnumber=MAXHEALTH;
                //player.jump(true);
            }
            else {
                // player dies!
            	if(bintangflag==1 ){
            		if(System.currentTimeMillis()-bintangtimeinitial > 1000){
            			
            			healthnumber+=0;
            			bintangtimeinitial = System.currentTimeMillis();
            			bintangflag=0;
            		}
            
            	}
	            else{
	            	soundManager.play(boopSound);
	            	player.setState(Creature.STATE_DYING);
	            	healthnumber = 0;

	            }
            	
            }
            
        }
    }


    /**
        Gives the player the speicifed power up and removes it
        from the map.
    */
    public void acquirePowerUp(PowerUp powerUp) {
        // remove it from the map
        map.removeSprite(powerUp);

        if (powerUp instanceof PowerUp.Star) {
            // do something here, like give the player points
            soundManager.play(prizeSound);
            // health plus 
            if(healthnumber+5 < MAXHEALTH) healthnumber += 5;
            else healthnumber=MAXHEALTH;
        }
        else if (powerUp instanceof PowerUp.Music) { //bintang
            // change the music
            soundManager.play(prizeSound);
            toggleDrumPlayback();
            bintangflag = 1;
            
            
        }
        else if (powerUp instanceof PowerUp.Goal) {
            // advance to next map
            soundManager.play(prizeSound,
                new EchoFilter(2000, .7f), false);
            map = resourceManager.loadNextMap();
        }
        else if (powerUp instanceof PowerUp.Apple) {
            // change the music
            soundManager.play(prizeSound);
            toggleDrumPlayback();
            healthnumber -= 5;
           
        }
    
        
    }
    
private void drawInterface( Graphics2D g2d ) {
        
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        
        //g2d.drawImage( imagemInterfaceMario, 20, 22, null );
        g2d.drawString("HEALTH  "+healthnumber,40,40);
        g2d.drawString("SCORE  "+healthnumber*5,650,40);
        //g2d.drawString("Bintang"+bintangflag, 650, 60);
        if(healthnumber<=0){
        	Player pl = (Player)map.getPlayer();
        	pl.setState(Creature.STATE_DYING);
        	healthnumber = 20;
        }

    }
    

}
