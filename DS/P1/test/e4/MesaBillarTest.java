package e4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MesaBillarTest
{
    MesaBillar mesa = new MesaBillar();

    @Test
    void testIniciarPartida()
    {
        assertEquals(mesa.bolasCajetin().size(), 16);
        assertEquals(mesa.bolasMesa().size(), 0);
        mesa.iniciarPartida();
        assertEquals(mesa.bolasCajetin().size(), 0);
        assertEquals(mesa.bolasMesa().size(), 16);
    }

    @Test
    void testMeterBola()
    {
        mesa.iniciarPartida();
        mesa.meterBola(BolaBillar.BOLA2);
        assertEquals(mesa.bolasCajetin().size(), 1);
        assertEquals(BolaBillar.BOLA2.toString(), "BOLA2");
        mesa.meterBola(BolaBillar.BLANCA);
        assertEquals(mesa.bolasCajetin().size(), 1);
        assertTrue(mesa.isPartidaIniciada());
        assertFalse(mesa.bolasCajetin().contains(BolaBillar.BLANCA));
        mesa.meterBola(BolaBillar.BOLA8);
        assertEquals(mesa.bolasCajetin().size(), 3);
        assertTrue(mesa.bolasCajetin().contains(BolaBillar.BLANCA));
        assertFalse(mesa.isPartidaIniciada());
    }

    @Test
    void testBolasMesaYCajetin()
    {
        assertEquals(mesa.bolasMesa().size(), 0);
        assertEquals(mesa.bolasCajetin().size(), 16);
        mesa.iniciarPartida();
        assertEquals(mesa.bolasMesa().size(), 16);
        assertEquals(mesa.bolasCajetin().size(), 0);
        mesa.meterBola(BolaBillar.BOLA2);
        assertEquals(mesa.bolasMesa().size(), 15);
        assertEquals(mesa.bolasCajetin().size(), 1);
    }

    @Test
    void testIsPartidaIniciada()
    {
        assertFalse(mesa.isPartidaIniciada());
        mesa.iniciarPartida();
        assertTrue(mesa.isPartidaIniciada());
    }

    @Test
    void testObtenerGanador()
    {
        assertEquals(mesa.obtenerGanador(), "EMPATE");
        assertThrows(IllegalArgumentException.class, () -> mesa.meterBola(BolaBillar.BOLA9));
        mesa.iniciarPartida();
        assertEquals(mesa.obtenerGanador(), "EMPATE");
        mesa.meterBola(BolaBillar.BLANCA);
        assertEquals(mesa.obtenerGanador(), "EMPATE");
        mesa.meterBola(BolaBillar.BOLA1);
        assertEquals(mesa.obtenerGanador(), "LISAS");
        mesa.meterBola(BolaBillar.BOLA9);
        assertThrows(IllegalArgumentException.class, () -> mesa.meterBola(BolaBillar.BOLA9));
        assertEquals(mesa.obtenerGanador(), "EMPATE");
        mesa.meterBola(BolaBillar.BOLA10);
        assertEquals(mesa.obtenerGanador(), "RAYADAS");
        assertTrue(mesa.isPartidaIniciada());
        mesa.meterBola(BolaBillar.BOLA8);
        assertFalse(mesa.isPartidaIniciada());
        assertEquals(mesa.obtenerGanador(), "RAYADAS");
        mesa.iniciarPartida();
        assertTrue(mesa.isPartidaIniciada());
        assertEquals(mesa.obtenerGanador(), "EMPATE");
    }
}