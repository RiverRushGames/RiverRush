package nl.tudelft.ti2806.riverrush.domain.event;

/**
 * Event listener.
 */
public abstract class EventListener<T extends Event>  {
    /**
     * Handle a Domain Event.
     *
     * @param event - The event to dispatch.
     */
    public abstract void handle(T event, EventDispatcher dispatcher);

    public final void dispatch(final Event event, final EventDispatcher dispatcher) {
        this.handle((T)event, dispatcher);
    }
}
