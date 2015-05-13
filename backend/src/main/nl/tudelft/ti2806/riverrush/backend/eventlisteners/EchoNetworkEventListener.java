package nl.tudelft.ti2806.riverrush.backend.eventlisteners;

import nl.tudelft.ti2806.riverrush.network.event.EchoNetworkEvent;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.domain.event.EventListener;
import nl.tudelft.ti2806.riverrush.network.event.SendEvent;

/**
 * Created by thomas on 13-5-15.
 */
public class EchoNetworkEventListener extends EventListener<EchoNetworkEvent> {

    private final EventDispatcher eventDispatcher;

    public EchoNetworkEventListener(final EventDispatcher dispatcher) {
        this.eventDispatcher = dispatcher;
    }

    @Override
    public void handle(final EchoNetworkEvent event, final EventDispatcher dispatcher) {
        eventDispatcher.dispatch(new SendEvent(new EchoNetworkEvent()));
    }
}
