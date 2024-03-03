package com.example.hw2.UI_Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hw2.Interface.Step_Callback;
import com.example.hw2.Logic.GameManager;
import com.example.hw2.Model.User;
import com.example.hw2.Model.Obstacle;
import com.example.hw2.R;
import com.example.hw2.Utilities.ImageLoader;
import com.example.hw2.Utilities.Sensors;
import com.example.hw2.Utilities.SoundExecutor;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 5;
    private final int STANDARD_ADDITION_SCORE = 10;
    private final int EXTRA_SCORE_COIN = 90;
    private final int FAST = 500;
    private final int SLOW = 1000;
    private int DELAY;
    private final int LEFT = 0;
    private final int RIGHT = 4;
    private final int IS_TOON = -1;
    private final int IS_LIFE = -4;
    private final int IS_TERRORIST = -2;
    private final int IS_COIN = -3;
    private boolean isToonNeedChange=false;

    User user;
    Intent intent;
    private MaterialTextView main_LBL_score;
    private Timer gameTimer;
    private ImageLoader imageLoader;
    private ImageButton main_BTN_right;
    private ImageButton main_BTN_left;
    private ShapeableImageView[][] main_IMG_cells = new ShapeableImageView[NUM_ROWS][NUM_COLS];
    private ShapeableImageView[] main_IMG_toon = new ShapeableImageView[NUM_COLS];
    private ShapeableImageView[] main_IMG_lives;
    private GameManager gameManager;
    Sensors sensors;
    boolean isSensor = false;
    boolean isFast = false;
    private SoundExecutor soundExecutor = new SoundExecutor(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageLoader = new ImageLoader(this);
        findViews();
        loadImages();
        initUser();
        intent= getIntent();
        setGameModes();
        gameManager = new GameManager(main_IMG_lives.length);
    }

    private void setGameModes() {
        isFast = getSpeedMode(intent);
        if(isFast)
            DELAY = FAST;
        else
            DELAY = SLOW;
        isSensor = getMovingMode(intent);
        if(isSensor)
            handleSensors();
        else
            handleButtons();
    }

    private void handleSensors() {
        main_BTN_right.setVisibility(View.INVISIBLE);
        main_BTN_left.setVisibility(View.INVISIBLE);
        sensors = new Sensors(this, new Step_Callback() {
            @Override
            public void stepLeft() {
                moveLeft();
            }

            @Override
            public void stepRight() {
                moveRight();
            }

            @Override
            public void stepSpeedUp() {
                DELAY = (DELAY != FAST) ? FAST : DELAY;
                cancelAndScheduleTimer();
            }
            @Override
            public void stepSpeedDown() {

                DELAY = (DELAY != SLOW) ? SLOW : DELAY;
                cancelAndScheduleTimer();

            }
        });
    }

    private void cancelAndScheduleTimer() {
        if(gameTimer != null){
            gameTimer.cancel();
            gameTimer.purge();
            startGame();
        }
    }

    private void handleButtons() {
        main_BTN_left.setVisibility(View.VISIBLE);
        main_BTN_right.setVisibility(View.VISIBLE);
        main_BTN_left.setOnClickListener(v -> moveLeft());
        main_BTN_right.setOnClickListener(v -> moveRight());
    }

    private void moveLeft() {
        if (gameManager.getToonLocation() > LEFT) {
            gameManager.updateToonLocation(gameManager.getToonLocation() - 1);
            updateToonLocation();
        }
    }
    private void moveRight(){
        if (gameManager.getToonLocation() < RIGHT) {
            gameManager.updateToonLocation(gameManager.getToonLocation() + 1);
            updateToonLocation();
        }
    }
    private void initUser() {
        user = new User();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null)
            user.setLat(location.getLatitude()).setLon(location.getLongitude());
    }

    @Override
    protected void onStart() {
        super.onStart();
        startGame();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(isSensor)
            sensors.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        gameTimer.cancel();
        if(isSensor)
            sensors.stop();
    }
    @Override
    protected void onStop(){
        super.onStop();
        gameTimer.cancel();
    }

    private void startGame() {
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateUI());
            }
        }, 0, DELAY);
    }

    private void updateUI() {
        isToonNeedChange = false;
        changeToonIconToHit();
        int status = gameManager.moveObstacles();
        if(status == GameManager.HIT_TERRORIST)
            hitTerroristDrill();
        else if (status == GameManager.MISS_TERRORIST)
            missTerroristDrill();
        else if (status == GameManager.HIT_COIN)
            hitCoinDrill();
        updateCells();
        user.addToScore(STANDARD_ADDITION_SCORE);
        main_LBL_score.setText("Score: " + user.getScore());
        }

    private void hitTerroristDrill() {
        isToonNeedChange = true;
        changeToonIconToHit();
        soundExecutor.playSound(R.raw.missle_hit);
    }

    private void hitCoinDrill() { // At full health - extra points. Not full health - extra life
        soundExecutor.playSound(R.raw.coin_hit);
        if(gameManager.getMisses() == 0){ //extra points
            Toast.makeText(this,"Extra Points!",Toast.LENGTH_SHORT).show();
            user.addToScore(EXTRA_SCORE_COIN);
            main_LBL_score.setText("Score: " + user.getScore());
        }
        else
        {
            Toast.makeText(this,"Extra Life!",Toast.LENGTH_SHORT).show();
            main_IMG_lives[main_IMG_lives.length - gameManager.getMisses()].setVisibility(View.VISIBLE);
            gameManager.decrementMisses();
        }

    }

    private void missTerroristDrill()
    {
        vibrate();
        if(gameManager.isGameOver())
        {
            soundExecutor.playSound(R.raw.game_over);
            Toast.makeText(this, "Game Over",Toast.LENGTH_LONG).show();
            gameTimer.cancel();
            gameManager.updateScoreInDB(user);
            finish();
        }
        else
        {
            main_IMG_lives[main_IMG_lives.length - gameManager.getMisses()].setVisibility(View.INVISIBLE);
            soundExecutor.playSound(R.raw.life_lost);
        }
    }


    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        else
            v.vibrate(500);
    }


    private void changeToonIconToHit() {
        if(isToonNeedChange)
            for (int i = 0; i < NUM_COLS; i++)
                imageLoader.load(R.drawable.hit, main_IMG_toon[i]);
        else
            loadImagesIntoToon();
    }


    private void updateToonLocation() {
        for (int i = 0; i < NUM_COLS; i++)
            if (i == gameManager.getToonLocation())
                main_IMG_toon[i].setVisibility(View.VISIBLE);
            else
                main_IMG_toon[i].setVisibility(View.INVISIBLE);
    }

    private boolean getMovingMode(Intent intent) {
        return intent.getStringExtra("Mode").equals(MenuActivity.SENSORS) ? true : false;
    }
    private boolean getSpeedMode(Intent intent) {
        return intent.getStringExtra("Speed").equals(MenuActivity.FAST) ? true : false;
    }

    private void updateCells() {
        ArrayList<Obstacle> obstacles = gameManager.getObstacles();

        // Clear everything first:
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                main_IMG_cells[row][col].setVisibility(View.INVISIBLE);
            }
        }

        // Now show obstacles at their current positions:
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            int row = obstacle.getRow();
            int col = obstacle.getCol();

            if (row >= 0 && row < NUM_ROWS) { // Ensure row is within the grid
                main_IMG_cells[row][col].setVisibility(View.VISIBLE);

                // Load the appropriate image based on obstacle type
                if (obstacle.getType() == Obstacle.ObstacleType.TERRORIST) {
                    loadImageIntoCell(IS_TERRORIST, row, col);
                } else {
                    loadImageIntoCell(IS_COIN, row, col);
                }
            }
        }
    }

    private void loadImageIntoCell(final int type, final int row, final int col) {
        if(type==IS_TERRORIST)
            imageLoader.load(R.drawable.sinwar, main_IMG_cells[row][col]);
        else
            imageLoader.load(R.drawable.unrwa, main_IMG_cells[row][col]);
    }

    private void loadImages() {
        loadImagesIntoToon();
        loadImagesIntoLives();
    }

    private void loadImagesIntoLives() {
        for (ShapeableImageView mainImgLive : main_IMG_lives)
            imageLoader.load(R.drawable.mouse, mainImgLive);
    }

    private void loadImagesIntoToon() {
        for (int i = 0; i < NUM_COLS; i++)
            imageLoader.load(R.drawable.rocket, main_IMG_toon[i]);
    }

    private void findViews() {
        // find each obstacle and toon's id:
        for (int i = 0; i<NUM_ROWS+1;i++)
            for (int j = 0; j < NUM_COLS; j++) {
                if (i<NUM_ROWS)
                    main_IMG_cells[i][j] = findViewById(getActualId(i,j));

                else
                    main_IMG_toon[j] = findViewById(getActualId(IS_TOON,j));
            }
        main_IMG_lives = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_life0),
                findViewById(R.id.main_IMG_life1),
                findViewById(R.id.main_IMG_life2)};

        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_LBL_score = findViewById(R.id.main_LBL_score);


    }

    private int getActualId(int row, int col) {
        String cellId;
        if(row != IS_TOON)
            cellId = String.format("main_IMG_cell%d%d", row, col);
        else
            cellId = String.format("main_IMG_toonCell%d", col);
        return getResources().getIdentifier(cellId,"id", getPackageName());
    }
}