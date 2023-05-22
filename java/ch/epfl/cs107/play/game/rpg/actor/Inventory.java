package ch.epfl.cs107.play.game.rpg.actor;

import ch.epfl.cs107.play.game.actor.Actor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Inventory {
    private float poidsMax;
    private float curentWeight = 0;
    protected Actor Holder;
    protected Map<InventoryItem, Integer> inventory = new HashMap<>();

    /**
     * Adds a given quantity of a given item. Returns true if success.
     *
     * @param item       (InventoryItem): Item to remove of the inventory
     * @param number    (int): Quantity to add
     */
    protected boolean addItem(InventoryItem item, int number){
        boolean status = true;
        if(isItemInInventory(item)){
            inventory.put(item, inventory.get(item) + number);
        } else {
            inventory.put(item, number);
        }
        return status;
    }

    /**
     * Removes a given quantity of a given item. Returns true if success, false if the item was not present in the
     * given quantity in the inventory. Updates the fortune.
     *
     * @param item       (ARPGItem): Item to remove of the inventory
     * @param number    (int): Quantity to remove
     */
    protected boolean removeItem(InventoryItem item, int number){
        boolean status = true;
        if(isItemInInventory(item, number)){
            if(number >= inventory.get(item)){
                inventory.remove(item);
            } else {
                inventory.put(item, inventory.get(item) - number);
            }
        } else {
            status = false;
        }
        return status;
    }

    /**
     * Removes a given quantity of a given item. Returns true if success, false if the item was not present in the
     * given quantity in the inventory. Updates the fortune.
     *
     * @param item       (ARPGItem): Item to remove of the inventory
     * @param number    (int): Quantity to remove
     */
    public boolean isItemInInventory(InventoryItem item, int number){
        return inventory.containsKey(item)&&(number <= inventory.get(item));
    }

    public boolean isItemInInventory(InventoryItem item){
        return isItemInInventory(item, 1);
    }

    protected void setPoidsMax(float poidsMax){
        this.poidsMax = poidsMax;
    }
}
