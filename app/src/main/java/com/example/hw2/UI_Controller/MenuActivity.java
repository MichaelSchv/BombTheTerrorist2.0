package com.example.hw2.UI_Controller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hw2.Utilities.ImageLoader;

import com.example.hw2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class MenuActivity extends AppCompatActivity {

    private MaterialButton menu_BTN_play;
    private MaterialButton menu_BTN_scores;
    private SwitchCompat menu_SWITCH_mode;
    private SwitchCompat menu_SWITCH_speed;
    private ShapeableImageView menu_IMG_background;
    private ImageLoader imageLoader;
    private final char MODE = 'M';
    private final char SPEED = 'S';
    public static final String FAST = "FAST";
    public static final String SLOW = "SLOW";
    public static final String SENSORS = "SENSORS";
    public static final String BUTTONS = "BUTTONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        imageLoader = new ImageLoader(this);
        getPermissions();
        handleButtons();
        loadMenuBackground();
    }

    private void getPermissions() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadMenuBackground() {
        imageLoader.load(R.drawable.israel, menu_IMG_background);
    }

    private void handleButtons() {
        menu_BTN_play.setOnClickListener(v->{
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            intent.putExtra("Mode", getSwitch(MODE));
            intent.putExtra("Speed", getSwitch(SPEED));
            startActivity(intent);

        });
        menu_BTN_scores.setOnClickListener(v->{
            Intent intent = new Intent(MenuActivity.this, ScoresActivity.class);
            startActivity(intent);
        });
    }

    private String getSwitch(char choice) {
        if(choice == MODE)
            return menu_SWITCH_mode.isChecked() ? SENSORS : BUTTONS;
        else
            return menu_SWITCH_speed.isChecked() ? FAST : SLOW;
    }

    private void findViews() {
        menu_BTN_play = findViewById(R.id.sfdgsfdg);
        menu_BTN_scores = findViewById(R.id.sfdgsfdgas);
        menu_SWITCH_mode = findViewById(R.id.menu_SWITCH_mode);
        menu_SWITCH_speed = findViewById(R.id.menu_SWITCH_speed);
        menu_IMG_background = findViewById(R.id.menu_IMG_background);
    }

}
