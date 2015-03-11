package parents;

import interfaces.Gravity;
import support.ConditionalHandler;
import support.KillableRunnable;
import android.content.Context;
/**
 * A ProjectileView with a constant downwards force. This force is removed when the instance reaches its lowest threshold. 
 * The downward force may be different from the upward speed.
 * 
 * @author JAMES LOWREY
 *
 */
public abstract class Projectile_GravityView extends Moving_ProjectileView implements Gravity{

	private int gravityThreshold;
	private boolean atThreshold;
	
	public Projectile_GravityView(Context context,float movingSpeedY,float movingSpeedX,int projectileDamage,
			int projectileHealth,int width,int height,int imageId){
		super(context, movingSpeedY, movingSpeedX,projectileDamage,projectileHealth, width, height, imageId);

		atThreshold=false;
		gravityThreshold=Gravity.NO_THRESHOLD;
		ConditionalHandler.postIfAlive(gravityRunnable, this);
	}

    //GRAVITY RUNNABLE
	KillableRunnable gravityRunnable = new KillableRunnable(){
    	@Override
        public void doWork() {
    		atThreshold=moveDirection(Moving_ProjectileView.DOWN);
    		
    		//if View is at lowest threshold stop reposting runnable
    		if(!atThreshold){
    			ConditionalHandler.postIfAlive(this, HOW_OFTEN_TO_MOVE/2,Projectile_GravityView.this);
    		}else{
    			reachedGravityPosition();
    		}
    	}
    };
    
    @Override
    public boolean moveDirection(int direction){
    	boolean offScreen =  super.moveDirection(direction);
    	
    	boolean atThreshold=false;
    	if(direction==DOWN){
    		float y=this.getY();
    		atThreshold = gravityThreshold!=NO_THRESHOLD && (y+getHeight())>gravityThreshold;
    	}
    	return offScreen || atThreshold;
    }
    
    /**
     * Once the View has achieved it's threshold, allow further logic to be called
     */
    public abstract void reachedGravityPosition();
    
	public void stopGravity(){
		this.removeCallbacks(gravityRunnable);		
	}
	public void startGravity(){
		ConditionalHandler.postIfAlive(gravityRunnable,this);		
	}
	
	public void setThreshold (int newLowestPositionThreshold){
		gravityThreshold=newLowestPositionThreshold;
	}
	//Interface Methods
	@Override
	public void restartThreads(){
		if( ! atThreshold){
			startGravity();
		}
	}

	@Override
	public int getThreshold() {
		return gravityThreshold;
	}
}
