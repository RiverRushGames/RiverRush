package nl.tudelft.ti2806.riverrush.graphics.entity.state;

import nl.tudelft.ti2806.riverrush.domain.entity.state.AnimalState;
import nl.tudelft.ti2806.riverrush.domain.entity.state.AnimalStateTest;
import nl.tudelft.ti2806.riverrush.domain.event.AnimalCollidedEvent;
import nl.tudelft.ti2806.riverrush.domain.event.Direction;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.graphics.entity.Animal;
import nl.tudelft.ti2806.riverrush.graphics.entity.AnimalActor;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link AnimalOnBoat}
 */
public class AnimalOnBoatTest extends AnimalStateTest {

    private EventDispatcher dispatcher;
    private Animal animal;

    @Override
    public AnimalState getAnimalState() {
        dispatcher = mock(EventDispatcher.class);
        animal = mock(Animal.class);
        when(animal.getActor()).thenReturn(mock(AnimalActor.class));
        return new AnimalOnBoat(animal, dispatcher);
    }

    @Test
    public void testJump() throws Exception {
        assertEquals(AnimalInAir.class.getName(), getAnimalState().jump().getClass().getName());
    }

    @Test
    public void testFall() throws Exception {
        assertEquals(AnimalInWater.class.getName(), getAnimalState().fall().getClass().getName());
    }

    @Test
    public void testCollide() throws Exception {
        getAnimalState().collide();
        Mockito.verify(dispatcher).dispatch(Mockito.isA(AnimalCollidedEvent.class));
    }

    @Test
    public void testVoteDirection() throws Exception {
        getAnimalState().voteDirection(Direction.LEFT);
        Mockito.verify(animal).setVoteDirection(Direction.LEFT);
    }
}
