package futebol;

import static org.junit.Assert.*;
import org.junit.Test;

public class JogadorTest {

    @Test
    public void testJogadorCreation() {
        Jogador jogador = new Jogador("Test Player", 3.5);
        assertEquals("Test Player", jogador.getNome());
        assertEquals(3.5, jogador.getForca(), 0.001);
    }

    @Test
    public void testJogadorWithZeroForce() {
        Jogador jogador = new Jogador("Weak Player", 0.0);
        assertEquals("Weak Player", jogador.getNome());
        assertEquals(0.0, jogador.getForca(), 0.001);
    }

    @Test
    public void testJogadorWithHighForce() {
        Jogador jogador = new Jogador("Strong Player", 5.0);
        assertEquals("Strong Player", jogador.getNome());
        assertEquals(5.0, jogador.getForca(), 0.001);
    }

    @Test
    public void testJogadorToString() {
        Jogador jogador = new Jogador("Bruno Borges", 4.0);
        String expected = "Bruno Borges (4.0)";
        assertEquals(expected, jogador.toString());
    }

    @Test
    public void testJogadorToStringWithDecimals() {
        Jogador jogador = new Jogador("Test", 3.25);
        String expected = "Test (3.25)";
        assertEquals(expected, jogador.toString());
    }

    @Test
    public void testJogadorWithEmptyName() {
        Jogador jogador = new Jogador("", 2.0);
        assertEquals("", jogador.getNome());
        assertEquals(2.0, jogador.getForca(), 0.001);
    }

    @Test
    public void testJogadorWithNegativeForce() {
        Jogador jogador = new Jogador("Negative Player", -1.0);
        assertEquals("Negative Player", jogador.getNome());
        assertEquals(-1.0, jogador.getForca(), 0.001);
    }
}