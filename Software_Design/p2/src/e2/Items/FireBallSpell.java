package e2.Items;

import e2.Characters.Character;
import e2.interfaces.AttackItem;

public class FireBallSpell extends Item implements AttackItem {

    int durability, max_durability, damage_value, min_mana;

    public FireBallSpell(String codename, int durability, int max_durability, int damage_value, int min_mana) {

        super(codename);
        if (durability <= 0 || max_durability <= 0 || durability > max_durability) throw new IllegalArgumentException();
        this.durability = durability;
        this.max_durability = max_durability;
        this.damage_value = damage_value;
        this.min_mana = min_mana;
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

        //FireBallSpell pierde uno de daño en su último uso
        if (this.durability == 1) this.damage_value--;

        attacked_character.reduceHealth(this.damage_value - attacked_character.getTotalProtection());
        this.durability--;
    }
}