package com.jtronlabs.to_the_moon.misc;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.ship_views.Gravity_MeteorView;
import com.jtronlabs.to_the_moon.ship_views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.ship_views.Shooting_DiagonalMovingView;
import com.jtronlabs.to_the_moon.ship_views.Shooting_MovingArrayView;

public class EnemyFactory{
	
//	public final static double ENEMY_ONE_SCORE=10,
//			ENEMY_ONE_HEALTH=10,
//			ENEMY_ONE_COLLISION_DAMAGE=10,
//			ENEMY_ONE_SPEED_X_AND_Y=3,
//			ENEMY_ONE_BULLET_FREQ
			
			
			
			
	private Context ctx;
	private Levels levelInfo = new Levels();
	private RelativeLayout gameLayout;
    
    private Handler enemySpawnHandler = new Handler();
    
    private Runnable meteorSpawningRunnable = new Runnable(){
    	@Override
        public void run() {
    		Gravity_MeteorView meteor = new Gravity_MeteorView(ctx);
    		gameLayout.addView(meteor,1);
    		GameActivity.enemies.add(meteor);
    		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		enemySpawnHandler.postDelayed(this, calculateMeteorSpawnInterval());
    	}
	};
	
	Runnable spawnSimpleShooterRunnable = new Runnable(){
    	@Override
        public void run() {
    		final int numShootersAlive = Shooting_MovingArrayView.allSimpleShooters.size();
    		final int maxNumShooters = Shooting_MovingArrayView.getMaxNumShips();
    		final int numShootersAliveCutoff = 4;
    		
    		if(levelInfo.getDifficulty()>0 && numShootersAlive<maxNumShooters){
    			spawnAllSimpleShooters();
//    			//if num shooters< .33 of the max, there is a 33% chance to respawn all shooters
//    			if(numShootersAlive<(maxNumShooters/numShootersAliveCutoff) && Math.random()<0.33 ){
//    				spawnAllSimpleShooters();
//    			}
//    			//otherwise, respawn just 1 simple shooter
//    			else{
//	    			SimpleEnemyShooterArray shooter = new SimpleEnemyShooterArray(ctx);
//	    			gameLayout.addView(shooter,1);
//	        		GameActivity.enemies.add(shooter);
//    			}
			}
    		 
    		enemySpawnHandler.postDelayed(this, calculateMovingSideToSideShooterSpawnInterval());
    	}
	};
	
	Runnable spawnDiagonalShooterRunnable = new Runnable(){
		@Override
		public void run() {
			final int diff = levelInfo.getDifficulty();
			
			final int score=5, background =R.drawable.ufo;
			final double speedY=1.8, speedX=10, 
					collisionDamage=10*diff,health=10*diff,
					bulletFreq=2000/Math.sqrt(diff)+Math.random()*3000/Math.sqrt(diff);
			final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
					width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
			
			Shooting_DiagonalMovingView shooter = new Shooting_DiagonalMovingView(ctx,score,speedY,speedX,
					collisionDamage,health,bulletFreq,background,height,width);
			
			gameLayout.addView(shooter,1);
    		GameActivity.enemies.add(shooter);
			
			enemySpawnHandler.postDelayed(this, calculatDiagonalShooterSpawnInterval());
		}
	};
	
	public EnemyFactory(Context context,RelativeLayout gameScreen){
		ctx=context;
		gameLayout=gameScreen;
		
		Shooting_MovingArrayView.resetSimpleShooterArray();
		Shooting_MovingArrayView.startMovingAllShooters();
	} 
	
	public void cleanUpThreads(){
		enemySpawnHandler.removeCallbacks(meteorSpawningRunnable);
	    enemySpawnHandler.removeCallbacks(spawnSimpleShooterRunnable);
	    enemySpawnHandler.removeCallbacks(spawnDiagonalShooterRunnable);
	}
	public void restartThreads(){
		enemySpawnHandler.postDelayed(meteorSpawningRunnable,calculateMeteorSpawnInterval());
	    enemySpawnHandler.postDelayed(spawnSimpleShooterRunnable,calculateMovingSideToSideShooterSpawnInterval());
	    enemySpawnHandler.postDelayed(spawnDiagonalShooterRunnable,calculateMovingSideToSideShooterSpawnInterval());
	}  
	
	private long calculateMeteorSpawnInterval(){
		final int meteorInterval = 3000;
		return (long) (meteorInterval/(Math.sqrt(levelInfo.getDifficulty()))+Math.random()*3000);
	}
	private long calculateMovingSideToSideShooterSpawnInterval(){
		final int simpleArrayShooterInterval = 6000;
		return (long) (simpleArrayShooterInterval/(Math.sqrt(levelInfo.getDifficulty()))+Math.random()*3000);
	}
	private long calculatDiagonalShooterSpawnInterval(){
		final int diagonalShooterInterval=2000; 
		return (long) (diagonalShooterInterval/(Math.sqrt(levelInfo.getDifficulty()))+Math.random()*4000);
	}
	public void spawnAllSimpleShooters(){
		int temp=Shooting_MovingArrayView.allSimpleShooters.size();
		
		for(int i=temp;i<Shooting_MovingArrayView.getMaxNumShips();i++){
			Shooting_MovingArrayView shooter = spawnOneShooting_MovingArrayView();
			
			gameLayout.addView(shooter,1);
    		GameActivity.enemies.add(shooter);
		}
	}
	private Shooting_MovingArrayView spawnOneShooting_MovingArrayView(){
		final int diff = levelInfo.getDifficulty();

		final int score=10*diff, background =R.drawable.ufo;
		final double speedY=3, speedX=3, 
				collisionDamage=20*diff,
				health=10*diff;
		
		final double bulletFreq = (5000/Math.sqrt(diff))+Math.random()*(4000/Math.sqrt(diff));
		final float height = ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width = ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_MovingArrayView shooter = new Shooting_MovingArrayView(ctx,score,speedY,speedX,collisionDamage,health,bulletFreq,
				background,height,width);
		
		return shooter;
	}
	public void spawnGiantMeteor(){
		Gravity_MeteorView giant = new Gravity_MeteorView(ctx);
		
		//change width and height. set X and Y positions
		final int width = (int)ctx.getResources().getDimension(R.dimen.giant_meteor_width);
		final int height= (int)ctx.getResources().getDimension(R.dimen.giant_meteor_height);
		giant.setLayoutParams(new LayoutParams(width,height));
		giant.setY(-height);
		giant.setX((float) ((ProjectileView.widthPixels-width)*Math.random()));
		
		//set damage and health to 200, score to 20
		giant.setDamage(150);
		giant.heal(150-Gravity_MeteorView.DEFAULT_HEALTH);
		giant.setScoreValue(20);
		giant.changeSpeedYDown(.45);
		
		//add to layout
		gameLayout.addView(giant,1);
		GameActivity.enemies.add(giant);		
	}
}