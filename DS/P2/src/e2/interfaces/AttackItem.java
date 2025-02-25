package e2.interfaces;

import e2.Characters.Character;

public interface AttackItem {

    int getMin_mana();

    int getDurability();

    void Attack(Character attacked_character);
}
