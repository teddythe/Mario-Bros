package com.brackeen.javagamebook.tilegame.sprites;
import com.brackeen.javagamebook.graphics.Animation;

/**
    BulletEnemy
*/
public class BulletEnemy extends Creature{

	public int direction;
    public BulletEnemy(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
        //direction = 1;
    }

    public void setDirection(int directions){
    	direction = directions;
    }

    public float getMaxSpeed() {
        return .6f;
    }
    
    public void wakeUp() {
        if (getState() == STATE_NORMAL && getVelocityX() == 0) {
        	if(direction == -1){
        		setVelocityX(getMaxSpeed());
        	}
        	else {//if(direction == -1){
        		setVelocityX(-getMaxSpeed());
        	}
        }
    }

    public void collideHorizontal(){
    	setVelocityX(0);
    	//remove();
    }
    public boolean isFlying() {
        return isAlive();
    }
    
    

}