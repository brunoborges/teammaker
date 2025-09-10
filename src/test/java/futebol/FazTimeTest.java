package futebol;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;

public class FazTimeTest {

    @Test
    public void testJogadoresPorTimeConstant() {
        assertEquals(2, FazTime.JOGADORES_POR_TIME);
    }

    @Test
    public void testInicializaJogadores() throws Exception {
        // Use reflection to access the private method
        Method inicializaJogadores = FazTime.class.getDeclaredMethod("inicializaJogadores");
        inicializaJogadores.setAccessible(true);
        inicializaJogadores.invoke(null);

        // Check that jogadores list is populated
        assertNotNull(FazTime.jogadores);
        assertTrue(FazTime.jogadores.size() > 0);
        
        // Check that some expected players are present
        boolean foundBrunoBorges = false;
        boolean foundAlexSouza = false;
        
        for (Jogador j : FazTime.jogadores) {
            if ("Bruno Borges".equals(j.getNome())) {
                foundBrunoBorges = true;
                assertEquals(4.0, j.getForca(), 0.001);
            }
            if ("Alex Souza".equals(j.getNome())) {
                foundAlexSouza = true;
                assertEquals(3.0, j.getForca(), 0.001);
            }
        }
        
        assertTrue("Bruno Borges should be in the list", foundBrunoBorges);
        assertTrue("Alex Souza should be in the list", foundAlexSouza);
    }

    @Test
    public void testPreparaTimes() throws Exception {
        // First initialize jogadores
        Method inicializaJogadores = FazTime.class.getDeclaredMethod("inicializaJogadores");
        inicializaJogadores.setAccessible(true);
        inicializaJogadores.invoke(null);

        // Then prepare times
        Method preparaTimes = FazTime.class.getDeclaredMethod("preparaTimes");
        preparaTimes.setAccessible(true);
        preparaTimes.invoke(null);

        // Check that times list is populated correctly
        assertNotNull(FazTime.times);
        int expectedTimes = FazTime.jogadores.size() / FazTime.JOGADORES_POR_TIME;
        assertEquals(expectedTimes, FazTime.times.size());

        // Check that teams have correct names
        if (FazTime.times.size() > 0) {
            assertEquals("Time A", FazTime.times.get(0).getNome());
        }
        if (FazTime.times.size() > 1) {
            assertEquals("Time B", FazTime.times.get(1).getNome());
        }
    }

    @Test
    public void testCalculaMedia() throws Exception {
        // Initialize jogadores first
        Method inicializaJogadores = FazTime.class.getDeclaredMethod("inicializaJogadores");
        inicializaJogadores.setAccessible(true);
        inicializaJogadores.invoke(null);

        // Get the force average via reflection
        java.lang.reflect.Field forcaMediaField = FazTime.class.getDeclaredField("forcaMedia");
        forcaMediaField.setAccessible(true);
        double forcaMedia = (Double) forcaMediaField.get(null);

        // Calculate expected average
        double totalForce = 0;
        for (Jogador j : FazTime.jogadores) {
            totalForce += j.getForca();
        }
        double expectedAverage = totalForce / FazTime.jogadores.size();

        assertEquals(expectedAverage, forcaMedia, 0.001);
    }

    @Test
    public void testGetJogadorByForce() throws Exception {
        // Setup test data
        FazTime.jogadores.clear();
        FazTime.jogadores.add(new Jogador("Player1", 3.0));
        FazTime.jogadores.add(new Jogador("Player2", 4.0));
        FazTime.jogadores.add(new Jogador("Player3", 3.0));

        // Test the private getJogador method
        Method getJogadorMethod = FazTime.class.getDeclaredMethod("getJogador", int.class);
        getJogadorMethod.setAccessible(true);

        Jogador result = (Jogador) getJogadorMethod.invoke(null, 3);
        assertNotNull(result);
        assertEquals(3.0, result.getForca(), 0.001);
        
        // Should remove the player from the list
        assertEquals(2, FazTime.jogadores.size());
    }

