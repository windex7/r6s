package plugin.R6S.customitem;

public class Shotgun extends SnowballGunTemplate {
	public Shotgun() {
		cooltime = 16; // tick
		reloadtime = 16;
		speed = 3;
		damage = 3;
		kb = 0.1;
		number = 9;
		spread = 0.3;
		recoil = 0.4;
	}
}
