package nl.tudelft.ti2806.riverrush.domain.event;

import nl.tudelft.ti2806.riverrush.network.event.NetworkEvent;
import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for {@link BasicEventDispatcher}.
 */
public class BasicEventDispatcherTest {

    /**
     * Class under test.
     */
    private EventDispatcher dispatcher;

    @Mock
    private EventListener<Event> listenerMock;

    @Mock
    private Event eventMock;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.dispatcher = new BasicEventDispatcher();
    }

    /**
     * register should add the event type and lsitener.
     */
    @Test
    public void registerAddsListener1() {
        this.dispatcher.register(Event.class, this.listenerMock);
        assertEquals(1, this.dispatcher.countRegistered(Event.class));
    }

    @Test
    public void registerAddsListener2() {
        this.dispatcher.register(Event.class, this.listenerMock);
        this.dispatcher.register(Event.class, this.listenerMock);
        assertEquals(2, this.dispatcher.countRegistered(Event.class));
    }

    @Test
    public void countRegistered() {
        assertEquals(0, this.dispatcher.countRegistered(Event.class));
    }

    @Test
    public void dispatch_callsListener() {
        this.dispatcher.register(this.eventMock.getClass(), this.listenerMock);
        this.dispatcher.dispatch(this.eventMock);
        verify(this.listenerMock).dispatch(this.eventMock, this.dispatcher);
    }

    @Test
    public void dispatch_callsAllListeners() {
        this.dispatcher.register(this.eventMock.getClass(), this.listenerMock);
        this.dispatcher.register(this.eventMock.getClass(), this.listenerMock);
        this.dispatcher.dispatch(this.eventMock);
        verify(this.listenerMock, Mockito.times(2)).dispatch(this.eventMock, this.dispatcher);
    }

    @Test
    public void dispatch_callsCorrectListener() {
        this.dispatcher.register(DummyEvent.class, this.listenerMock);
        this.dispatcher.dispatch(this.eventMock);
        verifyZeroInteractions(this.listenerMock);
    }

    private class DummyEvent implements NetworkEvent {

        @Override
        public String serialize(final Protocol protocol) {
            return "";
        }

        @Override
        public NetworkEvent deserialize(final Map<String, String> keyValuePairs) {
            return this;
        }
    }
}
