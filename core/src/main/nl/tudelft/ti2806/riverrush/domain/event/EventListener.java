package nl.tudelft.ti2806.riverrush.domain.event;

/**
 * Created by thomas on 13-5-15.
 */
public abstract class EventListener<T extends Event> implements EventBridge {
    /**
     * Handle a Domain Event.
     *
     * @param event - The event to dispatch.
     */
    public abstract void handle(T event, EventDispatcher dispatcher);

    @Override
    public final void dispatch(final Event event, final EventDispatcher dispatcher) {
        this.handle((T)event, dispatcher);
    }
}
