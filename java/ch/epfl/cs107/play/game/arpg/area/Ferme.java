package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleKey;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Ferme extends ARPGArea {


    @Override
    public String getTitle() {
        return "zelda/Ferme";
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        //initialisation DOOR
        registerActor(new Door("zelda/route",new DiscreteCoordinates(1,15), Logic.TRUE,this, Orientation.RIGHT,new DiscreteCoordinates(19,15), new DiscreteCoordinates(19,16)));
        registerActor(new Door("zelda/Village",new DiscreteCoordinates(4,18),Logic.TRUE,this, Orientation.DOWN,new DiscreteCoordinates(4,0), new DiscreteCoordinates(5,0)));
        registerActor(new Door("zelda/Village",new DiscreteCoordinates(14,18),Logic.TRUE,this, Orientation.DOWN,new DiscreteCoordinates(13,0), new DiscreteCoordinates(14,0), new DiscreteCoordinates(15, 0)));
        registerActor(new Door("PetalburgTimmy",new DiscreteCoordinates(4,1),Logic.TRUE,this, Orientation.DOWN,new DiscreteCoordinates(6,11)));
    }
}
