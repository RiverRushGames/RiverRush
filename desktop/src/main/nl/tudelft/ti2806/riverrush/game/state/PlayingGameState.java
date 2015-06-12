package nl.tudelft.ti2806.riverrush.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import nl.tudelft.ti2806.riverrush.desktop.MainDesktop;
import nl.tudelft.ti2806.riverrush.domain.entity.AbstractAnimal;
import nl.tudelft.ti2806.riverrush.domain.event.AddObstacleEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AddRockEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalAddedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalCollidedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalDroppedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalFellOffEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalJumpedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalMovedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalReturnedToBoatEvent;
import nl.tudelft.ti2806.riverrush.domain.event.BoatCollidedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.Direction;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.domain.event.HandlerLambda;
import nl.tudelft.ti2806.riverrush.domain.event.TeamProgressEvent;
import nl.tudelft.ti2806.riverrush.game.Game;
import nl.tudelft.ti2806.riverrush.game.TickHandler;
import nl.tudelft.ti2806.riverrush.graphics.entity.Animal;
import nl.tudelft.ti2806.riverrush.graphics.entity.AnimalActor;
import nl.tudelft.ti2806.riverrush.graphics.entity.BoatGroup;
import nl.tudelft.ti2806.riverrush.graphics.entity.CannonBallGraphic;
import nl.tudelft.ti2806.riverrush.graphics.entity.RockGraphic;
import nl.tudelft.ti2806.riverrush.graphics.entity.Team;
import nl.tudelft.ti2806.riverrush.screen.PlayingGameScreen;

import java.util.ArrayList;

/**
 * State for a game that is playing.
 */
public class PlayingGameState extends AbstractGameState {

    private final PlayingGameScreen screen;
    private final HandlerLambda<AnimalJumpedEvent> playerJumpedEventHandlerLambda = this::jumpHandler;
    private final HandlerLambda<AnimalDroppedEvent> playerDroppedEventHandlerLambda = this::dropHandler;
    private final HandlerLambda<AddObstacleEvent> addObstacleEventHandlerLambda = this::addObstacle;
    private final HandlerLambda<AddRockEvent> addRockEventHandlerLambda = this::addRock;
    private final HandlerLambda<TeamProgressEvent> TeamProgressEventHandler = this::teamProgress;
    private final HandlerLambda<AnimalFellOffEvent> animalFellOffEventHandlerLambda = this::fellOff;
    private final HandlerLambda<AnimalReturnedToBoatEvent> animalReturnedToBoatEventHandlerLambda = this::returnToBoat;

    private final HandlerLambda<AnimalAddedEvent> addAnimalHandlerLambda = this::addAnimalHandler;
    private final HandlerLambda<AnimalMovedEvent> animalMovedHandlerLambda = this::animalMoveHandler;

    private final TickHandler onTick = this::tick;

    private final ArrayList<RockGraphic> leftRockList;
    private final ArrayList<RockGraphic> rightRockList;
    private final ArrayList<CannonBallGraphic> leftObstList;
    private final ArrayList<CannonBallGraphic> rightObstList;


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

        this.screen = new PlayingGameScreen(assetManager, eventDispatcher);

        Gdx.app.postRunnable(() -> {
            PlayingGameState.this.screen.init(this.onTick);
            PlayingGameState.this.game.setScreen(PlayingGameState.this.screen);

            for (Team currentTeam : PlayingGameState.this.game.getTeams().values()) {
                PlayingGameState.this.addBoat(currentTeam);
                for (AbstractAnimal currentAnimal : currentTeam.getAnimals().values()) {
                    PlayingGameState.this.addAnimal(currentTeam, (Animal) currentAnimal);
                }
            }
        });

