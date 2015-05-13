package nl.tudelft.ti2806.riverrush.domain.entity.state;

/**
 * State in which the animal is in mid-air.
 * This means the player can't control the animal while in this state.
 */
public class AnimalJumping implements AnimalState {
    @Override
    public AnimalState jump() {
        return this;
    }
}
