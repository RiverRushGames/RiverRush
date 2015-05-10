package nl.tudelft.ti2806.riverrush.network.event;

import nl.tudelft.ti2806.riverrush.domain.event.Event;
import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;

import java.util.Map;

/**
 * All events that go through the network,
 * can be serialized an deserialized from a String format.
 */
public interface NetworkEvent extends Event {
    /**
     * Serialize the current event to it's string representation.
     * @param protocol - The protocol to use.
     * @return - A mesage that can be sent over sockets.
     */
    String serialize(Protocol protocol);

    /**
     * From key-value pairs, initialize this event's fields with the appropriate values.
     * @param keyValuePairs - A map containing a value for each field of this Event.
     * @return The fully initialized event, ready for dispatch.
     */
    NetworkEvent deserialize(Map<String, String> keyValuePairs);
}
