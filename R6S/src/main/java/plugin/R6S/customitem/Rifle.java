package plugin.R6S.customitem;

public class Rifle extends SnowballGunTemplate {
	public Rifle() {
		cooltime = 3; // tick
		reloadtime = 25;
		magazinesize = 40;
		speed = 3;
		damage = 2;
		kb = 0.1;
		number = 1;
		spread = 0.07;
		recoil = 0;
	}
}
