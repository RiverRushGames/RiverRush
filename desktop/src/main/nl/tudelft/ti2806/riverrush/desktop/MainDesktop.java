package nl.tudelft.ti2806.riverrush.desktop;

import java.net.URISyntaxException;

import nl.tudelft.ti2806.riverrush.CoreModule;
import nl.tudelft.ti2806.riverrush.controller.Controller;
import nl.tudelft.ti2806.riverrush.controller.RenderController;
import nl.tudelft.ti2806.riverrush.game.Game;
import nl.tudelft.ti2806.riverrush.network.Client;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * This class is the main class to be ran when starting the game. This class sets up the graphics
 * and the client connections.
 */
public class MainDesktop extends CoreModule {

    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private final Injector injector;

    /**
     * Calls the main desktop constructor that starts the game.
     *
     * @param arg not used
     * @throws URISyntaxException handles the situation where the URI has the wrong syntax.
     */
    public static void main(final String[] arg) throws URISyntaxException {
        new MainDesktop();
    }

    /**
     * Constructor for main desktop. Configures the client connections and sets up the graphics.
     *
     * @throws URISyntaxException handles the situation where the URI has the wrong syntax.
     */
    public MainDesktop() throws URISyntaxException {
        this.injector = Guice.createInjector(this);

        Client client = new Client("localhost", this.configureRendererProtocol());
        RenderController cntrl = this.injector.getInstance(RenderController.class);
        cntrl.setClient(client);
        client.setController(cntrl);

        this.setupGraphics();
        client.connect();

        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // this.injector.getInstance(EventDispatcher.class).dispatch(new GameAboutToStartEvent());
        //
        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // this.injector.getInstance(EventDispatcher.class).dispatch(new GameStartedEvent());
        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // AnimalAddedEvent ev = new AnimalAddedEvent();
        // for (int i = 0; i < 10; i++) {
        // try {
        // Thread.sleep(100);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // ev = new AnimalAddedEvent();
        // ev.setAnimal(i);
        // ev.setTeam(i % 2);
        // this.injector.getInstance(EventDispatcher.class).dispatch(ev);
        // }

        //
        // AnimalJumpedEvent jev = new AnimalJumpedEvent();
        // for (int i = 0; i < 5; i++) {
        // try {
        // Thread.sleep(100);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // jev = new AnimalJumpedEvent();
        // jev.setAnimal(i);
        // jev.setTeam(i % 2);
        // this.injector.getInstance(EventDispatcher.class).dispatch(jev);
        // }
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // AddObstacleEvent ev3 = new AddObstacleEvent();
        // ev3.setAnimal(1);
        // ev3.setTeam(0);
        // ev3.setLocation(0.5);
        // this.injector.getInstance(EventDispatcher.class).dispatch(ev3);

        // GameFinishedEvent fev = new GameFinishedEvent();
        // this.injector.getInstance(EventDispatcher.class).dispatch(fev);

    }

    /**
     * Return the current width of the main screen.
     *
     * @return an integer value representing the width.
     */
    public static int getWidth() {
        return WIDTH;
    }

    /**
     * Return the current height of the main screen.
     *
     * @return an integer value representing the height.
     */
    public static int getHeight() {
        return HEIGHT;
    }

    /**
     * Creates a Lwjgl Configurations with the given height and width. It will then get an instance
     * of the game class and use it to create the application.
     */
    private void setupGraphics() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 0;
        config.width = WIDTH;
        config.height = HEIGHT;
        // config.fullscreen = true;

        Game game = this.injector.getInstance(Game.class);
        new LwjglApplication(game, config);
    }

    /**
     * Method is called when creating an {@link Injector}. It configures all dependencies specific
     * to the desktop application.
     */
    @Override
    protected void configure() {
        super.configure();
        this.bind(AssetManager.class).toInstance(new AssetManager());
        this.bind(Controller.class).to(RenderController.class);
    }
}
