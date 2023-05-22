package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class RouteTemple extends ARPGArea {
    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door("zelda/route",new DiscreteCoordinates(18,10), Logic.TRUE,this, Orientation.UP,new DiscreteCoordinates(0,4),new DiscreteCoordinates(0,5),new DiscreteCoordinates(0,6)));
        registerActor(new Door("zelda/Temple",new DiscreteCoordinates(4,1), Logic.TRUE,this, Orientation.UP,new DiscreteCoordinates(5,6)));
    }

    @Override
    public String getTitle() {
        return "zelda/RouteTemple";
    }
}
