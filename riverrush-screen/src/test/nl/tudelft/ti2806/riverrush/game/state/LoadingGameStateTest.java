package nl.tudelft.ti2806.riverrush.game.state;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link LoadingGameState}
 */
public class LoadingGameStateTest extends AbstractGameStateTest {

    @Test
    public void testWaitForPlayers() throws Exception {
        assertEquals(WaitingGameState.class.getName(), state.waitForPlayers().getClass().getName());
    }

    @Override
    public AbstractGameState getState() {
        return new LoadingGameState(dispatcher, game);
    }
}
