package nl.tudelft.ti2806.riverrush.network;

import com.google.inject.Inject;
import nl.tudelft.ti2806.riverrush.domain.event.Event;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Web socket client for connecting to the backend endpoint.
 */
public class Client extends WebSocketClient {

    private Protocol protocol;

    private EventDispatcher dispatcher;

    /**
     * Constructs a WebSocketClient instance and sets it to the connect to the
     * specified URI. The channel does not attampt to connect automatically. You
     * must call {@code connect} first to initiate the socket connection.
     *
     * @param serverUri - The remote uri of the server.
     * @param draft     - The websocket draft to use.
     */
    public Client(final URI serverUri, final Draft draft) {
        super(serverUri, draft);
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata) {

    }

    /**
     * Send a domain event to the server.
     * @param event - The event to send.
     * @param d - The dispatcher that dispatched this event.
     */
    private void sendEvent(final Event event, final EventDispatcher d) {
        getConnection().send(event.serialize(protocol));
    }

    @Override
    public void onMessage(final String message) {
        Event event = this.protocol.deserialize(message);
        this.dispatcher.dispatch(event);
    }

    @Override
    public void onClose(final int code, final String reason,
                        final boolean remote) {

    }

    @Override
    public void onError(final Exception ex) {

    }

    @Inject
    public void setProtocol(final Protocol p) {
        this.protocol = p;
    }

    @Inject
    public void setDispatcher(final EventDispatcher d) {
        this.dispatcher = d;
    }
}
