package nl.tudelft.ti2806.riverrush.network;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Web socket client for connecting to the backend endpoint.
 */
public class Client extends WebSocketClient {
    /**
     * Constructs a WebSocketClient instance and sets it to the connect to the specified URI.
     * The channel does not attampt to connect automatically.
     * You must call {@code connect} first to initiate the socket connection.
     *
     * @param serverUri - The remote uri of the server.
     * @param draft - The websocket draft to use.
     */
    public Client(final URI serverUri, final Draft draft) {
        super(serverUri, draft);
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(final String message) {

    }

    @Override
    public void onClose(final int code, final String reason, final boolean remote) {

    }

    @Override
    public void onError(final Exception ex) {

    }
}
