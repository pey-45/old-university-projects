package e2.Characters;

import e2.interfaces.AttackItem;
import e2.interfaces.DefenseItem;

import java.util.ArrayList;

public abstract class Character {
    private final ArrayList<DefenseItem> defensive_items = new ArrayList<>();
    private final ArrayList<AttackItem> attack_items = new ArrayList<>();
    private String name;
    private int health, strength, mana;

    public Character(String name, int health, int strength, int mana) {
        if (health < 1 || health > 20)
            throw new IllegalArgumentException("El valor de vida debe estar entre 1 y 20");
        else if (strength < 0 || strength > 10)
            throw new IllegalArgumentException("El valor de fuerza debe estar entre 0 y 10");
        else if (mana < 0 || mana > 10)
            throw new IllegalArgumentException("El valor de mana debe estar entre 0 y 10");

        this.name = name;
        this.health = health;
        this.strength = strength;
        this.mana = mana;
    }
    public Character() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name != null) this.name = name;
    }

    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        if (health >= 0 && health <= 20) this.health = health;
    }
    public void reduceHealth(int damage) {
        if (damage > 0) this.health -= damage;
    }

    public int getStrength() {
        return strength;
    }
    public void setStrength(int strength) {
        if (strength >= 0 && strength <= 10) this.strength = strength;
    }

    public int getMana() {
        return mana;
    }
    public void setMana(int mana) {
        if (mana >= 0 && mana <= 10) this.mana = mana;
    }

    public void equipDefensiveItem(DefenseItem defensive_item) {

        if (this.defensive_items.size() == 5)
            throw new IllegalCallerException("El inventario defensivo está lleno, no puedes añadir un nuevo objeto");
        else
            this.defensive_items.add(defensive_item);
    }

    public void equipAttackItem(AttackItem attack_item) {

        if (this.attack_items.size() == 5)
            throw new IllegalCallerException("El inventario ofensivo está lleno, no puedes añadir un nuevo arma");
        else
            this.attack_items.add(attack_item);
    }

    public int getTotalProtection() {

        int total_protection = 0;

        //Recorre la lista de objetos defensivos, y suma su protección al total si el personaje cumple los requisitos mínimos de fuerza
        for (DefenseItem defensive_item : this.defensive_items) {
            if (this.strength >= defensive_item.getMin_strength())
                total_protection += defensive_item.getProtection();
        }

        return total_protection;
    }

    public void attackCharacter(Character character_attacked) {

        if (this.attack_items.isEmpty())
            throw new IllegalCallerException("No tienes armas");

        AttackItem current_weapon = null;
        int i;

        //Recorre la lista de armas buscando una usable. Si la encuentra, sale, si llega al final es que no ha encontrado ningún arma que pueda usar y se devuelve error
        for (i = 0; i < attack_items.size(); i++) {

            current_weapon = this.attack_items.get(i);

            if (this.mana >= current_weapon.getMin_mana())
                break;
            else if (i == attack_items.size() - 1)
                throw new IllegalCallerException("No puedes usar ninguna de tus armas para atacar");
        }

        current_weapon.Attack(character_attacked);

        if (current_weapon.getDurability() <= 0)
            this.attack_items.remove(i);
    }
}
