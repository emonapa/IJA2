/**
 * @author xsudato00
 */

package ija.ija2024.homework2.common;

import ija.ija2024.homework2.game.Game;
import ija.ija2024.tool.common.ToolField;
import ija.ija2024.tool.common.Observable;

public class GameNode implements ToolField {
    public enum nodeType {
        NONE, BULB, POWER, LINK
    }

    private nodeType type;
    private Position position;
    public Side[] sides;
    private Game game;
    public static int linkCount = 0, bulbCount = 0, powerCount = 0;
    public static Position LastPosition;

    public GameNode() {
        this.type = nodeType.NONE;
        this.sides = new Side[4];
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position p) {
        this.position = new Position(p);
    }

    public void turn() {
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] != null) {
                int currentOrdinal = sides[i].ordinal();
                int newOrdinal = (currentOrdinal + 1) % Side.values().length;
                sides[i] = Side.values()[newOrdinal];
            }
        }
    }

    public boolean isLink() {
        return type == nodeType.LINK;
    }

    public boolean isBulb() {
        return type == nodeType.BULB;
    }

    public boolean isPower() {
        return type == nodeType.POWER;
    }

    public boolean containsConnector(Side s) {
        for (Side side : sides) {
            if (side == s) return true;
        }
        return false;
    }

    public void setAsBulb(Side side) {
        GameNode.bulbCount++;
        this.type = nodeType.BULB;
        this.sides[0] = side;
    }

    public void setAsLink(Side... sides) {
        GameNode.linkCount++;
        this.type = nodeType.LINK;
        for (int i = 0; i < sides.length && i < 4; i++) {
            this.sides[sides[i].ordinal()] = sides[i];
        }
    }

    public void setAsPower(Side... sides) {
        GameNode.powerCount++;
        this.type = nodeType.POWER;
        for (int i = 0; i < sides.length && i < 4; i++) {
            this.sides[sides[i].ordinal()] = sides[i];
        }
    }

    public void setAsNone() {
        this.type = nodeType.NONE;
        this.sides[0] = Side.EAST;
        this.sides[1] = Side.NORTH;
        this.sides[2] = Side.SOUTH;
        this.sides[3] = Side.WEST;
    }

    private Position moveToPosition(Side side, Position currentPos) {
        if (currentPos == null) return null;
        int newRow = currentPos.getRow();
        int newCol = currentPos.getCol();

        switch (side) {
            case NORTH -> newRow--;
            case EAST -> newCol++;
            case SOUTH -> newRow++;
            case WEST -> newCol--;
        }

        return new Position(newRow + 1, newCol + 1);
    }

    public boolean connected(Position prevPos, Position currentPos, Side conectorSide) {
        GameNode neighbor = game.node(currentPos);
        if (neighbor.isPower()) {
            for (Side side : neighbor.sides) {
                if (conectorSide == null) return true;
                if ((conectorSide == Side.EAST && side == Side.WEST) ||
                    (conectorSide == Side.NORTH && side == Side.SOUTH) ||
                    (conectorSide == Side.SOUTH && side == Side.NORTH) ||
                    (conectorSide == Side.WEST && side == Side.EAST)) {
                    return true;
                }
            }
            return false;
        }
        else if (neighbor.isBulb() || neighbor.isLink()) {
            boolean activeConnector = false;
            for (Side side : neighbor.sides) {
                if (conectorSide == null) {
                    activeConnector = true;
                    break;
                }
                if ((conectorSide == Side.EAST && side == Side.WEST) ||
                    (conectorSide == Side.NORTH && side == Side.SOUTH) ||
                    (conectorSide == Side.SOUTH && side == Side.NORTH) ||
                    (conectorSide == Side.WEST && side == Side.EAST)) {
                    activeConnector = true;
                    break;
                }
            }
            if (!activeConnector) return false;

            for (Side side : neighbor.sides) {
                if (side != null) {
                    Position tmp = currentPos;
                    currentPos = moveToPosition(side, currentPos);
                    if (prevPos != null &&
                        prevPos.getCol() == currentPos.getCol() &&
                        prevPos.getRow() == currentPos.getRow()) {
                        currentPos = tmp;
                        continue;
                    }
                    if (currentPos == null) continue;

                    prevPos = tmp;
                    if (connected(prevPos, currentPos, side)) {
                        return true;
                    } else {
                        currentPos = tmp;
                    }
                }
            }
        }
        return false;
    }

    public boolean light() {
        if (this.type == nodeType.NONE)
            return false;
        return connected(null, this.getPosition(), null);
    }

    @Override
    public String toString() {
        char typeChar;
        switch (this.type) {
            case BULB -> typeChar = 'B';
            case LINK -> typeChar = 'L';
            case POWER -> typeChar = 'P';
            default -> typeChar = 'E';
        }

        String positionStr = position != null ?
                "[" + (position.getRow() + 1) + "@" + (position.getCol() + 1) + "]" :
                "[null@null]";

        StringBuilder connectors = new StringBuilder("[");
        boolean first = true;
        for (Side side : Side.values()) {
            if (containsConnector(side)) {
                if (!first) connectors.append(",");
                connectors.append(side.name());
                first = false;
            }
        }
        connectors.append("]");
        return "{" + typeChar + positionStr + connectors + "}";
    }

    // ===== ToolField interface methods =====
    @Override
    public boolean north() {
        return containsConnector(Side.NORTH);
    }

    @Override
    public boolean east() {
        return containsConnector(Side.EAST);
    }

    @Override
    public boolean south() {
        return containsConnector(Side.SOUTH);
    }

    @Override
    public boolean west() {
        return containsConnector(Side.WEST);
    }

    @Override
    public void addObserver(Observable.Observer observer) {
        // GUI nevyužívá – prázdná implementace
    }

    @Override
    public void removeObserver(Observable.Observer observer) {
        // GUI nevyužívá – prázdná implementace
    }

    @Override
    public void notifyObservers() {
        // GUI nevyužívá – prázdná implementace
    }
}
