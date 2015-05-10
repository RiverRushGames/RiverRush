package nl.tudelft.ti2806.riverrush.state;

/**
 * Created by thomas on 7-5-15.
 */
public class AnimalOnBoat implements AnimalState {
    @Override
    public AnimalState jump() {
        return new AnimalJumping();
    }
}
