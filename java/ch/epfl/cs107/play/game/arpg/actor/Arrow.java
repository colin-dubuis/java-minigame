package ch.epfl.cs107.play.game.arpg.actor;

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

public class Arrow extends Projectile {

    private Sprite[][] sprite = new Sprite[4][1];
    private Animation[] animation;
    private ARPGArrowHandler handler;
    boolean reachedTarget = false;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        setSpeed(2);
        this.handler = new ARPGArrowHandler();




        sprite[Orientation.UP.ordinal()][0] = new RPGSprite("zelda/arrow", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
        sprite[Orientation.RIGHT.ordinal()][0] = new RPGSprite("zelda/arrow", 1, 1, this, new RegionOfInterest(32, 0, 32, 32));
        sprite[Orientation.DOWN.ordinal()][0] = new RPGSprite("zelda/arrow", 1, 1, this, new RegionOfInterest(64, 0, 32, 32));
        sprite[Orientation.LEFT.ordinal()][0] = new RPGSprite("zelda/arrow", 1, 1, this, new RegionOfInterest(96, 0, 32, 32));
        animation = RPGSprite.createAnimations(8, sprite);


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animation[this.getOrientation().ordinal()].update(deltaTime);

        if (reachedTarget) {
            getOwnerArea().unregisterActor(this);
        }


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        animation[this.getOrientation().ordinal()].draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        (   (ARPGInteractionVisitor)v).interactWith(this);
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    class ARPGArrowHandler implements ARPGInteractionVisitor {

        public void interactWith(Door door) {

        }

        public void interactWith(Grass g) {
        g.slice();

        }

        @Override
        public void interactWith(ARPGPlayer player) {


        }

        @Override
        public void interactWith(Bomb b) {

        }

        @Override
        public void interactWith(LogMonster logMonster) {

        }

        @Override
        public void interactWith(DarkLord darkLord) {

        }

        @Override
        public void interactWith(FlameSkull flameSkull) {

        }
    }

}
