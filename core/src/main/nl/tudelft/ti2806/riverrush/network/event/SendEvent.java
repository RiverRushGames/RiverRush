package nl.tudelft.ti2806.riverrush.network.event;

import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;

import java.util.Map;

/**
 * Created by thomas on 13-5-15.
 */
public class SendEvent implements NetworkEvent {

    private final NetworkEvent toSend;

    public SendEvent(final NetworkEvent event) {
        this.toSend = event;
    }

    @Override
    public String serialize(final Protocol protocol) {
        return toSend.serialize(protocol);
    }

    @Override
    public NetworkEvent deserialize(final Map<String, String> keyValuePairs) {
        return toSend;
    }
}
