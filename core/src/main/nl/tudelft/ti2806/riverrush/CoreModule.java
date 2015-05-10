package nl.tudelft.ti2806.riverrush;

import com.google.inject.AbstractModule;
import nl.tudelft.ti2806.riverrush.domain.event.BasicEventDispatcher;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.network.event.JoinEvent;
import nl.tudelft.ti2806.riverrush.network.protocol.BasicProtocol;
import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;

/**
 * Configures dependency injection.
 */
public class CoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Protocol.class).toInstance(configureProtocol());
        bind(EventDispatcher.class).toProvider(BasicEventDispatcher::new);
    }

    /**
     * Configure the protocol by registering all valid messages that can be sent.
     * @return The fully configured protocol.
     */
    private Protocol configureProtocol() {
        Protocol protocol = BasicProtocol.getInstance();
        // Register available network actions
        // protocol.registerNetworkAction(...);
        protocol.registerNetworkAction(JoinEvent.class, JoinEvent::new);

        return protocol;
    }
}
