package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;

import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class Bomb extends AreaEntity implements Interactor {
    protected float timer;
    private TextGraphics message;
    //animation bomb
    private Animation animations;
    private Animation animationExplosion;
    //sprite bomb
    private Sprite[] spriteBomb = new Sprite[2];
    private Sprite[] spriteBombExplosion = new Sprite[7];
    private final static int ANIMATION_DURATION = 8;
    //handler
    private ARPGBombHandler handler;
    private boolean exploded = false;

    /**
     * Default Bomb constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the bomb in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the bomb in the Area. Not null
     */

    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {

        super(area, orientation, position);


        //handler
        this.handler = new ARPGBombHandler();
        //text msg

        message = new TextGraphics(Integer.toString((int) timer), 0.4f, Color.RED);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));

        //timer

        timer = 2;


        //sprite bomb
        spriteBomb[0] = new RPGSprite("zelda/bomb", 1, 1, this, new RegionOfInterest(0, 0, 16, 16),new Vector(0,0.25f));
        spriteBomb[1] = new RPGSprite("zelda/bomb", 1, 1, this, new RegionOfInterest(16, 0, 16, 16), new Vector(0,0.25f));
        animations = new Animation(5,spriteBomb);

        //sprite bomb explosion
        for (int i =0; i<7;i++){
            spriteBombExplosion[i] = new RPGSprite("zelda/explosion",2,2,this, new RegionOfInterest(32*i,0,32,32), new Vector(-0.5f, 0f));
        }

        animationExplosion = new Animation(2,spriteBombExplosion, false);



    }

    @Override
    public void draw(Canvas canvas) {


        if (timer > 0) {
            animations.draw(canvas);

        } else if (!animationExplosion.isCompleted()) {
            animationExplosion.draw(canvas);

        }




    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (timer > 0) {
            timer -= deltaTime;
            animations.update(deltaTime);

        } else if (timer <= 0) {
            animationExplosion.update(deltaTime);
            exploded = true;

            if (animationExplosion.isCompleted()) {
                getOwnerArea().unregisterActor(this);
            }


        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {

        return getCurrentMainCellCoordinates().getNeighbours();

    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
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
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);

    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);

    }



    @Override
    public void bip(Audio audio) {

    }


    class  ARPGBombHandler implements ARPGInteractionVisitor {

        public void interactWith(Door door) {

        }

        public void interactWith(Grass g) {

            if (timer <= 0) {
                g.slice();
            }
        }

        public void interactWith(ARPGPlayer player){

            if(timer <= 0 && !exploded){
                player.setHp(player.getHp() - 2);
            }
        }

        @Override
        public void interactWith(FlameSkull flameSkull) {
            //the interaction kill the flameSkull
            if(timer <= 0 && !exploded){
                flameSkull.die();
            }
        }

        @Override
        public void interactWith(LogMonster logMonster) {
            if(timer <= 0 && !exploded) {
                logMonster.setHp(logMonster.getHp() - 3f);
            }
        }
    }
}
