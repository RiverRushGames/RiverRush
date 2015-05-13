package nl.tudelft.ti2806.riverrush.network;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import nl.tudelft.ti2806.riverrush.domain.event.Event;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.InetSocketAddress;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for the socket layer Server class.
 */
public class ServerTest extends AbstractModule {
    /**
     * For dependency injection.
     */
    private Injector injector;

    /**
     * Placeholder socket for server calls.
     */
    @Mock
    private WebSocket webSocketMock;

    /**
     * Used to verify calls from the server.
     */
    @Mock
    private EventDispatcher dispatcherMock;

    /**
     * To verify that Server calls the provider.
     */
    @Mock
    private Provider<EventDispatcher> providerMock;

    /**
     * Used to verify calls from the server.
     */
    @Mock
    private Protocol protocolMock;

    /**
     * Class under test.
     */
    private Server server;

    /**
     * Every test needs a fresh {@link Server} instance.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        InetSocketAddress address = new InetSocketAddress(0);
        when(this.webSocketMock.getRemoteSocketAddress()).thenReturn(address);

        when(this.providerMock.get()).thenReturn(this.dispatcherMock);

        this.injector = Guice.createInjector(this);
        this.server = this.injector.getInstance(Server.class);
    }

    /**
     * When onOpen() is called, the server needs to create a new EventDispatcher
     * using the provider. The server should also use the remote address of the
     * socket.
     */
    @Test
    public void onOpen_callsProvider() {
        this.server.onOpen(this.webSocketMock, mock(ClientHandshake.class));
        verify(this.webSocketMock, atLeastOnce()).getRemoteSocketAddress();
        verify(this.providerMock).get();
    }

    /**
     * onClose should use the remote address to delete the dispatcher from
     * dispatcher list.
     */
    @Test
    public void onClose_usesRemoteAddress() {
        this.server.onClose(this.webSocketMock, 0, "", true);
        verify(this.webSocketMock).getRemoteSocketAddress();
        verifyNoMoreInteractions(this.webSocketMock);
    }

    /**
     * onMessage should use the Protocol to create an event, and then dispatch
     * the event via EventDispatcher.
     */
    @Test
    public void onMessage_usesProtocolAndDispatches() {
        this.server.onOpen(this.webSocketMock, mock(ClientHandshake.class));
        this.server.onMessage(this.webSocketMock, "HelloWorld");
        verify(this.protocolMock).deserialize("HelloWorld");
        verify(this.dispatcherMock).dispatch(any(Event.class));
    }

    /**
     * Configures injection of mocks.
     */
    @Override
    protected void configure() {
        this.bind(Protocol.class).toInstance(this.protocolMock);

        // Every time a new EventDispatcher is requested by code under test,
        // Guice will inject a fresh mock.
        this.bind(EventDispatcher.class).toProvider(this.providerMock);
        this.bind(Server.class);
    }
}
