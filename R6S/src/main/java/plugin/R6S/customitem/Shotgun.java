package plugin.R6S.customitem;

public class Shotgun extends SnowballGunTemplate {
	public Shotgun() {
		cooltime = 20; // tick
		reloadtime = 25;
		magazinesize = 5;
		speed = 2.5;
		damage = 3;
		this.headshotbonus = 1.2;
		kb = 0.2;
		number = 8;
		spread = 0.25;
		recoil = 0.4;
	}
}
