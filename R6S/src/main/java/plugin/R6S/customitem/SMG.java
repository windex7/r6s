package plugin.R6S.customitem;

public class SMG extends SnowballGunTemplate {
	public SMG() {
		cooltime = 1; // tick
		reloadtime = 50;
		magazinesize = 15;
		speed = 2;
		damage = 1;
		kb = 0.05;
		this.burst = 2;
		this.burstdelay = 2;
		number = 1;
		spread = 0.1;
		recoil = 0.05;
	}

}
