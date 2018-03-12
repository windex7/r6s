package plugin.R6S.customitem;

public class Sniper extends SnowballGunTemplate {
	public Sniper() {
		cooltime = 20; // tick
		reloadtime = 35;
		magazinesize = 5;
		speed = 4;
		damage = 16;
		this.headshotbonus = 1.9;
		kb = 0.8;
		number = 1;
		spread = 0;
		recoil = 0.6;
		this.scopelevel = 4;
	}
}
