package com.example.hw2.Logic;

import com.example.hw2.Model.Obstacle;
import com.example.hw2.Model.User;
import com.example.hw2.UI_Controller.MainActivity;
import com.example.hw2.Utilities.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    public static final int NO_COLLISION = -10;
    public static final int HIT_TERRORIST = 0;
    public static final int MISS_TERRORIST = 1;
    public static final int HIT_COIN = 2;
    public static final int NEW_ROW = 0;
    private final int INIT_PREVIOUS_COL = -1;
    private final int TOON_COL_INDICATOR = 2;
    private final int TOON_ROW = MainActivity.NUM_ROWS;
    private final int RANDOM_OBSTACLE_RANGE = 20;
    private final int COIN_END_RANGE = 3;
    private final int DROP_RATE = 3;
    int status = NO_COLLISION;
    private int newLocation;
    private int dropRateRunner = 0;
    private int previousObstacleColumn = INIT_PREVIOUS_COL;

    private ArrayList<Obstacle> obstacles;
    private Obstacle toon;
    private int life;
    private int misses;
    private Random rnd;



    public GameManager(int life)
    {
        this.life = life;
        this.rnd = new Random();
        this.misses=0;
        this.toon = new Obstacle(Obstacle.ObstacleType.TOON, TOON_ROW,TOON_COL_INDICATOR);
        obstacles = new ArrayList<>();
        //obstacles.add(toon);
    }

    public int getMisses() {
        return this.misses;
    }
    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }
    public void incrementMisses()
    {
        this.misses++;
    }
    public void decrementMisses()
    {
        this.misses--;
    }
    public int getLife() {
        return this.life;
    }

    public int getToonLocation(){
        return this.toon.getCol();
    }

    public boolean isGameOver()
    {
        return this.getMisses() >= this.getLife();
    }

    public int getRandomCol()
    {
        return rnd.nextInt(MainActivity.NUM_COLS);
    }

    public boolean getNextObstacleType() {
        int temp = rnd.nextInt(RANDOM_OBSTACLE_RANGE);
        if(temp <= COIN_END_RANGE)
            return true;
        return false;
    }



    public int moveObstacles(){
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);

            if(obstacle.getRow() < MainActivity.NUM_ROWS-1) // no potential collision in the next row
            {
                obstacle.moveDown();
                status = NO_COLLISION;
            }
            else // potential collision
            {
                if(obstacle.getCol() == getToonLocation()) // hit
                {
                    if(obstacle.getType() == Obstacle.ObstacleType.TERRORIST)
                        status = HIT_TERRORIST;
                    else if(obstacle.getType() == Obstacle.ObstacleType.UNRWA)
                    {
                        status = HIT_COIN;
                    }
                    obstacle.moveDown();
                }
                else
                {
                    if(obstacle.getType() == Obstacle.ObstacleType.TERRORIST){
                        status = MISS_TERRORIST;
                        incrementMisses();
                    }
                    obstacle.moveDown();
                }

                obstacles.remove(i);
                //i--;
                break;
                //obstacle.moveDown();

            }

        }
        if(dropRateRunner%DROP_RATE == 0)
        {
            do {
                newLocation = getRandomCol();
            }
            while (newLocation == previousObstacleColumn);
            generateObstacles(newLocation);
            previousObstacleColumn = newLocation;
        }
        dropRateRunner++;
        return status;

    }
    public void generateObstacles(final int col){
        boolean isNextCoin = getNextObstacleType();
        if (isNextCoin)
            obstacles.add(new Obstacle(Obstacle.ObstacleType.UNRWA, NEW_ROW,col));
        else
            obstacles.add(new Obstacle(Obstacle.ObstacleType.TERRORIST, NEW_ROW, col));
    }
    public void updateToonLocation(int newToonLocation)
    {
        this.toon.setCol(newToonLocation);
    }

    public void updateScoreInDB(User user) {
        SharedPreferencesManager.getInstance().saveRecordToDB(user);
    }
}

