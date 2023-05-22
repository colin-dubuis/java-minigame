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
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class FlameSkull extends Monster implements FlyableEntity {

    private float lifeTimeRandom;
    //handler
    private ARPGFlameSkullHandler handler;
    //animation
    private Sprite[][] sprites;
    private Animation[] animations;

    //move


    //untouchable frame
    //if canTouch is true -> the monster can touch the player to inflicts 1 damage
    private boolean canTouch = true;
    //player's time of invulnability
    private float untouchableTime = 3;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        //set hp in monster class
        setHp(0.5f);
        //handler
        this.handler = new ARPGFlameSkullHandler();
        //orientation

        //life duration
        this.lifeTimeRandom = randomNumberBounded(11,10);
        //sprite/animation
        sprites = RPGSprite.extractSprites("zelda/flameSkull", 3, 2, 2, this, 32, 32,new Vector(-0.5f,-0.5f), new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        animations = RPGSprite.createAnimations(8, sprites);

    }




    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        lifeTimeRandom -= deltaTime;
        if (lifeTimeRandom > 0) {


            animations[this.getOrientation().ordinal()].update(deltaTime);
            //displacement
           if(!isDisplacementOccurs()){
            moveRandom(15);}

        }else if(lifeTimeRandom<=0 || getHp()<=0){
            this.die();
        }

        //untouchable time
        //if the player has been touch start the condition
        if(!canTouch){
            untouchableTime -= deltaTime;

            if(untouchableTime<=0){
                //set canTouch at true -> the monster can inflicts damage to the player again
                canTouch = true;
                untouchableTime = 3;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
if(lifeTimeRandom>0 && !isDead())
        animations[this.getOrientation().ordinal()].draw(canvas);
    }




    @Override
    protected void moveOrientate(Orientation orientation, int speed) {
        super.moveOrientate(orientation, speed);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());

    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));

    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
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
    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);

    }

    class ARPGFlameSkullHandler implements ARPGInteractionVisitor {

        public void interactWith(Door door) {
            //nothing with Door
        }

        public void interactWith(Grass g) {
            //cut the grass if interaction
            g.slice();

        }

        @Override
        public void interactWith(ARPGPlayer player) {
            //if canTouch is true -> the monster can touch the player to inflicts 1 damage
            if(canTouch){
                player.setHp(player.getHp() - 1);
                //set canTouch at false -> the monster can't inflicts more damage
                canTouch = false;
            }
        }

        @Override
        public void interactWith(Bomb b) {
            //set the timer to 0 -> the monster has to explode the bomb if interaction
            b.timer = 0;
            //and die because the bomb kill the monster
            die();
        }

        @Override
        public void interactWith(LogMonster logMonster) {

        }
    }
}
