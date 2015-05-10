package nl.tudelft.ti2806.riverrush.domain.event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
    private EventListener listenerMock;

    @Mock
    private Event eventMock;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dispatcher = new BasicEventDispatcher();
    }

    /**
     * register should add the event type and lsitener.
     */
    @Test
    public void registerAddsListener1() {
        dispatcher.register(Event.class, listenerMock);
        assertEquals(1, dispatcher.countRegistered(Event.class));
    }

    @Test
    public void registerAddsListener2() {
        dispatcher.register(Event.class, listenerMock);
        dispatcher.register(Event.class, listenerMock);
        assertEquals(2, dispatcher.countRegistered(Event.class));
    }

    @Test
    public void countRegistered() {
        assertEquals(0, dispatcher.countRegistered(Event.class));
    }

    @Test
    public void dispatch_callsListener() {
        dispatcher.register(eventMock.getClass(), listenerMock);
        dispatcher.dispatch(eventMock);
        verify(listenerMock).handle(eventMock);
    }

    @Test
    public void dispatch_callsAllListeners() {
        dispatcher.register(eventMock.getClass(), listenerMock);
        dispatcher.register(eventMock.getClass(), listenerMock);
        dispatcher.dispatch(eventMock);
        verify(listenerMock, Mockito.times(2)).handle(eventMock);
    }

    @Test
    public void dispatch_callsCorrectListener() {
        dispatcher.register(DummyEvent.class, listenerMock);
        dispatcher.dispatch(eventMock);
        verifyZeroInteractions(listenerMock);
    }

    private class DummyEvent implements Event { }
}
