package e4;

import java.util.ArrayList;
import java.util.Arrays;

public class MesaBillar
{
    //variable initialization
    private final ArrayList<BolaBillar> ballsOverTable = new ArrayList<>();
    private final ArrayList<BolaBillar> ballsInBox = new ArrayList<>();
    private boolean partidaIniciada = false;

    //constructor puts all the balls inside the box
    public MesaBillar() { ballsInBox.addAll(Arrays.asList(BolaBillar.values())); }

    //auxiliar function to make the code cleaner
    /*it's private because we don't want anyone to use it directly, because of that we can't make a test for it; however,
    it's used in other functions, so it has to work*/
    private void move(ArrayList<BolaBillar> source, ArrayList<BolaBillar> destination, BolaBillar element)
    {
        //if the element is in the source list and not in the destination one it moves
        if (source.remove(element) && !destination.contains(element)) destination.add(element);
    }

    public void iniciarPartida()
    {
        //all the balls are moved from the box to the table and the match has started
        for (BolaBillar bola : BolaBillar.values()) move(this.ballsInBox, this.ballsOverTable, bola); //could have used clear and addAll too
        this.partidaIniciada = true;
    }

    public void meterBola(BolaBillar bola)
    {
        //if the match has not started, the ball is white or the ball is not over the table it has no effect
        if (!this.partidaIniciada || !this.ballsOverTable.contains(bola)) throw new IllegalArgumentException();

        if (bola.equals(BolaBillar.BLANCA)) return;

        //if the ball is black, the match ends
        if (bola.equals(BolaBillar.BOLA8))
        {
            /*we move the white and black balls inside the box to make it easier to calculate who is winning, making it
            that whenever the match is over both balls will be inside the box and else not*/
            move(this.ballsOverTable, this.ballsInBox, BolaBillar.BLANCA);
            move(this.ballsOverTable, this.ballsInBox, BolaBillar.BOLA8);
            this.partidaIniciada = false;
        }
        //else the ball is removed from the table and put inside the box
        else move(this.ballsOverTable, this.ballsInBox, bola);
    }

    public ArrayList<BolaBillar> bolasMesa() { return this.ballsOverTable; }

    public ArrayList<BolaBillar> bolasCajetin() { return this.ballsInBox; }

    public boolean isPartidaIniciada() { return partidaIniciada; }

    public String obtenerGanador()
    {
        int i, n_rayadas = this.partidaIniciada? 0:2, n_lisas = 0;
        /*we put 0:2 because whenever the match is not ongoing, the black and white balls are be inside the box, as we
        put them inside it whenever the match ends*/

        for (i = 0; i < this.ballsInBox.size(); i++)
        {
            //we ignore the white and black balls in case the match has *never* started (all balls are inside the box)
            if (this.ballsInBox.get(i).getColor().equals("WHITE") || this.ballsInBox.get(i).getColor().equals("BLACK")) continue;

            if (this.ballsInBox.get(i).getType().equals("RAYADA")) n_rayadas++;
            else n_lisas++;
        }

        return n_rayadas==n_lisas? "EMPATE":(n_rayadas > n_lisas? "RAYADAS":"LISAS");
    }
}
