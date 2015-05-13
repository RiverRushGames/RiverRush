package nl.tudelft.ti2806.riverrush.network.event;

import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;

import java.util.Map;

/**
 * Indicates that a client wants to join the game.
 */
public class JoinEvent implements NetworkEvent {

    @Override
    public String serialize(final Protocol protocol) {
        return "";
    }

    /**
     * A join request has no parameters. Thus, {@code keyValuePairs} is ignored.
     *
     * @param keyValuePairs
     *            - Ignored.
     * @return Just {@code this}.
     */
    @Override
    public NetworkEvent deserialize(final Map<String, String> keyValuePairs) {
        return this;
    }
}
