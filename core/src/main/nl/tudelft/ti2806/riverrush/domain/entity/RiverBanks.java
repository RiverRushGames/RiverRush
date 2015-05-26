package nl.tudelft.ti2806.riverrush.domain.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.inject.Inject;
import nl.tudelft.ti2806.riverrush.Assets;

public class RiverBanks extends Actor {

    private AssetManager manager;

    private static final int END_REGIONX = 229;
    private static final int END_REGIONY = 138;

    /**
     * Creates an river banks object with a given graphical representation.
     *
     * @param assetManager enables the object to retrieve its assets
     * @param xpos         represents the position of the river banks on the x axis
     * @param ypos         represents the position of the river banks on the y axis
     * @param width        represents the width of the river banks object
     * @param height       represents the height of the river banks object
     */
    @Inject
    public RiverBanks(AssetManager assetManager, float xpos, float ypos, float width, float height) {
        this.manager = assetManager;
        this.setPosition(xpos, ypos);
        this.setWidth(width);
        this.setHeight(height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Texture tex = this.manager.get(Assets.grass, Texture.class);
        TextureRegion region = new TextureRegion(tex, 0, 0, END_REGIONX, END_REGIONY);
        batch.draw(region, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
