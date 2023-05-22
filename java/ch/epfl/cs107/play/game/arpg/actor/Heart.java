package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Heart extends CollectableAreaEntity {
    private RPGSprite[] tab = new RPGSprite[4];
    private Animation heart;

    /**
     * Default Coin constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the coin in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the coin in the Area. Not null
     */
    public Heart(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        tab[0] = new RPGSprite("zelda/heart", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
        tab[1] = new RPGSprite("zelda/heart", 1, 1, this, new RegionOfInterest(16, 0, 16, 16));
        tab[2] = new RPGSprite("zelda/heart", 1, 1, this, new RegionOfInterest(32, 0, 16, 16));
        tab[3] = new RPGSprite("zelda/heart", 1, 1, this, new RegionOfInterest(48, 0, 16, 16));
        heart = new Animation(4, tab, true);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        heart.update(deltaTime);
    }

    public void draw(Canvas canvas){
        heart.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    public void collect(){
        getOwnerArea().unregisterActor(this);
    }
}
