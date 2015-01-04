package com.jtronlabs.views;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.new_proj.GameActivity;
import com.jtronlabs.new_proj.R;

public class ShootingView extends GravityView{
	
	private int bulletImgId;
	private double bulletDamage,bulletSpeed,bulletFreq;
	
	ArrayList<ProjectileView> myBullets = new ArrayList<ProjectileView>();
	private boolean shootingUp=true,autoSpawnBullets=false;
	
	Handler shooterHandler = new Handler();
    Runnable spawnBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		spawnBullet();
    		shooterHandler.postDelayed(this, (long) bulletFreq);
    	}
	};
	
	Runnable moveMyBulletsRunnable = new Runnable(){
    	@Override
        public void run() {
    		for(int i=0;i<myBullets.size();i++){
    			ProjectileView bullet = myBullets.get(i);
	    		if(shootingUp){
	    			bullet.move(ProjectileView.UP,true);
	    		}else{
	    			bullet.move(ProjectileView.DOWN,true);
	    		}
    		}
			shooterHandler.postDelayed(moveMyBulletsRunnable, HOW_OFTEN_TO_MOVE);
			
    	}
	};
	

	public void removeView(boolean showExplosion){
		super.removeView(showExplosion);
		cleanUpThreads();
	}
	
	public ShootingView(Context context,double projectileSpeedUp,double projectileSpeedDown,double projectileSpeedX, double projectileDamage,double projectileHealth,double bulletSpd,double bulletDmg,int bulletBackground) {
		super(context,projectileSpeedUp,projectileSpeedDown,projectileSpeedX,projectileDamage,projectileHealth);
		
		bulletDamage=bulletDmg;
		bulletSpeed=bulletSpd*screenDens;
		bulletImgId=bulletBackground;
	}
	
	public ShootingView(Context context,AttributeSet at,double projectileSpeedUp,double projectileSpeedDown,double projectileSpeedX, double projectileDamage,double projectileHealth,double bulletSpd,double bulletDmg,int bulletBackground) {
		super(context,at,projectileSpeedUp,projectileSpeedDown,projectileSpeedX,projectileDamage,projectileHealth);
		
		bulletDamage=bulletDmg;
		bulletSpeed=bulletSpd*screenDens;
		bulletImgId=bulletBackground;
	}
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
		shooterHandler.removeCallbacks(spawnBulletRunnable);
		shooterHandler.removeCallbacks(moveMyBulletsRunnable);
	}
	
	public void restartThreads(){
		super.restartThreads();
		if(autoSpawnBullets){
			shooterHandler.postDelayed(spawnBulletRunnable,(long) bulletFreq);		
		}
		shooterHandler.post(moveMyBulletsRunnable);
	}
	
	public void changeBulletFrequency(double newBulletFreq){
		bulletFreq=newBulletFreq;
	}
	public void changeBulletBackground(int newBackgroundResourceId){
		bulletImgId=newBackgroundResourceId;
	}
	public void changeBulletSpeed(double newBulletSpeed){
		bulletSpeed=newBulletSpeed;
	}
	public void changeBulletDamage(double newBulletDamage){
		bulletDamage=newBulletDamage;
	}
	/**
	 * set View to automatically create bullets. These bullets will be shot downwards, toward the protagonist
	 * @param bulletSpawningFrequency-frequency at which bullets are automatically spawned
	 */
	public void spawnBulletsAutomatically(double bulletSpawningFrequency){
		shootingUp=false;
		autoSpawnBullets=true;
		bulletFreq=bulletSpawningFrequency;
	}
	
	/**
	 * Create a bullet and add it to the screen
	 */
	public void spawnBullet(){
		ProjectileView bullet = new ProjectileView(ctx,bulletSpeed,bulletSpeed,1,bulletDamage,0.1);
		
		//add bullet to layout
		bullet.setBackgroundResource(bulletImgId);
		int width=(int)ctx.getResources().getDimension(R.dimen.bullet_width);
		int height=(int)ctx.getResources().getDimension(R.dimen.bullet_height);
		bullet.setLayoutParams(new LayoutParams(width,height));
		
		//initially position the Bullet View
		float xAvg = (ShootingView.this.getX()+ShootingView.this.getX()+ShootingView.this.getWidth())/2;
		bullet.setX(xAvg);
		if(shootingUp){
			bullet.setY(ShootingView.this.getY());//middle and top of ShootingView
		}else{
			bullet.setY(ShootingView.this.getY()+ShootingView.this.getHeight());//middle and bottom of ShootingView
		}
		GameActivity.gameScreen.addView(bullet,1);
		GameActivity.dangerousObjects.add(bullet);

		myBullets.add(bullet);
	}
	
}
