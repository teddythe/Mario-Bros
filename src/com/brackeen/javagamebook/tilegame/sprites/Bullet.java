package com.brackeen.javagamebook.tilegame.sprites;
import com.brackeen.javagamebook.graphics.Animation;

/**
    Bullet
*/
public class Bullet extends Creature{

	public int direction;
    public Bullet(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
            }

    public void setDirection(int direction){
    	this.direction = direction;
    }

    public float getMaxSpeed() {
        return 1.2f;
    }
    
    public void wakeUp() {
        if (getState() == STATE_NORMAL && getVelocityX() == 0) {
        	System.out.println(" "+this.direction);
        	if(this.direction == 1){
        		setVelocityX(-getMaxSpeed());
        	}
        	else {
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