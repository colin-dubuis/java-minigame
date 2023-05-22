package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Doctor;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class PetalburgTimmy extends ARPGArea {
    @Override
    public String getTitle() {
        return "PetalburgTimmy";
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        //initialisation DOOR
        registerActor(new Door("zelda/Ferme",new DiscreteCoordinates(6,10), Logic.TRUE,this, Orientation.RIGHT,new DiscreteCoordinates(3,0), new DiscreteCoordinates(4,0)));
        registerActor(new Doctor(this, Orientation.DOWN, new DiscreteCoordinates(9, 5)));
    }
}
