package com.example.hw2.Model;

import com.example.hw2.UI_Controller.MainActivity;
public class Obstacle {


    public static enum ObstacleType {TERRORIST, UNRWA, TOON}

    private ObstacleType type;
    private int row;
    private int col;



    public Obstacle(ObstacleType type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public ObstacleType getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col){
        this.col = col;
    }

    public void moveDown() {
        this.row++;
    }
    public boolean isAtBottom(){
        return this.row == MainActivity.NUM_ROWS-1;
    }
}
