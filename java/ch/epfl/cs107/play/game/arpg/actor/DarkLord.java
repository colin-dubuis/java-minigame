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
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DarkLord extends Monster {
    ARPGDarkLordHandler handler;


    private Sprite[][] spriteDarklord;
    private Animation[] animationDarkLord;

    private Sprite[][] spriteDarkLordSpell;
    private Animation[] animationDarkLordSpell;

    STATE currentState = STATE.INVOCATION;

    //boolean
    boolean animationSpellForce = true;

    //time
    float idleTime = 0;



    enum STATE {
        SLEEPING,
        IDLE,
        INVOCATION,
        FLAMESTRIKE,
        TELEPORTATION;
    }


    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.handler = new ARPGDarkLordHandler();

        //sprite
        spriteDarklord = RPGSprite.extractSprites("zelda/darkLord", 3, 2, 2, this, 32, 32, new Vector(-0.55f, 0.2f), new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        animationDarkLord = RPGSprite.createAnimations(8, spriteDarklord);

        spriteDarklord = RPGSprite.extractSprites("zelda/darkLord.spell", 3, 2, 2, this, 32, 32, new Vector(-0.55f, 0.2f), new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        animationDarkLordSpell = RPGSprite.createAnimations(8, spriteDarklord, false);



    }








    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        switch (currentState){
            case INVOCATION:
                idleTime = 0;
                if(animationSpellForce) {

                    if (!isDisplacementOccurs()) {
                        animationDarkLordSpell[this.getOrientation().ordinal()].update(deltaTime);


                        if (animationDarkLordSpell[this.getOrientation().ordinal()].isCompleted()) {
                            animationDarkLordSpell[this.getOrientation().ordinal()].reset();
                            getOwnerArea().registerActor(new FlameSkull(getOwnerArea(), this.getOrientation(), this.getCurrentMainCellCoordinates()));
                            animationSpellForce = false;
                            currentState = STATE.IDLE;

                        }
                    }
                }


                break;
            case IDLE:
                animationSpellForce = true;
                if(idleTime==0) {
                    idleTime = randomNumberBounded(2, 2);
                }
                    idleTime -= deltaTime;

                if(isDisplacementOccurs()) {
                    animationDarkLord[this.getOrientation().ordinal()].update(deltaTime);
                }
                if(!isDisplacementOccurs()){
                    moveRandom(20);
                }
                if (idleTime<0){
                    currentState = STATE.INVOCATION;
                }
                break;
            case TELEPORTATION:
                animationDarkLordSpell[getOrientation().ordinal()].update(deltaTime);
                if (animationDarkLordSpell[getOrientation().ordinal()].isCompleted())
                    completeTeleportation();
                break;
        }

    }

    private void invocTeleportationSpell() {
        if (currentState == STATE.IDLE || currentState == STATE.TELEPORTATION)
            return;
        for (Animation a : animationDarkLordSpell) a.reset();
        currentState = STATE.TELEPORTATION;
    }
    private void completeTeleportation() {
        List<DiscreteCoordinates> newPos = Collections.singletonList(new DiscreteCoordinates(-1, -1));
        while (!getOwnerArea().canEnterAreaCells(this, newPos))
            newPos = Collections.singletonList(getCurrentMainCellCoordinates().jump((int) (2 * (Math.random() - .5) * 10), (int) (2 * (Math.random() - .5) * 10)));
        getOwnerArea().leaveAreaCells(this, getCurrentCells());
        getOwnerArea().enterAreaCells(this, newPos);
        setCurrentPosition(newPos.get(0).toVector());
        currentState = STATE.IDLE;

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
       switch (currentState){
           case IDLE:
               animationDarkLord[this.getOrientation().ordinal()].draw(canvas);
               break;
           case INVOCATION:
           case FLAMESTRIKE:
           case TELEPORTATION:
               animationDarkLordSpell[this.getOrientation().ordinal()].draw(canvas);
               break;
       }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> coord = new ArrayList<>();
        for (int i = -1; i<= 1; i++){
            for (int j = -1; j<=1;j++) {
                coord.add(getCurrentMainCellCoordinates().jump(i, j));
            }
        }
        return coord;
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
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
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);

    }

    class ARPGDarkLordHandler implements ARPGInteractionVisitor {

        public void interactWith(Door door) {

        }

        public void interactWith(Grass g) {


        }

        @Override
        public void interactWith(ARPGPlayer player) {
        invocTeleportationSpell();
        }

        @Override
        public void interactWith(Bomb b) {

        }
    }
}
