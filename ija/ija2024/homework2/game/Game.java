/**
 * @author xsudato00
 */

 package ija.ija2024.homework2.game;

 import ija.ija2024.homework2.common.GameNode;
 import ija.ija2024.homework2.common.Position;
 import ija.ija2024.homework2.common.Side;
 
 /**
  * Třída reprezentující prostředí hry s mřížkou políček.
  */
 public class Game {
    private final int rows;
    private final int cols;
    private GameNode[][] board;
    private boolean hasPowerNode = false;

    public int init() {
        return 0;
    }

    public Game(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.board = new GameNode[rows][cols];
        

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.board[r][c] = new GameNode();
            }
        }
    }
    public static Game create(int rows, int cols) {
        GameNode.linkCount=GameNode.powerCount=GameNode.bulbCount=0;
        if( (rows < 1) ||( cols < 1)){
            throw new IllegalArgumentException("Argumenty nenej alebo rovno nula su nepovolene");
        }
        return new Game(rows,cols);
    }

    public GameNode createLinkNode(Position p,Side... sides){
        if ((p.getRow() >= rows() || p.getRow() < 0 )||( p.getCol() >= cols() ||  p.getCol() < 0)||sides.length<2){
            return null;
        }
        GameNode gn = node(p);
        gn.setPosition(p);
        gn.setAsLink(sides);
        return  gn;
    }

    public GameNode createPowerNode(Position p,Side... sides){
        if (((p.getRow() >= rows() || p.getRow() < 0 )||( p.getCol() >= cols() ||  p.getCol() < 0))|| sides.length<1 ){
            return null;
        }

        if (GameNode.powerCount >= 1){
            return null;
        }
        GameNode gn = node(p);
        gn.setPosition(p);
        gn.setAsPower(sides);
        return  gn;
    }

    public GameNode createBulbNode(Position p,Side sides){
        if ((p.getRow() >= rows() || p.getRow() < 0 )||( p.getCol() >= cols() ||  p.getCol() < 0)){
            return null;
        }
        GameNode gn = node(p);
        gn.setPosition(p);
        gn.setAsBulb(sides);
        return  gn;
    }

    public int rows(){return this.rows;}

    public int cols(){ return this.cols;}

    public GameNode node(Position p){
        return this.board[p.getRow()][p.getCol()];
    }


 }