package nl.tudelft.ti2806.riverrush.desktop;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.tudelft.ti2806.riverrush.CoreModule;
import nl.tudelft.ti2806.riverrush.controller.Controller;
import nl.tudelft.ti2806.riverrush.controller.RenderController;
import nl.tudelft.ti2806.riverrush.domain.event.BasicEventDispatcher;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.graphics.RiverGame;
import nl.tudelft.ti2806.riverrush.network.Client;

import java.net.URISyntaxException;

public class MainDesktop extends CoreModule {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    private final Injector injector;
    private final Client client;


    public static void main(String[] arg) throws URISyntaxException {
        new MainDesktop();

    }

    public MainDesktop() throws URISyntaxException {
        injector = Guice.createInjector(this);

        // This injector can inject all dependencies configured in CoreModule
        // and this, the desktop module.
        client = new Client("localhost",
            this.configureRendererProtocol(),
            injector.getInstance(EventDispatcher.class),
            injector.getInstance(Controller.class));

        setupGraphics();
        client.connect();
    }

    private void setupGraphics() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 0;
        config.width = WIDTH;
        config.height = HEIGHT;
        // config.fullscreen = true;

        // Get the game from the injector.
        // The injector should not be passed to any other classes as a
        // dependency!
        RiverGame game = injector.getInstance(RiverGame.class);
        new LwjglApplication(game, config);
    }

    /**
     * Method is called when creating an {@link Injector}. It configures all
     * dependencies specific to the desktop application.
     */
    @Override
    protected void configure() {
        super.configure();
        // When injecting an AssetManager, use this specific instance.
        this.bind(AssetManager.class).toInstance(new AssetManager());
        this.bind(Controller.class).to(RenderController.class);
        // this.bind(Table.class).to(RunningGame.class);
    }

    @Override
    protected EventDispatcher configureEventDispatcher() {
        return new BasicEventDispatcher();
    }
}