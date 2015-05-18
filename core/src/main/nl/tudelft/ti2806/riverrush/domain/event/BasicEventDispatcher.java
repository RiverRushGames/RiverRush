package nl.tudelft.ti2806.riverrush.domain.event;

import java.net.InetSocketAddress;
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
    private final Map<Class<? extends Event>, List<EventListener<?>>> registeredListeners = new Hashtable<>();

    private InetSocketAddress remoteAddress;

    @Override
    public <T extends Event> void register(final Class<T> eventType, final EventListener<T> eventListener) {
        List<EventListener<?>> listeners = registeredListeners.get(eventType);

        if (listeners == null) {
            listeners = new LinkedList<>();
        }
        listeners.add(eventListener);
        this.registeredListeners.put(eventType, listeners);
    }


    @Override
    public int countRegistered(final Class<? extends Event> eventType) {
        List<EventListener<?>> listeners = this.registeredListeners.get(eventType);
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
        List<EventListener<?>> listeners = this.registeredListeners.get(event.getClass());
        if (listeners != null) {
            listeners.forEach(
                eventListener -> eventListener.dispatch(event, this)
            );
        }
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    @Override
    public void setRemoteAddress(InetSocketAddress address) {
        this.remoteAddress = address;
    }
}