    @Test
    public void testGetJogadorByForceNotFound() throws Exception {
        // Setup test data with multiple players
        FazTime.jogadores.clear();
        FazTime.jogadores.add(new Jogador("Player1", 3.0));
        FazTime.jogadores.add(new Jogador("Player2", 4.0)); // Add second player so it doesn't return first one

        // Test the private getJogador method with non-existent force
        Method getJogadorMethod = FazTime.class.getDeclaredMethod("getJogador", int.class);
        getJogadorMethod.setAccessible(true);

        Jogador result = (Jogador) getJogadorMethod.invoke(null, 5);
        assertNull(result);
        
        // Should not remove any player from the list
        assertEquals(2, FazTime.jogadores.size());
    }

    @Test
    public void testGetJogadorEmptyList() throws Exception {
        // Setup empty list
        FazTime.jogadores.clear();

        // Test the private getJogador method
        Method getJogadorMethod = FazTime.class.getDeclaredMethod("getJogador", int.class);
        getJogadorMethod.setAccessible(true);

        Jogador result = (Jogador) getJogadorMethod.invoke(null, 3);
        assertNull(result);
    }

    @Test
    public void testGetJogadorSingleElement() throws Exception {
        // Setup single element list
        FazTime.jogadores.clear();
        Jogador singlePlayer = new Jogador("Only Player", 2.0);
        FazTime.jogadores.add(singlePlayer);

        // Test the private getJogador method
        Method getJogadorMethod = FazTime.class.getDeclaredMethod("getJogador", int.class);
        getJogadorMethod.setAccessible(true);

        Jogador result = (Jogador) getJogadorMethod.invoke(null, 5);
        assertEquals(singlePlayer, result);
        assertTrue(FazTime.jogadores.isEmpty());
    }

    @Test
    public void testCalculaEquilibrio() throws Exception {
        // Setup times
        FazTime.times.clear();
        Time time1 = new Time("Time A", 2);
        Time time2 = new Time("Time B", 2);
        
        // Add players to create different force levels
        time1.addJogador(new Jogador("P1", 3.0));
        time1.addJogador(new Jogador("P2", 3.0));
        
        time2.addJogador(new Jogador("P3", 4.0));
        time2.addJogador(new Jogador("P4", 4.0));
        
        FazTime.times.add(time1);
        FazTime.times.add(time2);

        // Test equilibrium calculation
        Method calculaEquilibrio = FazTime.class.getDeclaredMethod("calculaEquilibrio");
        calculaEquilibrio.setAccessible(true);
        Boolean result = (Boolean) calculaEquilibrio.invoke(null);

        // Force difference: 6 vs 8, min = 6, max = 8
        // 6 >= 0.7 * 8 = 5.6, so should be true
        assertTrue(result);
    }

    @Test
    public void testCalculaEquilibrioUnbalanced() throws Exception {
        // Setup times with big difference
        FazTime.times.clear();
        Time time1 = new Time("Time A", 2);
        Time time2 = new Time("Time B", 2);
        
        // Create very unbalanced teams
        time1.addJogador(new Jogador("P1", 1.0));
        time1.addJogador(new Jogador("P2", 1.0));
        
        time2.addJogador(new Jogador("P3", 5.0));
        time2.addJogador(new Jogador("P4", 5.0));
        
        FazTime.times.add(time1);
        FazTime.times.add(time2);

        // Test equilibrium calculation
        Method calculaEquilibrio = FazTime.class.getDeclaredMethod("calculaEquilibrio");
        calculaEquilibrio.setAccessible(true);
        Boolean result = (Boolean) calculaEquilibrio.invoke(null);

        // Force difference: 2 vs 10, min = 2, max = 10
        // 2 < 0.7 * 10 = 7, so should be false
        assertFalse(result);
    }
}