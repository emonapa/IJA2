/**
 * @author xsudato00
 */

package ija.ija2024.homework2.common;

public class GameNode {
    public enum nodeType {
        NONE, BULB, POWER, LINK
    }
    private nodeType type;
    Position position;
    public Side[] sides;
    public static int linkCount=0,bulbCount=0,powerCount =0;


    public GameNode(){
        this.type=nodeType.NONE;
        this.sides = new Side[4];
    }
    
    public Position getPosition(){return this.position;}
    
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

    public boolean light() {
        return true;
    }
    

    public String toString() {
        return "ano";
    }
}