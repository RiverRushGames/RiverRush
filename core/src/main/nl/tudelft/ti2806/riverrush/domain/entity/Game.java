package nl.tudelft.ti2806.riverrush.domain.entity;

import com.google.inject.Singleton;
import nl.tudelft.ti2806.riverrush.GameConfiguration;
import nl.tudelft.ti2806.riverrush.domain.entity.state.GameState;
import nl.tudelft.ti2806.riverrush.domain.entity.state.WaitingGameState;

import java.util.ArrayList;

/**
 * Represents an ongoing or waiting game.
 */
@Singleton
public class Game {

    /**
     * The current state of the game.
     */
    private GameState gameState;

    /**
     * The teams participating in the game.
     */
    private final ArrayList<Team> teams;

    /**
     * Construct an application.
     */
    public Game() {
        this.gameState = new WaitingGameState();

        this.teams = new ArrayList<>(GameConfiguration.TEAM_AMOUNT);
        for (int i = 0; i < GameConfiguration.TEAM_AMOUNT; i++) {
            this.teams.add(new Team());
        }
    }

}
