package nl.tudelft.ti2806.riverrush.domain.event;

/**
 * Bridges.
 */
@FunctionalInterface
public interface EventBridge {

    /**
     * Handle a Domain Event.
     *
     * @param event - The event to dispatch.
     */
    void dispatch(Event event, EventDispatcher dispatcher);
}
