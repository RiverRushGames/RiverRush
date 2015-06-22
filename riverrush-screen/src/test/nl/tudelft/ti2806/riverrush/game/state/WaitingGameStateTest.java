package nl.tudelft.ti2806.riverrush.game.state;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link WaitingGameState}
 */
public class WaitingGameStateTest extends AbstractGameStateTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testStart() throws Exception {
        assertEquals(PlayingGameState.class.getName(), state.start().getClass().getName());
    }

    @Test
    public void testStop() throws Exception {
        assertEquals(StoppedGameState.class.getName(), state.stop().getClass().getName());
    }

    @Override
    public AbstractGameState getState() {
        return new WaitingGameState(dispatcher, game);
    }
}
