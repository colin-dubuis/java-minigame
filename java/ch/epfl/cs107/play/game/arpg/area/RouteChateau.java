package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.FireSpell;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class RouteChateau extends ARPGArea {

    @Override
    public String getTitle() {
        return "zelda/RouteChateau";
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        //initialisation DOOR
        registerActor(new Door("zelda/route",new DiscreteCoordinates(9,18), Logic.TRUE ,this, Orientation.DOWN,new DiscreteCoordinates(9,0),new DiscreteCoordinates(10,0)));
        registerActor(new CastleDoor("zelda/Chateau",new DiscreteCoordinates(7,1),Logic.FALSE,this, Orientation.DOWN,new DiscreteCoordinates(9,13), new DiscreteCoordinates(10,13)));

        //dark lord


    }
}

