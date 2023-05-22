package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Monster extends MovableAreaEntity implements Interactor {
    private float hp;
    Sprite[] spriteMonsterDead = new Sprite[7];


    //boolean to say if the Monster should be dead or not
    private boolean isDead = false;

    private Animation animationDead;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Monster(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.setHp(getHp());

        for (int i =0; i<7;i++){
            spriteMonsterDead[i] = new RPGSprite("zelda/vanish",2,2,this, new RegionOfInterest(32*i,0,32,32), new Vector(-0.5f,0));
        }

        animationDead = new Animation(2, spriteMonsterDead, false);


    }
    /**
     * method to set the monster at dead -> unregister the monster of the area
     */
    protected void die(){
       isDead = true;

    }
    protected float randomNumberBounded(int MIN, int MAX){
        return RandomGenerator.getInstance().nextInt(MIN)+MAX;
    }
    /**
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param speed (int): movement speed
     */
    protected void moveOrientate(Orientation orientation,int speed) {
        //if monster is not dead can move on the area with a speed
        if(!isDead() && !isDisplacementOccurs()) {
            if (getOrientation().equals(orientation)) {
                move(speed);
            } else {
                orientate(orientation);

            }
        }
    }
    /**
     *method for the monster moves alone with 60% chance to take a switch direction
     */
    protected void moveRandom(int speed) {

        if (!isDisplacementOccurs() && !isDead) {
            float randomInt = RandomGenerator.getInstance().nextInt(10);


            //chose a new orientation
            if (randomInt <= 5) {

                    int randomDirec = RandomGenerator.getInstance().nextInt(4);
                    switch (randomDirec) {
                        case 0:
                            moveOrientate(Orientation.LEFT, speed);
                            break;
                        case 1:
                            moveOrientate(Orientation.UP, speed);
                            break;
                        case 2:
                            moveOrientate(Orientation.RIGHT, speed);
                            break;
                        case 3:
                            moveOrientate(Orientation.DOWN, speed);
                            break;
                    }
                    //other keep the same direction
                } else {
                    moveOrientate(getOrientation(), speed);
                    ;
                }
            }

        }





    @Override
    public void draw(Canvas canvas) {

       if(isDead()){
           animationDead.draw(canvas);
       }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //if monster is dead sprite of vanish has to be update
        if(isDead()){
            resetMotion();
            animationDead.update(deltaTime);
            //if the sprite is finished unregister the Actor
        }if (animationDead.isCompleted()) {
           getOwnerArea().unregisterActor(this);
        }
    }

    //



    protected boolean isDead() {
        return isDead;
    }


    protected void setHp(float hp) {
        this.hp = hp;
    }

    public float getHp() {
        return hp;
    }
}
