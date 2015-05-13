package nl.tudelft.ti2806.riverrush.network.event;

import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;

import java.util.Map;

/**
 * Created by thomas on 13-5-15.
 */
public class EchoNetworkEvent implements NetworkEvent {
    @Override
    public String serialize(final Protocol protocol) {
        return "";
    }

    @Override
    public NetworkEvent deserialize(final Map<String, String> keyValuePairs) {
        return this;
    }
}
