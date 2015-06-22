package nl.tudelft.ti2806.riverrush.game.state;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nl.tudelft.ti2806.riverrush.desktop.MainDesktop;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Test for {@link FinishedGameState}
 */
public class FinishedGameStateTest extends AbstractGameStateTest {

    @Test
    public void testStart() throws Exception {
        assertEquals(WaitingGameState.class.getName(), state.start().getClass().getName());
    }

    @Test
    public void testStop() throws Exception {
        assertEquals(StoppedGameState.class.getName(), state.stop().getClass().getName());
    }

    @Test
    public void testWaitForPlayers() throws Exception {
        assertEquals(WaitingGameState.class.getName(), state.waitForPlayers().getClass().getName());
    }

    @Override
    public AbstractGameState getState() {
        return new FinishedGameState(dispatcher, game, 1);
    }
}
