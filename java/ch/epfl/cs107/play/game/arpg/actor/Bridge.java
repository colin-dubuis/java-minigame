package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bridge extends AreaEntity{

    private RPGSprite bridge;
    protected Logic status;

    /**
     * Default Bridge constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the bridge in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the bridge in the Area. Not null
     */
    public Bridge(Area area, Orientation orientation, DiscreteCoordinates position, Logic status) {
        super(area, orientation, position);
        bridge = new RPGSprite("zelda/bridge", 5.3f, 3, this, new RegionOfInterest(0, 0, 64, 48),new Vector(-1.75f, -1f));
        this.status = status;
    }

    /**
     * Draws the bridge only if getStatus() returns true
     *
     * @param canvas           (Canvas): Target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if(getStatus()) {
            bridge.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Takes cell space only if getStatus() returns false
     *
     */
    @Override
    public boolean takeCellSpace() {
        if(getStatus()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }

    public boolean getStatus() {
        return status.isOn();
    }

    /**
     * Toggles the status of the bridge
     *
     */
    protected void toggleStatus(){
        if(getStatus()){
            status = Logic.FALSE;
        } else {
        status = Logic.TRUE;
    }
  }
}
