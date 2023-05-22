package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.*;
import ch.epfl.cs107.play.game.rpg.RPG;

import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG {

    private ARPGPlayer player;



    @Override
    public String getTitle() {
        return "ZeldIC";
    }

    private void createAreas(){

        addArea(new Ferme());
        addArea(new Village());
        addArea(new Route());
        addArea(new PetalburgTimmy());
        addArea(new RouteChateau());
        addArea(new Chateau());
        addArea(new RouteTemple());
        addArea(new Temple());

    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {

        if (super.begin(window, fileSystem)){
            createAreas();

            Area area = setCurrentArea("zelda/route", true);
            player = new ARPGPlayer(area, Orientation.DOWN,new DiscreteCoordinates(4,10), "zelda/player", 123, 100);

            initPlayer(player);
        }


        return true;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }


}
