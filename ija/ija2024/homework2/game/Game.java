/**
 * @author xsudato00
 */

package ija.ija2024.homework2.common;

import ija.ija2024.homework2.game.Game;
import java.util.Arrays;

import java.lang.reflect.Array;

public class GameNode {
    public enum nodeType {
        NONE, BULB, POWER, LINK
    }
    private nodeType type;
    Position position;
    public Side[] sides;
    public static int linkCount=0,bulbCount=0,powerCount =0;
    private Game game;
    public static Position LastPosition;
    public GameNode(){
        this.type=nodeType.NONE;
        this.sides = new Side[4];
    }

    public Position getPosition(){return this.position;}

    public void setGame(Game game) {
        this.game = game;
    }

    public void turn(){
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] != null) {
                // Získajte aktuálny index strany
                int currentOrdinal = sides[i].ordinal();
                // Vypočítajte nový index (otáčanie o 90 stupňov)
                int newOrdinal = (currentOrdinal + 1) % Side.values().length;
                // Nastavte novú stranu
                sides[i] = Side.values()[newOrdinal];
            }
        }
    }

    public boolean isLink(){
        return type==nodeType.LINK;
    }

    public boolean isBulb(){
        return type==nodeType.BULB;
    }

    public boolean isPower(){
        return type==nodeType.POWER;
    }

    public boolean containsConnector(Side s){
        for (Side side : sides) {
            if (side == s) {
                return true;
            }
        }
        return false;
    }

    public void setPosition(Position p){
        this.position =  new Position(p);
    }

    public void setAsBulb(Side sides){
        GameNode.bulbCount++;
        this.type = nodeType.BULB;
        this.sides[0] = sides; // Uloženie konektora
    }

    public void setAsLink(Side... sides){
        GameNode.linkCount++;
        this.type = nodeType.LINK;
        // Uloženie konektorov do poľa
        for (int i = 0; i < sides.length && i < this.sides.length; i++) {
            // this.sides[i] = sides[i];
            this.sides[sides[i].ordinal()] = sides[i];
        }
    }

    public void setAsPower(Side... sides){
        GameNode.powerCount++;
        this.type = nodeType.POWER;
        // Uloženie konektorov do poľa
        for (int i = 0; i < sides.length && i < 4; i++) {
            // this.sides[i] = sides[i];
            this.sides[sides[i].ordinal()] = sides[i];
        }

    }

    public void setAsNone(){
        this.type = nodeType.NONE;
        // Uloženie konektorov do poľa
        this.sides[0] = Side.EAST;
        this.sides[1] = Side.NORTH;
        this.sides[2] = Side.SOUTH;
        this.sides[3] = Side.WEST;
    }

    private Position moveToPosition(Side side,Position currentPos) {


        if (currentPos == null) return null;

        int newRow = currentPos.getRow();
        int newCol = currentPos.getCol();

        switch (side) {
            case NORTH -> newRow--;
            case EAST -> newCol++;
            case SOUTH -> newRow++;
            case WEST -> newCol--;
            default -> { return null; }
        }



        return new Position(newRow+1, newCol+1);
    }


    public Boolean conected( Position prevPos,Position currentPos,Side conectorSide){

        GameNode neighbor = game.node(currentPos);



        if(neighbor.isPower()) {

            boolean active_conector =false;

            for (Side side : neighbor.sides) {
                if(conectorSide!=null) {
                    switch (conectorSide) {
                        case EAST:
                            if (side == Side.WEST) {
                                active_conector = true;
                                return true;
                            }
                            break ;
                        case NORTH:
                            if (side == Side.SOUTH) {
                                active_conector = true;
                                return true;
                            }

                            break;
                        case SOUTH:
                            if (side == Side.NORTH) {
                                active_conector = true;
                                return true;
                            }
                            break;
                        case WEST:
                            if (side == Side.EAST) {
                                active_conector = true;
                                return true;
                            }
                            break;
                        default:

                            //ERROR
                            return false;

                    }
                }
                else{
                    return true;
                }

            }

            if (!active_conector) {
                return false;
            }

            return true;
        }
        else if(neighbor.isBulb() || neighbor.isLink()) {//        for (Side side : sides) {

            boolean active_conector =false;
            side_iteration:
            for (Side side : neighbor.sides) {

                if (conectorSide != null) {

                    switch (conectorSide) {
                        case EAST:
                            if (side == Side.WEST) {
                                active_conector = true;
                                break  side_iteration;
                            }
                            continue;
                        case NORTH:
                            if (side == Side.SOUTH) {
                                active_conector = true;

                                break side_iteration;
                            }
                            continue;
                        case SOUTH:
                            if (side == Side.NORTH) {
                                active_conector = true;
                                break  side_iteration;
                            }
                            continue;
                        case WEST:
                            if (side == Side.EAST) {
                                active_conector = true;
                                break  side_iteration;
                            }
                            continue;
                        default:

                            //ERROR
                            return false;

                    }
                }
                else {
                    active_conector = true;
                }
            }
            if (!active_conector) {
                return false;
            }
            Position tmp;
            for (Side side : neighbor.sides) {

                if (side !=null) {


                     tmp =currentPos;
                    currentPos = this.moveToPosition(side, currentPos);
                    if (prevPos != null) {


                        if (prevPos.getCol() == currentPos.getCol() && prevPos.getRow() == currentPos.getRow()) {

                            currentPos = tmp;
                            continue;
                        }
                    }

                    if (currentPos == null) {
                        continue;
                    }


                    prevPos = tmp;
                    if (conected(prevPos, currentPos, side)) {
                        prevPos= currentPos;
                        return true;
                    } else {

                        prevPos= currentPos;
                        currentPos=tmp;

                    }

                } else {

                    continue;
                }
            }
        }
        else {
            return false;
        }

    return false;

    }

    public boolean light(){
        boolean result;
        Position pos = this.getPosition();

        this.LastPosition = new Position(-1, -1);


        if( this.type.name()== "NONE")
            result= false;
        else {

            result = conected(null, this.getPosition(), null);
        }

        return result;
    }

    @Override
    public String toString() {
        // Určenie typu uzlu
        char typeChar;
        switch (this.type) {
            case BULB: typeChar = 'B'; break;
            case LINK: typeChar = 'L'; break;
            case POWER: typeChar = 'P'; break;
            default: typeChar = 'E'; // NONE
        }

        // Formátovanie pozície
        String positionStr = position != null ?
                "[" + (position.getRow()+1) + "@" + (position.getCol()+1) + "]" :
                "[null@null]";

        // Zoznam konektorov
        StringBuilder connectors = new StringBuilder("[");
        boolean first = true;

        // Prechádzame všetky možné strany v poradí NORTH, EAST, SOUTH, WEST
        for (Side side : Side.values()) {
            if (containsConnector(side)) {
                if (!first) {
                    connectors.append(",");
                }
                connectors.append(side.name());
                first = false;
            }
        }
        connectors.append("]");

        return "{" + typeChar + positionStr + connectors + "}";
    }
}