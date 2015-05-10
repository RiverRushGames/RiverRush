package nl.tudelft.ti2806.riverrush.domain.event;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Domain event dispatcher.
 * Allows registeredListeners to register to an event.
 * They get a call whenever an event of their type is fired.
 */
public class BasicEventDispatcher implements EventDispatcher {

    /**
     * Maps event types to a list of listeners.
     */
    private final Map<Class<? extends Event>, List<EventListener>> registeredListeners;

    /**
     * .
     */
    public BasicEventDispatcher() {
        this.registeredListeners = new Hashtable<>();
    }

    @Override
    public void register(final Class<? extends Event> eventType, final EventListener eventListener) {
        List<EventListener> listeners = registeredListeners.get(eventType);

        if (listeners == null) {
            listeners = new LinkedList<>();
        }
        listeners.add(eventListener);
        this.registeredListeners.put(eventType, listeners);
    }


    @Override
    public int countRegistered(final Class<? extends Event> eventType) {
        List<EventListener> listeners = this.registeredListeners.get(eventType);
        if (listeners == null) {
            return 0;
        } else {
            return listeners.size();
        }
    }

    @Override
    public void dispatch(final Event[] events) {
        for (Event event : events) {
            this.dispatch(event);
        }
    }

    @Override
    public void dispatch(final Event event) {
        List<EventListener> listeners = this.registeredListeners.get(event.getClass());
        if (listeners != null) {
            listeners.forEach(
                eventListener -> eventListener.handle(event)
            );
        }
    }
}
