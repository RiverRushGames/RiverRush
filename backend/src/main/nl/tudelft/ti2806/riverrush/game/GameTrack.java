package nl.tudelft.ti2806.riverrush.game;

import nl.tudelft.ti2806.riverrush.domain.entity.AbstractAnimal;
import nl.tudelft.ti2806.riverrush.domain.entity.Animal;
import nl.tudelft.ti2806.riverrush.domain.entity.Team;
import nl.tudelft.ti2806.riverrush.domain.event.AddObstacleEvent;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.domain.event.GameFinishedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.TeamProgressEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Representation of a game track.
 */
public class GameTrack {

    private static final long UPDATE_DELAY = 1000;
    public static final Integer trackLength = 100;
    public Integer DISTANCE_INTERVAL = 10;

    private HashMap<Integer, Team> teams;
    private HashMap<Integer, Double> distances;
    protected final HashMap<Integer, Double> levelMap;

    private EventDispatcher dispatcher;

    /**
     * Create a gametrack.
     *
     * @param level    - String which will determine when to add an cannonball
     * @param dispatch - See {@link EventDispatcher}
     */
    public GameTrack(final String level, final EventDispatcher dispatch) {
        this.dispatcher = dispatch;
        this.teams = new HashMap<>();
        this.distances = new HashMap<>();
        this.levelMap = new HashMap<>();
        parseLevel(level);
    }

    /**
     * This will parse the level for you.
     *
     * @param level - String that represents when the cannonballs need to start flying
     */
    public void parseLevel(final String level) {
        for (int i = 0; i < level.length(); i++) {
            char c = level.charAt(i);
            if (c == '#') {
                this.levelMap.put((i - this.levelMap.size()) * this.DISTANCE_INTERVAL, 0.5);
            }
        }
    }

    /**
     * Start updating the gametrack.
     */
    public void startTimer() {
        Timer tmr = new Timer();
        tmr.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GameTrack.this.updateProgress();
            }
        }, UPDATE_DELAY, UPDATE_DELAY);
    }

    /**
     * This method will update the progress of the gametrack.
     */
    protected void updateProgress() {
        ArrayList<Team> finishedTeams = new ArrayList<>();
        for (Team team : this.teams.values()) {
            Double speed = this.getSpeed(team);
            if (speed == Double.NaN) {
                speed = 0.0;
            }
            Double currentDistance = this.distances.get(team.getId());
            this.distances.put(team.getId(), currentDistance + speed);

            if (currentDistance + speed > this.trackLength) {
                finishedTeams.add(team);
            }

            updateCannonballObstacles(team, currentDistance);

            TeamProgressEvent event = new TeamProgressEvent();
            event.setProgress(currentDistance + speed);
            event.setTeamID(team.getId());
            this.dispatcher.dispatch(event);
        }

        Team winner = this.determineWinningTeam(finishedTeams);
        if (winner != null) {
            GameFinishedEvent event = new GameFinishedEvent();
            event.setWonTeam(winner.getId());
            this.dispatcher.dispatch(event);
        }
    }

    /**
     * This will check if it is time for the team to
     * get a cannonball to their faces.
     *
     * @param team            - The team
     * @param currentDistance - The distance this team has travelled
     */
    protected void updateCannonballObstacles(final Team team, final Double currentDistance) {
        if (this.levelMap.get(currentDistance.intValue()) != null) {
            AddObstacleEvent addEvent = new AddObstacleEvent();
            addEvent.setTeam(team.getId());
            addEvent.setLocation(0.5);
            this.dispatcher.dispatch(addEvent);
        }
    }

    /**
     * Checks which team won.
     *
     * @param finishedTeams - allTeam that won
     * @return the winning team
     */
    protected Team determineWinningTeam(final ArrayList<Team> finishedTeams) {
        Team finished = null;
        Double maxDistance = 0.0;

        for (final Team team : finishedTeams) {
            if (finished == null) {
                finished = team;
                maxDistance = this.distances.get(team.getId());
            } else {
                double newD = this.distances.get(team.getId());
                if (newD > maxDistance) {
                    maxDistance = newD;
                    finished = team;
                }
            }
        }

        return finished;
    }

    /**
     * @param t - the team you want the speed of
     * @return the speed of your team between 0 and 1
     */
    protected double getSpeed(final Team t) {
        int amountOnBoat = 0;
        int total = 0;
        for (AbstractAnimal anim : t.getAnimals().values()) {
            Animal animal = (Animal) anim;

            if (animal.isOnBoat()) {
                amountOnBoat++;
            }
            total++;
        }

        return (double) amountOnBoat / (double) total;
    }

    /**
     * Adds a team to the gametrack.
     *
     * @param team - the team you want to add
     */
    public void addTeam(final Team team) {
        this.teams.put(team.getId(), team);
        this.distances.put(team.getId(), 0.0);
    }

    /**
     * Adds an animal to a team.
     *
     * @param teamID - The id of the team
     * @param animal - The animal to add
     * @throws NoSuchTeamException - if team is not found
     */
    public void addAnimal(final Integer teamID, final AbstractAnimal animal)
        throws NoSuchTeamException {
        if (!this.teams.containsKey(teamID)) {
            throw new NoSuchTeamException();
        }

        this.getTeam(teamID).addAnimal(animal);
        animal.setTeamId(teamID);
    }

    /**
     * @param team - The team.
     * @return the distance the team has traveled
     */
    public Double getDistanceTeam(final Integer team) {
        return this.distances.get(team);
    }

    /**
     * @param team - Integer that represents a team
     * @return the team
     */
    public Team getTeam(final Integer team) {
        return this.teams.get(team);
    }
}
