package friendlies;

import guns.Gun;
import guns.Gun_AngledDualShot;
import guns.Gun_SingleShotStraight;
import interfaces.GameActivityInterface;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.View;
import bullets.Bullet_Basic_LaserLong;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class ProtagonistView extends Friendly_ShooterView{
	
	public static final int HOW_OFTEN_TO_MOVE_ROCKET=50;

	public static final int UPGRADE_BULLET_DAMAGE=0,UPGRADE_BULLET_SPEED=1,UPGRADE_BULLET_FREQ=3,
			UPGRADE_GUN=4,UPGRADE_FRIEND=5,UPGRADE_SCORE_MULTIPLIER=6,UPGRADE_HEAL=7;
	
	SharedPreferences gameState;
	GameActivityInterface myGame;
	private boolean isMoving;
	
	public ProtagonistView(Context context,GameActivityInterface interactWithGame) {
		super(context,DEFAULT_SPEED_Y,DEFAULT_SPEED_X,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH, (int)context.getResources().getDimension(R.dimen.ship_protagonist_game_width), 
				(int)context.getResources().getDimension(R.dimen.ship_protagonist_game_height),R.drawable.ship_protagonist);

		this.setX(  MainActivity.getWidthPixels()/2 - context.getResources().getDimension(R.dimen.ship_protagonist_game_width)/2 );//middle of screen

		gameState = getContext().getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		myGame=interactWithGame;
		
		createGunSet();
		this.post(exhaustRunnable);
	}
	
	
	private static final long EXHAUST_VISIBLE_TIME=500,
			HOW_OFTEN_TO_MOVE_EXHAUST=HOW_OFTEN_TO_MOVE_ROCKET / 2;
	private static final double EXHAUST_FREQ=5000;
	 
	private int count = 0;
    
    Runnable exhaustRunnable = new Runnable(){
    	 @Override
         public void run() {
				GameActivityInterface screen = (GameActivityInterface) getContext();
				if(count*HOW_OFTEN_TO_MOVE_ROCKET<EXHAUST_VISIBLE_TIME){
					screen.getExhaust().setVisibility(View.VISIBLE);					
		    		 //position the exhaust
					final float y=ProtagonistView.this.getY()+ProtagonistView.this.getHeight(); //set the fire's Y pos to behind rocket
					final float averageRocketsX= (2 * ProtagonistView.this.getX()+ProtagonistView.this.getWidth() )/2;//find average of rocket's left and right x pos
					final float x = averageRocketsX-screen.getExhaust().getLayoutParams().width /2;//fire's new X pos should set the middle of fire to middle of rocket
					screen.getExhaust().setY(y);
					screen.getExhaust().setX(x);
					count++;

					ConditionalHandler.postIfAlive(this,HOW_OFTEN_TO_MOVE_EXHAUST,ProtagonistView.this);//repost this runnable so the exhaust will reposition quickly
				
				}else{
					count=0;
					screen.getExhaust().setVisibility(View.GONE);					
					ConditionalHandler.postIfAlive(this,(long) (EXHAUST_FREQ+ 2 * EXHAUST_FREQ*Math.random()),ProtagonistView.this);//repost this to a random time in the future
				}
         }
    };
	
	@Override
	public void heal(int howMuchHealed){
		super.heal(howMuchHealed);
		myGame.setHealthBar();
	}
	@Override
	public void setHealth(int healthValue){
		super.setHealth(healthValue);
		myGame.setHealthBar();
	}
	

	@Override
	public void startShooting() {
		isShooting=true;
		for(Gun gun: myGuns){
			gun.startShootingImmediately();
		}
	}
	
	@Override 
	public boolean takeDamage(int howMuchDamage){ 
		//vibrate the phone  
        Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
		boolean isDead = super.takeDamage(howMuchDamage);
		myGame.setHealthBar();
		return isDead;
	}
	
	@Override
	public void restartThreads(){
		this.post(exhaustRunnable);
		super.restartThreads();
	}
	
	/**
	 * Do not allow the rocket to move off screen or past bounds
	 */
	@Override
	public boolean moveDirection(int direction){
		float x =this.getX();
		float y =this.getY();
		
		switch(direction){
		case Moving_ProjectileView.RIGHT:
			x+=this.getSpeedX();
			if((x+this.getWidth())<=MainActivity.getWidthPixels()){this.setX(x);}
			break;
		case Moving_ProjectileView.LEFT:
			x-=this.getSpeedX();
			if(x>=0){this.setX(x);}			
			break;
		case Moving_ProjectileView.UP:
			y-=this.getSpeedY();
			if(y > MainActivity.getHeightPixels()/2){this.setY(y);}			
			break;
		case Moving_ProjectileView.DOWN:
			y+=this.getSpeedY();
			if(y < ( GameActivity.getBottomScreen() - this.getHeight() ) ){this.setY(y);}			
			break;		
		}
		return false;
	}
	
	public void beginMoving(final int direction){
		isMoving=true;
		ConditionalHandler.startMoving(this, direction);
	}
	public void stopMoving(){
		isMoving=false;
	}
	public boolean isMoving(){
		return isMoving;
	}
	
	public void applyUpgrade(final int whichUpgrade){
		SharedPreferences gameState = getContext().getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		switch(whichUpgrade){
		case UPGRADE_BULLET_DAMAGE:
			final int gunDmg = gameState.getInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, 0);
			editor.putInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, gunDmg+1);
			break;
		case UPGRADE_BULLET_SPEED:
			final int gunSpd = gameState.getInt(GameActivity.STATE_BULLET_SPEED_LEVEL, 0);
			editor.putInt(GameActivity.STATE_BULLET_SPEED_LEVEL, gunSpd+1);
			break;
		case UPGRADE_BULLET_FREQ:
			final int gunFreq = gameState.getInt(GameActivity.STATE_BULLET_FREQ_LEVEL, 0);
			editor.putInt(GameActivity.STATE_BULLET_FREQ_LEVEL, gunFreq+1);
			break;
		case UPGRADE_GUN:
			final int gunSet = gameState.getInt(GameActivity.STATE_GUN_CONFIG, -1);
			editor.putInt(GameActivity.STATE_GUN_CONFIG, gunSet+1);
			break;
		case UPGRADE_FRIEND:
			final int friendLvl = gameState.getInt(GameActivity.STATE_FRIEND_LEVEL, 0);
			editor.putInt(GameActivity.STATE_FRIEND_LEVEL, friendLvl+1);
			break;
		case UPGRADE_SCORE_MULTIPLIER:
			final int resourceLvl = gameState.getInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, 0);
			editor.putInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, resourceLvl+1);
			break;
		case UPGRADE_HEAL:
			this.setHealth(getMaxHealth());
			break;
		}
		
		editor.commit();
		
		createGunSet();
	}

	/**
	 * define the different levels of guns protagonist may have
	 */
	
	public void createGunSet(){
		this.removeAllGuns();
		
		final float freq = getShootingDelay();
		final int dmg = (int) (DEFAULT_BULLET_DAMAGE + getBulletDamageLevel() * BULLET_DAMAGE_WEIGHT);
		final float speed = DEFAULT_BULLET_SPEED_Y + getBulletSpeedYLevel() * BULLET_SPEED_WEIGHT;
		
		switch(getGunLevel()){
		case 0:
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,speed,dmg,50) );
			break;
		case 1:
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,speed,dmg,20) );
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,speed,dmg,80) );
			break;
		case 2:
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,speed,dmg,20) );
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,speed,dmg,80) );
			break;
		case 3:
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,speed,dmg,20) );
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,speed,dmg,80) );
			break;
		case 4:
			Gun gun1 = new Gun_AngledDualShot(getContext(), this, new Bullet_Basic_LaserShort(),freq,speed,dmg,50) ;
			Gun gun2 = new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_Missile(),freq,speed,dmg,50) ;
			this.addGun(gun1);
			this.addGun(gun2);
			break;
		}
	}

	public float getShootingDelay(){
		return DEFAULT_BULLET_FREQ - getBulletBulletFreqLevel() * BULLET_FREQ_WEIGHT;
	}
	public int getGunLevel(){
		return gameState.getInt(GameActivity.STATE_GUN_CONFIG,-1);
	}
	public int getBulletDamageLevel(){
		return gameState.getInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, 0);
	}
	public int getBulletSpeedYLevel(){
		return gameState.getInt(GameActivity.STATE_BULLET_SPEED_LEVEL, 0);
	}
	public int getBulletBulletFreqLevel(){
		return gameState.getInt(GameActivity.STATE_BULLET_FREQ_LEVEL, 0);
	}
	
	@Override
	public void removeGameObject(){
		final long pattern[] = {0,50,100,50,100,50,100,400,100,300,100,350,50,200,100,100,50,600};
		createExplosion(this.getWidth(),this.getHeight(),R.drawable.explosion1,pattern);
		createExplosion(this.getWidth(),this.getHeight(),R.drawable.explosion1);
		createExplosion(this.getWidth(),this.getHeight(),R.drawable.explosion1);
		super.removeGameObject();
	}
	
}
