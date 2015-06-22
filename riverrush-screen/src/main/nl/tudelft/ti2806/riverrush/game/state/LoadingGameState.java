package nl.tudelft.ti2806.riverrush.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import nl.tudelft.ti2806.riverrush.domain.event.Event;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.game.Game;
import nl.tudelft.ti2806.riverrush.screen.LoadingScreen;

/**
 * When the game is loading assets, no operation should change the game state.
 */
public class LoadingGameState extends AbstractGameState {

    private Screen screen;

    /**
     * The state of the game that indicates that the game is busy loading assets. The purpose of
     * this state is to give the game a window where it can load assets to ensure that all needed
     * assets are loaded properly before use.
     *
     * @param eventDispatcher the dispatcher that is used to handle any relevant events for the game in this
     *                        state.
     * @param gm              refers to the game that this state belongs to.
     */
    public LoadingGameState(final EventDispatcher eventDispatcher,
                            final Game gm) {
        super(eventDispatcher, gm);

        this.screen = new LoadingScreen(eventDispatcher);
        Gdx.app.postRunnable(() -> {
            this.game.setScreen(this.screen);
        });
    }

    @Override
    public void dispose() {
        // Does not need to dispose
    }

    @Override
    public GameState start() {
        return this;
    }

    @Override
    public GameState stop() {
        return this;
    }

    @Override
    public GameState finish(final Integer team) {
        return this;
    }

    @Override
    public GameState waitForPlayers() {
        Gdx.app.postRunnable(this.screen::dispose);
        return new WaitingGameState(this.dispatcher, this.game);
    }

    @Override
    public Event getStateEvent() {
        return null;
    }
}
