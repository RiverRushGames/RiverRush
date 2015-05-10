package nl.tudelft.ti2806.riverrush.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.failfast.FailIf;
import nl.tudelft.ti2806.riverrush.network.event.NetworkEvent;
import nl.tudelft.ti2806.riverrush.network.protocol.InvalidActionException;
import nl.tudelft.ti2806.riverrush.network.protocol.InvalidProtocolException;
import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import javax.inject.Provider;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Map;

/**
 * Web socket endpoint for the backend to handle incoming tcp request from the client.
 */
@Singleton
public class Server extends WebSocketServer {

    /**
     * Maps a remote address to a handler for player actions.
     */
    private final Map<InetSocketAddress, EventDispatcher> eventDispatchers;

    /**
     * Provides instances of EventDispatcher when a client joins.
     */
    private final Provider<EventDispatcher> dispatcherProvider;

    /**
     * The protocol used to serialize/deserialize network messages.
     */
    private final Protocol protocol;

    /**
     * Constructs the server, does NOT start it (see the {@link #start()} method).
     * @param aProvider - A {@link Provider} for {@link EventDispatcher}s.
     * @param aProtocol - The protocol to use when receiving and sending messages.
     */
    @Inject
    public Server(final Provider<EventDispatcher> aProvider, final Protocol aProtocol) {
        super(new InetSocketAddress(aProtocol.getPortNumber()));
        this.eventDispatchers = new Hashtable<>();
        this.protocol = aProtocol;
        this.dispatcherProvider = aProvider;
    }

    @Override
    public void onOpen(final WebSocket conn, final ClientHandshake handshake) {
        FailIf.isNull(conn);
        EventDispatcher dispatcher = this.dispatcherProvider.get();
        eventDispatchers.put(conn.getRemoteSocketAddress(),
                dispatcher);
    }

    @Override
    public void onClose(final WebSocket conn, final int code, final String reason, final boolean remote) {
        FailIf.isNull(conn);
        eventDispatchers.remove(conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(final WebSocket conn, final String message) {
        FailIf.isNull(conn, message);
        final NetworkEvent event;
        try {
            event = this.protocol.deserialize(message);

            EventDispatcher dispatcher = eventDispatchers.get(conn.getRemoteSocketAddress());
            dispatcher.dispatch(event);

        } catch (InvalidProtocolException | InvalidActionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(final WebSocket conn, final Exception ex) {
        FailIf.isNull(conn, ex);
    }

}
