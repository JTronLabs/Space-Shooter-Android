package levels;

import android.content.Context;
import android.widget.RelativeLayout;
import enemies.Shooting_ArrayMovingView;

public class Factory_LevelWaves extends Factory_Waves{

	public static int DEFAULT_WAVE_DURATION=5000;
	
	protected static int currentProgressInLevel;
	
	//For runnables that last an entire level. Equal to (num waves in a level - 1)*wave_duration. The minus one is to ensure nothing new spawns at end of level
	//This time can be less than level, the only effect is waves will last less time, which could be useful
	public static final int[] LEVEL_LENGTHS={ 11*DEFAULT_WAVE_DURATION,
		11*DEFAULT_WAVE_DURATION,
		10*DEFAULT_WAVE_DURATION,
		12*DEFAULT_WAVE_DURATION,
		9*DEFAULT_WAVE_DURATION};
	
	public Factory_LevelWaves(Context context,RelativeLayout gameScreen){
		super( context, gameScreen);
	}
	
	/**
	 * define common waves here. for example, 2 meteor showers, one on each eadge of screen that force user to go into middle of screen.
	 * this class should contain runnables, not functions, so that the level factory can post delay them
	 */
	
	protected final static Runnable doNothing = new Runnable(){
		@Override
		public void run() {}};

	protected final static Runnable levelWavesOver = new Runnable(){
		@Override
		public void run() {	
			LevelSystem.notifyLevelWavesCompleted();
		}
	};
	
	//regular meteors
	final static Runnable meteorSidewaysOnePerSecondForWholeLevel = new Runnable(){
		@Override
		public void run() {
			spawnSidewaysMeteorsWave( LevelSystem.getCurrentLevelLength() /1500 ,1000);
			currentProgressInLevel++;
		}
	};
	final static Runnable meteorsStraightOnePerSecondForWholeLevel = new Runnable(){
		@Override
		public void run() {
			spawnStraightFallingMeteorsAtRandomXPositionsWave( LevelSystem.getCurrentLevelLength() /1500 ,1000);
			currentProgressInLevel++;
		}
	};
	final static Runnable meteorSidewaysThisWave = new Runnable(){
		@Override
		public void run() {
			spawnSidewaysMeteorsWave(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			currentProgressInLevel++;
		}
	};	
	
	//meteor showers
	final static Runnable meteorShowerLong = new Runnable(){//lasts 2 waves
		@Override
		public void run() {
			spawnMeteorShower( (DEFAULT_WAVE_DURATION * 2 )/1000,1000,true);
			currentProgressInLevel++;
		}
	};
	final static Runnable meteorShowersThatForceUserToMiddle = new Runnable(){//this does not last a whole wave, which is fine.
		@Override
		public void run() {
				spawnMeteorShower(4,400,true);
				spawnMeteorShower(4,400,false);
				currentProgressInLevel++;
		}
	};
	final static Runnable meteorShowersThatForceUserToRight = new Runnable(){
		@Override
		public void run() {
				spawnMeteorShower(9,DEFAULT_WAVE_DURATION/9,true);
				currentProgressInLevel++;
		}
	};
	final static Runnable meteorShowersThatForceUserToLeft = new Runnable(){
		@Override
		public void run() {
				spawnMeteorShower(9,DEFAULT_WAVE_DURATION/9,false);
				currentProgressInLevel++;
		}
	};

	//giant meteors
	final static Runnable meteorsGiantAndSideways = new Runnable(){
		@Override
		public void run() {
			spawnGiantMeteorWave(2,DEFAULT_WAVE_DURATION/2);//spawn for entire wave
			spawnSidewaysMeteorsWave(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			currentProgressInLevel++;
		}
	};
	final static Runnable meteorsOnlyGiants = new Runnable(){
		@Override
		public void run() {
			spawnGiantMeteorWave(4,DEFAULT_WAVE_DURATION/4);
			currentProgressInLevel++;
		}
	};
	
	//array shooters
	final static Runnable refreshArrayShooters = new Runnable(){
		@Override
		public void run() {
			int temp=Shooting_ArrayMovingView.allSimpleShooters.size();
			
			for(int i=temp;i<Shooting_ArrayMovingView.getMaxNumShips();i++){
				spawnOneShooting_MovingArrayView();
			}
			currentProgressInLevel++;
		}
	};
	
	//dive bombers	
	final static Runnable diveBomberOnePerSecond = new Runnable(){
		@Override
		public void run() {
			spawnDiveBomberWave(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
			currentProgressInLevel++;
		}
	};
	
	
}
