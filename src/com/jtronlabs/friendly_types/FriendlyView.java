package com.jtronlabs.friendly_types;

import android.content.Context;
import android.util.AttributeSet;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.parents.Projectile_ShootingView;

public class FriendlyView extends Projectile_ShootingView{
	
	public FriendlyView(Context context,double projectileSpeedY
			,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super( context, true, projectileSpeedY,projectileSpeedX, 
				 projectileDamage, projectileHealth);
		
		GameActivity.friendlies.add(this);
	}
	
	public FriendlyView(Context context,AttributeSet at,double projectileSpeedY,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super( context,at, true, projectileSpeedY,projectileSpeedX, 
				 projectileDamage, projectileHealth);
		
		GameActivity.friendlies.add(this);
	}
	
	@Override
	public void removeGameObject(){
		if(this.myGun.myBullets.size()==0){
			GameActivity.friendlies.remove(this);			
		}
		super.removeGameObject();
	}
}