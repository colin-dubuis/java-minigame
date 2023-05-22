package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.*;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Route extends ARPGArea {

    @Override
    public String getTitle() {
        return "zelda/route";
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));



        //initialisation DOOR
        registerActor(new Door("zelda/Ferme",new DiscreteCoordinates(18,15), Logic.TRUE,this, Orientation.UP,new DiscreteCoordinates(0,15), new DiscreteCoordinates(0,16)));
        registerActor(new Door("zelda/Village",new DiscreteCoordinates(29,18),Logic.TRUE,this, Orientation.DOWN,new DiscreteCoordinates(9,0), new DiscreteCoordinates(10,0)));
        registerActor(new Door("zelda/RouteChateau", new DiscreteCoordinates(9, 1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(10, 19), new DiscreteCoordinates(9, 19)));
        registerActor(new Door("zelda/RouteTemple",new DiscreteCoordinates(1,4), Logic.TRUE,this, Orientation.UP,new DiscreteCoordinates(19, 11), new DiscreteCoordinates(19, 10), new DiscreteCoordinates(19, 9), new DiscreteCoordinates(19, 8)));


        //flameskull test
        registerActor(new FlameSkull(this,Orientation.UP, new DiscreteCoordinates(5,5)));
        //logMonster test
        registerActor(new LogMonster(this,Orientation.UP,new DiscreteCoordinates(5,4)));
        //darkLord test
        registerActor(new DarkLord(this,Orientation.UP,new DiscreteCoordinates(10,10)));
        //projectile test
        //registerActor(new FireSpell(this,Orientation.UP,new DiscreteCoordinates(2,3)));

        //waterfall
        registerActor(new WaterFall(this,Orientation.UP, new DiscreteCoordinates(15,4)));

        //Grass
        for (int i = 5; i < 8; ++i){
            for (int j = 6; j < 12; ++j) {
                registerActor(new Grass(this, Orientation.UP, new DiscreteCoordinates(i, j)));
            }
        }
    }


}
