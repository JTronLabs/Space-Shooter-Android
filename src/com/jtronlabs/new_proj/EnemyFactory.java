package com.jtronlabs.new_proj;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.jtronlabs.views.MeteorView;
import com.jtronlabs.views.SideToSideMovingShooter;

public class EnemyFactory{
	
	private Context ctx;
	private Levels levelInfo = new Levels();
	private RelativeLayout gameLayout;
	
    //
    public int meteorInterval=4000;
    public int movingSideToSideShooterInterval=5000;
    
    Handler enemySpawnHandler = new Handler();
    
    Runnable meteorSpawningRunnable = new Runnable(){
    	@Override
        public void run() {
//    		double rand = Math.random()*100;
//    		switch(levelInfo.getLevel()){
//    		case 1:
//    			if(rand<)
//    			break;
//    		case 2:
//    			
//    			break;
//    		case 3:
//    			
//    			break;
//    		case 4:
//    			
//    			break;
//    		case 5:
//    			
//    			break;
//    		}
    		MeteorView meteor = new MeteorView(ctx);
    		gameLayout.addView(meteor,1);
    		GameActivity.enemies.add(meteor);
    		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		enemySpawnHandler.postDelayed(this, calculateMeteorInterval());
    	}
	};
	
	Runnable spawnMovingSideToSideShootingRunnable = new Runnable(){
    	@Override
        public void run() {
    		if(levelInfo.getLevel()>1 && SideToSideMovingShooter.allSideToSideShooters.size()<SideToSideMovingShooter.MAX_NUM_SIDE_TO_SIDE_SHOOTERS){
    			SideToSideMovingShooter shooter = new SideToSideMovingShooter(ctx);
    			gameLayout.addView(shooter,1);
        		GameActivity.enemies.add(shooter);
        		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		}
    		enemySpawnHandler.postDelayed(this, calculateMovingSideToSideShooterInterval());
    	}
	};
	
	public EnemyFactory(Context context,RelativeLayout gameScreen){
		ctx=context;
		gameLayout=gameScreen;
	}
	
	public void cleanUpThreads(){
		enemySpawnHandler.removeCallbacks(meteorSpawningRunnable);
	    enemySpawnHandler.removeCallbacks(spawnMovingSideToSideShootingRunnable);
	}
	public void restartThreads(){
		enemySpawnHandler.postDelayed(meteorSpawningRunnable,calculateMeteorInterval());
	    enemySpawnHandler.postDelayed(spawnMovingSideToSideShootingRunnable,calculateMovingSideToSideShooterInterval());
	}
	
	private long calculateMeteorInterval(){
		return (long) (meteorInterval/(Math.sqrt(levelInfo.getLevel()))+Math.random()*1000);
	}
	private long calculateMovingSideToSideShooterInterval(){
		return (long) (movingSideToSideShooterInterval/(Math.sqrt(levelInfo.getLevel()))+Math.random()*3000);
	}
}
