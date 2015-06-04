package nl.tudelft.ti2806.riverrush.game;

import nl.tudelft.ti2806.riverrush.domain.entity.AbstractAnimal;
import nl.tudelft.ti2806.riverrush.domain.entity.Animal;
import nl.tudelft.ti2806.riverrush.domain.entity.Team;
import nl.tudelft.ti2806.riverrush.domain.event.AddObstacleEvent;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.domain.event.GameFinishedEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by m.olsthoorn on 6/3/2015.
 */
public class GameTrackTest {

    GameTrack track = null;
    EventDispatcher dispatcher;
    private Team team;
    private final static double delta = 0.001;

    @Before
    public void setUp() throws Exception {
        dispatcher = Mockito.spy(EventDispatcher.class);
        track = new GameTrack("-#--#--#--#--#-", dispatcher);
        team = new Team();
        track.addTeam(team);
    }

    @Test
     public void testAddTeam() throws Exception {
        assertEquals(track.getTeam(team.getId()), team);
    }

    @Test
    public void testAddTeamLength() throws Exception {
        assertTrue(0.0 == track.getDistanceTeam(team.getId()));
    }

    @Test
    public void testAddAnimal() throws Exception {
        Animal animal = new Animal(dispatcher);
        try {
            track.addAnimal(team.getId(), animal);
            AbstractAnimal gottenAnimal = track.getTeam(team.getId()).getAnimals().get(animal.getId());
            assertEquals(animal, gottenAnimal);
        } catch (NoSuchTeamException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSpeed() throws Exception {
        Animal animal = new Animal(dispatcher);
        team.addAnimal(animal);
        assertEquals(track.getSpeed(team), 1.0, delta);
    }

    @Test
    public void testSpeedWhenFallenOff() throws Exception {
        Animal animal = new Animal(dispatcher);
        team.addAnimal(animal);
        animal.collide();
        assertEquals(track.getSpeed(team), 0.0, delta);
    }

    @Test
    public void testUpdateProgressOneCycle() throws Exception {
        Animal animal = new Animal(dispatcher);
        team.addAnimal(animal);
        track.updateProgress();
        assertEquals(1.0, track.getDistanceTeam(team.getId()), delta);
//        Mockito.verify(dispatcher, Mockito.calls(1)).dispatch(Mockito.any(TeamProgressUpdateEvent.class));
        //FIXME when merging to desktop
    }

    @Test
    public void testUpdateProgressOneHundredCycles() {
        Animal animal = new Animal(dispatcher);
        team.addAnimal(animal);
        for (int i = 0; i <= GameTrack.trackLength; i++) {
            track.updateProgress();
        }

        Mockito.verify(dispatcher, Mockito.times(1)).dispatch(Mockito.isA(GameFinishedEvent.class));

        assertEquals(GameTrack.trackLength + 1, track.getDistanceTeam(team.getId()), delta);
    }

    @Test
    public void testDetermineWinningTeamOneHundredCycles() throws Exception {
        Animal animal = new Animal(dispatcher);
        team.addAnimal(animal);
        for (int i = 0; i < GameTrack.trackLength; i++) {
            track.updateProgress();
        }


        ArrayList<Team> list = new ArrayList<>();
        list.add(team);
        assertEquals(team, track.determineWinningTeam(list));
    }

    @Test
    public void testDetermineWinningTeamTwoTeams() throws Exception {
        Animal animal = new Animal(dispatcher);
        team.addAnimal(animal);

        Team team2 = new Team();
        Animal animal2 = new Animal(dispatcher);
        team2.addAnimal(animal2);
        Animal animal3 = new Animal(dispatcher);
        team2.addAnimal(animal3);

        track.addTeam(team2);

        for (int i = 0; i <= GameTrack.trackLength - 1; i++) {
            track.updateProgress();
        }

        animal2.collide();

        track.updateProgress();



        assertEquals("Distance of team 1 is not equal", GameTrack.trackLength + 1, track.getDistanceTeam(team.getId()), delta);
        assertEquals("Distance of team 2 is not equal", GameTrack.trackLength + 0.5, track.getDistanceTeam(team2.getId()), delta);

        ArrayList<Team> list = new ArrayList<>();
        list.add(team);
        list.add(team2);

        assertEquals(team, track.determineWinningTeam(list));

    }

    @Test
    public void testParseLevel() throws Exception {
        assertEquals(track.levelMap.get(10), 0.5, delta);
    }

    @Test
    public void testDispatchObstacleAt10() throws Exception {
        Animal animal = new Animal(dispatcher);
        team.addAnimal(animal);

        track.updateCannonballObstacles(team, 10.0);

        Mockito.verify(dispatcher, Mockito.times(1)).dispatch(Mockito.isA(AddObstacleEvent.class));
    }


}
