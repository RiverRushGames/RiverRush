package nl.tudelft.ti2806.riverrush.domain.event;

/**
 * Event listener.
 */
@FunctionalInterface
public interface EventListener {

    /**
     * Handle a Domain Event.
     *
     * @param event - The event to handle.
     */
    void handle(Event event);
}
