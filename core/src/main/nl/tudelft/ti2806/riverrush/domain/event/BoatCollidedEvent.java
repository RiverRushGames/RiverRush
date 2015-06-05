package nl.tudelft.ti2806.riverrush.domain.event;

import nl.tudelft.ti2806.riverrush.network.protocol.InvalidProtocolException;
import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;

import java.util.Map;

public class BoatCollidedEvent implements Event {

    private Integer animalId;

    private Integer teamId;

    private Direction direction;

    @Override
    public String serialize(final Protocol protocol) {
        return "animal" + protocol.getKeyValueSeperator() + this.animalId.toString()
            + protocol.getPairSeperator() + "team" + protocol.getKeyValueSeperator()
            + this.teamId.toString();
    }

    @Override
    public Event deserialize(final Map<String, String> keyValuePairs) {
        if (keyValuePairs.containsKey("animal") && keyValuePairs.containsKey("team")) {
            this.animalId = Integer.parseInt(keyValuePairs.get("animal"));
            this.teamId = Integer.parseInt(keyValuePairs.get("team"));
        } else {
            throw new InvalidProtocolException("Does not contain all the keys");
        }
        return this;
    }

    @Override
    public Integer getAnimal() {
        return this.animalId;
    }

    @Override
    public void setAnimal(final Integer aAnimal) {
        this.animalId = aAnimal;
    }

    public Integer getTeam() {
        return this.teamId;
    }

    public void setTeam(final Integer team) {
        this.teamId = team;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
    }
}
