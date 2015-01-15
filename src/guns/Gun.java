package guns;
  
import interfaces.Shooter;
import support.ConditionalHandler;
import android.content.Context;
import bullets.Bullet;


/**
 * 
 * @author JAMES LOWREY
 *
 */
public abstract class Gun {
	
	/**
	 * Create bullets of the shooter's type at the shooter's position. Properties such as number of bullets, 
	 * direction of bullets, initial position of bullets, and more may be different
	 * @return true if gun is a special and is out of ammo. False otherwise.
	 */
	public abstract boolean shoot();	
	
	Shooter shooter;
	Bullet myBulletType;
	Context ctx;

	protected double bulletFreq,bulletSpeedY,bulletDamage;
	
	private Runnable shootingRunnable = new Runnable(){
		  	@Override
		      public void run() {
	  				Gun.this.shoot();
	  				ConditionalHandler.postDelayedIfShooting(this, (long) bulletFreq,shooter);
		  		}
			};
	
	public Gun(Context context,Shooter theShooter,Bullet bulletType,double bulletFrequency,double bulletSpeedVertical,double bulletDmg) {
		ctx=context;
		
		bulletFreq=bulletFrequency;
		bulletSpeedY=bulletSpeedVertical;
		bulletDamage=bulletDmg;
		myBulletType = bulletType;
		shooter=theShooter;
	} 
	
	public void startShootingImmediately(){
		ConditionalHandler.postDelayedIfShooting(shootingRunnable,0,shooter);
	}
	public void startShootingDelayed(){
		ConditionalHandler.postDelayedIfShooting(shootingRunnable,(long)bulletFreq,shooter);	
	}
	
	public void stopShooting(){
		shooter.removeCallbacks(shootingRunnable);
	}
	
	public Bullet getBulletType(){
		return myBulletType;
	}
	
	public void setBulletType(Bullet newBullet){
		myBulletType= newBullet;
	}
	
	public double getBulletSpeedY() {
		return bulletSpeedY;
	}

	public double getBulletDamage() {
		return bulletDamage;
	}

	public double getBulletFreq() {
		return bulletFreq;
	}
	
	public void setBulletFreq(double freq) {
		bulletFreq=freq;
	}

	public void setBulletSpeedY(double newSpeed) {
		bulletSpeedY=newSpeed;
	}

	public void setBulletDamage(double newDamage) {
		bulletDamage = newDamage;
	}

}
