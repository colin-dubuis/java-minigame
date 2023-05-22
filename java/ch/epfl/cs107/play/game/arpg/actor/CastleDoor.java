package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door {

    private RPGSprite openDoor;
    private RPGSprite closedDoor;
    private Graphics dialog = new Dialog("Il vous faut une CastleKey pour déverrouiller la porte !", "zelda/dialog", getOwnerArea());
    private boolean inDialog = false;

    /**
     * Default CastleDoor constructor
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param signal (Logic): LogicGate signal opening the door, may be null
     * @param area        (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     */
    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates... otherCells) {
        super(destination, otherSideCoordinates, signal, area, orientation, position, otherCells);
        openDoor = new RPGSprite("zelda/castleDoor.open", 2, 2, this, new RegionOfInterest(0, 0, 32, 32));
        closedDoor = new RPGSprite("zelda/castleDoor.close", 2, 2, this, new RegionOfInterest(0, 0, 32, 32));
    }

    /**
     * If the door is open, draws the openDoor sprite. Else draw the closedDoor sprite.
     *
     * @param canvas  (Canvas): Target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(isOpen()){
            openDoor.draw(canvas);
        } else {
            closedDoor.draw(canvas);
        }
        if(inDialog){
            dialog.draw(canvas);
        }
    }

    /**
     * If the door is open, set takes cell space to true, else set it to false.
     *
     */
    @Override
    public boolean takeCellSpace() {
        if(isOpen()){
            return false;
        } else {
            return true;
        }
    }

    protected void toggleDialog(){
        inDialog = !inDialog;
    }

    /**
     * Set the signal (opening state) of the CastleDoor
     *
     * @param signal  (Sigal): State of the door (open or closed)
     */
    @Override
    protected void setSignal(Logic signal) {
        super.setSignal(signal);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    /**
     * If the door is open, makes the door ViewInteractable. Else, makes it not ViewInteractable.
     *
     */
    @Override
    public boolean isViewInteractable() {
        if(isOpen()){
            return false;
        } else {
            return true;
        }
    }
}
