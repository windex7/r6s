package plugin.R6S.customitem;

public class Rifle extends SnowballGunTemplate {
	public Rifle() {
		cooltime = 3; // tick
		reloadtime = 25;
		magazinesize = 40;
		speed = 3;
		damage = 3;
		kb = 0.05;
		number = 1;
		spread = 0.03;
		recoil = 0;
	}
}
