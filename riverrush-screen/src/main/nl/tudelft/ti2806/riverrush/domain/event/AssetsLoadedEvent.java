package nl.tudelft.ti2806.riverrush.domain.event;

import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;

import java.util.Map;

/**
 * This event is called from the renderer when it has loaded all the assets. This is neccesary,
 * because we don't want the game to begin, when the renderer has not finished loading.
 */
public class AssetsLoadedEvent extends AbstractAnimalEvent {

    @Override
    public String serialize(final Protocol protocol) {
        return "";
    }

    @Override
    public Event deserialize(final Map<String, String> keyValuePairs) {
        return this;
    }

    @Override
    public void setAnimal(final Integer anAnimalID) {
        // Has to be empty
    }

    @Override
    public Integer getAnimal() {
        // Has to be empty
        return -1;
    }
}
