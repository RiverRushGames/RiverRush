package nl.tudelft.ti2806.riverrush.game.state;

import java.util.ArrayList;

import nl.tudelft.ti2806.riverrush.desktop.MainDesktop;
import nl.tudelft.ti2806.riverrush.domain.entity.AbstractAnimal;
import nl.tudelft.ti2806.riverrush.domain.event.AddObstacleEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalAddedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalCollidedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalJumpedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.domain.event.HandlerLambda;
import nl.tudelft.ti2806.riverrush.domain.event.TeamProgressEvent;
import nl.tudelft.ti2806.riverrush.game.Game;
import nl.tudelft.ti2806.riverrush.game.TickHandler;
import nl.tudelft.ti2806.riverrush.graphics.entity.Animal;
import nl.tudelft.ti2806.riverrush.graphics.entity.BoatGroup;
import nl.tudelft.ti2806.riverrush.graphics.entity.MonkeyActor;
import nl.tudelft.ti2806.riverrush.graphics.entity.ObstacleGraphic;
import nl.tudelft.ti2806.riverrush.graphics.entity.Team;
import nl.tudelft.ti2806.riverrush.screen.PlayingGameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

/**
 * State for a game that is playing.
 */
public class PlayingGameState extends AbstractGameState {

    private final PlayingGameScreen screen;
    private final HandlerLambda<AnimalJumpedEvent> playerJumpedEventHandlerLambda = (e) -> this
            .jumpHandler(e);
    private final HandlerLambda<AddObstacleEvent> addObstacleEventHandlerLambda = this::addObstacle;
    private final HandlerLambda<AnimalAddedEvent> addAnimalHandlerLambda = (e) -> this
            .addAnimalHandler(e);

    private final TickHandler OnTick = this::tick;

    private final ArrayList<ObstacleGraphic> leftObstList;
    private final ArrayList<ObstacleGraphic> rightObstList;
    private final HandlerLambda<? super TeamProgressEvent> TeamProgressEventHandler = this::teamProgress;

    /**
     * The state of the game that indicates that the game is currently playable.
     *
     * @param eventDispatcher the dispatcher that is used to handle any relevant events for the game
     *            in this state.
     * @param assetManager has all necessary assets loaded and available for use.
     * @param game refers to the game that this state belongs to.
     */
    public PlayingGameState(final EventDispatcher eventDispatcher, final AssetManager assetManager,
            final Game game) {
        super(eventDispatcher, assetManager, game);
        this.dispatcher.attach(AnimalJumpedEvent.class, this.playerJumpedEventHandlerLambda);
        this.dispatcher.attach(AddObstacleEvent.class, this.addObstacleEventHandlerLambda);
        this.dispatcher.attach(AnimalAddedEvent.class, this.addAnimalHandlerLambda);
        this.dispatcher.attach(TeamProgressEvent.class, this.TeamProgressEventHandler);

        this.screen = new PlayingGameScreen(assetManager, eventDispatcher);
        Gdx.app.postRunnable(() -> {
            PlayingGameState.this.screen.init(this.OnTick);
            PlayingGameState.this.game.setScreen(PlayingGameState.this.screen);
        });

        this.leftObstList = new ArrayList<>();
        this.rightObstList = new ArrayList<>();

    }

    @Override
    public void dispose() {
        this.dispatcher.detach(AnimalJumpedEvent.class, this.playerJumpedEventHandlerLambda);
        this.dispatcher.detach(AddObstacleEvent.class, this.addObstacleEventHandlerLambda);
        this.dispatcher.detach(AnimalAddedEvent.class, this.addAnimalHandlerLambda);
        this.screen.dispose();
    }

    @Override
    public GameState start() {
        return this;
    }

    @Override
    public GameState stop() {
        this.screen.dispose();
        return new StoppedGameState(this.dispatcher, this.assets, this.game);
    }

    @Override
    public GameState finish() {
        this.screen.dispose();
        return new FinishedGameState(this.dispatcher, this.assets, this.game);
    }

    @Override
    public GameState waitForPlayers() {
        return this;
    }

    /**
     * This method is called when the game renders the screen.
     */
    private void tick() {
        for (ObstacleGraphic graphic : this.leftObstList) {
            for (AbstractAnimal animal : this.game.getTeam(0).getAnimals().values()) { // TODO
                Animal animal1 = (Animal) animal;
                if (graphic.calculateCollision(animal1.getActor())) {
                    AnimalCollidedEvent ev = new AnimalCollidedEvent();
                    ev.setAnimal(animal1.getId());
                    ev.setTeam(animal.getTeamId());
                    this.dispatcher.dispatch(ev);
                }
            }
        }

        for (ObstacleGraphic graphic : this.rightObstList) {
            for (AbstractAnimal animal : this.game.getTeam(1).getAnimals().values()) {
                Animal animal1 = (Animal) animal;
                if (graphic.calculateCollision(animal1.getActor())) {
                    // TODO: Set animal
                    AnimalCollidedEvent ev = new AnimalCollidedEvent();
                    ev.setAnimal(animal1.getId());
                    ev.setTeam(animal.getTeamId());
                    this.dispatcher.dispatch(ev);
                }
            }
        }
    }

    /**
     * Is called when an obstacle event is received.
     *
     * @param e - the event
     */
    private void addObstacle(final AddObstacleEvent e) {
        ObstacleGraphic graphic = new ObstacleGraphic(this.assets, e.getLocation());
        // TODO: FIX This
        this.screen.addObstacle(e.getTeam() == 0, graphic);
        if (e.getTeam() == 0) {
            this.leftObstList.add(graphic);
        } else {
            this.rightObstList.add(graphic);
        }
    }

    public void addAnimalHandler(AnimalAddedEvent event) {
        // Temporary, has to get animal from event

        MonkeyActor actor = new MonkeyActor(this.assets, this.dispatcher);
        Animal anim = new Animal(this.dispatcher, event.getAnimal(), event.getTeam());
        anim.setActor(actor);

        Integer tm = event.getTeam();
        Team tim = this.game.getTeam(tm);
        if (tim == null) {
            BoatGroup group = new BoatGroup(this.assets, (MainDesktop.getWidth() / 2) - 450,
                    MainDesktop.getHeight() * 0.02f);
            tim = this.game.addTeam(tm);
            tim.setBoat(group);
            this.screen.addTeam(group, tm);
            // Determine corresponding team's stage

        }
        tim.addAnimal(anim);
        tim.getBoat().addAnimal(actor);
    }

    /**
     * Tells a given animal to perform the jump action.
     *
     * @param event The jump event
     */
    public void jumpHandler(AnimalJumpedEvent event) {
        Integer tm = event.getTeam();
        Team tim = this.game.getTeam(tm);
        Integer animalID = event.getAnimal();
        AbstractAnimal anim = tim.getAnimals().get(animalID);
        anim.jump();

    }

    /**
     * Is called when there is a team update on the progress.
     * @param teamProgressEvent - the event
     */
    private void teamProgress(final TeamProgressEvent teamProgressEvent) {
        this.screen.updateProgress(teamProgressEvent.getTeamID(), teamProgressEvent.getProgress());
    }
}
