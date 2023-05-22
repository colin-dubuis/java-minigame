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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogMonster extends Monster {

    private ARPGLogMonsterHandler handler;
    private Sprite[][] spriteLogMonster;
    private Animation[] animationLogMonster;

    private Sprite[] spriteSleeping = new Sprite[4];
    private Animation animationSleeping;

    private Sprite[] spriteWakingUp = new Sprite[3];
    private Animation animationWakingUp;

    //state
    STATE currentState = STATE.IDLE;
    STATE previousState = currentState;

    //float time
    float idleTime = 0;
    float sleepingTime = 0;
    float inactivityTime = 0;

    //boolean switch damage
    private boolean canAttack = true;
    //boolean damage
    private boolean canDamage = false;



    enum STATE {
        IDLE,
        INACTIVITY,
        ATTACK,
        SLEEPING,
        WAKE_UP;
    }


    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public LogMonster(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        //handler
        this.handler = new ARPGLogMonsterHandler();
        //hp
        setHp(5f);

        //Movement
        spriteLogMonster = RPGSprite.extractSprites("zelda/logMonster", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0.2f), new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        animationLogMonster = RPGSprite.createAnimations(8, spriteLogMonster);

        for (int i = 0; i < 4; i++) {
            spriteSleeping[i] = new RPGSprite("zelda/logMonster.sleeping", 2, 2, this, new RegionOfInterest(0, 32 * i, 32, 32), new Vector(-0.5f, 0.2f));
        }

        animationSleeping = new Animation(8, spriteSleeping);


        //Waking Up
        for (int i = 0; i < 3; i++) {
            spriteWakingUp[i] = new RPGSprite("zelda/logMonster.wakingUP", 2, 2, this, new RegionOfInterest(0, 32 * i, 32, 32), new Vector(-0.5f, 0.2f));
        }
        animationWakingUp = new Animation(8, spriteWakingUp, false);


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!isDead()) {
            switch (currentState) {
                case IDLE:
                case INACTIVITY:
                case ATTACK:
                    animationLogMonster[this.getOrientation().ordinal()].draw(canvas);
                    break;
                case WAKE_UP:
                    animationWakingUp.draw(canvas);
                    break;
                case SLEEPING:
                    animationSleeping.draw(canvas);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!isDead()) {
            switch (currentState) {
                case IDLE:

                    inactivityTime = 0;
                    if (idleTime == 0) {
                        idleTime = randomNumberBounded(5, 5);
                    }
                    if (!isDisplacementOccurs()) {
                        moveRandom(15);
                    }
                    animationLogMonster[this.getOrientation().ordinal()].update(deltaTime);
                    idleTime -= deltaTime;
                    if (idleTime < 0) {
                        currentState = STATE.INACTIVITY;
                        previousState = STATE.IDLE;
                    }
                    break;
                case ATTACK:
                    animationLogMonster[this.getOrientation().ordinal()].update(deltaTime);

                    if (canDamage)
                        currentState =STATE.SLEEPING;
                    if (!isDisplacementOccurs() && !move(5)) {
                        canDamage = true;

                    }
                    break;
                case INACTIVITY:
                    idleTime = 0;
                    if (inactivityTime == 0) {
                        inactivityTime = randomNumberBounded(5, 5);
                    }
                    animationLogMonster[this.getOrientation().ordinal()].reset();

                    inactivityTime -= deltaTime;
                    if (inactivityTime < 0 && (previousState == STATE.IDLE || previousState == STATE.WAKE_UP)) {
                        currentState = STATE.IDLE;
                    }

                    break;
                case SLEEPING:
                    inactivityTime = 0;
                    if (sleepingTime == 0) {
                        sleepingTime = randomNumberBounded(5, 5);
                    }
                    sleepingTime -= deltaTime;
                    animationSleeping.update(deltaTime);

                    if (sleepingTime < 0) {
                        currentState = STATE.WAKE_UP;
                    }

                    break;
                case WAKE_UP:

                    sleepingTime = 0;
                    animationWakingUp.update(deltaTime);
                    //
                    canAttack = true;
                    //
                    if (animationWakingUp.isCompleted()) {
                        animationWakingUp.reset();
                        currentState = STATE.INACTIVITY;
                        previousState = STATE.WAKE_UP;
                    }
                    break;
            }
        }

        if (getHp() <= 0) {
            this.die();
        }


    }




    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> coord = new ArrayList<>();

        if (currentState == STATE.IDLE || currentState == STATE.INACTIVITY) {

            for (int i = 0; i < 8; i++) {
                coord.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector().mul(i+1)));
            }
            return coord;
        }else if (currentState == STATE.ATTACK){
             return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        }
        return null;
    }


    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return currentState == STATE.ATTACK || currentState == STATE.IDLE || currentState == STATE.INACTIVITY;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);


    }

    @Override
    public boolean takeCellSpace() {
        return true;
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

    class ARPGLogMonsterHandler implements ARPGInteractionVisitor {

        public void interactWith(Door door) {

        }

        public void interactWith(Grass g) {


        }

        @Override
        public void interactWith(ARPGPlayer player) {

            if (currentState == STATE.IDLE || currentState == STATE.INACTIVITY) {
                currentState = STATE.ATTACK;
                canDamage=false;
            }
               else if (!isDisplacementOccurs() && canDamage) {
                    player.setHp(player.getHp() - 2);
                    currentState=STATE.SLEEPING;
               }
            }



        @Override
        public void interactWith(Bomb b) {

        }
    }
}
