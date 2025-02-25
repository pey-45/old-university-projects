package e2.World;

public class Fleet {
	private final String name;
	private int HP;
	private final int armor, fire_power, anti_air, line_of_sight;

	public Fleet(String name, int HP, int armor, int fire_power, int anti_air, int line_of_sight ) {

		if(name == null || HP <= 0 || armor < 0 || fire_power < 0 || anti_air < 0 || line_of_sight < 0)
			throw new IllegalArgumentException("Invalid value found");

		this.name = name;
		this.HP = HP;
		this.armor = armor;
		this.fire_power = fire_power;
		this.anti_air = anti_air;
		this.line_of_sight = line_of_sight;
	}

	public void reduceHP(int HP) {
		this.HP -= HP;
	}
	public int getHP() {
		return HP;
	}
	public int getArmor() {
		return armor;
	}
	public int getFire_power() {
		return fire_power;
	}
	public int getAnti_air() {
		return anti_air;
	}
	public int getLine_of_sight() {
		return line_of_sight;
	}
	public boolean isSunk() {
		return !(this.HP > 0);
	}

	@Override
	public String toString(){
		return this.name +
				"\n\nHP " + this.HP +
				"\nBlindaje " + this.armor +
				"\nPoder de fuego " + this.fire_power +
				"\nAntiaereo " + this.anti_air +
				"\nLinea de vision " + this.line_of_sight;
	}
}
