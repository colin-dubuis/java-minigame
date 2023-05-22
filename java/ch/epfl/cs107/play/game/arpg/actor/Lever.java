package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Lever extends AreaEntity {

    private Logic state;
    private RPGSprite leverOn;
    private RPGSprite leverOff;

    /**
     * Default Lever constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the lever in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the lever in the Area. Not null
     * @param signal (Logic): LogicGate signal the lever outputs
     */

    public Lever(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
        super(area, orientation, position);
        state = signal;
        leverOn = new RPGSprite("LeverDown", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
        leverOff = new RPGSprite("LeverUp", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
    }

    /**
     * Draws the lever in difference states according to the output
     *
     * @param canvas           (Canvas): Target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if(getOutput()){
            leverOn.draw(canvas);
        } else {
            leverOff.draw(canvas);
        }
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

    /**
     * Returns the current output of the lever
     *
     */
    public boolean getOutput() {
        return state.isOn();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    /**
     * Toggles the output of the lever
     *
     */
    protected void toggleLever(){
        if(getOutput()){
            state = Logic.FALSE;
        } else {
            state = Logic.TRUE;
        }
    }
}
