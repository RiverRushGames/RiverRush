package nl.tudelft.ti2806.riverrush.game;

import nl.tudelft.ti2806.riverrush.domain.entity.Team;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;

/**
 * A basic implementation of a game track.
 */
public class BasicGameTrack extends GameTrack {

    /**
     * Create a basic level.
     * @param dispatcher - See {@link EventDispatcher}
     */
    public BasicGameTrack(final EventDispatcher dispatcher) {
        super("-#--#--#--#--#-", dispatcher);
        this.addTeam(new Team());
        this.addTeam(new Team());

        // this.parseLevel(-);
    }
}
