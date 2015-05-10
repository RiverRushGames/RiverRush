package nl.tudelft.ti2806.riverrush.domain.entity;

import nl.tudelft.ti2806.riverrush.domain.event.Event;

/**
 * Event aware enitities.
 */
public class Eventable {
    /**
     * All events.
     */
    private Event[] events;


    public Event[] getEvents() {
        return this.events;
    }

    public void setEvents(final Event[] evensArray) {
        this.events = evensArray;
    }
}
