/**
 * @author xsudato00
 */

 package ija.ija2024.homework2.common;
 /**
  * Třída reprezentující pozici v prostředí. 
  * Pozice je neměnná (immutable) a reprezentována řádkem a sloupcem v mřížce.
  */
 public final class Position {
     private int row; 
     private int col; 
 
     public Position(int row, int col) {
        this.row = row-1;
        this.col = col-1;
    }
    public Position(Position p) {
        this.row = p.row;
        this.col = p.col;
    }
   public int getRow() {
        return this.row;
     }
     public int getCol() {
        return this.col;
     }
   
     
     @Override
    public boolean equals(Object obj) {
        // Kontrola, či je objekt rovnaký
        if (this == obj) {
            return true;
        }

        // Kontrola, či je objekt null alebo iného typu
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Pretypovanie na Position
        Position other = (Position) obj;

        // Porovnanie hodnôt row a col
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode() {
        // Implementácia hashCode pre konzistenciu s equals
        return 31 * row + col;
    }

 }