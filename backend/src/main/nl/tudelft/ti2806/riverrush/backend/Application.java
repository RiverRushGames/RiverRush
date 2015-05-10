package nl.tudelft.ti2806.riverrush.backend;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.tudelft.ti2806.riverrush.CoreModule;
import nl.tudelft.ti2806.riverrush.network.Server;
import nl.tudelft.ti2806.riverrush.state.GameState;
import nl.tudelft.ti2806.riverrush.state.WaitingGameState;

/**
 * Main backend application responsible for starting and stopping the game.
 */
public class Application extends AbstractModule {

    /**
     * The current state of the game.
     */
    private GameState gameState;

    /**
     * A {@link Server} that fires NetworkEvents for listeners to handle.
     */
    private final Server server;

    /**
     * Construct an application.
     */
    public Application() {
        this.gameState = new WaitingGameState();
        Injector injector = Guice.createInjector(new CoreModule(), this);
        this.server = injector.getInstance(Server.class);
    }

    /**
     * Starts the application.
     */
    public void start() {

    }

    /**
     * Stops the application.
     */
    public void stop() {

    }

    /**
     * Configure dependency injection.
     */
    @Override
    protected void configure() {
        bind(Server.class);
    }
}
