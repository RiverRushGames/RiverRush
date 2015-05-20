package nl.tudelft.ti2806.riverrush.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.Inject;
import nl.tudelft.ti2806.riverrush.Assets;
import nl.tudelft.ti2806.riverrush.desktop.MainDesktop;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;

import java.util.Timer;
import java.util.TimerTask;

public class WaitingScreen implements Screen {

    private static final int SECOND = 1000;
    private final EventDispatcher dispatcher;
    private Stage stage;
    private final AssetManager assets;

    private TextureAtlas atlas;
    private Skin skin;

    // Label for time left till game start
    private Timer tmr;
    private Label timer;
    private int time;

    // Label for amount of people connected
    private Label counter;
    private int count;

    @Inject
    public WaitingScreen(final AssetManager assetManager,
                         final EventDispatcher eventDispatcher) {
        this.dispatcher = eventDispatcher;
        this.assets = assetManager;
        this.time = Integer.MAX_VALUE;
    }

    public void init() {
    }

    @Override
    public void show() {
        this.atlas = new TextureAtlas("uiskin.atlas");
        this.skin = new Skin(Gdx.files.internal("uiskin.json"), this.atlas);
        this.stage = new Stage();

        Texture texture = new Texture(
            Gdx.files.internal(Assets.loading));
        TextureRegion region = new TextureRegion(texture, 0, 0,
            MainDesktop.WIDTH, MainDesktop.HEIGHT);

        Image image = new Image(region);
        image.setFillParent(true);
        this.stage.addActor(image);

        this.createTimerLabel();
        this.createCounterLabel();

    }

    /**
     * Creates a timerLabel
     */
    public void createTimerLabel() {
        this.timer = new Label("Time till game start: ", this.skin);
        this.timer.setPosition(1200, 540);
        this.stage.addActor(this.timer);
    }

    /**
     * Creates the counter label
     */
    public void createCounterLabel() {
        this.counter = new Label("Connected: ", this.skin);
        this.counter.setPosition(1200, 500);
        this.stage.addActor(this.counter);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act();
        this.stage.draw();

    }

    /**
     * Tell the waiting screen that someone has connected
     */
    public void addConnection() {
        this.count++;
        this.counter.setText("Connected: " + this.count);

    }

    /**
     * Start counting down to zero. When zero is reached go to the next screen
     *
     * @param amountOfTime - amount of seconds to count down to
     */
    public void startTimer(final int amountOfTime) {
        this.tmr = new Timer();
        this.time = amountOfTime;
        this.tmr.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                WaitingScreen.this.time--;
                WaitingScreen.this.timer.setText("Time till game start: "
                    + WaitingScreen.this.time);
                WaitingScreen.this.addConnection();

            }
        }, SECOND, SECOND);
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        this.tmr.cancel();
    }

}
