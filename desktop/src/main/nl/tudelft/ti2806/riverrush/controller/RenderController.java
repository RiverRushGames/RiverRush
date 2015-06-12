package nl.tudelft.ti2806.riverrush.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalCollidedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AssetsLoadedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.BoatCollidedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.Event;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.domain.event.GameFinishedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.GameStartedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.HandlerLambda;
import nl.tudelft.ti2806.riverrush.game.Game;
import nl.tudelft.ti2806.riverrush.network.Client;

/**
 * The render controller controls handles the game started and assets loaded events through
 * dispatching and attaching.
 */
@Singleton
public class RenderController implements Controller {

    private final EventDispatcher dispatcher;
    private final HandlerLambda<GameStartedEvent> onGameStartedLambda;
    private final HandlerLambda<GameFinishedEvent> onGameFinishedLambda;
    private final HandlerLambda<AssetsLoadedEvent> onAssetsLoadedLambda;
    private final HandlerLambda<Event> sendOverNetworkLambda;
    private final Game game;
    private Client client;

    /**
     * Creates a render controller using the given game and event dispatcher.
     *
     * @param gm              refers to the game that is to be controlled.
     * @param eventDispatcher refers to the dispatcher that sends and receives the relevant events.
     */
    @Inject
    public RenderController(final Game gm, final EventDispatcher eventDispatcher) {
        this.dispatcher = eventDispatcher;
        this.game = gm;
        this.onGameStartedLambda = (e) -> this.onGameStarted();
        this.onGameFinishedLambda = (e) -> this.onGameEnded(e);
        this.onAssetsLoadedLambda = (e) -> this.onAssetsLoaded();
        this.sendOverNetworkLambda = (e) -> this.client.sendEvent(e);
        this.dispatcher.attach(GameStartedEvent.class, this.onGameStartedLambda);
        this.dispatcher.attach(GameFinishedEvent.class, this.onGameFinishedLambda);
        this.dispatcher.attach(AssetsLoadedEvent.class, this.onAssetsLoadedLambda);
        this.dispatcher.attach(AnimalCollidedEvent.class, this.sendOverNetworkLambda);
        //this.dispatcher.attach(BoatCollidedEvent.class, this.sendOverNetworkLambda);
    }

    @Override
    public void initialize() {
        // Does not need to initialise
    }

    /**
     * Handles the assets Loaded event through sending a command to the game.
     */
    private void onAssetsLoaded() {
        this.game.waitForPlayers();
    }

    /**
     * Handles the game started event through sending a command to the game.
     */
    private void onGameStarted() {
        this.game.start();
    }

    /**
     * Handles the game finished event through sending a command to the game.
     */
    private void onGameEnded(GameFinishedEvent event) {
        this.game.finish(event.getTeam());
    }

    @Override
    public void onSocketMessage(final Event givenEvent) {
        this.dispatcher.dispatch(givenEvent);
    }

    @Override
    public void dispose() {
        this.dispatcher.detach(GameStartedEvent.class, this.onGameStartedLambda);
        this.dispatcher.detach(GameFinishedEvent.class, this.onGameFinishedLambda);
        this.dispatcher.detach(AssetsLoadedEvent.class, this.onAssetsLoadedLambda);
        this.dispatcher.detach(AnimalCollidedEvent.class, this.sendOverNetworkLambda);
        this.dispatcher.detach(BoatCollidedEvent.class, this.sendOverNetworkLambda);
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
