package nl.tudelft.ti2806.riverrush.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.tudelft.ti2806.riverrush.Assets;
import nl.tudelft.ti2806.riverrush.desktop.MainDesktop;
import nl.tudelft.ti2806.riverrush.domain.entity.Monkey;
import nl.tudelft.ti2806.riverrush.domain.entity.Player;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;
import nl.tudelft.ti2806.riverrush.game.Game;
import nl.tudelft.ti2806.riverrush.graphics.CenterStage;
import nl.tudelft.ti2806.riverrush.graphics.SideStage;

@Singleton
public class PlayingGameScreen implements Screen {

    private final EventDispatcher dispatcher;
    private int WIDTH = MainDesktop.WIDTH;
    private int HEIGHT = MainDesktop.HEIGHT;
    private static final double BANKSIZE = 1.0 / 20.0;
    private static final double SCREENSIZE = 1.0 / 5.0 * 2.0;
    private static final double MIDSIZE = 1.0 / 10.0;

    // Partioning
    private static final double FIRSTBANKEDGE = 1.0 / 20.0;
    private static final double LEFTSCREENEDGE = 1.0 / 20.0 * 9.0;
    private static final double MIDEDGE = 1.0 / 20.0 * 11.0;
    private static final double RIGHTSCREENEDGE = 1.0 / 20.0 * 19.0;

    private static final int ENDTEXTUREX = 229;
    private static final int ENDTEXTUREY = 138;

    private SideStage leftScreen;
    private SideStage rightScreen;
    private CenterStage midScreen;

    private Stage leftStage;
    private Stage midStage;
    private Stage rightStage;
    private Stage banksLeft;
    private Stage banksRight;
    public OrthographicCamera camera;
    private Game game;
    private AssetManager assets;
    private SpriteBatch spriteBatch;

    @Inject
    public PlayingGameScreen(final AssetManager assetsManager,
                             final EventDispatcher eventDispatcher) {
        this.dispatcher = eventDispatcher;
        this.assets = assetsManager;
    }

    public void init() {
        this.banksLeft = new Stage();

        this.leftScreen = new SideStage(this.assets, this.WIDTH, this.HEIGHT,
            this.dispatcher);
        this.leftStage = new Stage();
        this.leftStage.addActor(this.leftScreen);

        this.midScreen = new CenterStage(this.assets, this.WIDTH, this.HEIGHT);
        this.midStage = new Stage();
        this.midStage.addActor(this.midScreen);

        this.rightScreen = new SideStage(this.assets, this.WIDTH, this.HEIGHT,
            this.dispatcher);
        this.rightStage = new Stage();
        this.rightStage.addActor(this.rightScreen);

        this.banksRight = new Stage();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.WIDTH, this.HEIGHT);

        this.spriteBatch = new SpriteBatch();
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.camera.update();
        this.spriteBatch.setProjectionMatrix(this.camera.combined);

        this.drawLeftBanks();
        this.drawLeftStage();
        this.drawMidStage();
        this.drawRightStage();
        this.drawRightBanks();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    /**
     * Draw left sandy beach.
     */
    private void drawLeftBanks() {
        this.banksLeft.act(Gdx.graphics.getDeltaTime());
        int width = (int) (Gdx.graphics.getWidth() * BANKSIZE);
        Gdx.gl.glViewport(0, 0, width, // 0 - 0.05
            Gdx.graphics.getHeight());
        this.banksLeft.draw();
    }

    /**
     * Draw right river with stuff.
     */
    private void drawLeftStage() {
        this.leftStage.act(Gdx.graphics.getDeltaTime());
        int start = (int) (Gdx.graphics.getWidth() * FIRSTBANKEDGE);
        int width = (int) (Gdx.graphics.getWidth() * SCREENSIZE);
        Gdx.gl.glViewport(start, 0, // 0.05 - 0.45
            width, Gdx.graphics.getHeight());
        this.leftStage.draw();
    }

    /**
     * Draw the progressbar.
     */
    private void drawMidStage() {
        this.midStage.act(Gdx.graphics.getDeltaTime());
        int start = (int) (Gdx.graphics.getWidth() * LEFTSCREENEDGE);
        int width = (int) (Gdx.graphics.getWidth() * MIDSIZE);
        Gdx.gl.glViewport(start, 0, // 0.45 - 0.55
            width, Gdx.graphics.getHeight());
        this.midStage.draw();
    }

    /**
     * Draw left river with stuff.
     */
    private void drawRightStage() {
        this.rightStage.act(Gdx.graphics.getDeltaTime());
        int start = (int) (Gdx.graphics.getWidth() * MIDEDGE);
        int width = (int) (Gdx.graphics.getWidth() * SCREENSIZE);
        Gdx.gl.glViewport(start, 0, // 0.55 - 0.95
            width, Gdx.graphics.getHeight());
        this.rightStage.draw();
    }

    /**
     * Draw left sandy beach.
     */
    private void drawRightBanks() {
        this.banksRight.act(Gdx.graphics.getDeltaTime());
        int start = (int) (Gdx.graphics.getWidth() * RIGHTSCREENEDGE);
        int width = (int) (Gdx.graphics.getWidth() * BANKSIZE);
        Gdx.gl.glViewport(start, 0, // 0.95 - 1
            width, Gdx.graphics.getHeight());
        this.banksRight.draw();
    }

    @Override
    public void show() {
        // Get texture
        Texture tex = this.assets.get(Assets.grass, Texture.class);
        TextureRegion region = new TextureRegion(tex, 0, 0, ENDTEXTUREX,
            ENDTEXTUREY);

        Image leftImg = new Image(region);
        leftImg.setFillParent(true);
        this.banksLeft.addActor(leftImg);

        Image rightImg = new Image(region);
        rightImg.setFillParent(true);
        this.banksRight.addActor(rightImg);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        this.leftStage.dispose();
        this.rightStage.dispose();
        this.midStage.dispose();
        this.spriteBatch.dispose();

    }

    public void jump(Player player) {
        // TODO: Not actually considering which player, now only jumps the
        // animal on the left boat
        Monkey monk = this.leftScreen.getBoat().getAnimal(player);
        Action jump = monk.jump();
        monk.addAction(jump);

    }
}
