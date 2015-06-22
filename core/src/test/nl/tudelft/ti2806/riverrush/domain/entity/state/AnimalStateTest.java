package nl.tudelft.ti2806.riverrush.domain.entity.state;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Abstract test class to be extended. Belongs to {@link AnimalState}
 */
public abstract class AnimalStateTest {

    private AnimalState state;

    @Before
    public void setUp() throws Exception {
        state = getAnimalState();
    }

    public abstract AnimalState getAnimalState();

    @Test
    public void testJump() throws Exception {
        assertEquals(state.getClass().getName(), state.jump().getClass().getName());
    }

    @Test
    public void testDrop() throws Exception {
        assertEquals(state.getClass().getName(), state.drop().getClass().getName());
    }

    @Test
    public void testFall() throws Exception {
        assertEquals(state.getClass().getName(), state.fall().getClass().getName());
    }

    @Test
    public void testReturnToBoat() throws Exception {
        assertEquals(state.getClass().getName(), state.returnToBoat().getClass().getName());
    }
}
