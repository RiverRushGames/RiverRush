package nl.tudelft.ti2806.riverrush.domain.event;

import nl.tudelft.ti2806.riverrush.domain.entity.AbstractAnimal;
import nl.tudelft.ti2806.riverrush.domain.entity.AbstractTeam;
import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;

import java.util.Map;

/**
 * This is the event that is sent from the server to the device and the renderer to say that the animal
 * has fallen off the boat.
 */
public class AnimalFellOffEvent implements Event {

    private Integer animalId;

    private Integer teamId;

    @Override
    public String serialize(final Protocol protocol) {
        return "[Serialized string van een FallOffEvent]";
    }

    @Override
    public Event deserialize(final Map<String, String> keyValuePairs) {
        return this;
    }

    @Override
    public void setAnimal(final AbstractAnimal aAnimal) {
        this.animalId = aAnimal.getId();
    }

    @Override
    public Integer getAnimal() {
        return this.animalId;
    }

    public Integer getTeam() {
        return this.teamId;
    }

    public void setTeam(AbstractTeam team) {
        this.teamId = team.getId();
    }
}
