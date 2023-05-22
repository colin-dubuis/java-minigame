package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class MagicWaterProjectile extends Projectile {

    private Sprite[] sprite = new Sprite[4];
    private Animation animation;
    private ARPGMagicHandler handler;
    boolean reachedTarget = false;


    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        for (int i = 0; i < 4; i++) {
            sprite[i] = new RPGSprite("zelda/magicWaterProjectile", 1, 1, this, new RegionOfInterest(32*i, 0, 32, 32), new Vector(0, 0));
        }
        this.handler = new ARPGMagicHandler();
        setSpeed(4);

        animation = new Animation(1, sprite);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animation.update(deltaTime);

        if (reachedTarget){
            getOwnerArea().unregisterActor(this);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        animation.draw(canvas);
    }
    class ARPGMagicHandler implements ARPGInteractionVisitor {

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
