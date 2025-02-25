package e2;

import e2.Characters.Character;
import e2.Characters.Warrior;
import e2.Characters.Wizard;
import e2.Game.Game;
import e2.Items.Armor;
import e2.Items.FireBallSpell;
import e2.Items.Sword;
import e2.Items.Wand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    static Game game;
    static Warrior warrior;
    static Wizard wizard;
    static Armor lightArmor, heavyArmor;
    static Sword normalSword, uselessSword;
    static Wand firstUseWand, normalWand;
    static FireBallSpell fragileFireBall, normalFireBall;

    @BeforeAll
    static void setup() {
        //Armaduras
        lightArmor = new Armor("All-Knowing Armor", 2, 4);
        heavyArmor = new Armor("Berserker Armor", 5, 8);

        //Armas
        fragileFireBall = new FireBallSpell("Bola de fogo", 1, 4, 16, 8);
        normalFireBall = new FireBallSpell("Bola de fogo durable", 20, 20, 15, 5);

        //Mixto
        normalSword = new Sword("DragonSlayer", 6, 6, 8, 8, 4, 8);
        uselessSword = new Sword("Broken Straight Sword", 20, 20, 1, 1, 1, 1);
        firstUseWand = new Wand("Schierke's Wand", 1, 1, 7, 4, 3, 4);
        normalWand = new Wand("All-Knowing Staff", 1, 5, 12, 7, 3, 2);

        //Personajes y equipar
        warrior = new Warrior("Guts", 20, 8, 8); //5 + 4 + 3 = 12 protección total
        warrior.equipDefensiveItem(heavyArmor);
        warrior.equipDefensiveItem(normalSword);
        warrior.equipDefensiveItem(normalWand);
        warrior.equipAttackItem(normalSword); //10 daño
        warrior.equipAttackItem(uselessSword);

        wizard = new Wizard("Gideon-Ofnir", 15, 5, 10); //2 protección total
        wizard.equipDefensiveItem(lightArmor);
        wizard.equipAttackItem(fragileFireBall); //16 - 1 daño
        wizard.equipAttackItem(firstUseWand); //7 * 2 daño
        wizard.equipAttackItem(normalWand); //12 daño
        wizard.equipAttackItem(normalFireBall); //15 daño

        game = new Game(warrior, wizard);
    }

    @Test
    void testGetterSetter() {
        Character SettedWizard = new Wizard();
        SettedWizard.setName("Big Hat Logan");
        SettedWizard.setHealth(10);
        SettedWizard.setStrength(4);
        SettedWizard.setMana(10);
        assertEquals("Big Hat Logan", SettedWizard.getName());
        assertEquals(10, SettedWizard.getHealth());
        assertEquals(4, SettedWizard.getStrength());
        assertEquals(10, SettedWizard.getMana());

        Character SettedWarrior = new Warrior();
        SettedWarrior.setName("Artorias");
        SettedWarrior.setHealth(20);
        SettedWarrior.setStrength(10);
        SettedWarrior.setMana(10);
        assertEquals("Artorias", SettedWarrior.getName());
        assertEquals(20, SettedWarrior.getHealth());
        assertEquals(10, SettedWarrior.getStrength());
        assertEquals(10, SettedWarrior.getMana());
    }

    @Test
    void testExceptions() {

        //Personaje con demasiada vida, fuerza o maná
        assertThrows(IllegalArgumentException.class, () -> new Warrior("Saitama", 100, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> new Warrior("Saitama", 20, 100, 10));
        assertThrows(IllegalArgumentException.class, () -> new Warrior("Saitama", 20, 10, 100));

        //Personaje con demasiada poca vida, fuerza o maná
        assertThrows(IllegalArgumentException.class, () -> new Warrior("Zombie", 0, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Warrior("Zombie", 1, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Warrior("Zombie", 1, 0, -1));

        //Intentar equipar más de 5 AttackItem o DefensiveItem a un personaje
        Sword StarterWeapon = new Sword("Broken Straight Sword", 1, 1, 2, 1, 0, 1);
        Armor StarterArmor = new Armor("Hollow Soldier Armor", 1, 1);
        Character Mule = new Warrior("MulaDeCarga", 20, 1, 1);
        Mule.equipAttackItem(StarterWeapon);
        Mule.equipAttackItem(StarterWeapon);
        Mule.equipAttackItem(StarterWeapon);
        Mule.equipAttackItem(StarterWeapon);
        Mule.equipAttackItem(StarterWeapon);
        assertThrows(IllegalCallerException.class, () -> Mule.equipAttackItem(StarterWeapon));

        Mule.equipDefensiveItem(StarterArmor);
        Mule.equipDefensiveItem(StarterArmor);
        Mule.equipDefensiveItem(StarterArmor);
        Mule.equipDefensiveItem(StarterArmor);
        Mule.equipDefensiveItem(StarterArmor);
        assertThrows(IllegalCallerException.class, () -> Mule.equipDefensiveItem(StarterArmor));

        //Intentar atacar sin armas y sin armas usables
        Character WeakGuy = new Warrior("Bob", 1, 0, 0);
        Game GameExceptions = new Game(Mule, WeakGuy);
        assertThrows(IllegalCallerException.class, () -> GameExceptions.singleAttack(WeakGuy, Mule));
        WeakGuy.equipAttackItem(StarterWeapon);
        assertThrows(IllegalCallerException.class, () -> GameExceptions.singleAttack(WeakGuy, Mule));
    }

    @Test
    void testGame() {
        assertEquals(game.singleAttack(wizard, warrior), 3); //15 daño - 12 protección || Se rompe fragileFireball
        assertEquals(17, warrior.getHealth());
        assertEquals(game.singleAttack(wizard, warrior), 2); //14 daño - 12 protección || Se rompe firstUseWand
        assertEquals(15, warrior.getHealth());
        assertEquals(game.singleAttack(wizard, warrior), 0); //12 daño - 12 protección || Se rompe normalWand
        assertEquals(15, warrior.getHealth());

        //Se acaban los turnos
        assertNull(game.turnAttack(2));

        //Curamos a los personajes para el siguiente test
        warrior.setHealth(20);
        wizard.setHealth(20);

        //Gana warrior
        assertEquals(warrior, game.turnAttack(5));

        //Curamos a los personajes para el siguiente test
        warrior.setHealth(20);
        wizard.setHealth(20);

        //Gana wizard
        assertEquals(wizard, game.turnAttack(20));
    }
}























