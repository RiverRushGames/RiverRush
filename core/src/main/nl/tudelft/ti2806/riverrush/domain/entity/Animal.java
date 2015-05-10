package nl.tudelft.ti2806.riverrush.domain.entity;

/**
 * All common actions that an animal can perform.
 */
public interface Animal {
    /**
     * Called when the player jumps.
     */
    void jump();

    /**
     * Called when the animal disconnects.
     */
    void leave();
}
