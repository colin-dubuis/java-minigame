package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class FireSpell extends Monster {
    private ARPGFireSpell handler;

    private Sprite[] sprite = new Sprite[7];
    private Animation animations;


    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.handler = new ARPGFireSpell();

        for (int i =0; i<7;i++){
            sprite[i] = new RPGSprite("zelda/fire",1,1,this, new RegionOfInterest(16*i,0,16,16), new Vector(0f, 0.3f));
        }
        animations = new Animation(5,sprite);



    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animations.update(deltaTime);

        littleBrother();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        animations.draw(canvas);
    }

    private void littleBrother(){

           getOwnerArea().registerActor(new FireSpell(getOwnerArea(), this.getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector())) );


    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);

    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);

    }


    class ARPGFireSpell implements ARPGInteractionVisitor {

        public void interactWith(Door door) {

        }

        public void interactWith(Grass g) {


        }

        @Override
        public void interactWith(ARPGPlayer player) {

        }

        @Override
        public void interactWith(Bomb b) {

        }
    }
}
