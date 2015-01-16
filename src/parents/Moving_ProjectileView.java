package parents;

import interfaces.Projectile;
import support.ConditionalHandler;
import android.content.Context;
import android.graphics.Color;

import com.jtronlabs.to_the_moon.R;

/**
 * ImageView object to be placed on screen with gameplay properties such as health, score, speeds, and more
 * @author JAMES LOWREY
 *
 */
public abstract class Moving_ProjectileView extends MovingView implements Projectile{

	double speedX, damage, health,maxHealth;
	
	public Moving_ProjectileView(Context context,double movingSpeedY,double movingSpeedX,double projectileDamage,
			double projectileHealth,int width,int height,int imageId) {
		super(context, movingSpeedY, movingSpeedX, width, height, imageId);	

		damage=projectileDamage;
		health=projectileHealth;
		maxHealth=projectileHealth;
	}

	/**
	 * 
	 * Subtract @param/amountOfDamage from this View's health. Flash the View's background red to indicate damage taken. Remove View from game if it dies
	 * @param amountOfDamage-how much the view's health should be subtracted
	 * @return True if thi dies
	 */
	public boolean takeDamage(double amountOfDamage){		
		boolean dies= false;
		health-=amountOfDamage;
		
		if(health<=0){
			removeGameObject();
			dies = true;
		}else{
			//set the background behind this view, and then remove it after howLongBackgroundIsApplied milliseconds
			this.setBackgroundResource(R.drawable.view_damaged);
			final int howLongBackgroundIsApplied=80;
			Runnable removeDmg = new Runnable(){
				@Override
				public void run() {Moving_ProjectileView.this.setBackgroundColor(Color.TRANSPARENT);}
			};
			ConditionalHandler.postIfAlive(removeDmg,howLongBackgroundIsApplied,this);			
//			createExplosion();
		}
		
		return dies;
	}
	
	public void heal(double howMuchHealed){
		health+=Math.abs(howMuchHealed);
	}
	
	//SET METHODS
	public void setDamage(double newDamage){
		damage=newDamage;
	}
	
	//GET METHODS
	public double getHealth(){
		return health;
	}
	public double getMaxHealth(){
		return maxHealth;
	}
	public double getDamage(){
		return damage;
	}

	@Override
	public void restartThreads() {
		// do nothing for this class. Override in a child class if there are threads added
	}
}