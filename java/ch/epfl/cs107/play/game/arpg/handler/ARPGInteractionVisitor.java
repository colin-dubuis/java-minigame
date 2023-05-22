package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.actor.*;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;


import javax.print.Doc;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {

    //default method to interact with a cell
    default void interactWith(ARPGBehavior.ARPGCell arpgCell){
        // by default the interaction is empty
    }

    //default method to interact with a player
    default void interactWith(ARPGPlayer arpgPlayer){
        // by default the interaction is empty
    }

    //Monster

    default void interactWith(FlameSkull flameSkull){

    }

    default void interactWith(LogMonster logMonster){

    }
    default void interactWith(DarkLord darkLord){

    }



    //interactible

    default void interactWith(Grass g){
    }

    default void interactWith(Door g){
    }

    //items

    default void interactWith(Bomb b){
    }

    default void interactWith(Coin c){
    }

    default void interactWith(Heart h){
    }

    default void interactWith(CastleKey key){
    }

    default void interactWith(CastleDoor door){
    }

    default void interactWith(Doctor a){
    }

    default void interactWith(Lever l){
    }

}
