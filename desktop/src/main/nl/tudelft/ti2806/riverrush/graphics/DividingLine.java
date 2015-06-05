package nl.tudelft.ti2806.riverrush.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.inject.Inject;

/**
 * Game class represents the river bank.
 */
public class DividingLine extends Actor {
    private final AssetManager manager;
    private static final String FILENAME = "data/dividingLine.png";

    private static final int END_REGIONX = 1024; // 229;
    private static final int END_REGIONY = 768; // 138;

    /**
     * Creates an river banks object with a given graphical representation.
     *
     * @param assetManager enables the object to retrieve its assets
     * @param xpos         represents the position of the line on the x axis
     * @param ypos         represents the position of the line on the y axis
     * @param width        represents the width of the line object
     * @param height       represents the height of the line object
     */
    @Inject
    public DividingLine(
        final AssetManager assetManager,
        final float xpos,
        final float ypos,
        final float width,
        final float height
    ) {
        this.manager = assetManager;
        this.setPosition(xpos, ypos);
        this.setWidth(width);
        this.setHeight(height);
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        super.draw(batch, parentAlpha);
        Texture tex = this.manager.get(FILENAME, Texture.class);
        TextureRegion region = new TextureRegion(tex, 0, 0, END_REGIONX, END_REGIONY);
        batch.draw(region, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
    }
}
