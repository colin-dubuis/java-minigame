package ch.epfl.cs107.play.game.arpg.actor;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.InventoryItem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

public enum ARPGItem implements InventoryItem {

        Sword("Sword", 0, 10, "zelda/sword.icon"),
        Arrow("Arrow", 0, 1, "zelda/sword.icon"),
        Staff("Staff", 0, 10, "zelda/staff_water.icon"),
        Bomb("Bomb", 0, 3, "zelda/bomb"),
        Bow("Bow", 0, 10, "zelda/bow.icon"),
        CastleKey("Castle Key", 0, 100, "zelda/key"),
        Void("Void", 0, 100, null);

    protected String name;
    protected float poids;
    protected int prix;
    protected String spritename;

    ARPGItem(String name, float poids, int prix, String spritename){
            this.name = name;
            this.poids = poids;
            this.prix = prix;
            this.spritename = spritename;
    }

    public float getPoids(){
        return poids;
    }

    /**
     * Uses the item (each type of item is to be used differently)
     *
     * @param holder (ARPGPlayer): Holder of the item
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the coin in the Area. Not null
     */
    public boolean use(ARPGPlayer holder, Area area, Orientation orientation){
        boolean success = false;
        switch (this){
            case Bomb: {
                List<DiscreteCoordinates> coordinates;
                DiscreteCoordinates playerMainCell;
                if(orientation == Orientation.DOWN){
                    playerMainCell = holder.getCurrentCells().get(0).down();
                    coordinates = Collections.singletonList(playerMainCell);
                    if (area.canEnterAreaCells(holder, coordinates)) {
                            success = area.registerActor(new Bomb(area, Orientation.UP, playerMainCell));
                    }
                }
                if(orientation == Orientation.LEFT){
                    playerMainCell = holder.getCurrentCells().get(0).left();
                    coordinates = Collections.singletonList(playerMainCell);
                    if (area.canEnterAreaCells(holder, coordinates)) {
                            success = area.registerActor(new Bomb(area, Orientation.UP, playerMainCell));
                    }
                }
                if(orientation == Orientation.UP){
                    playerMainCell = holder.getCurrentCells().get(0).up();
                    coordinates = Collections.singletonList(playerMainCell);
                    if (area.canEnterAreaCells(holder, coordinates)) {
                            success = area.registerActor(new Bomb(area, Orientation.UP, playerMainCell));
                    }
                }
                if(orientation == Orientation.RIGHT){
                    playerMainCell = holder.getCurrentCells().get(0).right();
                    coordinates = Collections.singletonList(playerMainCell);
                    if (area.canEnterAreaCells(holder, coordinates)) {
                            success = area.registerActor(new Bomb(area, Orientation.UP, playerMainCell));
                    }
                }
            }
            default: {
            }
        }
        return success;
    }

}

