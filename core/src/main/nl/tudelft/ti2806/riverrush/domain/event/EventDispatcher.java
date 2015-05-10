package nl.tudelft.ti2806.riverrush.domain.event;

/**
 * Created by thomas on 9-5-15.
 */
public interface EventDispatcher {

    /**
     * Add a listener to an {@link Event} type.
     * Multiple registeredListeners per event are possible.
     * @param eventType - The runtime class to add a listener for.
     * @param eventListener - The listener itself.
     */
    void register(Class<? extends Event> eventType, EventListener eventListener);

    /**
     * Mainly used for testing.
     * Check the amount of registered {@link EventListener}s for a given {@link Event} type.
     * @param eventType - The type of event to check.
     * @return The amount of registered listeners.
     */
    int countRegistered(Class<? extends Event> eventType);

    /**
     * Dispatch an array of events to registered listeners.
     * @param events - All events to dispatch.
     */
    void dispatch(Event[] events);

    /**
     * Dispatch one event to registered listeners.
     * @param event - The single event.
     */
    void dispatch(Event event);
}
