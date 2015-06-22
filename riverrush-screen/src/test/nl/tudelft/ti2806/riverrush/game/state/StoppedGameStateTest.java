package nl.tudelft.ti2806.riverrush.game.state;

import static org.junit.Assert.*;

/**
 * Created by Martijn on 22-6-2015.
 */
public class StoppedGameStateTest extends AbstractGameStateTest {

    @Override
    public AbstractGameState getState() {
        return new StoppedGameState(dispatcher, game);
    }
}
