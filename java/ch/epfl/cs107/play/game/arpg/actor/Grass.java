package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Grass extends AreaEntity {

    private final static int ANIMATION_DURATION = 12;
    private Sprite[] tab = new Sprite[4];
    private Animation destruction;
    private Sprite grass;
    private Boolean cut = false;
    private final double PROBABILITY_TO_DROP_ITEM = 0.5;
    private final double PROBABILITY_TO_DROP_HEART = 0.5;
    private Area ownerArea = getOwnerArea();
    private DiscreteCoordinates mainCellCoordinates = getCurrentMainCellCoordinates();

    /**
     * Default Grass constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the grass in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the grass in the Area. Not null
     */
    public Grass(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        grass = new RPGSprite("zelda/grass", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
        tab[0] = new RPGSprite("zelda/grass.sliced", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
        tab[1] = new RPGSprite("zelda/grass.sliced", 1, 1, this, new RegionOfInterest(32, 0, 32, 32));
        tab[2] = new RPGSprite("zelda/grass.sliced", 1, 1, this, new RegionOfInterest(64, 0, 32, 32));
        tab[3] = new RPGSprite("zelda/grass.sliced", 1, 1, this, new RegionOfInterest(96, 0, 32, 32));
        destruction = new Animation(ANIMATION_DURATION/4, tab, true);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(cut){destruction.update(deltaTime);}
        if(destruction.isCompleted()){
            getOwnerArea().unregisterActor(this);
            drop();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(!cut){
            grass.draw(canvas);
        } else {
            if(!destruction.isCompleted()){
            destruction.draw(canvas);
            }
            destruction.setRepeat(false);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    private void drop(){
        if(RandomGenerator.getInstance().nextDouble() < PROBABILITY_TO_DROP_ITEM){
            if(RandomGenerator.getInstance().nextDouble() < PROBABILITY_TO_DROP_HEART){
                getOwnerArea().registerActor(new Heart(ownerArea, Orientation.UP, mainCellCoordinates));
            } else {
                getOwnerArea().registerActor(new Coin(ownerArea, Orientation.UP, mainCellCoordinates));
            }
        }
    }

    public void slice(){
        cut = true;
    }

    @Override
    public boolean takeCellSpace() {
        if(cut){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }


    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

}
