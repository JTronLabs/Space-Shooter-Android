package com.jtronlabs.to_the_moon.views;

import android.content.Context;
import android.widget.RelativeLayout;
 
import com.jtronlabs.to_the_moon.R;

public class DiagonalMovingShooterView extends ShootingView implements GameObject{
	
	private final static int DEFAULT_SCORE=10;
	public final static double DEFAULT_SPEED_UP=5,DEFAULT_SPEED_DOWN=.5,DEFAULT_SPEEDX=15,
			DEFAULT_COLLISION_DAMAGE=20, DEFAULT_HEALTH=10;
	private boolean travelingRight;
	
	Runnable moveDiagonalRunnable = new Runnable(){

		@Override
		public void run() {
			boolean offScreen = DiagonalMovingShooterView.this.move(ProjectileView.DOWN);
			if(offScreen){
				DiagonalMovingShooterView.this.removeView(false);
				DiagonalMovingShooterView.this.removeCallbacks(this);
				return;
			}
			if(travelingRight){
				DiagonalMovingShooterView.this.move(ProjectileView.RIGHT);
				final double farRight = widthPixels-DiagonalMovingShooterView.this.getSpeedX();
				final double rightSideOfShip = DiagonalMovingShooterView.this.getX()+DiagonalMovingShooterView.this.getWidth();
				if(rightSideOfShip>=farRight){//ship is on far right portion of screen
					travelingRight=false;
				}				
			}else{
				DiagonalMovingShooterView.this.move(ProjectileView.LEFT);
				final double farLeft = DiagonalMovingShooterView.this.getSpeedX();
				final double leftSideOfShip = DiagonalMovingShooterView.this.getX();
				if(leftSideOfShip <= farLeft){//ship is on far left portion of screen
					travelingRight=true;
				}		
			}
			
			DiagonalMovingShooterView.this.postDelayed(this,ProjectileView.HOW_OFTEN_TO_MOVE);
		}
		
	};
	
	public DiagonalMovingShooterView(Context context) {
		super(context,DEFAULT_SCORE,DEFAULT_SPEED_UP,DEFAULT_SPEED_DOWN,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH);
		
		final double bulletFreq = (2000+Math.random()*4000);
		spawnBulletsAutomatically(bulletFreq);
		
		this.lowestPositionThreshold=heightPixels;
		
		//set image background, width, and height
		this.setImageResource(R.drawable.ufo);
		final int height_int=(int)context.getResources().getDimension(R.dimen.diagonal_shooter_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		travelingRight = (Math.random()<.5);//50% chance of moving right or left
		this.setX((float) (widthPixels*Math.random()));
		this.setY(0);
		
		cleanUpThreads();
		restartThreads();
	}
	
	public int removeView(boolean showExplosion){
		cleanUpThreads();
		return super.removeView(showExplosion);
	}
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
		this.removeCallbacks(moveDiagonalRunnable);
	}
	public void restartThreads(){
		super.restartThreads();
		this.postDelayed(moveDiagonalRunnable, ProjectileView.HOW_OFTEN_TO_MOVE);
	}
}
