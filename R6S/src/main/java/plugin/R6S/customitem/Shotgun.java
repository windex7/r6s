package plugin.R6S.customitem;

public class Shotgun extends SnowballGunTemplate {
	public Shotgun() {
		cooltime = 16; // tick
		reloadtime = 20;
		magazinesize = 5;
		speed = 3;
		damage = 3;
		kb = 0.2;
		number = 9;
		spread = 0.2;
		recoil = 0.4;
	}
}
