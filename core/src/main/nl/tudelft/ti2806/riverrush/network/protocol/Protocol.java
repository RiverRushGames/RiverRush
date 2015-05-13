package nl.tudelft.ti2806.riverrush.network.protocol;

import nl.tudelft.ti2806.riverrush.network.event.NetworkEvent;

/**
 * Encapsulates the translation between dispatchable {@link NetworkEvent}s and string
 * messages to send over sockets.
 */
public interface Protocol {
    /**
     * Registers a {@link NetworkEvent} that this protocol supports.
     *
     * @param eventClass
     *            - The type of event.
     * @param eventInstatiator
     *            - A lambda that creates such an event.
     */
    void registerNetworkAction(Class<? extends NetworkEvent> eventClass,
            EventInstantiator eventInstatiator);

    /**
     * Test whether the protocol supports a certain {@link NetworkEvent}.
     *
     * @param eventClass
     *            - The type of event to check.
     * @return - True if the event is currently registered.
     */
    boolean isRegistered(Class<? extends NetworkEvent> eventClass);

    /**
     * Translates a string representation to an actual {@link NetworkEvent} instance.
     *
     * @param event
     *            - The String event message received over sockets.
     * @return - The actual event instance, ready for dispatch.
     * @throws InvalidProtocolException
     *             when the received message was syntactically incorrect.
     * @throws InvalidActionException
     *             when the type of event was not registered.
     */
    NetworkEvent deserialize(String event) throws InvalidProtocolException,
            InvalidActionException;

    /**
     * Translate a {@link NetworkEvent} instance to it's string representation
     * according to the protocol's rules.
     *
     * @param event
     *            - The event that we want to send over network.
     * @return The string to send.
     */
    String serialize(NetworkEvent event);

    /**
     * Get the port number that this protocol operates on.
     *
     * @return The port.
     */
    int getPortNumber();

    /**
     * The character that seperates keys from values.
     *
     * @return The seperating character.
     */
    String getKeyValueSeperator();

    /**
     * The character that seperates key-value pairs from each other.
     *
     * @return The seperating character.
     */
    String getPairSeperator();

    /**
     * The field that indicates the type of event transmitted.
     *
     * @return The field/key.
     */
    String getEventTypeFieldKey();

    /**
     * A lambda interface that instantiates a concrete {@link NetworkEvent}.
     */
    @FunctionalInterface
    interface EventInstantiator {
        /**
         * Instantiate a concrete event.
         *
         * @return The event.
         */
        NetworkEvent instantiate();
    }
}
