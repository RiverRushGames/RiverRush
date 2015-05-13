package nl.tudelft.ti2806.riverrush.network.protocol;

import nl.tudelft.ti2806.riverrush.network.event.NetworkEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the basic protocol.
 */
@RunWith(MockitoJUnitRunner.class)
public class BasicBasicProtocolTest {

    /**
     * Stub for the event to send over the network.
     */
    private NetworkEvent eventStub;

    /**
     * Mock for an event.
     */
    @Mock
    private NetworkEvent eventMock;

    /**
     * Class under test.
     */
    private Protocol protocol;

    /**
     * String representation of a valid serialized event.
     */
    private String stubEventSerialized;

    /**
     * String representation of an invalid serialized event.
     */
    private String unknownEventSerialized;

    /**
     * Initialize the protoco.
     */
    @Before
    public void setUp() {
        this.protocol = BasicProtocol.getInstance();
        this.eventStub = new StubEvent();
        this.stubEventSerialized = this.protocol.getEventTypeFieldKey()
                + this.protocol.getKeyValueSeperator()
                + this.eventStub.getClass().getSimpleName();

        this.unknownEventSerialized = this.protocol.getEventTypeFieldKey()
                + this.protocol.getKeyValueSeperator()
                + "SomeUnknownEventClass";
    }

    @Test
    public void serialize_callsEvent() throws InvalidActionException {
        this.protocol.serialize(this.eventMock);
        Mockito.verify(this.eventMock).serialize(this.protocol);
    }

    @Test
    public void testRegister() {
        this.protocol.registerNetworkAction(StubEvent.class,
                () -> this.eventStub);
        assertTrue(this.protocol.isRegistered(StubEvent.class));
    }

    @Test
    public void testDeserializeActionOnly() throws InvalidProtocolException,
            InvalidActionException {
        NetworkEvent expected = new StubEvent();
        this.protocol.registerNetworkAction(StubEvent.class, () -> expected);
        NetworkEvent actualEvent = this.protocol.deserialize(this.stubEventSerialized);
        assertEquals(expected, actualEvent);
    }

    @Test
    public void testDeserializeWithField() throws InvalidProtocolException,
            InvalidActionException {
        this.protocol.registerNetworkAction(StubEvent.class, StubEvent::new);

        final String expectedField = "field"
                + this.protocol.getKeyValueSeperator() + "HelloWorld"
                + this.protocol.getPairSeperator();
        NetworkEvent networkMessage = this.protocol.deserialize(expectedField
                + this.stubEventSerialized);

        assertTrue(networkMessage instanceof StubEvent);
        assertEquals("HelloWorld", ((StubEvent) networkMessage).getField());
    }

    @Test(expected = InvalidProtocolException.class)
    public void testDeserializeInvalidProtocol()
            throws InvalidProtocolException, InvalidActionException {
        this.protocol.deserialize("key=a=value");
    }

    @Test(expected = InvalidActionException.class)
    public void testDeserializeInvalidAction() throws InvalidProtocolException,
            InvalidActionException {
        this.protocol.deserialize(this.unknownEventSerialized);
    }

    /**
     * Stub event for testing.
     */
    private class StubEvent implements NetworkEvent {
        /**
         * A dummy field.
         */
        private String field;

        @Override
        public String serialize(final Protocol p) {
            return "field" + p.getKeyValueSeperator() + this.field;
        }

        @Override
        public NetworkEvent deserialize(final Map<String, String> keyValuePairs) {
            this.field = keyValuePairs.get("field");
            return this;
        }

        public String getField() {
            return this.field;
        }
    }
}
