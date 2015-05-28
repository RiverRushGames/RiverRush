package nl.tudelft.ti2806.riverrush.game;

import nl.tudelft.ti2806.riverrush.domain.entity.state.GameState;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.domain.event.GameStartedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.PlayerJumpedEvent;
import nl.tudelft.ti2806.riverrush.network.event.JumpEvent;

/**
 * State when the game is ongoing.
 */
public class PlayingGameState implements GameState {

    private final EventDispatcher eventDispatcher;

    /**
     * The game transitions to this state when the game starts.
     *
     * @param dispatcher The dispatcher used to listen to {@link JumpEvent}.
     */
    public PlayingGameState(final EventDispatcher dispatcher) {
        this.eventDispatcher = dispatcher;

        this.eventDispatcher.attach(JumpEvent.class,
            (e) -> this.eventDispatcher.dispatch(new PlayerJumpedEvent()));

        dispatcher.dispatch(new GameStartedEvent());
    }

    @Override
    public void dispose() {
        // Nothing to dispose.
    }

    @Override
    public GameState start() {
        return this;
    }

    @Override
    public GameState stop() {
        this.dispose();
        return new StoppedGameState(this.eventDispatcher);
    }

    @Override
    public GameState finish() {
        this.dispose();
        return new FinishedGameState(this.eventDispatcher);
    }

    @Override
    public GameState waitForPlayers() {
        return this;
    }


}
