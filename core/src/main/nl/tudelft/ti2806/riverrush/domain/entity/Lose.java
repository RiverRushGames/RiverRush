package nl.tudelft.ti2806.riverrush.domain.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import nl.tudelft.ti2806.riverrush.Assets;

public class Lose extends End {

    private static final int END_REGIONX = 375;
    private static final int END_REGIONY = 360;

    /**
     * Creates an lose game object that presents itself on the screen to the losing party.
     *
     * @param assetManager enables the object to retrieve its assets.
     */
    public Lose(AssetManager assetManager) {
        super(assetManager);
        Texture tex = assetManager.get(Assets.lose, Texture.class);
        this.region = new TextureRegion(tex, 0, 0, END_REGIONX, END_REGIONY);

    }

}
