package nl.tudelft.ti2806.riverrush.domain.entity;

import nl.tudelft.ti2806.riverrush.failfast.FailIf;
import nl.tudelft.ti2806.riverrush.state.AnimalOnBoat;
import nl.tudelft.ti2806.riverrush.state.AnimalState;

/**
 * An abstract implementation of {@link Animal}.
 */
public abstract class AbstractAnimal implements Animal {
    /**
     * The current state of the animal.
     */
    private AnimalState currentState;

    /**
     * Create an animal in default state: {@link AnimalOnBoat}.
     */
    public AbstractAnimal() {
        setState(new AnimalOnBoat());
    }

    @Override
    public void jump() {
        this.currentState = this.currentState.jump();
    }

    @Override
    public void leave() {

    }

    /**
     * Get the current state of the animal.
     * @return {@link AnimalState}, never null.
     */
    protected AnimalState getState() {
        return currentState;
    }

    /**
     * Set a new state of the anima.
     * @param newState - THe new state to set. Fails if null.
     */
    protected void setState(final AnimalState newState) {
        FailIf.isNull(newState);
        this.currentState = newState;
    }
}
