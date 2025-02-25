package e2.Game;

import e2.Characters.Character;

public class Game {

    private final Character p1, p2;

    public Game(Character p1, Character p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public int singleAttack(Character attacker, Character defender) {
        int health = defender.getHealth();
        attacker.attackCharacter(defender);
        return health - defender.getHealth();
    }

    public Character turnAttack(int turns) {

        while (this.p1.getHealth() > 0 && this.p2.getHealth() > 0 && turns > 0) {

            //P1 ataca, si la vida de P2 llega a 0 o menos, gana P1
            this.p1.attackCharacter(this.p2);
            if (this.p2.getHealth() <= 0)
                return p1;

            //P2 ataca, si la vida de P1 llega a 0 o menos, gana P2
            this.p2.attackCharacter(this.p1);
            if (this.p1.getHealth() <= 0)
                return p2;

            //Si ningún P muere, se reduen los turnos
            turns--;
        }

        return null;
    }
}
