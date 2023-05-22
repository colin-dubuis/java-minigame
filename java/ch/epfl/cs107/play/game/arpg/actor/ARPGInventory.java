package ch.epfl.cs107.play.game.arpg.actor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Inventory;
import ch.epfl.cs107.play.game.rpg.actor.InventoryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ARPGInventory extends Inventory {
    private int fortune = 0;
    private int money = 0;
    private List<ARPGItem> items = new ArrayList<ARPGItem>();
    private ARPGItem currentItem;
    private int currentIndex = 1;
    private ARPGPlayer Holder;
    private ARPGItem itemCache;

    /**
     * Update the player's fortune
     *
     */
    public void updateFortune() {
        int tempFortune = 0;
        for (InventoryItem item : inventory.keySet()) {
            tempFortune += item.prix;
        }
        fortune = tempFortune;
    }

    public ARPGItem getItemCache(){
        return itemCache;
    }

    public void updateItemCache(){
        itemCache = currentItem;
    }

    public int getFortune(){
        return fortune;
    }

    public ARPGItem getCurrentItem(){
        return currentItem;
    }
    /**
     * Sets the next item in the items list as the currentItem. If currentItem is the last item of the list,
     * sets the first item of the items list as the currentItem.
     * If the list is empty, sets Void as the currentItem.
     *
     */
    public void nextItem(){
        itemCache = currentItem;
        if(!items.isEmpty()) {
            ListIterator<ARPGItem> itr = items.listIterator(currentIndex);
            if (itr.hasNext()) {
                currentItem = itr.next();
                ++currentIndex;
            } else {
                currentIndex = 1;
                nextItem(0);
            }
        } else {
            currentItem = ARPGItem.Void;
        }
    }

    /**
     * Set the item that has the next index in the items list as the currentItem. If the list is empty,
     * sets Void as the currentItem.
     *
     *  @param index       (index):
     */
    public void nextItem(int index){
        itemCache = currentItem;
        if(!items.isEmpty()) {
            ListIterator<ARPGItem> itr = items.listIterator(index);
            if (itr.hasNext()) {
                currentItem = itr.next();
                currentIndex = index + 1;
            }
        } else {
            currentItem = ARPGItem.Void;
        }
    }

    /**
     * Adds a given quantity of a given item to the inventory. Returns true if success. Updates the fortune.
     *
     * @param item       (ARPGItem): Item to remove of the inventory
     * @param number    (int): Quantity to add
     */
    protected boolean addItem(ARPGItem item, int number) {
        boolean success;
        boolean alreadyInInventory = true;
        if(!isItemInInventory(item)){
            alreadyInInventory = false;
        }
        success = super.addItem(item, number);
        if(item != ARPGItem.Arrow){
           if(items.isEmpty()){
              currentItem = item;
           }
           if(success && !alreadyInInventory){
             items.add(item);
           }
        }
        updateFortune();
        return success;
    }

    /**
     * Uses the currentItem and removes it from the inventory (except for a CastleKey).
     *
     * @param area       (Area): Area of the Holder
     * @param orientation    (Orientation): Orientation of the Holder
     */
    protected void useItem(Area area, Orientation orientation){
        if(currentItem.use(Holder, area, orientation) && currentItem != ARPGItem.CastleKey) {
            if(removeItem(currentItem, 1)){
                if(!isItemInInventory(currentItem)) {
                    if (items.size() >= currentIndex) {
                        nextItem(currentIndex - 1);
                    } else if(items.size() > 1){
                        nextItem(currentIndex - 2);
                    } else {
                        nextItem(0);
                    }
                }
            }
        }
    }

    /**
     * Removes a given quantity of a given item to the inventory. Returns true if success, false if the item was
     * not present in the given quantity in the inventory. Updates the fortune.
     *
     * @param item       (ARPGItem): Item to remove of the inventory
     * @param number    (int): Quantity to remove
     */
    protected boolean removeItem(ARPGItem item, int number) {
        boolean success;
        success = super.removeItem(item, number);
        if(success && !isItemInInventory(item) && item != ARPGItem.Arrow){
            items.remove(item);
        }
        updateFortune();
        return success;
    }

    protected boolean addItem(InventoryItem item) {
        return addItem(item, 1);
    }

    protected boolean removeItem(InventoryItem item) {
        return removeItem(item, 1);
    }

    public int getMoney() {
        return money;
    }

    /**
     * Adds an amount of money to the inventory. If the new value exceeds 999, sets the amount of money to 999.
     *
     * @param a        (int): Amount of money to add to the inventory
     */
    protected void addMoney(int a) {
        if(money + a < 1000){
            money += a;
        } else {
            money = 999;
        }
    }

    /**
     * Default ARPGInventory constructor
     *
     * @param money       (int): Initial amount of money
     * @param poidsMax (float): Max allowed weight the Holder can carry in the inventory
     * @param Holder (ARPGPlayer): Holder of the inventory
     */
    public ARPGInventory(int money, float poidsMax, ARPGPlayer Holder) {
        this.money = money;
        this.setPoidsMax(poidsMax);
        this.Holder = Holder;
    }

}
