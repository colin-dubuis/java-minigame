package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class CastleKey extends CollectableAreaEntity {

    private RPGSprite key;
    /**
     * Default CastleKey constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the CastleKey in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the CastleKey in the Area. Not null
     */
    public CastleKey(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        key = new RPGSprite("zelda/key", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        key.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    /**
     * Removes the CastleKey from the area
     *
     */
    public void collect(){
        getOwnerArea().unregisterActor(this);
    }
}
