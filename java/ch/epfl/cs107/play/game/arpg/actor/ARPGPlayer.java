package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPG;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.arpg.area.Route;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.*;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ARPGPlayer extends Player {

    private Sprite[][] sprites;
    private Animation[] animations;
    private Sprite[][] spritesSword;
    private Animation[] animationsSword;
    private Sprite[][] spritesBow;
    private Animation[] animationsBow;
    private Sprite[][] spritesStaff;
    private Animation[] animationsStaff;
    /// Animation duration in frame number
    private final static int ANIMATION_DURATION = 4;
    //interaction attribute
    private final ARPGPlayerHandler handler;
    private Orientation orientation;
    private float maxHp = 5f;
    private float hp = 5f;
    private ARPGInventory inventaire;
    private ARPGPlayerStatusGUI GUI = new ARPGPlayerStatusGUI();
    private boolean isTalking = false;
    private Area currentArea;
    private boolean bridgeOut = false;
    private Bridge bridge;
    private boolean bridgeRegistered = false;
    private boolean canAction = false;


    /**
     * Default ARPGPlayer constructor
     *
     * @param owner       (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the player in the Area. Not null
     * @param coordinates (DiscreteCoordinate): Initial position of the player in the Area. Not null
     * @param spriteName  (String): Name of the file used to display the player
     * @param money       (int): Amount of money the player has
     * @param poidsMax    (int): max weight of the player inventory
     */
    public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, int money, int poidsMax) {
        super(owner, orientation, coordinates);
        this.handler = new ARPGPlayerHandler();
        currentArea = owner;

        sprites = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32, new Vector(0, 0), new Orientation[]{Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
        animations = RPGSprite.createAnimations(ANIMATION_DURATION / 2, sprites);

        spritesSword = RPGSprite.extractSprites("zelda/player.sword", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0), new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        animationsSword = RPGSprite.createAnimations(3, spritesSword, false);

        spritesBow = RPGSprite.extractSprites("zelda/player.bow", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0), new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        animationsBow = RPGSprite.createAnimations(3, spritesBow, false);

        spritesStaff = RPGSprite.extractSprites("zelda/player.staff_water", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0), new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        animationsStaff = RPGSprite.createAnimations(3, spritesStaff, false);
        this.orientation = orientation;

        inventaire = new ARPGInventory(money, poidsMax, this);

        inventaire.addItem(ARPGItem.Bomb, 3);
        inventaire.addItem(ARPGItem.Sword, 1);
        inventaire.addItem(ARPGItem.Bow, 1);
        inventaire.addItem(ARPGItem.Staff,1);
        inventaire.addItem(ARPGItem.Arrow,3);


        inventaire.nextItem(0);


        resetMotion();
        bridge = new Bridge(owner, Orientation.DOWN, new DiscreteCoordinates(16, 10), Logic.FALSE);
        if (getOwnerArea() instanceof Route) {
            owner.registerActor(bridge);
            bridgeRegistered = true;
        }

    }

    STATE currentState = STATE.IDLE;

    enum STATE {
        IDLE,
        SWORD,
        STAFF,
        BOW,
        BOMB;
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();

        inventaire.updateItemCache();

        super.update(deltaTime);

        useItem();
        nextItem();

        Button key2 = keyboard.get(Keyboard.SPACE);

        if (!bridgeRegistered && getOwnerArea() instanceof Route) {
            bridgeRegistered = true;
            getOwnerArea().registerActor(bridge);
        }

        switch (currentState) {
            case IDLE:

                if (!isTalking) {
                    moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
                    moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
                    moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
                    moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
                }
                canAction = false;
                animationsBow[this.getOrientation().ordinal()].reset();
                animationsSword[this.getOrientation().ordinal()].reset();
                animationsStaff[this.getOrientation().ordinal()].reset();
                if (isDisplacementOccurs()) {
                    animations[this.getOrientation().ordinal()].update(deltaTime);
                } else {
                    animations[this.getOrientation().ordinal()].reset();
                }

                if (key2.isPressed() && inventaire.getCurrentItem() == inventaire.getItemCache()) {
                    if (inventaire.getCurrentItem() == ARPGItem.Sword) {
                        currentState = STATE.SWORD;
                    } else if (inventaire.getCurrentItem() == ARPGItem.Bow) {
                        currentState = STATE.BOW;
                    }else if(inventaire.getCurrentItem()==ARPGItem.Bomb){
                        currentState = STATE.BOMB;
                    }else if(inventaire.getCurrentItem()==ARPGItem.Staff){
                        currentState = STATE.STAFF;
                    }
                }
                break;
            case SWORD:
                canAction = true;
                animationsSword[this.getOrientation().ordinal()].update(deltaTime);
                if (animationsSword[this.getOrientation().ordinal()].isCompleted()) {

                    currentState = STATE.IDLE;
                }
                break;
            case STAFF:
                animationsStaff[this.getOrientation().ordinal()].update(deltaTime);
                if (animationsStaff[this.getOrientation().ordinal()].isCompleted()){
                    getOwnerArea().registerActor(new MagicWaterProjectile(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates()));
                    currentState = STATE.IDLE;
                }
                break;
            case BOW:
                animationsBow[this.getOrientation().ordinal()].update(deltaTime);
                if (animationsBow[this.getOrientation().ordinal()].isCompleted() ) {
                    if (inventaire.isItemInInventory(ARPGItem.Arrow,1)) {
                        inventaire.removeItem(ARPGItem.Arrow, 1);
                        getOwnerArea().registerActor(new Arrow(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates()));
                    }
                    currentState = STATE.IDLE;
                }
                break;
            case BOMB:

                    inventaire.useItem(this.getOwnerArea(), this.orientation);
                    currentState = STATE.IDLE;

                break;
        }
    }

    /**
     * Orientate or Move this player in the given orientation if the given button is pressed down
     *
     * @param orientation (Orientation): given orientation, not null
     * @param b           (Button): button corresponding to the given orientation, not null
     */
    private void moveOrientate(Orientation orientation, ch.epfl.cs107.play.window.Button b) {

        if (b.isDown()) {
            if (getOrientation() == orientation) {
                move(ANIMATION_DURATION);
            } else {
                orientate(orientation);
                this.orientation = orientation;
            }
        }
    }


    @Override
    public void draw(Canvas canvas) {


        GUI.draw(canvas, inventaire.getCurrentItem(), hp, inventaire.getMoney(), maxHp);

        switch (currentState) {
            case BOW:
                animationsBow[this.getOrientation().ordinal()].draw(canvas);
                break;
            case STAFF:
                animationsStaff[this.getOrientation().ordinal()].draw(canvas);
                break;
            case SWORD:
                animationsSword[this.getOrientation().ordinal()].draw(canvas);
                break;
            case BOMB:
            case IDLE:
                animations[this.getOrientation().ordinal()].draw(canvas);
                break;
        }
    }


    /**
     * If E is pressed, call the useItem method of the inventory
     */
    private void useItem() {
        Keyboard Keyboard = getOwnerArea().getKeyboard();
        Button key2 = Keyboard.get(Keyboard.SPACE);
        if (key2.isPressed()) {
            inventaire.useItem(this.getOwnerArea(), this.orientation);
        }
    }

    /**
     * If TAB is pressed, call the nextItem method of the inventory
     */
    private void nextItem() {
        Keyboard Keyboard = getOwnerArea().getKeyboard();
        Button key = Keyboard.get(Keyboard.TAB);
        if (key.isPressed()) {
            inventaire.nextItem();
        }
    }

    public void damage(float hit){
        this.setHp(Math.max(0, getHp()-hit));
    }


    public float getHp() {
        return hp;
    }

    protected void setHp(float hp) {
        this.hp = hp;
    }

    /**
     * Check if player has given item in his inventory
     *
     * @param item (ARPGItem): Given item
     */
    public boolean posses(ARPGItem item) {
        return inventaire.isItemInInventory(item);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    //
    @Override
    public void interactWith(Interactable other) {
        //to accept
        other.acceptInteraction(handler);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        //general method
        ((ARPGInteractionVisitor) v).interactWith(this);

    }


    class ARPGPlayerHandler implements ARPGInteractionVisitor {

        /**
         * Makes the player cross the door.
         *
         * @param door (Door): Door located in the same cell the player is in
         */
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }


        /**
         * Checks if the key E is pressed down, and slices the grass if it is the case.
         *
         * @param g (Grass): Grass in the cell the player is looking at
         */
        public void interactWith(Grass g) {
            switch (currentState) {
                case IDLE:
                    Keyboard keyboard = getOwnerArea().getKeyboard();
                    Button key = keyboard.get(Keyboard.E);
                    if (key.isPressed()) {
                        g.slice();
                    }
                    break;
                case SWORD:
                    if (canAction)
                        g.slice();
                    break;
            }
        }

        public void interactWith(Bomb b) {
        }

        /**
         * Collects the coin (removes it from the area) and add the corresponding amount to the inventory.
         *
         * @param c (Coin): Collected coin
         */
        public void interactWith(Coin c) {
            c.collect();
            inventaire.addMoney(c.coinValue);
        }

        /**
         * Collects the heart (removes it from the area) and adds one heart to the player health, or set it to max if his
         * health was already almost full.
         *
         * @param h (Heart): Collected heart
         */
        public void interactWith(Heart h) {
            h.collect();



                if (hp > maxHp - 1) {
                    hp = maxHp;

                } else {
                    hp = hp + 1;
                }

        }

        /**
         * Collects key (removes it from the area) and adds it to the inventory
         *
         * @param key (CastleKey): Collected key
         */
        public void interactWith(CastleKey key) {
            key.collect();
            inventaire.addItem(ARPGItem.CastleKey, 1);
        }

        /**
         * Checks if the door is open. If it is, allows the player to cross the door and closes it behind him.
         * Else, check if the E key is pressed and if the player has a CastleKey, and open the door if both conditions are met.
         * If the player doesn't have a key and the key E is pressed, toggle a dialog.
         *
         * @param door (CastleDoor): Door the player is interacting with
         */
        public void interactWith(CastleDoor door) {
            if (door.isOpen()) {
                setIsPassingADoor(door);
                door.setSignal(Logic.FALSE);
            }
            Keyboard keyboard = getOwnerArea().getKeyboard();
            Button key = keyboard.get(Keyboard.E);
            if (key.isPressed() && inventaire.isItemInInventory(ARPGItem.CastleKey)) {
                door.setSignal(Logic.TRUE);
            }
            if (key.isPressed() && !inventaire.isItemInInventory(ARPGItem.CastleKey)) {
                isTalking = !isTalking;
                door.toggleDialog();
            }
        }

        @Override
        public void interactWith(FlameSkull flameSkull) {
        }

        public void interactWith(Doctor d) {
            Keyboard keyboard = getOwnerArea().getKeyboard();
            Button key = keyboard.get(Keyboard.E);
            if (key.isPressed()) {
                if (d.getDialogCounter() < d.DialogMax) {
                    isTalking = true;
                    d.Dialog();
                } else {
                    isTalking = false;
                    d.Dialog();
                }
            }
        }

        /**
         * If the key E is pressed, toggle the lever and toggle the bridge status
         *
         * @param l (Lever): Lever the player is interacting with
         */
        public void interactWith(Lever l) {
            Keyboard keyboard = getOwnerArea().getKeyboard();
            Button key = keyboard.get(Keyboard.E);
            if (key.isPressed()) {
                l.toggleLever();
                bridge.toggleStatus();
            }
        }

        @Override
        public void interactWith(LogMonster logMonster) {

        }
    }
}





