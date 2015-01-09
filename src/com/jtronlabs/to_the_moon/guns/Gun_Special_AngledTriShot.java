package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.bullets.Bullet_Interface;
import com.jtronlabs.to_the_moon.bullets.Projectile_BulletView;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public  class Gun_Special_AngledTriShot extends Gun_Special {
	
	private static final double DEFAULT_ANGLE=8;
	
	public Gun_Special_AngledTriShot(Context context,Gravity_ShootingView theShooter,boolean shootingUpwards,double bulletSpeedVertical,
			double bulletDamage,double bulletFrequency) {
		super(context,theShooter,shootingUpwards,bulletSpeedVertical,bulletDamage,bulletFrequency);
	}
	public Gun_Special_AngledTriShot(Context context,
			Gravity_ShootingView theShooter) {
		super(context,theShooter);
	}
	public boolean shoot(){
		//create 3 bullets at center of shooter, with left and right bullets traveling X at speed speedY*tan(DEFAULT_ANGLE)
		double bulletSpeedX = shooter.myGun.getBulletSpeedY() * Math.tan(Math.toRadians(DEFAULT_ANGLE));
		
		Projectile_BulletView bulletLeft = shooter.myBulletType.getBullet(ctx, shooter, shootingUp, bulletSpeedY, 
				-1 * bulletSpeedX,bulletDamage, Bullet_Interface.BULLET_MIDDLE);
		Projectile_BulletView bulletMid = shooter.myBulletType.getBullet(ctx, shooter, shootingUp, bulletSpeedY, 
				Bullet_Interface.BULLET_TRAVELS_STRAIGHT,bulletDamage, Bullet_Interface.BULLET_MIDDLE);
		Projectile_BulletView bulletRight = shooter.myBulletType.getBullet(ctx, shooter, shootingUp, bulletSpeedY, 
				bulletSpeedX,bulletDamage, Bullet_Interface.BULLET_MIDDLE);
		
		//add bullets to layout
		((RelativeLayout)shooter.getParent()).addView(bulletLeft,1);
		((RelativeLayout)shooter.getParent()).addView(bulletMid,1);
		((RelativeLayout)shooter.getParent()).addView(bulletRight,1);

		//add bullets to shooter's list of bullets
		myBullets.add(bulletLeft);
		myBullets.add(bulletMid);
		myBullets.add(bulletRight);
		
		return decrementAmmo();
	}
}