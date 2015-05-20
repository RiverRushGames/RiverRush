package nl.tudelft.ti2806.riverrush.domain.entity.state;

/**
 * Represents the current state of the game.
 */
public interface GameState {
    void dispose();

    GameState start();

    GameState stop();

    GameState finish();

    GameState waitForPlayers();
}
