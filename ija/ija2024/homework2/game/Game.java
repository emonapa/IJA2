/**
 * @author xsudato00
 */

 package ija.ija2024.homework2.game;

 import ija.ija2024.homework2.common.GameNode;
 import ija.ija2024.homework2.common.Position;
 import ija.ija2024.homework2.common.Side;

import ija.ija2024.tool.common.ToolEnvironment;
import ija.ija2024.tool.common.ToolField;
 
 /**
  * Třída reprezentující prostředí hry s mřížkou políček.
  */
 public class Game implements ToolEnvironment  {
     public  int rows;
     public  int cols;
     private GameNode[][] board;
     private boolean hasPowerNode = false;
 
     public Game(int rows, int cols){
         this.rows = rows;
         this.cols = cols;
         this.board = new GameNode[rows][cols];
 
 
         for (int r = 0; r < rows; r++) {
             for (int c = 0; c < cols; c++) {
                 this.board[r][c] = new GameNode();
                 this.board[r][c].setGame(this);
             }
         }
     }
 
     public Game() {
 
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
         gn.setGame(this);
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
         gn.setGame(this);
         gn.setPosition(p);
         gn.setAsPower(sides);
         return  gn;
     }
 
     public GameNode createBulbNode(Position p,Side sides){
         if ((p.getRow() >= rows() || p.getRow() < 0 )||( p.getCol() >= cols() ||  p.getCol() < 0)){
             return null;
         }
         GameNode gn = node(p);
         gn.setGame(this);
         gn.setPosition(p);
         gn.setAsBulb(sides);
         return  gn;
     }
 
    @Override
    public int rows(){return this.rows;}

    @Override
    public int cols(){ return this.cols;}

    @Override
    public ToolField fieldAt(int row, int col) {
        return this.board[row - 1][col - 1]; // Pozor: 1-based indexování
    }
 
     public GameNode node(Position p){
         return this.board[p.getRow()][p.getCol()];
     }
 
     public void init() {
         int powerCount = 0;
         int bulbCount = 0;
         GameNode powerNode = null;
         Position powerPos = null;
 
         // First pass: count power nodes and bulbs, and locate the power node
         for (int r = 1; r <= rows(); r++) {
             for (int c = 1; c <= cols(); c++) {
                 Position pos = new Position(r, c);
                 GameNode node = node(pos);
 
                 if (node != null) {
                     if (node.isPower()) {
                         powerCount++;
                         powerNode = node;
                         powerPos = pos;
                     } else if (node.isBulb()) {
                         bulbCount++;
                     }
                 }
             }
         }
 
         // Validate game rules
         if (powerCount != 1) {
             throw new IllegalStateException("Game must have exactly one power node, found " + powerCount);
         }
         if (bulbCount < 1) {
             throw new IllegalStateException("Game must have at least one bulb, found " + bulbCount);
         }
 
         // Second pass: light up connected nodes starting from power node
         if (!powerNode.light()) {
             throw new IllegalStateException("Power node is not properly connected to any bulbs");
         }
 
 //        // Optional: Mark all lit nodes (if you want to track this separately)
 //        for (int r = 0; r < rows(); r++) {
 //            for (int c = 0; c < cols(); c++) {
 //                Position pos = new Position(r, c);
 //                GameNode node = node(pos);
 //                if (node != null && node.light()) {
 //                    // You could add node.setLit(true) here if you add this method
 //                }
 //            }
 //        }
     }
 
 //    public GameNode getNode(Position currentPos) {
 //        return this.board[currentPos.getRow()][currentPos.getCol()];
 //    }
 }