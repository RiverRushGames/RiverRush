package nl.tudelft.ti2806.riverrush.game.state;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Clipboard;
import nl.tudelft.ti2806.riverrush.desktop.MainDesktop;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.game.Game;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Martijn on 22-6-2015.
 */
public abstract class AbstractGameStateTest {

    Game game;
    EventDispatcher dispatcher;
    AbstractGameState state;

    @Before
    public void setUp() throws Exception {
        game = mock(Game.class);

        dispatcher = mock(EventDispatcher.class);
        Application app = mock(Application.class);
        doNothing().when(app).postRunnable(Mockito.any());
        Gdx.app = app;

        state = getState();
    }

    @Test
    public void testStart() throws Exception {
        assertEquals(state, state.start());
    }

    @Test
    public void testStop() throws Exception {
        assertEquals(state, state.stop());
    }

    @Test
    public void testFinish() throws Exception {
        assertEquals(state, state.finish(5));
    }

    @Test
    public void testWaitForPlayers() throws Exception {
        assertEquals(state, state.waitForPlayers());
    }

    @Test
    public void testGetStateEvent() throws Exception {
        assertEquals(null, state.getStateEvent());
    }

    public abstract AbstractGameState getState();


}
