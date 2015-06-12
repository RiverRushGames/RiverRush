package nl.tudelft.ti2806.riverrush.graphics.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import nl.tudelft.ti2806.riverrush.desktop.MainDesktop;
import nl.tudelft.ti2806.riverrush.domain.event.Direction;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class RockGraphic extends AbstractObstacle {

    /**
     * Size of the graphic.
     */
    private static final float SIZE = 256;
    private static final float VELOCITY = ((MainDesktop.getHeight() + (SIZE * 2)) / 720f) * 2;
    // 720f = velocity river

    private final AssetManager assets;
    private static final float DESKTOP_HEIGHT = MainDesktop.getHeight();
    private static final float DESKTOP_WIDTH = MainDesktop.getWidth();
    private static final float INIT_POS = 800f;
    private static final float OFFSET_SIZE = 320f;

    private static final int NEGATIVE_MULTIPLIER = -2;
    private static final int TEXTURE_SIZE_X = 679;
    private static final int TEXTURE_SIZE_Y = 436;

    private final float offset;
    private final Direction direction;
    private Circle bounds;

    /**
     * Creates a new obstacle.
     *
     * @param assetsManager refers to the manager that has made all loaded assets available for use.
     * @param off Configures the place from which the obstacle is fired. Must be between 0 and 1
     */
    public RockGraphic(final AssetManager assetsManager, final Direction dir) {
        this.assets = assetsManager;

        this.direction = dir;
        if (dir == Direction.LEFT) {
            this.offset = 0.2f;
        } else if (dir == Direction.RIGHT) {
            this.offset = 0.8f;
        } else {
            this.offset = 0.5f;
        }
    }

    /**
     * Actually adds the obstacle to the screen.
     */
    public void init() {
        this.setWidth(SIZE);
        this.setHeight(SIZE * DESKTOP_HEIGHT / DESKTOP_WIDTH / 2);
        this.setPosition((DESKTOP_WIDTH * this.offset) - SIZE / 2, DESKTOP_HEIGHT); // 1080
        this.setOrigin(SIZE / 2, SIZE / 2);

        Vector2 v = new Vector2(this.getWidth() / 2, this.getHeight() / 2);
        this.localToStageCoordinates(v);

        this.bounds = new Circle(v.x, v.y, this.getHeight() / 2);

        MoveToAction moveDown = new MoveToAction();
        moveDown.setPosition(this.getX(), NEGATIVE_MULTIPLIER * SIZE); // 1592 distance
        moveDown.setDuration(VELOCITY);

        this.addAction(moveDown);
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        Texture tex = this.assets.get("data/iceberg.png", Texture.class);
        TextureRegion region = new TextureRegion(tex, 0, 0, TEXTURE_SIZE_X, TEXTURE_SIZE_Y);
        batch.enableBlending();

        Color color = this.getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        Vector2 v = new Vector2(this.getWidth() / 2, this.getHeight() / 2);
        this.localToStageCoordinates(v);

        this.bounds = new Circle(v.x, v.y, this.getHeight() / 2);

        batch.draw(region, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(),
                this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(),
                this.getRotation());

    }

    /**
     * @return true if the animation is done
     */
    public boolean isDone() {
        return this.getY() == NEGATIVE_MULTIPLIER * SIZE;
    }

    public boolean calculateCollision(final BoatGroup boat) {
        boolean ret = this.bounds.overlaps(boat.getBounds());
        return ret;
    }

    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Display the collide animation on the rock
     * and then remove the rock from the game.
     */
    public void collideAnimation() {
        final int shakeCount = 10;
        final int shakeDistance = 20;
        final float shakeSpeed = 0.02f;

        this.addAction(
            sequence(
            parallel(
                Actions.scaleTo(0, 0, 1f),
                Actions.moveBy(-800, 0, 1f),
                Actions.repeat(shakeCount, (
                    Actions.sequence(
                        Actions.moveBy(shakeDistance, 0, shakeSpeed),
                        Actions.moveBy(-shakeDistance, 0, shakeSpeed),
                        Actions.moveBy(0, shakeDistance, shakeSpeed),
                        Actions.moveBy(0, -shakeDistance, shakeSpeed)
                    )
                )),
                Actions.fadeOut(1f)),
            new Action() {
                @Override
                public boolean act(float delta) {
                    RockGraphic.this.remove();
                    return true;
                }
            }));
    }
}
