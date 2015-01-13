package abstract_parents;

import interfaces.GameObjectInterface;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jtronlabs.to_the_moon.MainActivity;
/**
 * A ProjectileView with a constant downwards force. This force is removed when the instance reaches its lowest threshold. 
 * The downward force may be different from the upward speed.
 * 
 * 
 * 
 * Any children that have RUNNABLES must include a condition for !MovingView.this.isRemoved() before running.
 * this ensures resources are not instance that has been removed from game.
 * Why? Handler.removeCallbacks(null) only removes pending messages. If  the Runnable has begun then it will get through,
 * and since all these game runnables have Handler.postDelayed(this,delayInMillisec) at the end, it will continue to execute
 * 
 * 
 * 
 * @author JAMES LOWREY
 *
 */
public abstract class MovingView extends ImageView implements GameObjectInterface{

	private static int referenceId=Integer.MIN_VALUE;
	
	public static final int HOW_OFTEN_TO_MOVE=100,
			UP=0,SIDEWAYS=1,DOWN=2,LEFT=3,RIGHT=4,
			NOT_DEAD=-1;

	private int myId;
	boolean isRemoved;
	double speedY,speedX;
	
	public MovingView(Context context,double movingSpeedY,double movingSpeedX) {
		super(context);

		myId=referenceId;
		referenceId++;
		speedY=Math.abs(movingSpeedY)*MainActivity.getScreenDens();
		speedX=movingSpeedX*MainActivity.getScreenDens();
		isRemoved=false;
	}
	
	public MovingView(Context context,AttributeSet at,double movingSpeedY,double movingSpeedX) {
		super(context,at);

		myId=referenceId;
		referenceId++;
		speedY=Math.abs(movingSpeedY)*MainActivity.getScreenDens();
		speedX=movingSpeedX*MainActivity.getScreenDens();
		isRemoved=false;
	}
	
	public boolean collisionDetection(View two){
		float left1,right1,top1,bottom1;
		float left2,right2,top2,bottom2;
		
		//find the values of the x,y positions of the two views
		left1=getX();
		right1=getX()+getWidth();
		top1=getY();
		bottom1=getY()+getHeight();

		left2=two.getX();
		right2=two.getX()+two.getWidth();
		top2=two.getY();
		bottom2=two.getY()+two.getHeight();
		
		//Simple collision detection - determine if the two rectangular areas intersect
		//http://devmag.org.za/2009/04/13/basic-collision-detection-in-2d-part-1/
		return !((bottom1 < top2) ||(top1 > bottom2) || (left1>right2) || (right1<left2));
	}
	
	/**
	 * Move the View on the screen according to is speedY or speedX.
	 * @param direction-whichDirection the View should move. Input needs to be ProjectileView.UP, ProjectileView.RIGHT,ProjectileView.DOWN, or ProjectileView.LEFT
	 * @return Always returns false, overwrite for different behavior
	 */
	public boolean moveDirection(int direction) throws IllegalArgumentException{
		if(direction!=UP && direction!=SIDEWAYS && direction!=DOWN && direction!=LEFT && direction != RIGHT){
			throw new IllegalArgumentException("direction argument must be ProjectileView.UP, ProjectileView.RIGHT,ProjectileView.DOWN, or ProjectileView.LEFT");
		}

		//Move by setting this instances X or Y position to its current position plus its respective speed.
		float x =this.getX();
		float y =this.getY();
		boolean outOfScreen=false;
		switch(direction){
		case UP:
			y-=Math.abs(speedY);
			outOfScreen=y< -getHeight();
			this.setY(y);
			break;
		case DOWN:
			y+=Math.abs(speedY);
			outOfScreen=y>MainActivity.getHeightPixels();
			this.setY(y);
			break;
		//move in X direction at speed X, move right if speed X positive and left otherwise
		case SIDEWAYS:
			x+=speedX;
			outOfScreen = x < -this.getWidth() || x > (MainActivity.getWidthPixels() + this.getWidth());
			this.setX(x);
			break;
		//move Left or right on screen, use abs(speedX)
		case LEFT:
			x-=Math.abs(speedX);
			outOfScreen = x < -this.getWidth();
			this.setX(x);
			break;
		case RIGHT:
			x+=Math.abs(speedX);
			outOfScreen = x > (MainActivity.getWidthPixels() + this.getWidth());
			this.setX(x);
			break;
		}
		
		if(outOfScreen){
			this.removeGameObject();
		}
		
		return outOfScreen;
	}

	
	
	public void setSpeedX(double newSpeed){
		this.speedX=newSpeed;
	}
	public double getSpeedX(){
		return speedX;
	}
	public boolean isRemoved(){
		return isRemoved;
	}
	public double getMagnitudeOfSpeedY(){
		return speedY;
	}
	public void setSpeedY(double newSpeed){
		this.speedY=newSpeed;
	}

	public abstract void removeGameObject();
	
	/**
	 * To be called on every implementation of removeGameObject();
	 */
	protected void deaultCleanupOnRemoval(){
		isRemoved=true;
		this.removeCallbacks(null);
		ViewGroup parent = (ViewGroup)this.getParent();
		if(parent!=null){parent.removeView(this);}		
	}
	
	public int getId(){
		return this.myId;
	}

//	/**
//	 * must override the .equals method in order for ArrayList.remove(Object) to function as
//	 * intended.
//	 */
//	@Override
//	public boolean equals(Object other){
//		if(other instanceof MovingView){
//			final int id= ((MovingView)other).getId();
////			Log.d("lowrey", "id1"+myId+ " id2= "+id );
//			return this.myId == id;
//		}else{
//			return false;
//		}
////		return true;
//	}
}
