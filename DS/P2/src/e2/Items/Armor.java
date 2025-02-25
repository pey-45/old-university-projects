package e2.Items;

import e2.interfaces.DefenseItem;

public class Armor extends Item implements DefenseItem {

    int protection_value, min_strength;

    public Armor(String codename, int protection_value, int min_strength) {

        super(codename);

        this.protection_value = protection_value;
        this.min_strength = min_strength;
    }

    //Funciones de DefensiveItem
    @Override
    public int getMin_strength() {
        return this.min_strength;
    }

    @Override
    public int getProtection() {
        return this.protection_value;
    }
}
