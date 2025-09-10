package futebol;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TimeTest {

    private Time time;
    private Jogador jogador1;
    private Jogador jogador2;
    private Jogador jogador3;

    @Before
    public void setUp() {
        time = new Time("Time A", 2);
        jogador1 = new Jogador("Player 1", 3.0);
        jogador2 = new Jogador("Player 2", 4.0);
        jogador3 = new Jogador("Player 3", 2.0);
    }

    @Test
    public void testTimeCreation() {
        assertEquals("Time A", time.getNome());
        assertEquals(0, time.getForca());
        assertFalse(time.isCompleto());
    }

    @Test
    public void testAddJogador() {
        time.addJogador(jogador1);
        assertEquals(3, time.getForca());
        assertFalse(time.isCompleto());
    }

    @Test
    public void testTimeCompletion() {
        time.addJogador(jogador1);
        time.addJogador(jogador2);
        assertEquals(7, time.getForca());
        assertTrue(time.isCompleto());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddJogadorToCompleteTime() {
        time.addJogador(jogador1);
        time.addJogador(jogador2);
        // This should throw an exception
        time.addJogador(jogador3);
    }

    @Test
    public void testTimeReset() {
        time.addJogador(jogador1);
        time.addJogador(jogador2);
        assertTrue(time.isCompleto());
        assertEquals(7, time.getForca());

        time.reset();
        assertFalse(time.isCompleto());
        assertEquals(0, time.getForca());
    }

    @Test
    public void testTimeToString() {
        time.addJogador(jogador1);
        time.addJogador(jogador2);
        
        String result = time.toString();
        assertTrue(result.contains("Time A"));
        assertTrue(result.contains("forca = 7"));
        assertTrue(result.contains("Player 1"));
        assertTrue(result.contains("Player 2"));
        assertTrue(result.contains("(3.0)"));
        assertTrue(result.contains("(4.0)"));
    }

    @Test
    public void testTimeWithDifferentLimit() {
        Time bigTime = new Time("Big Team", 5);
        assertFalse(bigTime.isCompleto());
        
        for (int i = 0; i < 5; i++) {
            bigTime.addJogador(new Jogador("Player " + i, 1.0));
        }
        assertTrue(bigTime.isCompleto());
        assertEquals(5, bigTime.getForca());
    }

    @Test
    public void testTimeWithZeroForcePlayer() {
        Jogador weakPlayer = new Jogador("Weak", 0.0);
        time.addJogador(weakPlayer);
        assertEquals(0, time.getForca());
        assertFalse(time.isCompleto());
    }

    @Test
    public void testTimeForceAccumulation() {
        time.addJogador(new Jogador("P1", 2.5));
        assertEquals(2, time.getForca()); // Force is stored as int, 2.5 becomes 2
        
        time.addJogador(new Jogador("P2", 1.7));
        assertEquals(3, time.getForca()); // 2 + 1 = 3 (each double cast to int before addition)
    }
}