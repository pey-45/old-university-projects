package e2.Items;

import e2.Characters.Character;
import e2.interfaces.AttackItem;
import e2.interfaces.DefenseItem;

public class Sword extends Item implements AttackItem, DefenseItem {

    int durability, max_durability, damage_value, min_mana, protection_value, min_strength;

    public Sword(String codename, int durability, int max_durability, int damage_value, int min_mana, int protection_value, int min_strength) {

        super(codename);

        if (durability <= 0 || max_durability <= 0 || durability > max_durability) throw new IllegalArgumentException();
        this.durability = durability;
        this.max_durability = max_durability;
        this.damage_value = damage_value;
        this.min_mana = min_mana;

        this.protection_value = protection_value;
        this.min_strength = min_strength;
    }

    //Funciones de OffensiveItem
    @Override
    public int getMin_mana() {
        return this.min_mana;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public void Attack(Character attacked_character) {

        attacked_character.reduceHealth(this.damage_value - attacked_character.getTotalProtection());
        this.durability--;
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
