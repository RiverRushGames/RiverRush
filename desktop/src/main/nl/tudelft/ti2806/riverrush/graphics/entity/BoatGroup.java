package nl.tudelft.ti2806.riverrush.graphics.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.google.inject.Inject;

import nl.tudelft.ti2806.riverrush.domain.entity.AbstractAnimal;
import nl.tudelft.ti2806.riverrush.domain.entity.Sector;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a boat that the animals row on.
 */
public class BoatGroup extends Group {

    private final int SIZE = 900;

    /**
     * The asset manager.
     */
    private final AssetManager manager;

    private final ArrayList<BoatSector> sectors;

    private static final float MOVE_DISTANCE = 400;
    private static final double HITBOX_OFFSET = 0.3;

    private static final int COL_COUNT = 5;
    private static final int ROW_COUNT = 2;

    private final Texture tex;
    private static final int MOVE_VOTE_THRESHOLD = 1;

    private final HashMap<AbstractAnimal, Integer> directionVotes;
    private int votingSum = 0;
    private float totalNumAnimals = 0;
    private Circle bounds;

    private MoveToAction move;
    private long previousMillis;

    private float VELOCITY = 0;
    private final float origX;
    private final float origY;

    /**
     * Creates an boat object with a given graphical representation.
     *
     * @param assetManager enables the object to retrieve its assets
     * @param xpos represents the position of the boat on the x axis
     * @param ypos represents the position of the boat on the y axis
     */
    @Inject
    public BoatGroup(final AssetManager assetManager, final float xpos, final float ypos) {
        this.manager = assetManager;
        this.setX(xpos);
        this.setY(ypos);
        this.setWidth(this.SIZE);
        this.setHeight(this.SIZE);

        this.origX = xpos;
        this.origY = ypos;

        this.tex = this.manager.get("data/ship.png", Texture.class);

        this.setOriginX((this.getWidth() / 2));
        this.setOriginY((this.getHeight() / 2));

        this.sectors = new ArrayList<>();
        this.directionVotes = new HashMap<>();

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.WHITE);

        for (int i = Sector.countSectors() - 1; i >= 0; i--) {
            Color color = colors.get(i);
            BoatSector sec = new BoatSector(assetManager, ROW_COUNT, COL_COUNT, color);
            float secPosX = (this.getWidth() / 2) - (sec.getWidth() / 2);
            float secPosY = 50f + ((20f + sec.getHeight()) * i);
            sec.setPosition(secPosX, secPosY);
            this.sectors.add(sec);
            this.addActor(sec);
        }

        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
    }

    public void init() {
        Vector2 v = new Vector2(this.getWidth() / 2, this.getHeight() / 2);
        v = this.localToStageCoordinates(v);

        this.bounds = new Circle(v.x, v.y, ((float) (this.getHeight() * HITBOX_OFFSET)));
        this.move = new MoveToAction();
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {

        Vector2 v = new Vector2(this.getWidth() / 2, this.getHeight() / 2);
        this.localToStageCoordinates(v);

        this.bounds = new Circle(v.x, v.y, ((float) (this.getHeight() * HITBOX_OFFSET)));

        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Color color = this.getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.draw(this.tex, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(),
                this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(),
                this.getRotation(), 0, 0, this.tex.getWidth(), this.tex.getHeight(), false, false);

        batch.setColor(Color.WHITE);

        super.draw(batch, parentAlpha);
        batch.disableBlending();

    }

    @Override
    public void act(final float delta) {
        super.act(delta);
    }

    public void addAnimal(final AnimalActor actor, final Sector sector) {
        BoatSector sec = this.sectors.get(sector.getIndex());
        sec.addAnimal(actor);
        this.totalNumAnimals++;
        this.updateBoatPosition();
    }

    public void voteForDirection(final AbstractAnimal animal, final int direction) {
        Integer currentVote = this.directionVotes.getOrDefault(animal, 0);
        if (currentVote != direction) {
            this.votingSum -= currentVote;
            this.votingSum += direction;
            this.directionVotes.put(animal, direction);
            this.updateBoatPosition();

            this.directionVotes.put(animal, direction);
        }
    }

    private void updateBoatPosition() {
        this.VELOCITY = (this.votingSum / this.totalNumAnimals) * MOVE_DISTANCE;
        float newX = this.origX + this.VELOCITY;

        this.clearActions();
        this.move = new MoveToAction();
        this.move.setPosition(newX, this.getY());

        this.move.setDuration(0.5f);
        this.addAction(this.move);
    }

    /**
     * Move to dodge an obstacle. Can dodge left or right based on direction.
     *
     * @param direction this parameter determines direction. 1 is to the right, -1 is to the left.
     */
    public void move(final int direction) {
        this.move = new MoveToAction();
        this.move.setPosition(this.getX() + (MOVE_DISTANCE * direction), this.getY());

        this.move.setDuration(0.5f);
        this.move.setInterpolation(new Interpolation.Elastic(2, 1, 1, 0.3f));
        this.addAction(this.move);
    }

    public void removeAnimal(AbstractAnimal absAnimal) {
        Animal anim = (Animal) absAnimal;
        AnimalActor actor = anim.getActor();
        for (BoatSector sec : this.sectors) {
            if (sec.getAnimals().contains(actor)) {
                sec.removeActor(actor);
                sec.getAnimals().remove(actor);
            }
        }
        this.totalNumAnimals--;
        this.directionVotes.remove(anim);
        this.updateBoatPosition();
    }

    public Circle getBounds() {
        return this.bounds;
    }
}
