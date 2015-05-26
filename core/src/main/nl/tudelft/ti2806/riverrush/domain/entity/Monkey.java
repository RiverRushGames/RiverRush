package nl.tudelft.ti2806.riverrush.domain.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.google.inject.Inject;

import nl.tudelft.ti2806.riverrush.domain.entity.state.AnimalOnBoat;
import nl.tudelft.ti2806.riverrush.domain.event.EventDispatcher;

import java.util.Timer;
import java.util.TimerTask;

import nl.tudelft.ti2806.riverrush.Assets;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Monkey">Tutorial on monkeys.</a>
 */
public class Monkey extends AbstractAnimal {

    private static final float JUMP_HEIGHT = 100;
    private static final int END_REGIONX = 432;
    private static final int END_REGIONY = 432;
    private static final int FALL_DISTANCEX = 200;
    private static final int FALL_DISTANCEY = -520;
    private static final float FALL_DURATION = 0.5f;
    private static final float JUMP_UP_DURATION = 0.3f;
    private static final float JUMP_DOWN_DURATION = 0.15f;
    private static final float DELAY_DURATION = 5f;
    private static final float WIGGLE_DURATION = 0.5f;
    private static final float WIGGLE_DISTANCE = 5f;

    private AssetManager manager;
    private float origX;
    private float origY;

    /**
     * Creates a monkey object that represents player characters.
     *
     * @param assetManager enables the object to retrieve its assets
     * @param xpos         represents the position of the monkey on the x axis
     * @param ypos         represents the position of the monkey on the y axis
     * @param width        represents the width of the monkey object
     * @param height       represents the height of the monkey object
     */
    @Inject
    public Monkey(AssetManager assetManager, float xpos, float ypos, float width, float height,
                  EventDispatcher dispatcher) {
        this.manager = assetManager;
        this.setX(xpos);
        this.setY(ypos);
        this.setWidth(width);
        this.setHeight(height);

        this.origX = xpos;
        this.origY = ypos;
        this.setState(new AnimalOnBoat(this, dispatcher));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Texture tex = this.manager.get(Assets.raccoon, Texture.class);
        TextureRegion region = new TextureRegion(tex, 0, 0, END_REGIONX, END_REGIONY);

        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Color color = this.getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.draw(region, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(),
            this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());

        batch.setColor(Color.WHITE);

        batch.disableBlending();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    public void collide() {
        this.setState(this.getState().collide());
    }

    /**
     * Creates an action that represents getting hit graphically (falling off the boat).
     *
     * @return an action that can be added to the actor
     */
    public Action getHit() {
        MoveToAction fall = new MoveToAction();
        fall.setPosition(this.getX() + FALL_DISTANCEX, this.getY() + FALL_DISTANCEY);
        fall.setDuration(FALL_DURATION);

        AlphaAction fade = Actions.fadeOut(FALL_DURATION);

        return Actions.parallel(fade, fall);
    }

    /**
     * Creates an action that represents returning to the boat graphically.
     *
     * @return an action that can be added to the actor
     */
    public Action returnToBoat() {
        MoveToAction fall = new MoveToAction();
        fall.setPosition(this.origX, this.origY);

        VisibleAction fade = Actions.show();

        return Actions.parallel(fade, fall);

    }

    @Override
    public Action jump() {
        this.setState(this.getState().jump());
        MoveToAction jumpUp = new MoveToAction();
        jumpUp.setPosition(this.getX(), this.getY() + JUMP_HEIGHT);
        jumpUp.setDuration(JUMP_UP_DURATION);

        MoveToAction jumpDown = new MoveToAction();
        jumpDown.setPosition(this.getX(), this.origY);
        jumpDown.setDuration(JUMP_DOWN_DURATION);

        this.setOrigin((this.getWidth() / 2), (this.getHeight() / 2));

        RotateByAction wiggleLeft = Actions.rotateBy(WIGGLE_DISTANCE);
        wiggleLeft.setDuration(WIGGLE_DURATION / 4);

        RotateByAction wiggleRight = Actions.rotateBy(-(WIGGLE_DISTANCE * 2));
        wiggleRight.setDuration(WIGGLE_DURATION / 2);

        RotateByAction wiggleBack = Actions.rotateBy(WIGGLE_DISTANCE);
        wiggleBack.setDuration(WIGGLE_DURATION / 4);

        SequenceAction wiggle = sequence(wiggleLeft, wiggleRight, wiggleBack);

        SequenceAction jump = sequence(jumpUp,
            Actions.repeat((int) (DELAY_DURATION / WIGGLE_DURATION), wiggle), jumpDown);

        int time = (int) ((JUMP_DOWN_DURATION + JUMP_UP_DURATION + DELAY_DURATION) * 1000);
        Timer tmr = new Timer();
        tmr.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Monkey.this.setState(Monkey.this.getState().drop());
                tmr.cancel();
            }
        }, time, time);
        return jump;
    }

}