        this.dispatcher.attach(AnimalJumpedEvent.class, this.playerJumpedEventHandlerLambda);
        this.dispatcher.attach(AddObstacleEvent.class, this.addObstacleEventHandlerLambda);
        this.dispatcher.attach(AddRockEvent.class, this.addRockEventHandlerLambda);
        this.dispatcher.attach(AnimalAddedEvent.class, this.addAnimalHandlerLambda);
        this.dispatcher.attach(TeamProgressEvent.class, this.TeamProgressEventHandler);
        this.dispatcher.attach(AnimalMovedEvent.class, this.animalMovedHandlerLambda);
        this.dispatcher.attach(AnimalDroppedEvent.class, this.playerDroppedEventHandlerLambda);
        this.dispatcher.attach(AnimalFellOffEvent.class, this.animalFellOffEventHandlerLambda);
        this.dispatcher.attach(AnimalReturnedToBoatEvent.class,
                this.animalReturnedToBoatEventHandlerLambda);

        this.leftObstList = new ArrayList<>();
        this.rightObstList = new ArrayList<>();
        this.rightRockList = new ArrayList<>();
        this.leftRockList = new ArrayList<>();
    }

    @Override
    public void dispose() {
        this.dispatcher.detach(AnimalJumpedEvent.class, this.playerJumpedEventHandlerLambda);
        this.dispatcher.detach(AddObstacleEvent.class, this.addObstacleEventHandlerLambda);
        this.dispatcher.detach(AnimalAddedEvent.class, this.addAnimalHandlerLambda);
        this.dispatcher.detach(TeamProgressEvent.class, this.TeamProgressEventHandler);
        this.dispatcher.detach(AnimalMovedEvent.class, this.animalMovedHandlerLambda);
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
    public GameState finish(Integer team) {
        this.screen.dispose();
        return new FinishedGameState(this.dispatcher, this.assets, this.game, team);
    }

    @Override
    public GameState waitForPlayers() {
        return this;
    }

    /**
     * This method is called when the game renders the screen.
     */
    private void tick() { // TODO: CAUSE HOLY SHIT
        for (RockGraphic graphic : this.leftRockList) {
            Team team = this.game.getTeam(0);
            BoatGroup boat = team.getBoat();
            if (graphic.calculateCollision(boat)) {
                graphic.collideAnimation();
                BoatCollidedEvent event = new BoatCollidedEvent();
                event.setTeam(team.getId());
                event.setDirection(graphic.getDirection());
                this.dispatcher.dispatch(event);
            }
        }
        for (RockGraphic graphic : this.rightRockList) {
            Team team = this.game.getTeam(1);
            BoatGroup boat = team.getBoat();
            if (graphic.calculateCollision(boat)) {
                graphic.collideAnimation();
                BoatCollidedEvent event = new BoatCollidedEvent();
                event.setTeam(team.getId());
                event.setDirection(graphic.getDirection());
                this.dispatcher.dispatch(event);
            }
        }
        for (CannonBallGraphic graphic : this.leftObstList) {
            for (AbstractAnimal animal : this.game.getTeam(0).getAnimals().values()) {
                Animal animal1 = (Animal) animal;
                if (graphic.calculateCollision(animal1.getActor())) {
                    AnimalCollidedEvent event = new AnimalCollidedEvent();
                    event.setAnimal(animal1.getId());
                    event.setTeam(animal1.getTeamId());
                    this.dispatcher.dispatch(event);
                }
            }
        }
        for (CannonBallGraphic graphic : this.rightObstList) {
            for (AbstractAnimal animal : this.game.getTeam(1).getAnimals().values()) {
                Animal animal1 = (Animal) animal;
                if (graphic.calculateCollision(animal1.getActor())) {
                    AnimalCollidedEvent event = new AnimalCollidedEvent();
                    event.setAnimal(animal1.getId());
                    event.setTeam(animal1.getTeamId());
                    this.dispatcher.dispatch(event);
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
        CannonBallGraphic graphic = new CannonBallGraphic(this.assets, e.getLocation());
        // TODO: FIX This
        this.screen.addObstacle(e.getTeam() == 0, graphic);
        if (e.getTeam() == 0) {
            this.leftObstList.add(graphic);
        } else {
            this.rightObstList.add(graphic);
        }
    }

    /**
     * Is called when an obstacle event is received.
     *
     * @param e - the event
     */
    private void addRock(final AddRockEvent e) {
        RockGraphic graphic = new RockGraphic(this.assets, e.getLocation());
        // TODO: FIX This
        this.screen.addRock(e.getTeam() == 0, graphic);
        if (e.getTeam() == 0) {
            this.leftRockList.add(graphic);
        } else {
            this.rightRockList.add(graphic);
        }
    }

    /**
     * Adds an animal to a team.
     *
     * @param team the animal
     * @param animal the team
     */
    private void addAnimal(final Team team, final Animal animal) {
        AnimalActor actor = new AnimalActor(this.assets, this.dispatcher);
        animal.setActor(actor);

        team.addAnimal(animal);
        team.getBoat().addAnimal(actor, animal.getSectorOnBoat());

        actor.init();
    }

    /**
     * Add an animal.
     *
     * @param event The add event
     */
    public void addAnimalHandler(final AnimalAddedEvent event) {
        // Temporary, has to get animal from event

        Animal anim = new Animal(this.dispatcher, event.getAnimal(), event.getTeam(),
                event.getVariation(), event.getSector());
        Team tm = this.game.getTeam(event.getTeam());
        this.addAnimal(tm, anim);
    }

    public void addBoat(Team team) {
        BoatGroup group = new BoatGroup(this.assets, (MainDesktop.getWidth() / 2) - 450,
                MainDesktop.getHeight() * 0.02f);
        team.setBoat(group);
        this.screen.addTeam(group, team.getId());
        group.init();
    }

    /**
     * Tells a given animal to perform the jump action.
     *
     * @param event The jump event
     */

    public void jumpHandler(final AnimalJumpedEvent event) {
        Integer tm = event.getTeam();
        Team tim = this.game.getTeam(tm);
        Integer animalID = event.getAnimal();
        AbstractAnimal anim = tim.getAnimals().get(animalID);
        anim.jump();
    }

    public void animalMoveHandler(final AnimalMovedEvent event) {
        Integer tm = event.getTeam();
        Team tim = this.game.getTeam(tm);
        AbstractAnimal animal = tim.getAnimals().get(event.getAnimal());
        animal.voteOneDirection(event.getDirection());
        if (event.getDirection() == Direction.LEFT) {
            tim.getBoat().voteForDirection(animal, -1);
        } else {
            tim.getBoat().voteForDirection(animal, 1);
        }
    }

    /**
     * Tells a given animal to perform the drop action.
     *
     * @param event The drop event
     */
    public void dropHandler(final AnimalDroppedEvent event) {
        Integer tm = event.getTeam();
        Team tim = this.game.getTeam(tm);
        Integer animalID = event.getAnimal();
        AbstractAnimal anim = tim.getAnimals().get(animalID);
        anim.drop();
    }

    /**
     * Is called when there is a team update on the progress.
     *
     * @param teamProgressEvent - the event
     */
    private void teamProgress(final TeamProgressEvent teamProgressEvent) {
        this.screen.updateProgress(teamProgressEvent.getTeam(), teamProgressEvent.getProgress());
    }

    /**
     * Kicks the animal off the boat.
     *
     * @param event - The event
     */
    private void fellOff(AnimalFellOffEvent event) {
        this.game.getTeam(event.getTeam()).getAnimals().get(event.getAnimal()).fall();
    }

    /**
     * Moves the animal back to the boat.
     *
     * @param event - the event
     */
    private void returnToBoat(final AnimalReturnedToBoatEvent event) {
        Team t = this.game.getTeam(event.getTeam());
        AbstractAnimal a = t.getAnimals().get(event.getAnimal());
        a.returnToBoat();
    }
}
