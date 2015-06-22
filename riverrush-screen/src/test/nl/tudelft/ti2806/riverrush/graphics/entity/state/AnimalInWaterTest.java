package nl.tudelft.ti2806.riverrush.graphics.entity.state;

import nl.tudelft.ti2806.riverrush.domain.entity.state.AnimalState;
import nl.tudelft.ti2806.riverrush.domain.entity.state.AnimalStateTest;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.graphics.entity.Animal;
import nl.tudelft.ti2806.riverrush.graphics.entity.AnimalActor;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link AnimalInWater}
 */
public class AnimalInWaterTest extends AnimalStateTest{

    private EventDispatcher dispatcher;
    private Animal animal;

    @Override
    public AnimalState getAnimalState() {
        dispatcher = mock(EventDispatcher.class);
        animal = mock(Animal.class);
        when(animal.getActor()).thenReturn(mock(AnimalActor.class));
        return new AnimalInWater(animal, dispatcher);
    }

    @Test
    public void testReturnToBoat() throws Exception {
        assertEquals(AnimalOnBoat.class.getName(), getAnimalState().returnToBoat().getClass().getName());
    }

    @Test
    public void testCollide() throws Exception {
        verifyZeroInteractions(dispatcher);
        getAnimalState().collide();
    }
}
