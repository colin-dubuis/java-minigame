package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Doctor extends AreaEntity {

    private RPGSprite sprite;
    private boolean inDialog = false;
    private Graphics dialog1 = new Dialog("Salut !!", "zelda/dialog", getOwnerArea());
    private Graphics dialog2 = new Dialog("L'autre jour, j'ai cru voir quelque chose derrière la cascade", "zelda/dialog", getOwnerArea());
    private Graphics dialog3 = new Dialog("Tu devrais aller y jeter un oeil", "zelda/dialog", getOwnerArea());
    private int dialogCounter = 0;
    protected final int DialogMax = 3;

    /**
     * Default Doctor constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Doctor(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        sprite = new RPGSprite("flora", 1, 1.2f, this, new RegionOfInterest(0, 0, 16, 21));
    }

    /**
     * Draws the doctor as well as the current dialog window if the player is talking with the doctor.
     *
     * @param canvas       (Canvas): Target, not null
     *
     */
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        if(inDialog && dialogCounter == 1){
            dialog1.draw(canvas);
        }
        if(inDialog && dialogCounter == 2){
            dialog2.draw(canvas);
        }
        if(inDialog && dialogCounter == 3){
            dialog3.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
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
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /**
     * If the current dialog window is not the last one, set the next dialog window as the current dialog window.
     * Else, exit the dialog.
     *
     */
    public void Dialog(){
        if(dialogCounter < DialogMax){
            ++ dialogCounter;
            inDialog = true;
        } else {
            dialogCounter = 0;
            inDialog = false;
        }
    }

    protected int getDialogCounter(){
        return dialogCounter;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
}
