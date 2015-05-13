package nl.tudelft.ti2806.riverrush.domain.entity.state;

/**
 * Represents the state of an animal.
 */
public interface AnimalState {
    /**
     * Animals can jump.
     * @return The new state when an animal jumps.
     */
    AnimalState jump();
}
