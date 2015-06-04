package nl.tudelft.ti2806.riverrush.graphics.entity;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.inject.Inject;

/**
 * Game class representing a river.
 */
public class RiverActor extends Actor {

    private static final int END_REGIONX = 1599; // 570;
    private static final int END_REGIONY = 931; // 570;
    private static final float FLOW_DURATION = 3f;

    private final AssetManager manager;
    private final float mid;

    /**
     * Creates an river object with a given graphical representation.
     *
     * @param assetManager enables the object to retrieve its assets
     * @param ypos represents the position of the river on the y axis
     * @param width represents the width of the river object
     * @param height represents the height of the river object
     */
    @Inject
    public RiverActor(final AssetManager assetManager, final float ypos, final float width,
            final float height) {
        this.manager = assetManager;
        this.setPosition(0, height);
        this.setWidth(width);
        this.setHeight(height);
        this.mid = width / 2;

        MoveToAction moveDown = new MoveToAction();
        moveDown.setPosition(0, height * -1);
        moveDown.setDuration(FLOW_DURATION);

        MoveToAction moveUp = new MoveToAction();
        moveUp.setPosition(0, ypos);

        SequenceAction seq = sequence(moveUp, moveDown);
        RepeatAction rep = forever(seq);

        // SequenceAction s = (SequenceAction) rep.getAction();
        // MoveToAction m = (MoveToAction) s.getActions().get(1);
        // m.setDuration(FLOW_DURATION + 100f);

        this.addAction(rep);
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        Texture tex = this.manager.get("data/river.png", Texture.class);
        TextureRegion region = new TextureRegion(tex, 0, 0, END_REGIONX, END_REGIONY);
        batch.draw(region, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(),
                this.getWidth(), this.getHeight() * 2, this.getScaleX(), this.getScaleY(),
                this.getRotation());
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
    }

    /**
     * Gives the middle of the river on the x-axis.
     *
     * @return a float that represents the middle of the river
     */
    public float getMid() {
        return this.mid;
    }
}
